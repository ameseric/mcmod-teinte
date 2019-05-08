package witherwar;

import java.util.HashMap;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.block.BlockAsh;
import witherwar.block.BlockCatarCortex;
import witherwar.block.BlockFlesh;
import witherwar.block.BlockGuidestone;
import witherwar.block.BlockCatarMaw;
import witherwar.block.BlockRefHolder;
import witherwar.block.BlockSerpentmind;
import witherwar.entity.EntityMotusGhast;
import witherwar.entity.EntitySerpentWither;
import witherwar.network.MessageRegionOverlayOn;
import witherwar.network.MessageRegionOverlayOn.HandleMessageRegionOverlayOn;
import witherwar.network.*;//MessageRegionOverlayOn.MessageHandleRegionOverlayOn;
import witherwar.network.MessageEditGuidestone.HandleMessageEditGuidestone;
import witherwar.proxy.IProxy;
import witherwar.tileentity.TileEntityCataromotus;
import witherwar.tileentity.TileEntityMaw;
import witherwar.tileentity.TileEntitySerpentmind;
import witherwar.util.ChunkManager;
import witherwar.util.TeinteWorldSavedData;



@Mod(modid = WitherWar.MODID, version = WitherWar.VERSION)
public class WitherWar
{
    public static final String MODID = "witherwar";
    public static final String VERSION = "0.1";
    public static final int TICKSASECOND = 20;
	public static final SimpleNetworkWrapper snwrapper = NetworkRegistry.INSTANCE.newSimpleChannel("teinte");
	private TeinteWorldSavedData data;
	
    public static CreativeTabs teinteTab;
    public static HashMap<String ,BlockRefHolder> newBlocks = new HashMap<String,BlockRefHolder>();
    //private HashMap<ChunkPos ,TileEntityGuidestone> regionalMap;
	private int tickcount = 0;
	
	@SidedProxy( clientSide="witherwar.proxy.ClientOnlyProxy" ,serverSide="witherwar.proxy.ServerOnlyProxy")
	public static IProxy proxy;
	
	@Mod.Instance("witherwar")
	public static WitherWar instance;


	
	
    
    @EventHandler
    public void preInit( FMLPreInitializationEvent event) {  
    	
    	teinteTab = new CreativeTabs( "witherwar_tab"){
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem() {
				return new ItemStack( Items.END_CRYSTAL);
			}};
   	
    	registerBlocks(); //followup code in proxy.preInit()    	
    	registerEntities();
    	registerTileEntities();
    	
    	snwrapper.registerMessage( HandleMessageRegionOverlayOn.class, MessageRegionOverlayOn.class, 0, Side.CLIENT);
    	snwrapper.registerMessage( HandleMessageEditGuidestone.class, MessageEditGuidestone.class, 1, Side.CLIENT);
    	snwrapper.registerMessage( HandleMessageEditGuidestone.class, MessageEditGuidestone.class, 1, Side.SERVER);
    	
		proxy.preInit();
		
    }   
    
    
	
	@EventHandler
    public void init( FMLInitializationEvent event){
    	ForgeChunkManager.setForcedChunkLoadingCallback( instance, new ChunkManager());
    	MinecraftForge.EVENT_BUS.register( instance);
    	proxy.init();
    }
	
    
	
	
	
    @EventHandler
    public void postInit( FMLPostInitializationEvent event) {
  	 	proxy.postInit();
    }
    
    
    
    private void registerBlocks() {   //modelResourceLocation strings refer only to the item model file (SRC.models.item) i.e. the strings below are texture only
    	WitherWar.newBlocks.put( "terra_kali"      ,new BlockRefHolder( new BlockSerpentmind() ,"witherwar:terra_kali"));
    	WitherWar.newBlocks.put( "flesh"           ,new BlockRefHolder( new BlockFlesh()       ,"minecraft:nether_wart_block"));
    	WitherWar.newBlocks.put( "terra_catar_maw" ,new BlockRefHolder( new BlockCatarMaw()    ,"minecraft:nether_wart_block"));
    	WitherWar.newBlocks.put( "dead_ash"        ,new BlockRefHolder( new BlockAsh()         ,"witherwar:dead_ash"));
    	WitherWar.newBlocks.put( "terra_catar"     ,new BlockRefHolder( new BlockCatarCortex() ,"witherwar:terra_kali"));
    	WitherWar.newBlocks.put( "guidestone"	   ,new BlockRefHolder( new BlockGuidestone()  ,"minecraft:glowstone"));
    	
    	for( BlockRefHolder brh : WitherWar.newBlocks.values()) {
    		brh.registerBlock();
    	}    	
    }    
    
    
    private void registerEntities() {
    	EntityEntry entry1 = EntityEntryBuilder.create()
        	    .entity( EntitySerpentWither.class)
        	    .id( new ResourceLocation( WitherWar.MODID ,"serpent_wither_skeleton"), 2)
        	    .name( "serpent_wither_skeleton")
        	    .egg( 0xFFFFFF, 0xAAAAAA)
        	    .tracker( 80, 3, false)
        	    .build();
    	ForgeRegistries.ENTITIES.register( entry1);    	
    	
    	EntityEntry entry2 = EntityEntryBuilder.create()
        	    .entity( EntityMotusGhast.class)
        	    .id( new ResourceLocation( WitherWar.MODID ,"motus_ghast"), 3)
        	    .name( "motus_ghast")
        	    .egg( 0xFFFFFA, 0xAAAAAA)
        	    .tracker( 80, 3, false)
        	    .build();
    	ForgeRegistries.ENTITIES.register( entry2);
    }
    
    
    
    
    private void registerTileEntities() {
		GameRegistry.registerTileEntity( TileEntitySerpentmind.class ,new ResourceLocation("witherwar:tile_entity_serpentmind"));
		GameRegistry.registerTileEntity( TileEntityMaw.class ,new ResourceLocation("witherwar:tile_entity_maw"));
		GameRegistry.registerTileEntity( TileEntityCataromotus.class ,new ResourceLocation("witherwar:tile_entity_cataromotus"));
    }
    
    
    
