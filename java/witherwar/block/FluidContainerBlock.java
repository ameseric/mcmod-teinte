package witherwar.block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.util.BlockUtil;

public abstract class FluidContainerBlock extends Block implements FluidContainer{

	private Fluid contents;

	
	
	
	
	public FluidContainerBlock(Material materialIn) {
		super(materialIn);
	}	

	
	
	public Fluid pullFluid( FluidContainer requester ,BlockPos pos ,World world) {
		HashMap<BlockPos ,Block> neighbors = BlockUtil.getNeighborBlocks( pos ,world);
		
		Fluid input = new Fluid();
		for( BlockPos neighborPos : neighbors.keySet()) {
			Block neighbor = neighbors.get( neighborPos);
			if( neighbor instanceof FluidContainer && neighbor != requester) {
				input.add( ((FluidContainer) neighbor).pullFluid( this ,neighborPos ,world));
			}
		}
		
		return this.cycleFluid( input);
	}
	
	
	
	public Fluid cycleFluid( Fluid input) {
		Fluid output = this.contents;
		this.contents = input;
		return output;
	}
	
	
	public boolean hasFluid() {
		return this.contents != null && !this.contents.isEmpty();
	}
	
	
}
