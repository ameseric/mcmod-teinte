package witherwar;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import witherwar.block.BlockSerpentmind;
import witherwar.entity.EntityMotusGhast;
import witherwar.entity.EntitySerpentWither;
import witherwar.tileentity.TileEntityCataromotus;
import witherwar.tileentity.TileEntityMaw;
import witherwar.tileentity.TileEntitySerpentmind;
import witherwar.block.BlockAsh;
import witherwar.block.BlockCatarCortex;
import witherwar.block.BlockCatarMaw;
import witherwar.block.BlockFlesh;
import witherwar.block.BlockGuidestone;


// stateless singleton
public class ObjectCatalog {

	
	private static List<NewEntity> entitiesToRegister = new ArrayList<>();
	private static List<NewTileEntity> tileEntitesToRegister = new ArrayList<>();
	private static List<NewBlock> blocksToRegister = new ArrayList<>();
	

	
	public final static Block TERRA_KALI;
	public final static Block FLESH;
	public final static Block MAW;
	public final static Block DEAD_ASH;
	public final static Block TERRA_CATAR;
	public final static Block GUIDESTONE;
	
	
	static {
		
		entitiesToRegister.add( 	new NewEntity( 		EntitySerpentWither.class 		,"serpent_wither_skeleton" 	,2));
		entitiesToRegister.add( 	new NewEntity( 		EntityMotusGhast.class 			,"motus_ghast" 				,3));
		
		
		tileEntitesToRegister.add( 	new NewTileEntity( 	TileEntitySerpentmind.class 	,"witherwar:tile_entity_serpentmind"));
		tileEntitesToRegister.add( 	new NewTileEntity( 	TileEntityCataromotus.class 	,"witherwar:tile_entity_cataromotus"));
		tileEntitesToRegister.add( 	new NewTileEntity( 	TileEntityMaw.class 			,"witherwar:tile_entity_maw"));
		
		
		TERRA_KALI = 	new BlockSerpentmind();
		TERRA_CATAR = 	new BlockCatarCortex();
		FLESH = 		new BlockFlesh();
		MAW = 			new BlockCatarMaw();
		DEAD_ASH =	 	new BlockAsh();
		GUIDESTONE = 	new BlockGuidestone();
		
		
		blocksToRegister.add( 		new NewBlock( "terra_kali"		,TERRA_KALI 	,"witherwar:terra_kali"));
		blocksToRegister.add( 		new NewBlock( "terra_catar"		,TERRA_CATAR 	,"witherwar:terra_kali"));
		blocksToRegister.add( 		new NewBlock( "flesh"			,FLESH 		,"minecraft:nether_wart_block"));
		blocksToRegister.add( 		new NewBlock( "terra_catar_maw"	,MAW 			,"minecraft:nether_wart_block"));
		blocksToRegister.add( 		new NewBlock( "dead_ash"		,DEAD_ASH 		,"witherwar:dead_ash"));
		blocksToRegister.add( 		new NewBlock( "guidestone"		,GUIDESTONE 	,"minecraft:glowstone"));
		
		
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
	
	
	
	
	public static class NewTileEntity{
		public Class<? extends TileEntity> tileEntityClass;
		public ResourceLocation resourceLocation;
		
		public NewTileEntity( Class<? extends TileEntity> tileEntityClass ,String resourceLocationName) {
			this.tileEntityClass = tileEntityClass;
			this.resourceLocation = new ResourceLocation( resourceLocationName);
		}
	}
	
	
	
	
	public static class NewBlock{
		public String name;
		public Block block;
		public ItemBlock item;
		public ResourceLocation resourceLocation;
		
		public NewBlock( String name ,Block block ,String modelResourceLocation) {
			this.name = name;
			this.block = block;
			this.resourceLocation = new ResourceLocation( modelResourceLocation);
		}
	}
	
}
