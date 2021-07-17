package witherwar.tilelogic;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.hermetics.ElementalFluid;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;

public abstract class ElementalFluidContainerTile extends TileLogic implements ElementalFluidContainer{

	
	private ElementalFluid contents = new ElementalFluid();
	
	
	
	
	public ElementalFluidContainerTile(BlockPos pos, Block homeblock, boolean active ,int ticksUntilUpdate) {
		super(pos, homeblock, active ,ticksUntilUpdate);
	}	
	public ElementalFluidContainerTile() {}
	
	
	
	
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
