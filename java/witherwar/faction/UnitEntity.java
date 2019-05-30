package witherwar.faction;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class UnitEntity {
	private boolean isPuppet = true;
	private EntityLiving e;
	private HashSet<Job> allowedJobs = new HashSet<>();
	private Job assignment = Job.PATROL;
	private ArrayList<ChunkPos> path;
	
	private PuppetMovement move;
	
	
	public enum Type{
		 SCOUT
		,GATHER
		,MOVER
		,FIGHTER
	}
	
	
	public enum Job{
		  IDLE
		  ,PATROL
		  ,EXPLORE;
	}
	
	
	
	public UnitEntity( Type t ,BlockPos pos) {
		this.addJob( Job.IDLE);
		this.move = new PuppetMovement( pos);
		//this.e = new ... we're going to have to extend the class, aren't we?
		
		switch(t) {
		case SCOUT:
			this.addJob( Job.PATROL);
			this.addJob( Job.EXPLORE);
			break;
		case GATHER:
			break;
		case FIGHTER:
			break;
		case MOVER:
			break;
		default:
			break;
			
		}
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
		this.isPuppet = !world.getChunkFromBlockCoords( this.move.getPos()).isLoaded();
		if( oldState != this.isPuppet) {
			this.switchState();
		}
	}
	
	private void switchState() {
		this.move.reset();
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
	
	public void record() {
		
	}
	
	private void gather() {
		
	}
	
	private void fight() {
		
	}
	
	
	public ChunkPos getCPos() {
		return new ChunkPos( this.move.getPos());
	}
	
	
	
	//----------------- Jobs --------------------------//
	private void patrol( World world) {
		if( this.move.idle()) {
			this.move.to( new BlockPos(0,0,0));
		}else if( this.move.finished() ) {
			this.record();
			this.move.reset();
		}
	}
	
	private void explore( World world) {
		
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
	private BlockPos moveTo;
	private BlockPos pos;
	private BlockPos lastPos;
//	boolean haveReset = false;
	
	
	public PuppetMovement( BlockPos pos) {
		this.pos = pos;
		this.lastPos = new BlockPos(0,0,0);
	}
	
	public void to( BlockPos pos) {
		this.moveTo = pos;
//		this.haveReset = false;
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
		
		world.setBlockToAir( this.lastPos);
		world.setBlockState( this.pos ,Blocks.CYAN_GLAZED_TERRACOTTA.getDefaultState());
	}
	
	public void setStartingPosition( BlockPos pos) {
		this.lastPos = new BlockPos(0,0,0);
		this.pos = pos;
	}
	
	public boolean finished() {
		return (this.moveTo == this.pos);// && !this.haveReset;
	}
	
	public boolean idle() {
		return this.moveTo == null;
	}
	
	public void reset() {
		this.moveTo = null;
		this.timer.reset();
//		this.haveReset = true;
	}
	
	public BlockPos getPos() {
		return this.pos;
	}
	
	
}











