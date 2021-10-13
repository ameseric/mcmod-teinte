package teinte.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import teinte.faction.UnitEntity.Job;
import teinte.faction.UnitEntity.UnitType;
import teinte.utility.WeightedHashMap;


/**
 *  
 * @author Guiltygate
 * 
 */
public class Troop {	
	private HashMap< Job ,List<UnitEntity>> jobAssignments;
	private ArrayList< UnitEntity> units;
	protected WeightedHashMap<Job> weights = new WeightedHashMap<Job>();
	private UnitEntity.UnitType troopType;
	
	private Faction faction;
	
	private static final HashMap<UnitEntity.UnitType ,HashMap< Job ,Integer>> defaultStartingWeights = new HashMap<>();
	
	
	/*
	 * Default Starting Weights - Factions are encouraged to override these with their own.
	 */
	static {
		defaultStartingWeights.put( UnitType.SCOUT ,new HashMap< Job ,Integer>());
		defaultStartingWeights.get( UnitType.SCOUT).put( Job.EXPLORE ,4);
		defaultStartingWeights.get( UnitType.SCOUT).put( Job.PATROL ,0);
		
		defaultStartingWeights.put( UnitType.GATHER ,new HashMap< Job ,Integer>());
		defaultStartingWeights.get( UnitType.GATHER).put( Job.HARVEST ,4);
		defaultStartingWeights.get( UnitType.GATHER).put( Job.MINE ,2);
	}
	
	
	public Troop( UnitEntity.UnitType t ,Faction parent) {
		this.troopType = t;
		this.faction = parent;
		
		for( Job job : defaultStartingWeights.get( this.troopType).keySet()) {
			this.weights.put( job ,defaultStartingWeights.get( this.troopType).get(job));
		}
	}
	
	
	public void addUnit( BlockPos pos ,World world){
		this.units.add( new UnitEntity( this.troopType ,new ChunkPos(pos) ,this ,world));
	}
	
	public void updateMemberActions( World world) {
		for( UnitEntity u : this.units) {
			u.update( world);
		}
	}
	
	public Faction getFaction() {
		return this.faction;
	}
	
	public void addJob( Job job ,int weight) {
		this.weights.put( job ,weight);
	}
	
	// First attempt at moving UnitEntities around the job assignment map.
	// Probably can be improved upon.
	public void updateJobAssignments() {
		if( this.jobAssignments.size() == 1) { return;}
		
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
