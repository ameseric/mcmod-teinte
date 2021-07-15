package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.hermetics.ElementalFluid;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;


public abstract class FluidContainerBlock extends Block implements ElementalFluidContainer ,TileLogicContainer{

	
	
	
	public FluidContainerBlock(Material materialIn) {
		super(materialIn);
	}	

	
	
	public ElementalFluid _takeFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		return ((ElementalFluidContainer) this.getTileLogic(myPos))._takeFluid( requesterPos ,myPos ,world);
	}


	
	public TileLogic getTileLogic(BlockPos pos) {
		return TEinTE.instance.getTileLogic( pos);
	}

	

	
	
}