    @SubscribeEvent
    public void onWorldLoad( WorldEvent.Load event) {
    	if( event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote) {
    		this.data = TeinteWorldSavedData.get( event.getWorld());
    		this.data.regionMap.setWorld( event.getWorld());
    		MinecraftForge.EVENT_BUS.register( this.data.regionMap);
    	}
    }
    
    
    

    public void removeRegion( BlockPos pos) { this.data.regionMap.removeRegion(pos);}
    public void setRegionName( String name ,BlockPos pos) { this.data.regionMap.updateRegionName(name, pos);}
	public void guidestoneActivated( World world ,BlockPos pos ,EntityPlayer player) {
		if( config.allowRegionOverlay) {
			this.data.regionMap.guidestoneActivated( world ,pos, player);
		}
	}
	

  
	@SubscribeEvent
	public void onWorldTick( TickEvent.WorldTickEvent event){
		if( event.world.isRemote) { return;} //if logical client, return
		tickcount += 1;
		
		if( tickcount == TICKSASECOND ){
			tickcount = 0;
			
			//tickRegions( tickcount ,event.world);
			if( config.allowRegionOverlay) {
				this.data.regionMap.tick( tickcount ,event.world);
			}
			//tickTerraliths();
			//tickVoid();
			//tickAleph();

/**			int rand = new Random().nextInt(120);
			if( rand > 1) {
				
				WorldInfo wi = world.getWorldInfo();
				wi.setCleanWeatherTime( 0);
				wi.setThunderTime( TICKSASECOND * 60);
				wi.setRainTime( TICKSASECOND * 60);
				wi.setRaining( true);
				wi.setThundering( true);				
				
				if( rand > 1 ) {
					//placeSerpent();
				}

			}**/
		}

	}

	
/**	
	private void birthTeralith() {
		World world = DimensionManager.getWorld(0);

		List<EntityPlayer> players = world.playerEntities;
		Random rand = new Random();
				
		if( players.size() > 0) {
			int choice = rand.nextInt( players.size());
			BlockPos center = players.get(choice).getPosition();
			
			int x = center.getX() + (rand.nextInt( 16) - rand.nextInt( 16));
			int z = center.getZ() + (rand.nextInt( 16) - rand.nextInt( 16));
			//BlockPos placement = new BlockPos( x+center.getX() ,200 ,z+center.getZ());
			
			for( int i=200; i>2; i--) {
				BlockPos current = new BlockPos( x ,i ,z);
				IBlockState cs = world.getBlockState( current);
				if( cs.getBlock().getDefaultState() != Blocks.AIR.getDefaultState()) {
					world.setBlockState( current ,WitherWar.newBlocks.get("flesh").block.getDefaultState() );
					break;
				}
			}
		}
	}
**/	

	
	
	@Config( modid=MODID ,name="This Needs To Be Set" ,category="General")
	public static class config {
		@Comment( value={ "When true, allows the Aleph to appear in your world" ,"and slowly sculpt your world into a monument-city."})
		@Name( value = "Enable Aleph Faction")
		public static boolean allowAleph = true;
		
		@Comment( value={ "When true, allows your world to be consumed by Rot," ,"slowly sinking parts of your world into the quiet void."})
		@Name( value = "Enable Void Rot")
		public static boolean allowRot = true;
		
		@Comment( value={ "When true, allows villages of other races to spawn."})
		@Name( value = "Enable TaF Villages")
		public static boolean allowMyVillages = true;
		
		@Comment( value={ "When true, allows players to craft portals" ,"and travel to pocket dimensions."})
		@Name( value = "An Option Name")
		public static boolean allowPDimensions = true;
		
		@Comment( value= {"When true, allows Guidestones to project the current Region name","onto the player's screen."})
		@Name( value="Enable Region Overlay")
		public static boolean allowRegionOverlay = true;
		
		@Config( modid=MODID ,name="Terralith Config" ,category="TConfig")
		public static class terralithConfig{
			
			@Comment( value= { "When true, allows the Cataromotus to spawn."})
			@Name( value = "Cataromotus can spawn")
			public static boolean allowCatar = true;
			
			
		}
	}
	


	
}	
    
    
    
    
    






