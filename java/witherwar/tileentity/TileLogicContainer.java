package witherwar.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TileLogicContainer {

	public TileLogic getTileLogic( BlockPos pos);
	
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state);

	
}
