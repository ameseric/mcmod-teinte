package witherwar.hermetics;

import java.util.HashSet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ElementalFluidContainer {
	
	public ElementalFluid _takeFluid( BlockPos requesterPos ,HashSet<BlockPos> traversed);
	
	public ElementalFluid peekAtContents();
	
	
	
	
}
