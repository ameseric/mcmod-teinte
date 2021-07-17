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
import witherwar.hermetics.ElementalFluid;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.utility.BlockUtil;
import witherwar.utility.Tile;



public class ConduitTile extends ElementalFluidContainerTile {

	
	static {
		TileLogicManager.registerClass( new ConduitTile());
	}
	
	
	
	
	public ConduitTile() {
		this( new BlockPos(0,0,0));
	}	
	public ConduitTile(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,false ,1);
	}





	public ElementalFluid _takeFluid( BlockPos requesterPos ,HashSet<BlockPos> traversed) {
		traversed.add( getPos());
		
		HashMap<BlockPos ,Tile> neighbors = MCForge.getNeighbors( getPos());
		ElementalFluid input = new ElementalFluid();
		
		for( BlockPos neighborPos : neighbors.keySet()) {
			TileLogic neighbor = neighbors.get( neighborPos).logic();
			if( neighbor instanceof ElementalFluidContainer && !traversed.contains(neighborPos)) {
				input.add( ((ElementalFluidContainer) neighbor)._takeFluid( getPos() ,traversed));
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
