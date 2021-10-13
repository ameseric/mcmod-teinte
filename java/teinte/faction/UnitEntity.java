package teinte.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import teinte.faction.ResourceMap.RMChunk;
import teinte.utility.BlockTypeCollection;
import teinte.utility.BlockUtil;
import teinte.utility.SearchBlock;

public class UnitEntity {
	private static int count = 0;
	
	public World world;
	
	private boolean isPuppet = true;
	private EntityLiving e;
	private Job assignment = Job.PATROL;
	private ArrayList<ChunkPos> path;
	private Troop troop;
	
	private SearchBlock findWood;
	private PuppetMovement move;
	
	private static BlockTypeCollection woodTraversable;
	private static BlockTypeCollection woodReturn;	
	
	
	public enum UnitType{
		 SCOUT
		,GATHER
		,MOVER
		,FIGHTER
	}	
	
	public enum Job{
		 IDLE
		,PATROL
		,EXPLORE
		,HARVEST
		,MINE //5-15, with slight variation.
	}	
	
//	static{ //Block searching and filters setup
//		woodTraversable = (b) -> { 
//			return (b == Blocks.AIR) || (b == Blocks.LEAVES); };
//		woodReturn = (b) -> { return b == Blocks.LOG;};
//	}
	
	
	
	public UnitEntity( UnitType t ,ChunkPos pos ,Troop troop ,World world) {
		this.move = new PuppetMovement( pos ,count ,count%16);
		this.troop = troop;
		//this.e = new ... we're going to have to extend the class, aren't we?
		
		this.findWood = new SearchBlock( world ,woodReturn ,woodTraversable ,16);
		
		++UnitEntity.count;
	}
	
	
	
	
	public void update( World world) {
		this.updateState( world);
		
		switch( this.assignment) {
		case EXPLORE:
			this.explore( world);
			break;
		case IDLE:
			break;
		case PATROL:
			this.patrol(world);
			break;
		case HARVEST:
			break;
		case MINE:
			break;
			
		}
		

		if( this.isPuppet()) {
			this.move.update( world);
		}
		
	}
	
	
	public void assignJob( Job j) {
		this.assignment = j;
	}

	
	
	//We don't directly call the chunk.isLoaded to save lookups. Need to be careful
	//that the flag is kept updated.
	public boolean isPuppet() {
		//return this.isPuppet;
		return true; //for debug
	}
	
	public void updateState( World world) {
		boolean oldState = this.isPuppet;
		this.isPuppet = !world.getChunkFromBlockCoords( this.move.getBXYZPos()).isLoaded();

		if( oldState != this.isPuppet) {
			this.switchState();
		}
	}
	
	private void switchState() {
		this.move.reset();
	}
	
	private Faction getFaction() {
		return this.troop.getFaction();
	}
	
	private ResourceMap getMap() {
		return this.getFaction().getMap();
	}
	
	
	
	//------------- Core Actions ------------------//
	public void move() {
		
	}
	
	public void record( World world) {
		this.getMap().record( this.move.getPos() ,world );
	}
	
	
	private void fight() {
		
	}
	
	
	
	//----------------- Jobs --------------------------//
	//switch patrols to radial, with occasional random r jumps
	private void patrol( World world) {
		if( this.move.idle()) {
			int randint = world.rand.nextInt( 10);

			HashSet<ChunkPos> chunks = BlockUtil.getNeighborChunks( this.move.getPos());

			if( randint == 0) {
				this.move.to( (ChunkPos) chunks.toArray()[0] );
			}else {			
				for( ChunkPos pos : chunks) {
					RMChunk chunk = this.getMap().getChunk(pos);
					if( chunk != null && chunk.r == this.getMap().calcR( this.move.getPos()) ) {
						this.move.to( pos);
					}
				}
			}			
			
		}else if( this.move.finished() ) {
			this.record( world);
			this.move.reset();
		}
	}
	
