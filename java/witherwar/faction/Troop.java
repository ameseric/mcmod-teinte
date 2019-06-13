package witherwar.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import witherwar.faction.UnitEntity.Job;
import witherwar.util.WeightedHashMap;


/**
 *  
 * @author Guiltygate
 * @param <Job> Object that is being weighted by HashMap
 * 
 */
public class Troop {	
	private HashMap< Job ,List<UnitEntity>> jobAssignments;
	private ArrayList< UnitEntity> units;
	public WeightedHashMap<Job> weights = new WeightedHashMap<Job>();
	private UnitEntity.Type troopType;
	
	private Faction parent;
	
	
	public Troop( UnitEntity.Type t ,Faction parent) {
		this.troopType = t;
	}
	
	public Troop( UnitEntity.Type t ,WeightedHashMap<Job> map) {
		this.troopType = t;
		this.weights = map;
	}
	
	
	public void add( BlockPos pos){
		this.units.add( new UnitEntity( this.troopType ,new ChunkPos(pos) ,this ,this.world));
	}
	
	public void updateMemberActions( World world) {
		for( UnitEntity u : this.units) {
			u.update( world);
		}
	}
	
	public Faction getParent() {
		return this.parent;
	}
	
	// First attempt at moving UnitEntities around the job assignment map.
	// Probably can be improved upon.
	public void updateJobAssignments() {
		HashMap<Job ,Integer> newUnitAllocation = this.weights.allocate( this.units.size());		
		ArrayList<UnitEntity> unitsToReassign = new ArrayList<UnitEntity>();
		
		for( Job t : this.jobAssignments.keySet()) {
			int diff = newUnitAllocation.get(t) - this.jobAssignments.get(t).size();
			if( diff < 0) {
				for( int i=0; i>diff; i--) {
					int index = Math.abs(i);
					unitsToReassign.add( this.jobAssignments.get(t).get( index));
					this.jobAssignments.get(t).remove( index);
				}
			}
		}
		
		for( Job job : this.jobAssignments.keySet()) {
			int diff = newUnitAllocation.get(job) - this.jobAssignments.get(job).size();
			if( diff > 0) {
				for( int i=0; i<diff; i++) {
					UnitEntity ue = unitsToReassign.remove(0);
					this.jobAssignments.get(job).add( ue);
					ue.assignJob( job);
				}
			}
		}
	}
	
	
	public int size() {
		return this.units.size();
	}
	
}
