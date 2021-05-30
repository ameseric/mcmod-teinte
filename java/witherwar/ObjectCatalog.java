package witherwar;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import witherwar.block.KaliCoreBlock;
import witherwar.block.ConduitBlock;
import witherwar.block.DebugBlock;
import witherwar.block.RitualBlock;
import witherwar.entity.GhastTestEntity;
import witherwar.entity.WitherSkeletonTestEntity;
import witherwar.items.TestFireball;
import witherwar.potions.TestPotion;
import witherwar.potions.TestPotionType;
import witherwar.entity.DroneEntity;
import witherwar.tileentity.TileEntityCataromotus;
import witherwar.tileentity.TileEntityMaw;
import witherwar.tileentity.TileEntitySerpentmind;
import witherwar.block.AlchemyGeyserBlock;
import witherwar.block.AshReplicatingBlock;
import witherwar.block.BlockAsh;
import witherwar.block.BlockCatarCortex;
import witherwar.block.BlockCatarMaw;
import witherwar.block.BlockFlesh;
import witherwar.block.BlockGuidestone;


// stateless singleton
// necessary due to Minecraft's structure, not desired choice
public class ObjectCatalog {

	
	private static List<NewEntity> entitiesToRegister = new ArrayList<>();
	private static List<NewTileEntity> tileEntitesToRegister = new ArrayList<>();
	private static List<NewBlock> blocksToRegister = new ArrayList<>();
	private static List<NewItem> itemsToRegister = new ArrayList<>();
	

	
	public final static Block TERRA_KALI;
	public final static Block FLESH;
	public final static Block MAW;
	public final static Block DEAD_ASH;
	public final static Block TERRA_CATAR;
	public final static Block GUIDESTONE;
	public final static Block CONDUIT;
	public final static Block GEYSER;
	public final static Block RITUALBLOCK;
	public final static Block ASH_REPL_BLOCK;
	public final static Block DEBUG_BLOCK;

	
	
	static {
		
		entitiesToRegister.add( 	new NewEntity( 		WitherSkeletonTestEntity.class 		,"serpent_wither_skeleton" 	,2));
		entitiesToRegister.add( 	new NewEntity( 		GhastTestEntity.class 				,"motus_ghast" 				,3));
		entitiesToRegister.add( 	new NewEntity( 		DroneEntity.class					,"test_blob" 				,4));
		
		
		tileEntitesToRegister.add( 	new NewTileEntity( 	TileEntitySerpentmind.class 	,"witherwar:tile_entity_serpentmind"));
		tileEntitesToRegister.add( 	new NewTileEntity( 	TileEntityCataromotus.class 	,"witherwar:tile_entity_cataromotus"));
		tileEntitesToRegister.add( 	new NewTileEntity( 	TileEntityMaw.class 			,"witherwar:tile_entity_maw"));
		
		
		itemsToRegister.add( new NewItem());
		
		
		TERRA_KALI = 	new KaliCoreBlock();
		TERRA_CATAR = 	new BlockCatarCortex();
		FLESH = 		new BlockFlesh();
		MAW = 			new BlockCatarMaw();
		DEAD_ASH =	 	new BlockAsh();
		GUIDESTONE = 	new BlockGuidestone();
		CONDUIT =		new ConduitBlock();
		GEYSER =		new AlchemyGeyserBlock();
		RITUALBLOCK =	new RitualBlock();
		ASH_REPL_BLOCK =new AshReplicatingBlock();
		DEBUG_BLOCK =	new DebugBlock();

		
		
		blocksToRegister.add( 		new NewBlock( TERRA_KALI 	,"witherwar:terra_kali"));
		blocksToRegister.add( 		new NewBlock( TERRA_CATAR 	,"witherwar:terra_kali"));
		blocksToRegister.add( 		new NewBlock( FLESH			,"witherwar:flesh"));
		blocksToRegister.add( 		new NewBlock( MAW 			,"minecraft:nether_wart_block"));
		blocksToRegister.add( 		new NewBlock( DEAD_ASH 		,"witherwar:dead_ash"));
		blocksToRegister.add( 		new NewBlock( GUIDESTONE 	,"witherwar:guidestone"));
		blocksToRegister.add( 		new NewBlock( CONDUIT	 	,"witherwar:conduit"));
		blocksToRegister.add(		new NewBlock( GEYSER		,"witherwar:geyser"));
		blocksToRegister.add(		new NewBlock( RITUALBLOCK	,"witherwar:ritualblock"));
		blocksToRegister.add(		new NewBlock( ASH_REPL_BLOCK,"witherwar:dead_ash"));
		blocksToRegister.add(		new NewBlock( DEBUG_BLOCK	,"witherwar:debug_block"));

		
	}
	
	
	
