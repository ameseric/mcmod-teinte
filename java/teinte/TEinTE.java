package teinte;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
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
import teinte.command.CommandDarkenSky;
import teinte.command.CommandTeleportWW;
import teinte.disk.NBTSaveObject;
import teinte.disk.TeinteWorldSavedData;
import teinte.entity.WitherSkeletonTestEntity;
import teinte.faction2.Faction2;
import teinte.faction2.TestFaction;
import teinte.hermetics.Atmosphere;
import teinte.hermetics.Muir;
import teinte.network.ClientFogUpdate;
import teinte.network.MessageEditGuidestone;
import teinte.network.MessageRegionOverlayOn;
import teinte.network.PlayerDashMessage;
import teinte.network.ClientFogUpdate.HandleClientFogUpdate;
import teinte.network.MessageEditGuidestone.HandleMessageEditGuidestone;
import teinte.network.MessageRegionOverlayOn.HandleMessageRegionOverlayOn;
import teinte.network.PlayerDashMessage.HandlePlayerDashMessage;
import teinte.proxy.Proxy;
import teinte.region.RegionManager;
import teinte.tilelogic.TileLogic;
import teinte.tilelogic.TileLogicManager;
import teinte.worlds.WorldCatalog;
import teinte.worlds.structures.FortressTunnelBuilder;


//TODO: Decide whether Manager classes can/should be discarded.


@Mod(modid = TEinTE.MODID, version = TEinTE.VERSION)
public class TEinTE
{
	
	public static final String MODID = "witherwar";
	public static final String VERSION = "0.1.00";
	public static final int TICKSASECOND = 20;
	public static final int DEPTH_CUTOFF = 50;
	public static final Random RNG = new Random();
	public static final SimpleNetworkWrapper networkwrapper = NetworkRegistry.INSTANCE.newSimpleChannel("teinte");
	public static CreativeTabs teinteTab;
    
	public HashMap<EntityPlayer ,ArrayDeque<Vec3d>> lastPlayerPos = new HashMap<>(); //TODO see if better solution
    
	private TeinteWorldSavedData savedata;
	private RegionManager regions;
	private TileLogicManager tiles;
	private TemplateManager templates;
	private Atmosphere atmosphere;
	private int tickcount = 0;

	
	
	private Vec3d newFogColor = new Vec3d(0,0,0);
	private float newFogDensity = 0;
	private float currentFogDensity = 0;
	private Vec3d currentFogColor = newFogColor;
	private Vec3d rawColor = newFogColor;
	private int fogRenderCount = 0;
	

	@SidedProxy( clientSide="witherwar.proxy.ClientOnlyProxy" ,serverSide="witherwar.proxy.ServerOnlyProxy")
	public static Proxy proxy;
	
	@Mod.Instance("witherwar")
	public static TEinTE instance;
	

	
	//temporary
	public ArrayList<Faction2> factions = new ArrayList<>();
	



	
	
    
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
    	networkwrapper.registerMessage( HandlePlayerDashMessage.class ,PlayerDashMessage.class, 2, Side.SERVER);
    	networkwrapper.registerMessage( HandleClientFogUpdate.class ,ClientFogUpdate.class, 3, Side.CLIENT);

    	WorldCatalog.registerDimensions();
    	
