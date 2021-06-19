package witherwar.worlds.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import witherwar.TEinTE;
import witherwar.utility.Pair;

//NOTE: The "Blocks" used in this generator are not minecraft blocks.


public abstract class StructureBuilder {
	
	private HashMap<BlockPos ,Block> blocks = new HashMap<>();
	private ArrayList<Room> rooms = new ArrayList<>();

	private BlockPos start ,end;	
	private int layerSize = 7;
	
	private HashSet<Pair<Integer,Integer>> supportcolumn = new HashSet<>();
	
	
	
	
	public StructureBuilder( BlockPos start ,BlockPos end) {
		this.start = new BlockPos( start.getX()>>1 ,start.getY()>>1 ,start.getZ()>>1);
		this.end = new BlockPos( end.getX()>>1 ,end.getY()>>1 ,end.getZ()>>1);
		
		this.initializeBlocks();
		
		
	}
	
	
	
	
	protected void build() {
		BlockPos firstRoomStart = this.start.add( this.getXSize()/2 ,0 ,this.getZSize()/2);
		BlockPos firstRoomEnd = firstRoomStart.add( 10 ,this.layerSize ,8);
		
		Room startRoom = new Room( firstRoomStart ,firstRoomEnd);
//		startRoom.addDoor( EnumFacing.UP ,false);
		startRoom.addDoor( EnumFacing.NORTH ,false ,false);
		startRoom.addDoor( EnumFacing.SOUTH ,false ,true);
		startRoom.addDoor( EnumFacing.EAST ,false ,false);
		startRoom.addDoor( EnumFacing.WEST ,false ,false);
		
		if( !startRoom.tryToClaim( this.blocks)) {
			System.out.println("Something has gone terribly wrong....");
		}
		this.addRoom( startRoom);
		//end starting room
		System.out.println( "Starting dimension: " + this.start + " " + this.end);

		
		this.branchRooms( 0);
		this.cleanup();
		System.out.println( "Finished");
	}
	
	
	
	
	protected void cleanup() {

		this.postprocess();
		
		for( int y=this.end.getY(); y>=this.start.getY(); y--) { //start at ceiling and work down
			for( int x=this.start.getX(); x<=this.end.getX(); x++) {
				for( int z=this.start.getZ(); z<=this.end.getZ(); z++) {
					BlockPos pos = new BlockPos( x ,y ,z);
					Block block = this.getBlock(pos);
					this.blockPostProcess( block);
					if( block.isSolid()) {
						this.supportcolumn.add( new Pair<Integer,Integer>(x,z));
					}
				}
			}
		}
		
		this.cleanDoors();
		
	}
	
	/**
	 * Called automatically by the parent class after build finishes.
	 */
	abstract void postprocess();
	
	/**
	 * Same as postprocess, but called for every block.
	 * 
	 * @param block
	 */
	abstract void blockPostProcess( Block block);
	
	
	
	
	private void branchRooms( int index) {
		if( index >= this.rooms.size()) {//this.rooms.size()) {
			return;
		}
		
		Room currentRoom = this.rooms.get( index);
		Room newRoom;
		Iterator< Door> iter;
		final int FAILURE_LIMIT = 2;
		
		for( iter = currentRoom.doors().iterator(); iter.hasNext();) {
			Door door = iter.next();
			if( door.isSource()) { 
				continue;
			}
			
			int failures = 0;
			while( true) {
				newRoom = this.newRandomRoom();
				newRoom.findStartFromDoor( door);
				if( newRoom.tryToClaim( this.blocks)) {
					newRoom.addDoor( door.facing.getOpposite() ,false ,true);
					newRoom.addRandomDoors(door.facing.getOpposite());
					this.addRoom( newRoom);
//					this.registerXZColumns( newRoom);
					break;
				}else{
					++failures;
				}
				
				if( failures >= FAILURE_LIMIT) {
//					System.out.println( "Failed on side: " + door.facing);
					iter.remove();
					break;
				}			
			}			
		}
		this.branchRooms( ++index);
	}
	
	
	
	private Room newRandomRoom() {
		return new Room( new BlockPos(0 ,0 ,0) 
				,new BlockPos( this.getRandomRoomSize()-1 ,this.layerSize ,this.getRandomRoomSize()-1));
	}
	
	
	
