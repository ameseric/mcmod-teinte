package teinte.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teinte.TEinTE;
import teinte.tileentity.TileEntityCataromotus;

public class BlockCatarCortex extends Block{
	
	public BlockCatarCortex() {
		super( Material.ROCK);
	    setHardness(30F);
		setUnlocalizedName( "terra_catar");
		setRegistryName( "terra_catar");
		//this.setHarvestLevel("pickaxe" ,2);
		//setLightLevel(0.8F);
		//setCreativeTab( CreativeTabs.MISC);
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
		return new TileEntityCataromotus();
	}

	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityCataromotus catar = (TileEntityCataromotus) worldIn.getTileEntity( pos);
		catar.destroyForm(false);
		super.breakBlock( worldIn ,pos, state);
	}

}

