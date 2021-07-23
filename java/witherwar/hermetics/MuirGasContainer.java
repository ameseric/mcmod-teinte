package witherwar.hermetics;

import net.minecraft.util.math.BlockPos;

public interface MuirGasContainer {

	public void averageWith( Muir m ,BlockPos requester);
	
	public void add( Muir m);
	
}
