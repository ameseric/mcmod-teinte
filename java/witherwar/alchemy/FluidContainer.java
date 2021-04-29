package witherwar.alchemy;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface FluidContainer {
	
	public Fluid pullFluid( FluidContainer requester ,BlockPos pos ,World world);
	
	public boolean hasFluid();
	
	
}
