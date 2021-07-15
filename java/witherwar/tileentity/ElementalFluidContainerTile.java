package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.block.FluidContainerBlock;
import witherwar.hermetics.ElementalFluid;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;

public abstract class ElementalFluidContainerTile extends TileLogic implements ElementalFluidContainer{

	private ElementalFluid contents = new ElementalFluid();
	
	
	public ElementalFluidContainerTile(BlockPos pos, Block homeblock, int id, boolean active ,int ticksPerCycle) {
		super(pos, homeblock, id, active ,ticksPerCycle);
	}
	
	
	protected ElementalFluid _cycleFluid( ElementalFluid input) {
		ElementalFluid output = peekAtContents();
		setContents( input);
		return output;
	}
	
	
	

	public ElementalFluid peekAtContents() {
		return this.contents;
	}
	
	
	protected void setContents( ElementalFluid f) {
		this.contents = f;
	}
	

}