	private int getRandomRoomSize() {
		double r = TEinTE.RNG.nextDouble();
		if( r <= 0.25) { return 7;}
		if( r <= 0.5) { return 9;}
		if( r <= 0.75) { return 11;}
		return 13;
	}
	
	
	
	private void cleanDoors() {
		for( Room room : this.rooms) {
			for( Door door : room.doors()) {
//				this.getBlock( door.location).blockstate = Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState();
				BlockPos pos = door.location;
//				System.out.println( "Position: " + pos + "  Facing: " + door.facing);
				this.getBlock( pos).blockstate = Blocks.AIR.getDefaultState();
			}
		}
	}
	
	
	
	
	private void initializeBlocks() {
		for( int x=this.start.getX(); x<=this.end.getX(); x++) {
			for( int y=this.start.getY(); y<=this.end.getY(); y++) {
				for( int z=this.start.getZ(); z<=this.end.getZ(); z++) {
					BlockPos pos = new BlockPos( x ,y ,z);
					this.blocks.put( pos ,new Block( pos));
				}
			}
		}
	}
	
	
	
//	private void registerXZColumns( Room room) {
//		for( int x=room.start.getX(); x<=room.end.getX(); x++) {
//			for( int z=room.start.getZ(); z<=room.end.getZ(); z++) {
//				this.supportcolumn[x][z] = true;
//			}
//		}
//	}
	
	
	
	private void addRoom( Room room) {
		this.rooms.add( room);
	}
	
	
	//======================= Public =====================================================
	
	public IBlockState getBlockState( BlockPos pos) {
		Block block = this.getBlock( pos);
		return block != null ? block.blockstate : null;
	}
	
	
	
	public IBlockState getTranslatedBlockState( int x ,int y ,int z) {
		return this.getBlockState( new BlockPos( x>>1 ,y>>1 ,z>>1));
	}
	public IBlockState getTranslatedBlockState( BlockPos pos) {
		return this.getTranslatedBlockState( pos.getX() ,pos.getY() ,pos.getZ());
	}
	
	
	
	public Block getBlock( BlockPos pos) {
		return this.blocks.get( pos);
	}
	
	
	
	public int getXSize() {
		return end.getX() - start.getX() + 1; //+1 to account for block-based, not point-based
	}	
	public int getZSize() {
		return end.getZ() - start.getZ() + 1;
	}
	public int getHeight() {
		return end.getY() - start.getY() + 1;
	}

	
	
	
	//============== Protected ====================
	protected void buildIfWall( Block b ,IBlockState bs) {
		if( b.isEdge() ) {
			b.setState( bs);
		}
	}
	
	
	protected void buildIfSupport( Block b ,IBlockState bs) {
		if( this.supportcolumn.contains( new Pair<Integer,Integer>( b.x() ,b.z())) && !b.isClaimed()) {
			b.setState( bs);
		}
	}
	
	
	
}




//assuming 1 Block = 4 actual blocks
class Block {
	
	BlockSegment parent;
	IBlockState blockstate = Blocks.AIR.getDefaultState();
	BlockPos position;
	public boolean isWall = false;
	public boolean isFloor = false;
	public boolean isCeiling = false;
	public boolean isDoor = false; //ignore for now
	
	
	public Block( BlockPos pos) {
		this.position = pos;
	}
	

	public boolean isSolid() {
		return this.blockstate != Blocks.AIR.getDefaultState();
	}


	public IBlockState getState() {
		return this.blockstate;
	}
	
	
	public void claim( BlockSegment bs) {
		this.parent = bs;
//		this.blockstate = Blocks.COBBLESTONE.getDefaultState();
	}
	
	public boolean isClaimed() {
		return this.parent != null;
	}
	
	public void setState( IBlockState bs) {
		this.blockstate = bs;
	}
	
	public boolean isEdge() {
		return (this.isCeiling || this.isFloor || this.isWall);
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
	public boolean sourceDoor;
	
	
	public Door( BlockPos location ,EnumFacing facing ,boolean source) {
		this.location = location;
		this.facing = facing;
		this.sourceDoor = source;
	}
	
	
	public boolean isSource() {
		return this.sourceDoor;
	}
	
	
}




//assumed to have odd-numbered sizes, will always have center.
class BlockSegment {
	BlockPos start ,end;
	int xPartial ,zPartial ,height ,dx ,dz;
	
