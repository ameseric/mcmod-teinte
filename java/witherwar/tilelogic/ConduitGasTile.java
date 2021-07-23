package witherwar.tilelogic;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.MCForge;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.hermetics.Muir;
import witherwar.hermetics.MuirGasContainer;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;
import witherwar.utility.Tile;



public class ConduitGasTile extends MuirGasContainerTile {

	
	static {
		TileLogicManager.registerClass( new ConduitGasTile());
	}
	
	
	
	
	public ConduitGasTile() {
		this( new BlockPos(0,0,0));
	}	
	public ConduitGasTile(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,true ,20);
	}


	@Override
	public void _ticklogic(World world) {
		HashMap<BlockPos ,Tile> neighbors = MCForge.getNeighbors( pos());
		
		for( BlockPos neighborPos : neighbors.keySet()) {
			TileLogic neighbor = neighbors.get( neighborPos).logic();
			if( neighbor instanceof MuirGasContainer) {
				( (MuirGasContainer) neighbor).averageWith( contents() ,pos());
			}
		}
		contents().bleedExcess( DEFAULT_MUIR_LIMIT);
	}


	
	
	
	@Override
	public String getDataName() {
		return null;
	}

	
	




	@Override
	protected NBTTagCompound __writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected void __readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

}
