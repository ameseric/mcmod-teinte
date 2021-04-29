package witherwar.alchemy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface FluidContainer {
	
	public Fluid pullFluid( BlockPos requesterPos ,BlockPos requesteePos ,World world);
	
	
}
