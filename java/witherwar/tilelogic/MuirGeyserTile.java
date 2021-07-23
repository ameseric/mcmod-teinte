package witherwar.tilelogic;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.MCForge;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.hermetics.Muir;
import witherwar.hermetics.MuirGasContainer;
import witherwar.utility.Tile;

public class MuirGeyserTile extends MuirGasContainerTile{
	
	
	static {
		TileLogicManager.registerClass( new MuirGeyserTile());
	}
	
	
	public MuirGeyserTile() {}

	public MuirGeyserTile(BlockPos pos) {
		super(pos ,ObjectCatalog.GEYSER ,true ,200);
		this.contents = Muir.random();
		System.out.println( contents());
	}
	
	
	
	

	@Override
	public String getDataName() {
		return null;
	}

	@Override
	public void _ticklogic(World world) {
		HashMap<BlockPos ,Tile> neighbors = MCForge.getNeighbors( pos());
		
		for( BlockPos neighborPos : neighbors.keySet()) {
			TileLogic neighbor = neighbors.get( neighborPos).logic();
			if( neighbor instanceof MuirGasContainer) {
				( (MuirGasContainer) neighbor).add( contents());
				System.out.println( "Adding contents...");
			}
		}
	}
	
	
	@Override
	public void averageWith( Muir m ,BlockPos requester) {}	
	
	@Override
	public void add( Muir m) {}




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
