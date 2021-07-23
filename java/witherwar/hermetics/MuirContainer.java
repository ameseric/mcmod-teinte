package witherwar.hermetics;

import java.util.HashSet;

import net.minecraft.util.math.BlockPos;

public interface MuirContainer {

	public Muir _takeMatter( int numOfUnits ,HashSet<BlockPos> traversed ,BlockPos requester);
	
	
	
}
