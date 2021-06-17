package witherwar.worlds.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

//NOTE: The "Blocks" used in this generator are not minecraft blocks.


public class StructureGenerator {
	
	HashMap<BlockPos ,Block> blocks;
	private ArrayList<Room> rooms = new ArrayList<>();

	BlockPos start ,end;	
	int layerSize = 7;	
	private static final IBlockState DEFAULT = Blocks.NETHER_BRICK.getDefaultState();
	private static final Random RNG = new Random();

	
	
	public StructureGenerator( BlockPos start ,BlockPos end) {
		this.start = start;
		this.end = end;		
		
		this.initializeBlocks();		
		
		int xStart = (this.getXSize() / 2) - 5; //TODO formalize
		int zStart = (this.getZSize() / 2) - 3;
		
		BlockPos firstRoomStart = new BlockPos( xStart ,0 ,zStart);
		BlockPos firstRoomEnd = new BlockPos( (this.getXSize() / 2) + 4 ,this.layerSize ,(this.getZSize() / 2) + 2);
		Room startRoom = new Room( firstRoomStart ,firstRoomEnd);
		startRoom.addDoor( EnumFacing.UP ,false);
		startRoom.addDoor( EnumFacing.NORTH ,false);
		startRoom.addDoor( EnumFacing.SOUTH ,false);
		startRoom.tryToClaim( this.blocks);
		this.addRoom( startRoom);
		this.branchRooms( 0);
		this.cleanDoors();
	}
	
	
	
	private void branchRooms( int index) {
		if( index >= this.rooms.size()) {
			return;
		}
		
		Room currentRoom = this.rooms.get( index);
		Room newRoom;
		Iterator< Door> iter;
		
		for( iter = currentRoom.doors().iterator(); iter.hasNext();) { //TODO I hate this
			Door door = iter.next();
			int failures = 0;
			do {
				newRoom = this.newRandomRoom();
				newRoom.findStartFromDoor( door);
				if( newRoom.tryToClaim( this.blocks)) {
					newRoom.addDoor( door.facing.getOpposite() ,false);
					this.addRoom( newRoom);
					break;
				}else{
					failures++;
				}
			
			}while( failures < 2);
			
			if( failures >= 2) {
				iter.remove();
			}
			
		}
		this.branchRooms( index++);
	}
	
	
	
	private Room newRandomRoom() {
		return new Room( new BlockPos(0 ,0 ,0) 
				,new BlockPos( this.getRandomRoomSize() ,this.layerSize ,this.getRandomRoomSize()));
	}
	
	
	
	private int getRandomRoomSize() {
		double r = RNG.nextDouble();
		if( r <= 0.25) { return 3;}
		if( r <= 0.5) { return 5;}
		if( r <= 0.75) { return 7;}
		return 9;
	}
	
	
	
	private void cleanDoors() {
		for( Room room : this.rooms) {
			for( Door door : room.doors()) {
				this.getBlock( door.location).blockstate = Blocks.AIR.getDefaultState();
			}
		}
	}
	
	
	
	
	private void initializeBlocks() {
		for( int x=this.start.getX(); x<this.end.getX(); x++) {
			for( int y=this.start.getY(); y<this.end.getY(); y++) {
				for( int z=this.start.getZ(); z<this.end.getZ(); z++) {
					BlockPos pos = new BlockPos( x ,y ,z);
					this.blocks.put( pos ,new Block( pos));
				}
			}
		}
	}
	
	
	
	private void addRoom( Room room) {
		this.rooms.add( room);
	}
	
	
	public IBlockState getBlockState( BlockPos pos) {
		return this.getBlock( pos).blockstate;
	}
	
	
	public IBlockState getTranslatedBlockState( int x ,int y ,int z) {
		return this.getBlockState( new BlockPos( x>>2 ,y>>2 ,z>>2));
	}
	public IBlockState getTranslatedBlockState( BlockPos pos) {
		return this.getTranslatedBlockState( pos.getX() ,pos.getY() ,pos.getZ());
	}
	
