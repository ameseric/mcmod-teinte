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
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;
import witherwar.utility.Tile;



public class ConduitFluidTile extends MuirFluidContainerTile {

	
	static {
		TileLogicManager.registerClass( new ConduitFluidTile());
	}
	
	
	
	
	public ConduitFluidTile() {
		this( new BlockPos(0,0,0));
	}	
	public ConduitFluidTile(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,false ,1);
	}





	public Muir _takeFluid( BlockPos requesterPos ,HashSet<BlockPos> traversed) {
		traversed.add( pos());
		
		HashMap<BlockPos ,Tile> neighbors = MCForge.getNeighbors( pos());
		Muir input = new Muir();
		
		for( BlockPos neighborPos : neighbors.keySet()) {
			TileLogic neighbor = neighbors.get( neighborPos).logic();
			if( neighbor instanceof ElementalFluidContainer && !traversed.contains(neighborPos)) {
				input.add( ((ElementalFluidContainer) neighbor)._takeFluid( pos() ,traversed));
			}
		}
		return _cycleFluid( input);
	}
	
	
	
	@Override
	public String getDataName() {
		return null;
	}

	
	
	@Override
	public void _ticklogic(World world) {}



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
