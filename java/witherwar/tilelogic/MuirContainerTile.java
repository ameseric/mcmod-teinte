package witherwar.tilelogic;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import witherwar.hermetics.MuirContainer;

public abstract class MuirContainerTile extends TileLogic implements MuirContainer{


	
	public MuirContainerTile(BlockPos pos, Block homeblock, boolean active ,int ticksUntilUpdate) {
		super( pos ,homeblock ,active ,ticksUntilUpdate);			
	}
	public MuirContainerTile() {}
	
	
	
	
	
	
	



}
