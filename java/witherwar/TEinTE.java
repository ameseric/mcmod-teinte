package witherwar;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.network.MessageRegionOverlayOn;
import witherwar.network.MessageRegionOverlayOn.HandleMessageRegionOverlayOn;
import witherwar.command.CommandDarkenSky;
import witherwar.command.CommandTeleportWW;
import witherwar.disk.NBTSaveFormat;
import witherwar.disk.NBTSaveObject;
import witherwar.disk.TeinteWorldSavedData;
import witherwar.faction.FactionManager;
import witherwar.network.MessageEditGuidestone;
import witherwar.network.MessageEditGuidestone.HandleMessageEditGuidestone;
import witherwar.proxy.IProxy;
import witherwar.region.RegionManager;
import witherwar.system.SystemBlockDegrade;
import witherwar.system.SystemPower;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;
import witherwar.tileentity.TileLoadManager;
import witherwar.worlds.WorldCatalog;
import witherwar.worlds.WorldTypeTeinte;


//TODO: Decide whether Manager classes can/should be discarded.


@Mod(modid = TEinTE.MODID, version = TEinTE.VERSION)
public class TEinTE
{
    public static final String MODID = "witherwar";
    public static final String VERSION = "0.1.00";
    public static final int TICKSASECOND = 20;
	public static final SimpleNetworkWrapper networkwrapper = NetworkRegistry.INSTANCE.newSimpleChannel("teinte");
    public static CreativeTabs teinteTab;
    
    public FactionManager factions = new FactionManager();
	
	private TeinteWorldSavedData savedata;
	private RegionManager regions;
	private SystemBlockDegrade sysBlockDegrade;  
	private SystemPower sysPower;
	private TileLoadManager blockEntities;
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
    	
    	WorldCatalog.registerDimensions();
    	
