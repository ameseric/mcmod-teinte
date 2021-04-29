package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.block.FluidContainerBlock;
import witherwar.util.BlockUtil;

public abstract class FluidContainerBlockEntity extends BlockEntity implements FluidContainer{

	private Fluid contents = new Fluid();
	
	
	public FluidContainerBlockEntity(BlockPos pos, Block homeblock, int id, boolean active) {
		super(pos, homeblock, id, active);
	}
	
	
	
	
	public Fluid pullFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		HashMap<BlockPos ,Block> neighbors = BlockUtil.getNeighborBlocks( myPos ,world);
		Fluid input = new Fluid();
		for( BlockPos neighborPos : neighbors.keySet()) {
			Block neighbor = neighbors.get( neighborPos);
			if( neighbor instanceof FluidContainer && !neighborPos.equals( requesterPos)) {
				input.add( ((FluidContainer) neighbor).pullFluid( myPos ,neighborPos ,world));
			}
		}
		
		return this.cycleFluid( input);
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