	public Block getBlock( BlockPos pos) {
		return this.blocks.get( pos);
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




//assuming 1 Block = 4 actual blocks
class Block {
	
	BlockSegment parent;
	IBlockState blockstate = Blocks.AIR.getDefaultState();
	BlockPos position;
	
	
	public Block( BlockPos pos) {
		this.position = pos;
	}
	
	
	public void claim( BlockSegment bs) {
		this.parent = bs;
	}
	
	public boolean isClaimed() {
		return this.parent == null;
	}
	
	public void setState( IBlockState bs) {
		this.blockstate = bs;
	}
	
	
	public int x() {
		return this.position.getX();
	}
	public int y() {
		return this.position.getY();
	}
	public int z() {
		return this.position.getZ();
	}
	
	
}



class Door {
	
	public BlockPos location;
	public EnumFacing facing;
	
	
	public Door( BlockPos location ,EnumFacing facing) {
		this.location = location;
		this.facing = facing;
	}
	
}


//assumed to have odd-numbered sizes, will always have center.
class BlockSegment {
	BlockPos start ,end;
	int xPartial ,zPartial ,height ,dx ,dz;
	
	public BlockSegment( BlockPos start ,BlockPos end) {
		this.start = start;
		this.end = end;
		this.dx = end.getX() - start.getX();
		this.dz = end.getZ() - start.getZ();
		this.xPartial = dx / 2;
		this.zPartial = dz / 2;
		this.height = (end.getY() - start.getY());

	}
	
	
	public void setStartPos( BlockPos pos) {
		this.start = pos;
		this.calculateEndPos();
	}
	
	
	private void calculateEndPos() {
		int xf = this.start.getX() + this.xPartial * 2;
		int zf = this.start.getZ() + this.zPartial * 2;
		int yf = this.start.getY() + this.height;
		this.end = new BlockPos( xf ,yf ,zf);
	}
	
	
	public int xCenter() {
		return this.start.getX() + this.xPartial;
	}
	public int zCenter() {
		return this.start.getZ() + this.zPartial;
	}

	
	
	
	public boolean tryToClaim( HashMap<BlockPos,Block> blocks) {
		BlockPos pos = new BlockPos(0,0,0);
		Block block;
		ArrayList<Block> toClaim = new ArrayList<>();
		for( int x=this.start.getX(); x<this.end.getX(); x++) {
			for( int y=this.start.getY(); y<this.end.getY(); y++) {
				for( int z=this.start.getZ(); z<this.end.getZ(); z++) {
					block = blocks.get( pos.add( x ,y ,z));
					if( block == null || block.isClaimed()) {
						return false;
					}else {
						toClaim.add( block);
					}
				}
			}
		}
		
		for( Block b : toClaim) {
			b.claim( this);
			this.setIfEdgeBlock( b);
		}
		
		return true;
	}
	
	
	private void setIfEdgeBlock( Block b) {
		if( ( b.x() == this.start.getX() || b.x() == this.end.getX() ) 
				&& ( b.y() == this.start.getY() || b.y() == this.end.getY() ) 
				&& (b.z() == this.start.getZ() || b.z() == this.end.getZ()) ) {
			b.setState( Blocks.NETHER_BRICK.getDefaultState());
		}
	}
	

}


class Room extends BlockSegment{
	
	ArrayList<Door> doors = new ArrayList<>();
	

	public Room(BlockPos start, BlockPos end) {
		super(start, end);
	}
	
	
	public void addDoor( Door door) {
		this.doors.add( door);
	}
	
	
	public void addDoor( EnumFacing side ,boolean decenter) {
		BlockPos pos = new BlockPos(this.start.getX() ,this.start.getY() ,this.start.getZ());
		switch( side) {
			case EAST: 
				pos.add( end.getX() ,0 ,this.zPartial);
			case WEST:
				pos.add( start.getX() ,0 ,this.zPartial);
			case NORTH:
				pos.add( this.xPartial ,0 ,this.start.getZ());
			case SOUTH:
				pos.add( this.xPartial ,0 ,this.end.getZ());
			case UP:
				pos.add( this.xPartial ,this.end.getY() ,this.zPartial);
				
		}
		this.addDoor( new Door( pos ,side));		
	}
	
	
	public void findStartFromDoor( Door door){
		BlockPos roomStart = door.location;
		
		switch( door.facing) {
			case EAST: roomStart.add( 1 ,-this.zPartial ,0);
			case WEST: roomStart.add( -this.dx ,-this.zPartial ,0);
			case NORTH: roomStart.add( -this.xPartial ,0 ,-this.dz);
			case SOUTH: roomStart.add( -this.xPartial ,0 ,1);
			case UP: roomStart.add( -this.xPartial ,0 ,-this.zPartial);
		}
		this.setStartPos( roomStart);
	}
	
	
	public ArrayList<Door> doors() {
		return this.doors;
	}
	
	

	
	
}














