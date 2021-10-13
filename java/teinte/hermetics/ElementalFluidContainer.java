package teinte.hermetics;

import java.util.HashSet;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ElementalFluidContainer {
	
	public Muir _takeFluid( BlockPos requesterPos ,HashSet<BlockPos> traversed);
	
	public Muir peekAtContents();
	
	
	
	
}
