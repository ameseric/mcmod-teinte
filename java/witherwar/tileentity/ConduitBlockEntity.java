package witherwar.tileentity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;



public class ConduitBlockEntity extends FluidContainerBlockEntity {

	public ConduitBlockEntity(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,BlockEntity.CONDUIT_ID ,false);
	}

	
	
	@Override
	public String getDataName() {
		return null;
	}

	
	
	@Override
	public void ticklogic(World world) {
		//passive
	}

}
