package witherwar.worlds.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import witherwar.TEinTE;
import witherwar.utility.Pair;
import witherwar.utility.VectorUtility;

//NOTE: The "Blocks" used in this generator are not minecraft blocks.

//NOTE: we try to always use even values for sizes, as these become odd for block segments (0,0,0) -> (x,y,z)

public abstract class StructureBuilder {
	
	private HashMap<BlockPos ,Block> blocks = new HashMap<>();
	private ArrayList<Room> rooms = new ArrayList<>();

	private BlockPos start ,end;	
	private int layerSize = 8;
	
	private HashSet<Pair<Integer,Integer>> supportcolumn = new HashSet<>();
	
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();
	
	
	
	
	public StructureBuilder( BlockPos start ,BlockPos end) {
//		this.start = new BlockPos( start.getX()>>1 ,start.getY()>>1 ,start.getZ()>>1);
//		this.end = new BlockPos( end.getX()>>1 ,end.getY()>>1 ,end.getZ()>>1);
		
		this.start = start;
		this.end = end;
		
		this.initializeBlocks();
		
		
	}
	
	
	
	//==================  build phase  ======================
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
				if( this.tryToAddNewRoom( door ,newRoom)) {
//				newRoom.findStartFromDoor( door);
//				if( newRoom.tryToClaim( this.blocks)) {
//					newRoom.addDoor( door.facing.getOpposite() ,false ,true);
//					newRoom.addRandomDoors(door.facing.getOpposite());
//					this.addRoom( newRoom);
////					this.registerXZColumns( newRoom);
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
		return new Room( this.getRandomRoomSize() ,this.layerSize ,this.getRandomRoomSize());
	}
	
	
	
	
//	private boolean tryToAddNewRoom( Door door ,int length ,int width) {
//		Room newRoom = this.newRandomRoom();
//		return this.tryToAddNewRoom( door ,newRoom);
//	}
		
		
	private boolean tryToAddNewRoom( Door door ,Room room) {
		room.findStartFromDoor( door);
		if( room.tryToClaim( this.blocks)) {
			room.addDoor( door.facing.getOpposite() ,false ,true);
			room.addRandomDoors(door.facing.getOpposite());
			this.addRoom( room);
			return true;
		}
		return false;
	}
	
	
	
	
	private int getRandomRoomSize() {
		double r = TEinTE.RNG.nextDouble();
		if( r <= 0.25) { return 7;}
		if( r <= 0.5) { return 9;}
		if( r <= 0.75) { return 11;}
		return 13;
	}
	
	
	
	
	//================  cleanup phase  ===========================
	protected void cleanup() {

		this.postprocess_initial();
		
		
		for( Room room : this.rooms) {
			this.roomPostProcess( room);

			for( Door door : room.doors()) {
				this.doorPostProcess( door);
			}
		}
		
		
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
		

		
	}
	