	//switch exploration to linear radial progression
	private void explore( World world) {
		if( this.move.idle()) {
			int randint = world.rand.nextInt( 10);

			HashSet<ChunkPos> chunks = BlockUtil.getNeighborChunks( this.move.getPos());

			if( randint == 0) {
				this.move.to( (ChunkPos) chunks.toArray()[0] );
			}else {			
				for( ChunkPos pos : chunks) {
					RMChunk chunk = this.getMap().getChunk(pos);
					if( chunk != null && chunk.r == this.getMap().calcR( this.move.getPos()) ) {
						this.move.to( pos);
					}
				}
			}			
			
		}else if( this.move.finished() ) {
			this.record( world);
			this.move.reset();
		}
	}
	
	
	private void harvest( World world) {
		if( this.getMap().getChunk( this.move.getPos()).hasWood ) {
			BlockPos pos = this.findWood.search( this.move.getBXYZPos() ,true);
		}else {
			
		}
	}
	
	

	
	//------------------ World Actions ---------------//
//	public void setPosition( BlockPos pos ,World world) {
//		this.lastPos = this.pos;
//		this.pos = pos;
//		
////		this.e.setPosition( this.pos.getX() ,this.pos.getY() ,this.pos.getZ());
////		world.getChunkFromBlockCoords( this.lastPos).removeEntity( this.e);
////		world.getChunkFromBlockCoords( this.pos).addEntity( this.e);
//		
//		world.setBlockToAir( this.lastPos);
//		world.setBlockState( this.pos ,Blocks.CYAN_GLAZED_TERRACOTTA.getDefaultState());
//	}
	
	

	
}


class Timer{
	private int counter;
	private final int MAX;
	
	public Timer( int maxCounter) {
		this.MAX = maxCounter;
		this.reset();
	}
	
	public void reset() {
		this.counter = this.MAX;
	}
	
	public void tick() {
		--this.counter;
	}
	
	public boolean done() {
		return this.counter == 0;
	}
}




class PuppetMovement{
	private Timer timer = new Timer( 4);
	private ChunkPos moveTo;
	private ChunkPos pos;
	private ChunkPos lastPos;
	
	private int rx;
	private int rz;
	private int ry;
	
	
	public PuppetMovement( ChunkPos pos ,int rx ,int rz) {
		this.pos = pos;
		this.lastPos = new ChunkPos(0,0);
		this.rx = rx;
		this.rz = rz;
	}
	
	public void to( BlockPos pos) {
		this.to( new ChunkPos(pos));
	}
	
	public void to( ChunkPos pos) {
		this.moveTo = pos;
	}
	
	public void update( World world) {
		if( this.idle() || this.finished() ) return;
		
		this.timer.tick();
		if( this.timer.done()) {
			this.setPosition( world);
			this.timer.reset();
		}	
	}
	
	private void setPosition( World world) {
		this.lastPos = this.pos;
		this.pos = moveTo;
		
		world.setBlockToAir( this.getBXYZPos());
		world.setBlockState( this.getBXYZPos() ,Blocks.CYAN_GLAZED_TERRACOTTA.getDefaultState());
		
		this.calcBlockY( world);
	}
	
	public void setStartingPosition( ChunkPos pos) {
		this.lastPos = new ChunkPos(0,0);
		this.pos = pos;
	}
	
	public boolean finished() {
		return (this.moveTo == this.pos);
	}
	
	public boolean idle() {
		return this.moveTo == null;
	}
	
	public void reset() {
		this.moveTo = null;
		this.timer.reset();
	}
	
	public ChunkPos getPos() {
		return this.pos;
	}
	
	public BlockPos getBXZPos() {
		return new BlockPos( this.pos.x + this.rx ,0 ,this.pos.z+this.rz);
	}
	
	public BlockPos getBXYZPos() {
		return new BlockPos( this.pos.x + this.rx ,this.ry ,this.pos.z+this.rz);
	}
	
	public ChunkPos getLastPos() {
		return this.lastPos;
	}
	
	
	/*
	 * For debug purposes only.
	 */
	private void calcBlockY( World world) {
		this.ry = BlockUtil.getAirYPos( this.getBXZPos() ,world).getY() + 15;		
	}
	
	
}











