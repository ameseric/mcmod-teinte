package witherwar.worlds.structures;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import witherwar.TEinTE;




//assumed to have odd-numbered sizes, will always have center.
public class BlockSegment {
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
		return this.getCenteredDoorBlock( EnumFacing.DOWN).add( 0 ,this.height/2 ,0);
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
						if( block != null && !block.isClaimed()) {
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
		
		if( b.y() == this.end.getY()) { //TODO probably change definition of floor/ceiling
			b.isCeiling = true;
		}else if( b.y() == this.start.getY()) {
			b.isFloor = true;
		}else if( b.x() == this.start.getX() || b.x() == this.end.getX() || b.z() == this.start.getZ() || b.z() == this.end.getZ() ) {
			b.isWall = true;
		}
		
		
	}
	
	
	public BlockPos getCenteredDoorBlock( EnumFacing side) { //TODO change, doesn't actually return center for horizontal facings
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
	private boolean isEntrance = false;
	private EnumFacing nearestSide = null; //NESW
	
	

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
		BlockPos pos = this.getCenteredDoorBlock( side);
		this.addDoor( new Door( pos ,side ,source));
	}
	
	
	public void addDoor( EnumFacing side) {
		this.addDoor( side ,false ,false);
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
	
	
	public void setAsEntrance() {
		this.isEntrance = true;
	}
	
	public boolean isEntrance() {
		return this.isEntrance;
	}
	
	
	public void setNearestSide( EnumFacing side) {
		this.nearestSide = side;
	}
	
	
	public EnumFacing nearestSide() {
		return this.nearestSide;
	}
	
	

	
	
}

