package witherwar.faction;

import java.util.ArrayList;
import java.util.HashSet;

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
	
	
	private UnitEntity() {
		this.addJob( Job.IDLE);
	}
	
	public static UnitEntity getNewScout() {
		UnitEntity ue = new UnitEntity();
		ue.addJob( Job.PATROL);
		ue.addJob( Job.EXPLORE);
		return ue;
	}
	
	
	
	public void update( World world) {
		this.assignment.update();
	}
	
	private void addJob( Job j) {
		this.allowedJobs.add(j);
	}
	
	public boolean isPuppet() {
		return this.isPuppet;
	}
	
	public void moveTo( ChunkPos pos) {
		if( this.isPuppet()) {
			//e.moveToBlockPosAndAngles( pos, 0.0, 0.0);
			
		}else {
			//tell entity to move to location
		}
	}
	
	
	public ChunkPos getCPos() {
		return new ChunkPos( this.pos);
	}
	

	
	

	
}







