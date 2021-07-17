package witherwar.hermetics;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ElementalFluidContainer {
	
	public ElementalFluid _takeFluid( BlockPos requesterPos);
	
	public ElementalFluid peekAtContents();
	
	
	
	
}