	public BlockSegment( BlockPos start ,BlockPos end) {
		this.start = start;
		this.end = end;
		this.dx = end.getX() - start.getX() + 1; //because this is not point-based but block-based, the "length"
		this.dz = end.getZ() - start.getZ() + 1; //is not end - start.
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
//		System.out.println( "Starting dimension: " + this.start + " " + this.end);
		ArrayList<Block> toClaim = new ArrayList<>();
		for( int x=this.start.getX(); x<=this.end.getX(); x++) {
			for( int y=this.start.getY(); y<=this.end.getY(); y++) {
				for( int z=this.start.getZ(); z<=this.end.getZ(); z++) {
					block = blocks.get( pos.add( x ,y ,z));
					if( block == null || block.isClaimed()) {
						if( block != null) {
//							System.out.println( "Failed block due to claim.");
//							System.out.println( "Failed block: " + block.position);
//							System.out.println( "Tried to claim: " + this.start + " " + this.end);
//							System.out.println( "Claimed by: " + block.parent.start + " " + block.parent.end);
						}
						return false;
					}else {
						toClaim.add( block);
					}
				}
			}
		}
		
		for( Block b : toClaim) {
			b.claim( this);
			this.markBlockDetails( b);
		}
		
		return true;
	}
	
	
	private void markBlockDetails( Block b) {
//		if( ( b.x() == this.start.getX() || b.x() == this.end.getX() ) 
//				|| ( b.y() == this.start.getY() || b.y() == this.end.getY() ) 
//				|| (b.z() == this.start.getZ() || b.z() == this.end.getZ()) ) {
////			b.setState( Blocks.NETHER_BRICK.getDefaultState());
//			b.markAsEdge();
//		}
		
		if( b.y() == this.end.getY()) { //TODO probably change definition of floor/ceiling
			b.isCeiling = true;
		}else if( b.y() == this.start.getY()) {
			b.isFloor = true;
		}else if( b.x() == this.start.getX() || b.x() == this.end.getX() || b.z() == this.start.getZ() || b.z() == this.end.getZ() ) {
			b.isWall = true;
		}
		
		
	}
	

}




class Room extends BlockSegment{
	
	private ArrayList<Door> doors = new ArrayList<>();
	

	public Room(BlockPos start, BlockPos end) {
		super(start, end);
	}
	
	
	public void addDoor( Door door) {
		this.doors.add( door);
	}
	
	
	public void addDoor( EnumFacing side ,boolean decenter ,boolean source) {
		BlockPos pos = null;
		switch( side) {
			case EAST: 
				pos = this.start.add( this.dx-1 ,1 ,this.zPartial);
				break;
			case WEST:
				pos = this.start.add( 0 ,1 ,this.zPartial);
				break;
			case NORTH:
				pos = this.start.add( this.xPartial ,1 ,0);
				break;
			case SOUTH:
				pos = this.start.add( this.xPartial ,1 ,this.dz-1);
				break;
			case UP:
				pos = this.start.add( this.xPartial ,7 ,this.zPartial);
				break;
			case DOWN:
				pos = this.start.add( this.xPartial ,0 ,this.zPartial);
				break;
				
		}
		this.addDoor( new Door( pos ,side ,source));
	}
	
	
	
	public void addRandomDoors( EnumFacing avoid) {
		double threshold = 0.5;
		for(EnumFacing face : EnumFacing.VALUES) {
			if( face != avoid && face != EnumFacing.DOWN) {
				if( TEinTE.RNG.nextDouble() > threshold) {
					threshold += 0.2;
					this.addDoor( face ,false ,false);
				}else {
					threshold -= 0.2;
				}
//				this.addDoor( face ,false ,false);
			}
		}
	}
	
	
	
	public void findStartFromDoor( Door door){
		BlockPos roomStart = door.location;
		
		switch( door.facing) {
			case EAST: roomStart = roomStart.add( 1 ,-1 ,-this.zPartial); break;
			case WEST: roomStart = roomStart.add( -this.dx ,-1 ,-this.zPartial); break;
			case NORTH: roomStart = roomStart.add( -this.xPartial ,-1 ,-this.dz); break;
			case SOUTH: roomStart = roomStart.add( -this.xPartial ,-1 ,1); break;
			case UP: roomStart = roomStart.add( -this.xPartial ,1 ,-this.zPartial); break;
		}

		this.setStartPos( roomStart);
	}
	
	
	public ArrayList<Door> doors() {
		return this.doors;
	}
	
	

	
	
}














