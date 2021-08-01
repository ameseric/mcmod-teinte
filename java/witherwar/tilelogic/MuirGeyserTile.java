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
import witherwar.hermetics.MuirElement;
import witherwar.hermetics.MuirGasContainer;
import witherwar.utility.Tile;

public class MuirGeyserTile extends MuirGasContainerTile{
	
	private static final int PRESSURE = 10000;
	
	
	
	public MuirGeyserTile() {
		this( new BlockPos(0,0,0));
	}
	public MuirGeyserTile(BlockPos pos) {
		super(pos ,ObjectCatalog.GEYSER ,true ,20);
		this.contents = Muir.empty();//Muir.random();
		this.contents.add( MuirElement.B ,2000);
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
			}else {
				if( TEinTE.instance.acceptingMuir( pos() ,PRESSURE)) {
					TEinTE.instance.addToAtmosphere( pos() ,contents());
				}
			}
		}
	}
	
	
	@Override
	public void averageWith( Muir m ,BlockPos requester) {}	
	
	@Override
	public void add( Muir m) {}




	@Override
	protected NBTTagCompound __writeToNBT(NBTTagCompound nbt) {
		this.contents.writeToNBT( nbt);
		return nbt;
	}

	@Override
	protected void __readFromNBT(NBTTagCompound nbt) {
		this.contents.readFromNBT(nbt);		
	}

}
