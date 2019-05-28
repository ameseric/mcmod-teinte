package witherwar.faction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import witherwar.util.Symbol;

public class UnitEntity {
	private boolean isPuppet = true;
	private EntityLiving e;
	public BlockPos pos;
	public BlockPos moveTo;	
	private HashSet<Job> allowedJobs = new HashSet<>();
	private Job assignment = Job.IDLE;
	private Timer moveTimer = new Timer(4);
	private Timer gatherTimer = new Timer(2);
	private ArrayList<ChunkPos> path;
	
	public enum Type{
		 SCOUT
		,GATHER
		,MOVER
		,FIGHTER
	}
	
	
	
	public UnitEntity( Type t ,BlockPos pos) {
		this.addJob( Job.IDLE);
		this.pos = pos;
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
		
		this.assignment.update( this);

		if( this.moveTo != null && this.moveTo != this.pos) {
			if( this.path == null) {
				this.generatePuppetPath();
			}
			this.move();
		}
		
	}
	
	private void addJob( Job j) {
		this.allowedJobs.add(j);
	}
	
	//We don't directly call the chunk.isLoaded to save lookups. Need to be careful
	//that the flag is kept updated.
	public boolean isPuppet() {
		//return this.isPuppet;
		return true;
	}
	
	public void updateState( World world) {
		boolean oldState = this.isPuppet;
		this.isPuppet = !world.getChunkFromBlockCoords( this.pos).isLoaded();
		if( oldState != this.isPuppet) {
			this.switchState();
		}
	}
	
	private void switchState() {
		this.moveTimer.reset();
	}
	
	
	
	//------------- Core Actions ------------------//
	public void move() {
		if( this.isPuppet()) {
			moveTimer.tick();
			if( moveTimer.done()) {
				this.placeUnit( this.moveTo);
				moveTimer.reset();
			}
			
		}else {
			//tell entity to move to location
		}
	}
	
	public void gather() {
		
	}
	
	public void fight() {
		
	}
	
	
	public ChunkPos getCPos() {
		return new ChunkPos( this.pos);
	}
	

	
	//------------------ CHeat Actions ---------------//
	public void placeUnit( BlockPos pos) {
		this.pos = pos;
	}
	
	

	
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




