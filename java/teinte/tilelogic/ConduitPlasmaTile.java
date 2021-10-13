package teinte.tilelogic;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.MCForge;
import teinte.ObjectCatalog;
import teinte.TEinTE;
import teinte.hermetics.ElementalFluidContainer;
import teinte.hermetics.Muir;
import teinte.hermetics.MuirContainer;
import teinte.utility.BlockUtil;
import teinte.utility.Tile;



public class ConduitPlasmaTile extends MuirContainerTile {
	
	private int txLoss = 1;

	
	static {
		TileLogicManager.registerClass( new ConduitPlasmaTile());
	}
	
	
	
	
	public ConduitPlasmaTile() {
		this( new BlockPos(0,0,0));
	}	
	public ConduitPlasmaTile(BlockPos pos) {
		super( pos ,ObjectCatalog.CONDUIT ,false ,1);
	}





	public Muir _takeMatter( int numOfUnits ,HashSet<BlockPos> traversed ,BlockPos requester ) {
		traversed.add( pos());
		
		HashMap<BlockPos ,Tile> neighbors = MCForge.getNeighbors( pos());
		Muir input = new Muir();
		
		for( BlockPos neighborPos : neighbors.keySet()) {
			TileLogic neighbor = neighbors.get( neighborPos).logic();
			if( neighbor instanceof MuirContainer && !traversed.contains(neighborPos)) {
				input.add( ((MuirContainer) neighbor)._takeMatter( numOfUnits+this.txLoss ,traversed ,pos()));
			}
		}
		return null;
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
