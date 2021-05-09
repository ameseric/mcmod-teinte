package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.TEinTE;
import witherwar.tileentity.AlchemyGeyserTile;
import witherwar.tileentity.ReplicatingTile;
import witherwar.tileentity.RitualBlockTile;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;


public class BlockFlesh extends DirectionalBlock implements TileLogicContainer{
	
	public static final MaterialFlesh matFlesh = new MaterialFlesh( MapColor.TNT);
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	
	public BlockFlesh() {
        super( matFlesh);
	    setHardness(8F);
		setUnlocalizedName( "flesh");
		setSoundType( SoundType.SLIME);
		setRegistryName( "flesh");
//        this.setDefaultState( this.blockState.getBaseState());
        this.setHarvestLevel("axe" ,1);
		//setCreativeTab( CreativeTabs.BUILDING_BLOCKS);
		setCreativeTab( TEinTE.teinteTab);
//"nether_wart_block", (new Block(Material.GRASS, MapColor.RED)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("netherWartBlock"));
	}
	
	
	@Override
	public EnumBlockRenderType getRenderType( IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}
	
	@SideOnly( Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	@Override
	public boolean hasTileEntity( IBlockState state) {
		return false;
	}


	@Override
	public TileLogic getTileLogic(BlockPos pos) {
		return null;
	}
	
	

	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerBlockEntity( new ReplicatingTile( pos)); //TODO consider comparing classes for TileLogic
	}
	

	



}




class MaterialFlesh extends Material{

	public MaterialFlesh(MapColor color) {
		super(color);
		this.setRequiresTool();
	}
	
}