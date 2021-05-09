package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;


public abstract class FluidContainerBlock extends Block implements FluidContainer ,TileLogicContainer{

	
	
	
	public FluidContainerBlock(Material materialIn) {
		super(materialIn);
	}	

	
	
	public Fluid pullFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		return ((FluidContainer) this.getTileLogic(myPos)).pullFluid( requesterPos ,myPos ,world);
	}


	
	public TileLogic getTileLogic(BlockPos pos) {
		return TEinTE.instance.getTileLogic( pos);
	}

	
	

	
	
}
