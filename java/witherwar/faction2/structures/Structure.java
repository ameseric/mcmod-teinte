package witherwar.faction2.structures;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import witherwar.utility.Pair;

public abstract class Structure {

	
	
	public abstract ArrayList<Pair<BlockPos,Block>> getNextSegment();
	
	
	
}
