package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.block.FluidContainerBlock;
import witherwar.util.BlockUtil;

public abstract class FluidContainerTile extends TileLogic implements FluidContainer{

	private Fluid contents = new Fluid();
	
	
	public FluidContainerTile(BlockPos pos, Block homeblock, int id, boolean active ,int ticksPerCycle) {
		super(pos, homeblock, id, active ,ticksPerCycle);
	}
	
	
	public Fluid cycleFluid( Fluid input) {
		Fluid output = this.contents;
		this.contents = input;
		return output;
	}
	

	public Fluid getContents() {
		return this.contents;
	}
	
	
	public void setContents( Fluid f) {
		this.contents = f;
	}
	

}
