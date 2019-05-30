package witherwar.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.faction.UnitEntity.Job;
import witherwar.util.WeightedHashMap;


/**
 *  
 * @author Guiltygate
 * @param <T> Object that is being weighted by HashMap
 * 
 */
public class Troop<T> {	
	private HashMap< T ,List<UnitEntity>> jobAssignments;
	private ArrayList< UnitEntity> units;
	public WeightedHashMap<T> weights = new WeightedHashMap<T>();
	private UnitEntity.Type troopType;
	
	
	public Troop( UnitEntity.Type t) {
		this.troopType = t;
	}
	
	public Troop( UnitEntity.Type t ,WeightedHashMap<T> map) {
		this.troopType = t;
		this.weights = map;
	}
	
	
	public void add( BlockPos pos){
		this.units.add( new UnitEntity( this.troopType ,pos));
	}
	
	public void updateMemberActions( World world) {
		for( UnitEntity u : this.units) {
			u.update( world);
		}
	}
	
	// First attempt at moving UnitEntities around the job assignment map.
	// Probably can be improved upon.
	public void updateJobAssignments() {
		HashMap<T ,Integer> newUnitAllocation = this.weights.allocate( this.units.size());		
		ArrayList<UnitEntity> unitsToReassign = new ArrayList<UnitEntity>();
		
		for( T t : this.jobAssignments.keySet()) {
			int diff = newUnitAllocation.get(t) - this.jobAssignments.get(t).size();
			if( diff < 0) {
				for( int i=0; i>diff; i--) {
					int index = Math.abs(i);
					unitsToReassign.add( this.jobAssignments.get(t).get( index));
					this.jobAssignments.get(t).remove( index);
				}
			}
		}
		
		for( T t : this.jobAssignments.keySet()) {
			int diff = newUnitAllocation.get(t) - this.jobAssignments.get(t).size();
			if( diff > 0) {
				for( int i=0; i<diff; i++) {
					UnitEntity ue = unitsToReassign.remove(0);
					this.jobAssignments.get(t).add( ue);
					ue.assignJob( t); //TODO
				}
			}
		}
	}
	
	
	public int size() {
		return this.units.size();
	}
	
}
