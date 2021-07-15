package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.hermetics.ElementalFluid;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;



public class ConduitTile extends ElementalFluidContainerTile {

	public ConduitTile(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,TileLogic.CONDUIT_ID ,false ,1);
	}


	
	public ElementalFluid _takeFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		HashMap<BlockPos ,Block> neighbors = BlockUtil.getNeighborBlocks( myPos ,world);
		ElementalFluid input = new ElementalFluid();
		for( BlockPos neighborPos : neighbors.keySet()) {
			Block neighbor = neighbors.get( neighborPos);
			if( neighbor instanceof ElementalFluidContainer && !neighborPos.equals( requesterPos)) {
				input.add( ((ElementalFluidContainer) neighbor)._takeFluid( myPos ,neighborPos ,world));
			}
		}

		return this._cycleFluid( input);
	}
	
	
	
	@Override
	public String getDataName() {
		return null;
	}

	
	
	@Override
	public void _ticklogic(World world) {
		//passive
	}



	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

}
