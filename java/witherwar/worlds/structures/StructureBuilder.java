package witherwar.worlds.structures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.annotation.Nullable;

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
	
	private BlockPos entryPoint = null;
	private HashSet<EnumFacing> entrances = new HashSet<EnumFacing>();
	
	
	private boolean isClosedType = true; //TODO placeholder, may make different subclasses for "open" fill builders
	
	
	
	//TODO need entry point generation and possibly pass-in
	public StructureBuilder( BlockPos start ,BlockPos end ,EnumFacing[] entrances ,@Nullable BlockPos entry) {
//		this.start = new BlockPos( start.getX()>>1 ,start.getY()>>1 ,start.getZ()>>1);
//		this.end = new BlockPos( end.getX()>>1 ,end.getY()>>1 ,end.getZ()>>1);
		
		this.start = start;
		this.end = end;
		
		this.entryPoint = entry;
//		this.entrances = entrances;
		
		for( EnumFacing side : entrances) {
			this.entrances.add( side);
		}
		
		this.initializeBlocks();
		
	}
	
	
	
	//==================  build phase  ======================
	protected void build() {
		
		int length = 8;
		int width = 10;
		BlockPos frStart, frEnd;		
		if( this.entryPoint == null) {
			frStart = this.start.add( this.getXSize()/2 ,0 ,this.getZSize()/2);
			frEnd = frStart.add( width ,this.layerSize ,length);			

		}else {
			frStart = this.entryPoint.add( -(width/2) ,0 ,-(length/2));
			frEnd = frStart.add( width ,this.layerSize ,length);
			
		}
		
		
		Room startRoom = new Room( frStart ,frEnd);
		
		if( !startRoom.tryToClaim( this.blocks)) {
			System.out.println("Something has gone terribly wrong....");
		}
		
		startRoom.addDoor( EnumFacing.NORTH); //TODO this will depend on the type of start room. Should be child dependent.
		startRoom.addDoor( EnumFacing.SOUTH);
		startRoom.addDoor( EnumFacing.EAST);
		startRoom.addDoor( EnumFacing.WEST);

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

		ArrayList<Pair<EnumFacing,Room>> exitRooms = new ArrayList<>();
		
		this.postprocess_initial();
		
		
		for( Room room : this.rooms) {
			this.markIfValidEntrance( room);
			if( this.entrances.contains( room.nearestSide())) {
				exitRooms.add( new Pair<EnumFacing,Room>(room.nearestSide() ,room));
				this.entrances.remove( room.nearestSide()); //TODO for now only one entrance per side requested
				room.setAsEntrance();
			}
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

		System.out.println( exitRooms);
//		for( Pair<EnumFacing,Room> pair : exitRooms) {
//			pair.second()
//		}
		

		
	}
	
	/**
	 * Auto-called at start of cleanup phase (after build phase).
	 */
	abstract protected void postprocess_initial();
	
	
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
		if( room.isEntrance()) { //TODO fix hallway creation
			this.makeVariableHallway( room ,room.nearestSide() ,20 ,false);
		}
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
	
	
	
	private void markIfValidEntrance( Room room) {
		BlockPos center = room.center();
		
		final int LIMIT = 20;
		
		if( Math.abs( this.start.getY() - center.getY()) < 10 ) {
			if( Math.abs( this.start.getX() - center.getX()) < LIMIT ) { //TODO shouldn't be static value
				room.setNearestSide( EnumFacing.WEST);
			}else if( Math.abs( this.end.getX() - center.getX()) < LIMIT) {
				room.setNearestSide( EnumFacing.EAST);
			}else if( Math.abs( this.start.getZ() - center.getZ()) < LIMIT) {
				room.setNearestSide( EnumFacing.NORTH);
			}else if( Math.abs( this.end.getZ() - center.getZ()) < LIMIT) {
				room.setNearestSide( EnumFacing.SOUTH);
			}
		}
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
	
	
//	public int xf( boolean abs) {
//		if( abs) {
//			return Math.abs( this.end.getX());
//		}
//		return this.end.getX();
//	}
//	public int zf( boolean abs) {
//		if( abs) {
//			return Math.abs( this.end.getZ());
//		}
//		return this.end.getZ();
//	}
//	public int yf( boolean abs) {
//		if( abs) {
//			return Math.abs( this.end.getY());
//		}
//		return this.end.getY();
//	}
//	
//	
//	
//	public int xo( boolean abs) {
//		if( abs) {
//			return Math.abs( this.start.getX());
//		}
//		return this.start.getX();
//	}
//	public int zo( boolean abs) {
//		if( abs) {
//			return Math.abs( this.start.getZ());
//		}
//		return this.start.getZ();
//	}
//	public int yo( boolean abs) {
//		if( abs) {
//			return Math.abs( this.start.getY());
//		}
//		return this.start.getY();
//	}

	
	
	
	//============== Protected ====================
	protected void buildIfWall( Block b ,IBlockState bs) {
		if( b.isSolidEdge() ) {
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
			BlockPos bottom = room.getCenteredDoorBlock( EnumFacing.DOWN);
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
//			Vec3i v = VectorUtility.scale( side.getDirectionVec() ,searchDistance);
			this.makeVariableHallway( room ,side ,searchDistance ,true);
		}
	}
	
	
	/**
	 * If no satisfactory conditions exist, no hallway is created.
	 * 
	 * @param room
	 * @param direction
	 * @param maxLength
	 */
	protected void makeVariableHallway( Room room ,EnumFacing direction ,int maxLength ,boolean onlyConnectToRooms) {
		BlockPos start = room.getCenteredDoorBlock( direction); 
		BlockPos end = start;
		
		for( int i=0; i<=maxLength; i++) {
			end = end.add( direction.getDirectionVec());
			Block b = this.getBlock( end);
			
			if( (!onlyConnectToRooms && b == null) || ( b != null && b.isClaimed())) {
				this.makeFixedHallway( room ,direction ,i);
				return;
			}
			
		}
		
	}
	
	
	
	protected void makeFixedHallway( Room room ,EnumFacing direction ,int length) { 
		if( length < 2) { return;}
		
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
		Door door = new Door( room.getCenteredDoorBlock( direction) ,direction ,false);
		room.addDoor( door);
		hallway.findStartFromDoor( door);
		hallway.partialClaim( this.blocks);
//		Door endOfHallway = new Door( hallway.getCenteredDoorBlock( direction) ,direction ,false);
//		hallway.addDoor( door.facing ,false ,false);
//		hallway.addDoor( endOfHallway);
//		hallway.addDoor( door.facing.getOpposite() ,false ,false); //TODO add list of hallways for decorating?
		
		
		BlockPos endDoor = hallway.getCenteredDoorBlock( direction);
		BlockPos startDoor = hallway.getCenteredDoorBlock( direction.getOpposite());
		this.getBlock( endDoor).markAsDoor();
		this.getBlock( endDoor.add(0,1,0)).markAsDoor();
		this.getBlock( startDoor).markAsDoor();
		this.getBlock( startDoor.add(0,1,0)).markAsDoor();		
		
		//manual fix, TODO do better
		BlockPos fauxDoorPos = endDoor.add( direction.getDirectionVec());
		Block b = this.getBlock( fauxDoorPos); 
		if( b != null) {
			b.markAsDoor();  //TODO can be null? Not sure how.
			this.getBlock( fauxDoorPos.add(0,1,0)).markAsDoor();
		}
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


















