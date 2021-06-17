package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.tileentity.GrowingTile;
import witherwar.tileentity.MonumentCell;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;





public class DebugBlock extends Block implements TileLogicContainer{

	
	
	
	public DebugBlock() {
		super( Material.ROCK);
		setUnlocalizedName("debug_block");
		setRegistryName("debug_block");
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerBlockEntity( new MonumentCell( pos));
		System.out.println( "Placed debug block.");
	}





	@Override
	public TileLogic getTileLogic(BlockPos pos) {
		return TEinTE.instance.getTileLogic( pos);
	}

	
	
}