		proxy.preInit();
		
    }

    

	
	@EventHandler
    public void init( FMLInitializationEvent event){
    	//ForgeChunkManager.setForcedChunkLoadingCallback( instance, new ChunkManager());
    	MinecraftForge.EVENT_BUS.register( instance);
    	proxy.init();
    }
	
    
	
	
	
    @EventHandler
    public void postInit( FMLPostInitializationEvent event) {
  	 	proxy.postInit();
    }    

    
    
    
 //------------------------------  END Initialization ------------------------------------------------------------//   
    
    
 

 //------------------------------  Non-Init Event Handlers ------------------------------------------------------------//
    
    @SubscribeEvent
    //Triggers on client and server
    public void onWorldLoad( WorldEvent.Load event) {
    	
    	
//    	if( this.data == null) {
//    		if( !event.getWorld().isRemote) {
//    			//stuff
//    		}
//    	}
    	
//    	System.out.println(event.getWorld().isRemote);
    	
//    	proxy.onWorldLoad( event);
    	
    	
    	World world = event.getWorld();
    	if( world.provider.getDimension() == 0 && !world.isRemote) {
    		if( world.getWorldType() == WorldCatalog.worldTypePillar) {
    			world.setSpawnPoint( new BlockPos(0,0,0));
    		}

    	}
    	
    }
    
    
    @SubscribeEvent
    public void playerLoggedOn( PlayerLoggedInEvent event) {
//    	this.regions.playerMap.put( event.player ,""); //changed access, good?
    	this.regions.addPlayer( event.player);
    }
    
    
    
    @SubscribeEvent
    //Appears to be server-side only
    public void onPlayerBlockPlace( BlockEvent.PlaceEvent event) {
    	BlockPos pos = event.getPos();
    	event.getPlayer();
    	
    	//got another particle to work, some particles need specific arguments, also
    	//need to call through the proper channels
    	WorldServer world = (WorldServer) event.getWorld();
    	world.spawnParticle( EnumParticleTypes.EXPLOSION_NORMAL ,pos.getX() ,pos.getY(),pos.getZ() ,3 ,0 ,0 ,0 ,0 ,null);
    	
    	
    }
    
    
    
    @EventHandler
    // Happens after dimension loading
    public void onServerLoad(FMLServerStartingEvent event)
    {
    	event.registerServerCommand( new CommandDarkenSky());
    	event.registerServerCommand( new CommandTeleportWW());
    	
    	WorldServer world = DimensionManager.getWorld(0);
    	

		this.savedata = TeinteWorldSavedData.getInstance( world);
    	
		
		this.regions = new RegionManager( this.savedata ,world);
		this.blockEntities = new TileLoadManager( this.savedata ,world);
		
		NBTSaveObject[] objectsToSave = { this.blockEntities ,this.regions};		
		this.savedata.setObjectsToSave( objectsToSave);
		this.savedata.forceReadFromNBT();
		
		MinecraftForge.EVENT_BUS.register( this.regions);
    		//if config data is saved, use that in place of current config data?

    }
    
    
    @SubscribeEvent
    //client & server-side
    //ticks are phased, start/end
    public void onServerTick( TickEvent.ServerTickEvent event) {
    	
    	
    	WorldServer world = DimensionManager.getWorld(0);    	
		if( world.isRemote || event.phase == Phase.START) { return;} //if logical client or tick.start phase, return
		
		//System.out.println( world.getWorldType().getName());
    	
    	tickcount += 1;
    	
    		
    		
        	
    	//this.factions.tick(world);

		this.blockEntities.tick( tickcount ,world);
    	
    	
		if( config.allowRegionOverlay) {
			this.regions.tick( tickcount ,world); //TODO going to change when regionMap is removed from savedata
		}
        	
	    			
    }
  
	//@SubscribeEvent
	//triggers twice per tick, for 'start' and 'end'
	//triggers independently for each dimension
    //TODO: Move all server-only code into onServerTick
	public void onWorldTick( TickEvent.WorldTickEvent event){
		
		if( event.world.isRemote) { return;} //if logical client, return
		

	}

	
 //------------------------------  API Calls ------------------------------------------------------------//   
	/* I chose this structure to allow Teinte full access to the various Managers/Handlers, while shielding their public
	 * methods from other classes.
	 */
	
    public void removeRegion( BlockPos pos) {
    	this.regions.removeRegion(pos);
    }
    
    
    public void setRegionName( String name ,BlockPos pos) { 
    	this.regions.updateRegionName( name ,pos);
    }
    
    
	public void guidestoneActivated( World world ,BlockPos pos ,EntityPlayer player) {
		if( config.allowRegionOverlay) {
			this.regions.guidestoneActivated( world ,pos, player);
		}
	}
	
	
	public void registerBlockEntity( TileLogic be) {
		this.blockEntities.add( be);
	}
	
	
	public void removeBlockEntity( BlockPos pos) {
		this.blockEntities.remove( pos);
	}
	
	
	public TileLogic getTileLogic( BlockPos pos) {
		return this.blockEntities.get(pos);
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
		
		@Comment( value= {"The settings below will only affect *new* worlds."," Saved worlds are locked into their configuration."})
		@Name( value="README?")
		public static boolean thisDoesNothing = true;
		
		@Comment( value= { "When true, allows blocks placed by players to degrade over time."})
		@Name( value="Enable Player Block Degradation")
		public static boolean allowDegradation = true;
		
//		@Comment( value={ "When true, allows the Aleph to appear in your world" ,"and slowly sculpt your world into a monument-city."})
//		@Name( value = "Enable Aleph Faction")
//		public static boolean allowAleph = true;
//		
		@Comment( value={ "Controls radius of Pillar World Type. Does not affect worlds already generated."})
		@Name( value = "Pillar World Size")
		public static int pillarWorldSize = 1;

		
		@Comment( value= {"When true, allows Guidestones to project the current Region name","onto the player's screen."})
		@Name( value="Enable Region Overlay")
		public static boolean allowRegionOverlay = true;
		
		@Comment( value= {"Rate of invasions when playing with Invasion turned on."})
		@Name( value="Invasion Intensity")
		public static int invasionIntensity = 5;
	}
	


	
}	
    
    
    
    
    






