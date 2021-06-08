package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.TEinTE;
import witherwar.tileentity.deprecated.TileEntityMaw;
import witherwar.tileentity.deprecated.TileEntitySerpentmind;

public class BlockCatarMaw extends Block{
	
	public BlockCatarMaw() {
		super( Material.ROCK);
	    setHardness(30F);
		setUnlocalizedName( "terra_catar_maw");
		setRegistryName( "terra_catar_maw");
		//this.setHarvestLevel("pickaxe" ,2);
		//setLightLevel(0.8F);
		//setCreativeTab( CreativeTabs.BUILDING_BLOCKS);
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	@SideOnly( Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	
	@Override
	public EnumBlockRenderType getRenderType( IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean hasTileEntity( IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity( World world ,IBlockState state) {
		return new TileEntityMaw();
	}

	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityMaw maw = (TileEntityMaw) worldIn.getTileEntity( pos);
		maw.destroyBranch();		
		super.breakBlock( worldIn ,pos, state);
	}

}



/**
public BlockChunkLoader() {
    super(Material.ROCK);
    setHardness(20F);
    setDefaultState(getDefaultState().withProperty(TYPE, Type.BLOCK));
    setResistance(100F);
    setSoundType(SoundType.STONE);
    setUnlocalizedName(ChickenChunks.MOD_ID + ":chunk_loader");
    setCreativeTab(CreativeTabs.MISC);
}**/