		proxy.preInit();		
    }

    

	@EventHandler
	public void init( FMLInitializationEvent event){
		MinecraftForge.EVENT_BUS.register( instance);
		proxy.init();
	}    
	
	
	
	@EventHandler
	public void postInit( FMLPostInitializationEvent event) {
    	proxy.postInit();
	}    

    
    
    
    
 

 //------------------------------  Non-Init Event Handlers ------------------------------------------------------------//
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void _onRenderBlockOverlay( RenderBlockOverlayEvent event) {
//    	System.out.println( "OVERLAY TEST");
    }
    
    
    
    @SubscribeEvent
    public void _onChunkLoad( ChunkEvent.Load event) {

    }
    
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    //does not trigger if we cancel in fogDensity
    //EXP & EXP2 cause exponential increase, only affected by density value.
    //LINEAR doesn't seem to be affected by denstiy, but only start/end
    //called 6x per screen render, 5x for 0, 1x for -1, detailed below
    //6 draws over fluids
    //5 draws over ??? (looked, unsure)
    //4 draws over terrain
    //3 is used to apply fog *between* the y cloud level and the ground level. Going above this level reveals the clouds, and hides the ground.
    //2 draws over ??? (looked, unsure)
    //1, the -1 call fogs the skybox (not the horizon) (+sun? inconclusive)
    //TODO: move fog render stuff to dedicated client render class? register from proxy?
    public void _onFogRender(EntityViewRenderEvent.RenderFogEvent event) {
    	
    	
//    	Entity player = event.getEntity();
//    	BlockPos pos = player.getPosition();
////    	if( pos.getY() > TEinTE.DEPTH_CUTOFF) {
//    	
//	        float f = event.getFarPlaneDistance();        
//	        	
	    	this.currentFogDensity = transitionATowardsB( this.currentFogDensity ,this.newFogDensity ,0.00005f);
//	    	
//	  	
//	    	
//	    	float segment = f * 0.75f;
//	    	float delta34 = segment * clampTo1f(this.currentFogDensity * 2.86f);
//	    	float delta60 = segment * clampTo1f(this.currentFogDensity * 1.67f);
//	    	float delta100 = (f*0.5f) * this.currentFogDensity;
//	    			
//	    	
//	        if(event.getFogMode() == -1) { //sky fogs quickly, completely //TODO try experimenting with this normal again
//	        	this.fogRenderCount = 1;
//		        GlStateManager.setFogStart( 0f);
//	        	GlStateManager.setFogEnd( f - (delta34*1.3f));
//	        }else if( this.fogRenderCount == 3){ //clouds fog completely
//		        GlStateManager.setFogStart( (f * 0.75f) - delta34);
//	        	GlStateManager.setFogEnd( f - (delta60*1.3f));
//	
//	        }else {
//		        GlStateManager.setFogStart( (f * 0.75f) - (delta34*1.2f));
//	        	GlStateManager.setFogEnd( (f-10) - delta100); //cap out at 1.0, @50% //move far plane in to help hide pop-in?
//	        }        
//        
//    
//	        this.fogRenderCount++;
//    	}else {
//    		GlStateManager.setFog( GlStateManager.FogMode.EXP);
//    		float density = ( 1 - (pos.getY() / (float) TEinTE.DEPTH_CUTOFF)) * 0.2f;
//    		GlStateManager.setFogDensity( 0.15f);
//    		Atmosphere.displayAtmosphericParticles( player ,0.4f);
//    	}
//		Atmosphere.displayAtmosphericParticles( player ,1f);


    }
	private static float clampTo1f( float value) {
	    return Math.min( value ,1f);
	}
    
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void _onFogDensity( EntityViewRenderEvent.FogDensity event) {
//    	this.currentFogDensity = transitionATowardsB( this.currentFogDensity ,this.newFogDensity ,0.0001f);
//        GlStateManager.setFog(GlStateManager.FogMode.EXP);
//    	
//    	
//	    event.setDensity( 0.8f);
//	    event.setCanceled(true); //must be cancelled    	

    }
    
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void _onFogColor( EntityViewRenderEvent.FogColors event) {
//    	BlockPos pos = event.getEntity().getPosition();
//    	float blue ,green ,red;
//    	
////    	if( pos.getY() > TEinTE.DEPTH_CUTOFF) {    	
//	    	blue = transitionATowardsB( (float) this.currentFogColor.z ,(float) this.newFogColor.z ,0.0005f);
//	    	green = transitionATowardsB( (float) this.currentFogColor.y ,(float) this.newFogColor.y ,0.0005f);
//	    	red = transitionATowardsB( (float) this.currentFogColor.x ,(float) this.newFogColor.x ,0.0005f);
//	    	this.currentFogColor = new Vec3d( red ,green ,blue);
//	    	
//
////    	}else {
////    		float depth = ( (pos.getY() / (float) TEinTE.DEPTH_CUTOFF)) * -0.3f;
////    		blue = red = green = 0.1f; 
////    	}
//	    event.setBlue( 	event.getBlue() + blue );
//	    event.setGreen( event.getGreen() + green);
//	    event.setRed( event.getRed() + red);
//	    System.out.println( event.getRed());
//	    System.out.println( event.getGreen());
//	    System.out.println( event.getBlue());
    }
    
    //TODO: move?
    private float transitionATowardsB( float valueA ,float valueB ,float minimum) {
    	float factor = (1f / minimum);
    	
    	if( valueA != valueB) {
    		float diff = valueB - valueA;
    		float absDiff = Math.abs( diff);
    		if( absDiff > minimum) {
    			float delta = diff / (absDiff * factor);
    			return valueA = valueA + delta;
    		}
    	}
    	return valueA;
    }
    
    
    
    
    public void _onChunkUnload( ChunkEvent.Unload event) {

    }
    
    
    
    @SubscribeEvent
    //Triggers on client and server
    public void _onWorldLoad( WorldEvent.Load event) {
    	
    	
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
    public void _playerLoggedOn( PlayerLoggedInEvent event) {
    	this.regions.addPlayer( event.player);
    	this.atmosphere.addPlayer( (EntityPlayerMP) event.player);

    }
    


    
	@SubscribeEvent
    @SideOnly(Side.CLIENT)
	public void _onClientTick(TickEvent.ClientTickEvent event) throws Exception {
		
		if(event.phase.equals(Phase.END)){
			if( config.allowPlayerDash && proxy.playerIsDashing()) {
				TEinTE.networkwrapper.sendToServer( new PlayerDashMessage( ));
			}
			
			TEinTE.proxy.renderMuirParticles( this.currentFogDensity ,this.rawColor);
			
		}

	}
	

	
    
    
    
    
    @SubscribeEvent
    //Appears to be server-side only
    public void _onPlayerBlockPlace( BlockEvent.PlaceEvent event) {

    	BlockPos pos = event.getPos();
    	WorldServer world = (WorldServer) event.getWorld();
    	
//    	world.spawnParticle( EnumParticleTypes.EXPLOSION_NORMAL ,pos.getX() ,pos.getY()+2 ,pos.getZ() ,5 ,0 ,0 ,0 ,0.5 ,null);

    	
   		
    		
		if( event.getPlacedBlock().getBlock() == Blocks.STONE) {
			
//			System.out.println( "Placed stone block");

			
//			String filepath = "C:\\Users\\Guiltygate\\Documents\\mc_work\\old_setup\\wither_war\\"; 
//			System.out.println( "======================= STARTING STRUCTURE GENERATION");
//    		JodhHomeStructure gen = new JodhHomeStructure( pos ,Shape.RING ,Pattern.CROSS ,100);
//			GreyScaleNoisePrinter.greyWriteImage( gen.getHMap() ,filepath + "height.png");
//			GreyScaleNoisePrinter.greyWriteImage( gen.getBMap() ,filepath + "build.png");
//			LamedHomeStructure gen = new LamedHomeStructure( pos);
//			System.out.println( "======================= STARTING STRUCTURE BUILDING");			
			
			//ArrayList<Pair<BlockPos,Block>> newBlocks = gen.getNextSegment();
			
	
//			int px = pos.getX();
//			int py = pos.getY();
//			int pz = pos.getZ();
//	
//			for( int x=-100; x<100; x++) {
//				for( int z=-100; z<100; z++) {
//					for( int y=0; y<100; y++) {
//						BlockPos cpos = new BlockPos( x ,y ,z);
//						BlockPos worldPos = new BlockPos( x+px ,y+py ,z+pz);
//						if( gen.isValidPosition(cpos)) {
//	    					world.setBlockState( worldPos ,Blocks.STONE.getDefaultState());
//						}
//					}	    		
//				}
//			}
//			System.out.println( "======================= FINISHED");
		}
    		

    	
    }
    
    
  
    
    
    
    
    
    @EventHandler
    // Happens after dimension loading
    public void _onServerLoad(FMLServerStartingEvent event) throws InstantiationException, IllegalAccessException
    {
    	event.registerServerCommand( new CommandDarkenSky());
    	event.registerServerCommand( new CommandTeleportWW());
    	
    	WorldServer world = DimensionManager.getWorld(0);
    	

    	
		
		this.regions = new RegionManager( world);
		this.tiles = new TileLogicManager( world ,"tilelogicmanager");
		this.atmosphere = new Atmosphere();
		this.atmosphere.setupInitialCellMap(); //TODO will be executed based on savedata at a later date

		
		
		NBTSaveObject[] objectsOnDisk = { this.tiles ,this.regions ,this.atmosphere};
		this.savedata = TeinteWorldSavedData.getInstance( world ,objectsOnDisk);
		
		

    }
    
    
    
    
    @SubscribeEvent
    //client & server-side
    //ticks are phased, start/end
    public void _onServerTick( TickEvent.ServerTickEvent event) {
    	
    	WorldServer world = DimensionManager.getWorld(0);
		if( world.isRemote) {
			System.out.println( "Server tick on client?");
			return;} //if logical client return
		
		
		if( event.phase == Phase.START) {
		}
		
		
		if( event.phase == Phase.END) {


	    	
	    	this.logPlayerPosition();
	    	
	    	this.atmosphere._tick( tickcount);
			
			this.tiles.tick( tickcount ,world);
			
			this.savedata._tick( tickcount ,world);
			
    	
	    	
			if( config.allowRegionOverlay) {
				this.regions.tick( tickcount ,world);
			}
			
			
			tickcount += 1;
		}
        	
	    			
    }
    
    
    
    @SubscribeEvent
    public void _onEntityDeath( LivingDeathEvent event) {

    }
    
    
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void _onConfigChanged( ConfigChangedEvent.OnConfigChangedEvent event) {
    	ConfigManager.sync( MODID ,Config.Type.INSTANCE);
    }
    
    
    
    

    
    
  
	//@SubscribeEvent
	//triggers twice per tick, for 'start' and 'end'
	//triggers independently for each dimension
	public void _onWorldTick( TickEvent.WorldTickEvent event){
		
		if( event.world.isRemote) { return;} //if logical client, return
		

	}
	
	
	
	
	
	
//------------------  Temporary behavior tests  ------------------
    //TODO move
    private void nbtStructureLoadTest( BlockPos pos ,WorldServer world) {
		ResourceLocation tree_path = new ResourceLocation("witherwar:my_tree");
		Template tree = world.getStructureTemplateManager().get( world.getMinecraftServer() ,tree_path);
		System.out.println( tree.getSize());
		tree.addBlocksToWorld( world ,pos ,(new PlacementSettings()).setIgnoreEntities( true) );    	
    }
    
    
    
    private void entitySpawnTest( World world ,BlockPos pos) {
    	WitherSkeletonTestEntity skele = new WitherSkeletonTestEntity( world);
    	skele.setPosition( pos.getX() ,pos.getY()+2 ,pos.getZ());
    	 
    	//works, but have to reload chunk.
    	//world.getChunkFromBlockCoords( pos).addEntity( skele);
    	
    	//completely works
    	world.spawnEntity( skele);
    	
    	System.out.println( world.getChunkFromBlockCoords(pos).isLoaded());
    }
    
    
    
    private void createStructureTest( BlockPos pos ,World world) {
    	long count = 0;
    	int total = 0;
    	System.out.println( "Starting generation...");
    	FortressTunnelBuilder sg = new FortressTunnelBuilder( pos ,pos.add(100 ,50 ,100));
    	System.out.println( "Generation finished. Starting placement...");
		int px = pos.getX();
		int py = pos.getY();
		int pz = pos.getZ();

		for( int x=0; x<120; x++) {
			for( int z=0; z<120; z++) {
				for( int y=0; y<70; y++) {
//					BlockPos cpos = new BlockPos( x ,y ,z);
					BlockPos worldPos = new BlockPos( x+px ,y+py ,z+pz);
					IBlockState bs = sg.getBlockState( worldPos);
					if( bs != null) {
						world.setBlockState( worldPos ,bs);
					}
					++count;
					if(count > 100000) {
						count = 0;
						++total;
						System.out.println( "Total count: " + total);
					}
				}	    		
			}
		}
    	System.out.println( "Placement finished.");
    }
	
	
	

	
 //------------------------------  Public Interface  ----------------------------------------------------//   
	/* I chose this structure to allow Teinte full access to the various Managers/Handlers, while shielding their public
	 * methods from other classes.
	 */
	
    public void createNewFactionTest( BlockPos pos) {
    	this.factions.add( new TestFaction( pos));
    }
	
	
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
	
	
	public void registerTileLogic( TileLogic be) {
		this.tiles.add( be);
	}
	
	
	public void removeBlockEntity( BlockPos pos) {
		this.tiles.remove( pos);
	}
	
	
	public TileLogic getTileLogic( BlockPos pos) {
		return this.tiles.get(pos);
	}
	
	
	public Template getTemplate( String name) {
		return this.templates.get( null ,new ResourceLocation( MODID+":"+name));
	}
	
	
	public void addToAtmosphere( BlockPos pos ,Muir m) {
		this.atmosphere.addMuir( new ChunkPos(pos) ,m);
	}
	
	public void removeFromAtmosphere( BlockPos pos ,int amount) {
//		this.atmosphere.addMuir( new ChunkPos(pos) ,m);
//		this.atmosphere.remove();
	}
	
	public boolean acceptingMuir( BlockPos pos ,int pressure) {
		return this.atmosphere.acceptingMuir( pos ,pressure);
	}
	
	
//	public void registerTileLogicClass( TileLogic tl) {
////		this.factory.register( tl);
//		TileLogicFactory.register( tl);
//	}
	
	
//	public void spawnTileLogicIfNeeded( Block b ,BlockPos pos) {
//		this.tiles.spawnTileLogicIfNeeded( b ,pos);
//	}
	
	


	
//------------------------------  Assist Methods ------------------------------------------------------------//  
	
	//Must be called server-side
	private void logPlayerPosition() {
		
		List<EntityPlayerMP> players = MCForge.getAllPlayersOnServer();
						
		
		for( EntityPlayerMP player : players) {
    		if( !this.lastPlayerPos.containsKey(player)) {
    			this.lastPlayerPos.put( player ,new ArrayDeque<>());
    		}
    		
    		ArrayDeque<Vec3d> posQ = this.lastPlayerPos.get( player);
    		
    		if( posQ.size() > 1) {
    			posQ.poll();
    		}	    		
    		Vec3d pos = new Vec3d( player.lastTickPosX ,player.lastTickPosY ,player.lastTickPosZ);
    		posQ.add( pos);
    	}
	}	
	
	
	
	
	public void updatePlayerFogValues( float red ,float blue ,float green ,float density ,float scale) {
		this.rawColor = new Vec3d( red ,green ,blue);
		this.newFogColor = rawColor.scale( scale); 
		this.newFogDensity = density;
	}
	
	

	
	//TODO some of these need to be saved to and read from world data, otherwise every config change will impact every world.
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
		
		
		@Comment( value= {"Amount of Food consumed when dashing."})
		@Name( value="Hunger cost of Dash")
		@RangeInt( min=0 ,max=40)
		public static int dashHungerCost = 1;		
		
		
		@Comment( value= {"Allow Player Dash"})
		@Name( value="Allow Player Dash")
		public static boolean allowPlayerDash = true;
		
		
		@Comment( value= {"Players have a limited number of lives."})
		@Name( value="Limited Player Lives")
		public static boolean limitedLives = false;
		
		
		@Comment( value= {"Players share a limited number of lives."})
		@Name( value="Shared Player Lives")
		public static boolean sharedLives = false;

	}
	


	
}	
    
    
    
    
    






