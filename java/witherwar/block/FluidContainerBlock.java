package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.tileentity.BlockEntity;
import witherwar.tileentity.BlockEntityContainer;


public abstract class FluidContainerBlock extends Block implements FluidContainer ,BlockEntityContainer{

	
	
	
	public FluidContainerBlock(Material materialIn) {
		super(materialIn);
	}	

	
	
	public Fluid pullFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		return ((FluidContainer) this.getBlockEntity(myPos)).pullFluid( requesterPos ,myPos ,world);
	}


	
	public BlockEntity getBlockEntity(BlockPos pos) {
		return TEinTE.instance.getBlockEntity( pos);
	}

	
	

	
	
}
