package teinte.faction2.structures;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class HailMary {

	
	
	BlockPos start = new BlockPos( 0,0,0);
	BlockPos end = new BlockPos( 180,100,180);
	
	ArrayList<Subdivision> rooms;
	
	int layerSize = 16;
	
	private static final Random RNG = new Random();
	
	
	IBlockState mainBT = Blocks.NETHER_BRICK.getDefaultState();
	
	
	
	public HailMary() {
		
		int sa = 20 / 2;
		int sb = 16 / 2;
		int xStart = (this.getXSize() / 2) - sa;
		int zStart = (this.getZSize() / 2) - sb;
		
		BlockPos firstRoomStart = new BlockPos( xStart ,0 ,zStart);
		BlockPos firstRoomEnd = new BlockPos( (this.getXSize() / 2) + sa ,this.layerSize ,(this.getZSize() / 2) + sb);
		Subdivision startRoom = new Subdivision( firstRoomStart ,firstRoomEnd);
		startRoom.addDoor( EnumFacing.UP ,false);
		startRoom.addDoor( EnumFacing.NORTH ,false);
		startRoom.addDoor( EnumFacing.SOUTH ,false);
		this.branchRoomsFromThis( startRoom);
	}
	
	
	
	
	private void branchRoomsFromThis( Subdivision s) {
		for( BlockPos door : s.getDoors()) {
			
		}			
	}
	
	
	
	
	private int getRandomRoomSize() {
		int min = 6;
		int max = 16;
		return RNG.nextInt(max - min) + min;
	}
	
	
	public int getXSize() {
		return start.getX() - end.getX();
	}	
	public int getZSize() {
		return start.getZ() - end.getZ();
	}
	public int getHeight() {
		return start.getY() - end.getY();
	}
	
	
	
	

	
	
	
}


//TODO: Subdivision class, extends to Room class?
class Subdivision {
	BlockPos start ,end;
	ArrayList<BlockPos> doors = new ArrayList<>();
	int xHalf ,zHalf;
	
	public Subdivision( BlockPos start ,BlockPos end) {
		this.start = start;
		this.end = end;
		this.xHalf = (end.getX() - start.getX()) / 2;
		this.zHalf = (end.getZ() - start.getZ()) / 2;
	}
	
	public void addDoor( BlockPos pos) {
		this.doors.add( pos);
	}
	
	public void addDoor( EnumFacing side ,boolean decenter) {
		BlockPos pos = new BlockPos(this.start.getX() ,this.start.getY() ,this.start.getZ());
		switch( side) {
			case EAST: 
				pos.add( start.getX() ,0 ,this.zHalf);
			case WEST:
				pos.add( end.getX() ,0 ,this.zHalf);
			case NORTH:
				pos.add( this.xHalf ,0 ,this.start.getZ());
			case SOUTH:
				pos.add( this.xHalf ,0 ,this.end.getZ());
			case UP:
				pos.add( this.xHalf ,this.end.getY() ,this.zHalf);
				
		}
		this.addDoor( pos);		
	}
	
	public ArrayList<BlockPos> getDoors() {
		return this.doors;
	}
	
	
	
	
}






