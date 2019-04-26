package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import witherwar.WitherWar;
import witherwar.tileentity.TileEntitySerpentmind;


public class BlockGuidestone extends Block{
	
	public BlockGuidestone() {
		super( Material.ROCK);
		setUnlocalizedName( "guidestone");
		setRegistryName( "guidestone");
		setCreativeTab( WitherWar.wwCreativeTab);
	}

	@Override
	public boolean hasTileEntity( IBlockState state) {
		return true;
	}
	
	@Override
	public TileEntity createTileEntity( World world ,IBlockState state) {
		return new TileEntitySerpentmind();
	}
	
}