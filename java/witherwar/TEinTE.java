package witherwar;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.network.MessageRegionOverlayOn;
import witherwar.network.MessageRegionOverlayOn.HandleMessageRegionOverlayOn;
import witherwar.network.*;//MessageRegionOverlayOn.MessageHandleRegionOverlayOn;
import witherwar.network.MessageEditGuidestone.HandleMessageEditGuidestone;
import witherwar.proxy.IProxy;
import witherwar.util.ChunkManager;
import witherwar.util.TeinteWorldSavedData;
import witherwar.worlds.WorldManager;



@Mod(modid = TEinTE.MODID, version = TEinTE.VERSION)
public class TEinTE
{
    public static final String MODID = "witherwar";
    public static final String VERSION = "0.1.00";
    public static final int TICKSASECOND = 20;
	public static final SimpleNetworkWrapper networkwrapper = NetworkRegistry.INSTANCE.newSimpleChannel("teinte");
    public static CreativeTabs teinteTab;
	
	private TeinteWorldSavedData data;
	private int tickcount = 0;
	
	@SidedProxy( clientSide="witherwar.proxy.ClientOnlyProxy" ,serverSide="witherwar.proxy.ServerOnlyProxy")
	public static IProxy proxy;
	
	@Mod.Instance("witherwar")
	public static TEinTE instance;


	
	
    
    @EventHandler
    public void preInit( FMLPreInitializationEvent event) {  
    	
    	teinteTab = new CreativeTabs( "witherwar_tab"){
			@Override
			@SideOnly(Side.CLIENT)
			public ItemStack getTabIconItem() {
				return new ItemStack( Items.END_CRYSTAL);
			}};
			
		ObjectCatalog.registerPreInitObjects();		
   	
    	
    	networkwrapper.registerMessage( HandleMessageRegionOverlayOn.class, MessageRegionOverlayOn.class, 0, Side.CLIENT);
    	networkwrapper.registerMessage( HandleMessageEditGuidestone.class, MessageEditGuidestone.class, 1, Side.CLIENT);
    	networkwrapper.registerMessage( HandleMessageEditGuidestone.class, MessageEditGuidestone.class, 1, Side.SERVER);
    	
    	WorldManager.registerDimensions();
    	
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

    
    
    
 //------------------------------  END Initialization ------------------------------------------------------------//   
    
    
    
    
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
		
		if( tickcount == TICKSASECOND / 2 ){
			tickcount = 0;
			
			if( config.allowRegionOverlay) {
				this.data.regionMap.tick( tickcount ,event.world);
			}
			
//			if( this.aleph == null) {
//				this.aleph = new FactionAleph( event.world);
//			}
			

			//aleph.update( event.world);

/**			int rand = new Random().nextInt(120);
 * 			int rand = event.world.rand.nextInt(120);
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
	private void birthTerralith() {
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
    
    
    
    
    






