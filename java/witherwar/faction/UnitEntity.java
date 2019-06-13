package witherwar.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import witherwar.faction.ResourceMap.RMChunk;
import witherwar.util.BlockUtil;
import witherwar.util.SearchBlock;
import witherwar.util.SearchBlock.FilterBlock;

public class UnitEntity {
	private static int count = 0;
	
	public World world;
	
	private boolean isPuppet = true;
	private EntityLiving e;
	private HashSet<Job> allowedJobs = new HashSet<>();
	private Job assignment = Job.PATROL;
	private ArrayList<ChunkPos> path;
	private Troop troop;
	
	private SearchBlock findWood;
	private PuppetMovement move;
	
	private static FilterBlock woodTraversable;
	private static FilterBlock woodReturn;
	
	
	
	public enum Type{
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
	
	
	static{ //Block searching and filters setup
		woodTraversable = (b) -> { 
			return (b == Blocks.AIR) || (b == Blocks.LEAVES); };
		woodReturn = (b) -> { return b == Blocks.LOG;};
	}
	
	
	
	public UnitEntity( Type t ,ChunkPos pos ,Troop troop ,World world) {
		this.addJob( Job.IDLE);
		this.move = new PuppetMovement( pos ,count ,count%16);
		//this.e = new ... we're going to have to extend the class, aren't we?
		
		switch(t) {
		case SCOUT:
			this.addJob( Job.PATROL);
			this.addJob( Job.EXPLORE);
			this.troop = troop;
			break;
		case GATHER:
			this.findWood = new SearchBlock( world ,woodReturn ,woodTraversable ,16);
			break;
		case FIGHTER:
			break;
		case MOVER:
			break;
		default:
			break;
			
		}
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
			
		}
		

		if( this.isPuppet()) {
			this.move.update( world);
		}
		
	}	

	
	private void addJob( Job j) {
		this.allowedJobs.add(j);
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
		return this.troop.getParent();
	}
	
	private ResourceMap getMap() {
		return this.getFaction().getMap();
	}
	
	
	
	//------------- Core Actions ------------------//
//	private void move( World world) {
//		moveTimer.tick();
//		if( moveTimer.done()) {
//			this.setPosition( this.moveTo ,world);
//			moveTimer.reset();
//			this.moveTo = null;
//		}			
//	}
	
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
		this.patrol(world); //for now, they're the same.
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
		
		this.calcDebugY( world);
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
	
	
	private void calcDebugY( World world) {
		int y = 240;		
		Block b;
		
		do{
			b = world.getBlockState( this.getBXZPos().add(0,0,0) ).getBlock();
			--y;
			
		}while( b == Blocks.AIR);
		
		this.ry = y+15;
		
		
	}
	
	
}