	/**
	 * Auto-called at start of cleanup phase (after build phase).
	 */
	abstract protected  void postprocess_initial();
	
	
	/**
	 * Same as postprocess, but called for every block.
	 * 
	 * @param block
	 */
	abstract protected void blockPostProcess( Block block);
	

	
	/**
	 * Default room modifier/decorator. Called after block and door postprocess.
	 * @param room
	 */
	protected void roomPostProcess( Room room) {
		this.buildIfStairwell( room);
	}
	
	
	
	
	/**
	 * Default door modifier/decorator. Replaces door blockstates with air, and some neighbors.
	 *  Called before room postprocess, after blocks.
	 * @param door
	 */
	protected void doorPostProcess( Door door) {
		BlockPos pos = door.location;
		if( door.isStairwell()) {
			for( int x=-1; x<=1; x++) {
				for( int z=-1; z<=1; z++) {
					//this.getBlock( pos.add( x ,0 ,z)).blockstate = Blocks.AIR.getDefaultState();
					this.getBlock( pos.add( x ,0 ,z)).markAsStairwell();
				}
			}
			
		}else {
			this.getBlock( pos).markAsDoor();//.blockstate = Blocks.AIR.getDefaultState();
			this.getBlock( pos.add( 0,1,0)).markAsDoor();//blockstate = Blocks.AIR.getDefaultState();
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
	
	
	
	
	private void addRoom( Room room) {
		this.rooms.add( room);
	}
	
	
	
	
	//======================= Public =====================================================
	
	public IBlockState getBlockState( BlockPos pos) {
		Block block = this.getBlock( pos);
		return block != null ? block.blockstate : null;
	}
	
	
	
//	public IBlockState getTranslatedBlockState( int x ,int y ,int z) {
//		return this.getBlockState( new BlockPos( x>>1 ,y>>1 ,z>>1));
//	}
//	public IBlockState getTranslatedBlockState( BlockPos pos) {
//		return this.getTranslatedBlockState( pos.getX() ,pos.getY() ,pos.getZ());
//	}
	
	
	
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
	
	
	protected void buildIfStairwell( Room room) {
		Door door = room.getDoor( EnumFacing.UP);
		if( door != null) {
			BlockPos top = door.location;
			BlockPos bottom = room.getCenterBlock( EnumFacing.DOWN);
			int height = top.subtract( bottom).getY() + 2; //+2 accounts for block vs point & reaching into the next room's threshold
			IBlockState stair = Blocks.STONE_BRICK_STAIRS.getDefaultState();
			IBlockState brick = Blocks.STONEBRICK.getDefaultState();
			
			EnumFacing face = EnumFacing.NORTH;
			for( int i=1; i<height; i++) {
				// pillar block
				BlockPos pos = bottom.add( 0,i,0);
				this.getBlock( pos).setState( brick);
				
				//stairs
				pos = pos.add( face.getDirectionVec());
				this.getBlock( pos).setState( stair.withProperty( BlockStairs.FACING ,face.rotateY() ));
				face = face.rotateY();
				this.getBlock( pos.add( face.getDirectionVec())).setState( brick);
			}	
		}
	}
	
	
	/**
	 * default window test
	 * @param room
	 * @param blocksBetweenWindows
	 */
	protected void buildIfWindow( Room room ,int numberOfWindows ,IBlockState decoration) {
		for( EnumFacing side : EnumFacing.HORIZONTALS) {
			if( side == EnumFacing.NORTH) {
				//start xo/zo/ym - xf/zo/ym
				//TODO
			}
		}
	}
	
	
	
	protected void tryToBuildNewConnections( Room room ,IBlockState blockType) {
		if( room.doors().size() >= 2) { return;}

		
		int searchDistance = 10;		
		
		for( EnumFacing side : EnumFacing.HORIZONTALS) {
			Vec3i v = VectorUtility.scale( side.getDirectionVec() ,searchDistance);
			BlockPos start = room.getCenterBlock( side); 
			BlockPos end = start.add( v);
			Block b = this.getBlock( end);
			if( b != null && b.isClaimed()) {
				
				this.makeHallway( room ,side ,searchDistance);
//				this.makeHallway( start ,end ,side);
				return;
			}
		}
	}
	
	
	private void makeHallway( Room room ,EnumFacing direction ,int length) {
//		Room hallway = new Room();
		int dx = 0;
		int dz = 0;
		switch( direction.getAxis()) {
			case X: 
				dx = length;
				dz = 3;
				break;
			case Z:
				dx = 3;
				dz = length;
				break;
		}
		Room hallway = new Room( dx ,5 ,dz);
		Door door = new Door( room.getCenterBlock( direction) ,direction ,false);
		room.addDoor( door);
		hallway.findStartFromDoor( door);
		hallway.partialClaim( this.blocks);
		Door endOfHallway = new Door( hallway.getCenterBlock( direction) ,direction ,false);
//		hallway.addDoor( door.facing ,false ,false);
		hallway.addDoor( endOfHallway);
		hallway.addDoor( door.facing.getOpposite() ,false ,false);
		//keep reference?
//		this.addRoom( hallway);
		
		//manual fix, TODO do better
//		BlockPos fauxDoorPos = endOfHallway.location.add( direction.getDirectionVec());
//		this.getBlock( fauxDoorPos).markAsDoor();  //TODO can be null? Not sure how.
//		this.getBlock( fauxDoorPos.add(0,1,0)).markAsDoor();
	}
	
	
	
}






class Block {
	
	BlockSegment parent;
	IBlockState blockstate = Blocks.AIR.getDefaultState();
	BlockPos position;
	public boolean isWall = false;
	public boolean isFloor = false;
	public boolean isCeiling = false;
	public boolean isDoor = false; //ignore for now
	public boolean isStairwell = false;
	
	
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
	
//	public ArrayList<BlockPos> getHorizontalNeighbors() {
//		ArrayList<BlockPos> neighbors = new ArrayList<>();
//		for( int x=-1; x<=1; x++) {
//			for( int z=-1; z<=1; z++) {
//				neighbors.add( new BlockPos( x ,0 ,z));
//			}
//		}
//		return neighbors;
//	}
//	
//	public ArrayList<BlockPos> getVerticalNeighbors() {
//		ArrayList<BlockPos> neighbors = new ArrayList<>();
//		for( int x=-1; x<=1; x++) {
//			for( int z=-1; z<=1; z++) {
//				neighbors.add( new BlockPos( x ,y ,z));
//			}
//		}
//		return neighbors;
//	}
	
	public boolean isEdge() {
		return (this.isCeiling || this.isFloor || this.isWall) && !this.isDoor && !this.isStairwell;
	}
	
	
	public Block markAsDoor() {
		this.isDoor = true;
		return this;
	}
	
	
	public Block markAsStairwell() {
		this.isStairwell = true;
		return this;
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
	
	
	public boolean isStairwell() {
		return ( this.facing == EnumFacing.UP || this.facing == EnumFacing.DOWN);
	}
	
	
}




//assumed to have odd-numbered sizes, will always have center.
class BlockSegment {
	BlockPos start ,end;
	//Partials are one short of half the dimension for odd-sized segments ( should always be odd).
	int xPartial ,zPartial;
	int height ,dx ,dz;

	
	public BlockSegment( BlockPos start ,BlockPos end) {
		this.start = start;
		this.end = end;
		this.dx = end.getX() - start.getX() + 1; //because this is not point-based but block-based, the "length"
		this.dz = end.getZ() - start.getZ() + 1; //is not end - start.
		this.xPartial = dx / 2; //partials are rounded down
		this.zPartial = dz / 2;
		this.height = (end.getY() - start.getY() + 1);

	}
	
	
	
	public BlockSegment( int width ,int height ,int length) {
		this( new BlockPos( 0,0,0) ,new BlockPos( width-1 ,height-1 ,length-1));
	}
	
	
	public void setStartPos( BlockPos pos) {
		this.start = pos;
		this.calculateEndPos();
	}
	
	
	private void calculateEndPos() {
		int xf = this.start.getX() + this.dx - 1;
		int zf = this.start.getZ() + this.dz - 1;
		int yf = this.start.getY() + this.height - 1;
		this.end = new BlockPos( xf ,yf ,zf);
	}
	
	
	public int xCenter() {
		return this.start.getX() + this.xPartial;
	}
	public int zCenter() {
		return this.start.getZ() + this.zPartial;
	}
	public BlockPos center() {
		return this.getCenterBlock( EnumFacing.DOWN).add( 0 ,this.height/2 ,0);
	}

	
	
	
	public boolean tryToClaim( HashMap<BlockPos,Block> blocks) {
		return this.claim( blocks ,false ,false);
	}
	
	
	public void partialClaim( HashMap<BlockPos,Block> blocks) {
		this.claim( blocks ,false ,true);
	}
	
	
	public boolean claim( HashMap<BlockPos,Block> blocks , boolean override ,boolean partial) {
		BlockPos pos = new BlockPos(0,0,0);
		Block block;

		ArrayList<Block> toClaim = new ArrayList<>();
		for( int x=this.start.getX(); x<=this.end.getX(); x++) {
			for( int y=this.start.getY(); y<=this.end.getY(); y++) {
				for( int z=this.start.getZ(); z<=this.end.getZ(); z++) {
					
					block = blocks.get( pos.add( x ,y ,z));					
					
					if( override) {
						if( block != null) {
							toClaim.add( block);
						}
					}else if( partial) {
						if( !block.isClaimed()) {
							toClaim.add( block);
						}
					}else {						
						if( block == null || block.isClaimed()) {
							return false;
						}else {
							toClaim.add( block);
						}
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
	
	
	public BlockPos getCenterBlock( EnumFacing side) { //TODO change, doesn't actually return center for horizontal facings
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
		return pos;
	}
	

}




class Room extends BlockSegment{
	
	private ArrayList<Door> doors = new ArrayList<>();
	
	

	public Room(BlockPos start, BlockPos end) {
		super(start, end);
	}	
	
	public Room(int width, int height, int length) {
		super( width ,height ,length); 
	}
	


	
	public void addDoor( Door door) {
		this.doors.add( door);
	}
	
	
	public void addDoor( EnumFacing side ,boolean decenter ,boolean source) {
		BlockPos pos = this.getCenterBlock( side);
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
	
	
	public Door getDoor( EnumFacing side) {
		for( Door door : this.doors) {
			if( door.facing == side) {
				return door;
			}
		}
		return null;
	}
	
	
	public boolean hasDoor( EnumFacing side) {
		return this.getDoor( side) != null;
	}
	
	
//	public Door getStairwell() {
//		for( Door door : this.doors) {
//			if( door.isStairwell()) {
//				return door;
//			}
//		}
//		return null;
//	}
	
	
	
//	public int getPartial( EnumFacing side) {
//		if( side == EnumFacing.X)
//	}
	
	
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














