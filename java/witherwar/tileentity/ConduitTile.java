package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.utility.BlockUtil;



public class ConduitTile extends FluidContainerTile {

	public ConduitTile(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,TileLogic.CONDUIT_ID ,false ,1);
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
	
	
	
	@Override
	public String getDataName() {
		return null;
	}

	
	
	@Override
	public void ticklogic(World world) {
		//passive
	}



	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

}
