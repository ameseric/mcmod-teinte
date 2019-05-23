package witherwar.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import witherwar.util.WeightedHashMap;


/**
 *  
 * @author Guiltygate
 * @param <T>
 * 
 */
public class Troop<T> {
	
	private HashMap< T ,List<UnitEntity>> jobAssignments;
	private ArrayList< UnitEntity> units;
	public WeightedHashMap<T> weights = new WeightedHashMap<T>();
	
	
	public void add( ){ //TODO: fix for first test
		this.units.add( UnitEntity.getNewScout());
	}
	
	public void updateMemberActions() {
		//pushNonPuppetUnits() //???


		//direct puppet units to act
		for( UnitEntity u : this.units) {
			if( u.isPuppet()) {
				u.act();
			}
		}
	}
	
	// First attempt at moving UnitEntites around the job assignment map.
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
					this.jobAssignments.get(t).add( unitsToReassign.remove(0));
				}
			}
		}
	}
	
	
	public int size() {
		return this.units.size();
	}
	
}
