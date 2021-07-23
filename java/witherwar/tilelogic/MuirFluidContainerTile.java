package witherwar.tilelogic;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.hermetics.Muir;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;

public abstract class MuirFluidContainerTile extends TileLogic implements ElementalFluidContainer{

	
	protected Muir contents = new Muir();
	
	
	
	
	public MuirFluidContainerTile(BlockPos pos, Block homeblock, boolean active ,int ticksUntilUpdate) {
		super(pos, homeblock, active ,ticksUntilUpdate);
	}	
	public MuirFluidContainerTile() {}
	
	
	
	
	protected Muir _cycleFluid( Muir input) {
		Muir output = peekAtContents();
		setContents( input);
		return output;
	}
	
	
	

	public Muir peekAtContents() {
		return this.contents;
	}
	
	
	protected void setContents( Muir f) {
		this.contents = f;
	}
	

}