	private ObjectCatalog() {}

	
	public static List<NewBlock> getNewBlocks(){
		return blocksToRegister;
	}
	
	
	public static List<NewEntity> getNewEntities(){
		return entitiesToRegister;
	}
	
	
	public static List<NewTileEntity> getNewTileEntities(){
		return tileEntitesToRegister;
	}
	
	
	
	//Pre-Init call only!  Client-only code followup in the client proxy for Block and Entity Rendering.
	public static void registerPreInitObjects() {
		for( NewBlock nb : getNewBlocks()) {
			ForgeRegistries.BLOCKS.register( nb.block);
			nb.item = new ItemBlock( nb.block);
			nb.item.setRegistryName( nb.block.getRegistryName());
			ForgeRegistries.ITEMS.register( nb.item);
		}
		
		for( NewItem ni : itemsToRegister) {
			ForgeRegistries.ITEMS.register( new TestFireball());
		}
		
		TestPotion test = new TestPotion();
		ForgeRegistries.POTIONS.register( test );
		ForgeRegistries.POTION_TYPES.register( 
				new PotionType( "noidea" ,new PotionEffect[] {new PotionEffect( test ,3600)}).setRegistryName( "noidea") );
		
		for( NewEntity ne : getNewEntities()) {
			ForgeRegistries.ENTITIES.register( ne.entityEntry); //followup code in client proxy
		}		
		
		for( NewTileEntity nte : getNewTileEntities()) {
			GameRegistry.registerTileEntity( nte.tileEntityClass ,nte.resourceLocation);
		}
	}
	

	
	
	
	public static class NewEntity{
		public EntityEntry entityEntry;
		public Class<? extends Entity> entityClass;
		
		public NewEntity ( Class<? extends Entity> entityClass ,String resourceLocationName ,int id) {
			this.entityClass = entityClass;
			this.entityEntry = EntityEntryBuilder.create()
	        	    .entity( entityClass)
	        	    .id( new ResourceLocation( TEinTE.MODID ,resourceLocationName), id)
	        	    .name( resourceLocationName)
	        	    .egg( 0xFFFFFF, 0xAAAAAA)
	        	    .tracker( 80, 3, false)
	        	    .build();
		}
		
	}
	
	
	
	public static class NewItem{
		
		public NewItem() {
			
		}
		
	}
	
	
	
	
	public static class NewTileEntity{
		public Class<? extends TileEntity> tileEntityClass;
		public ResourceLocation resourceLocation;
		
		public NewTileEntity( Class<? extends TileEntity> tileEntityClass ,String resourceLocationName) {
			this.tileEntityClass = tileEntityClass;
			this.resourceLocation = new ResourceLocation( resourceLocationName);
		}
	}
	
	
	
	
	public static class NewBlock{
		public Block block;
		public ItemBlock item;
		public ResourceLocation resourceLocation;
		
		public NewBlock( Block block ,String modelResourceLocation) {
			this.block = block;
			this.resourceLocation = new ResourceLocation( modelResourceLocation);
		}
	}
	
}
