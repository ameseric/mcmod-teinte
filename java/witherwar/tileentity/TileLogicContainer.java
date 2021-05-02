package witherwar.tileentity;

import net.minecraft.util.math.BlockPos;

public interface TileLogicContainer {

	public TileLogic getBlockEntity( BlockPos pos);
	
}
