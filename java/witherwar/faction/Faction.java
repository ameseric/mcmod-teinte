package witherwar.faction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.ChunkPos;
import witherwar.region.Region;

public abstract class Faction {

	private ResourceList materials;
	private List<Entity> units;
	private LinkedList<Event> memory;
	
	private Action goal;
	
	private Personality personality;
	
	private int counter = 0;
	private int unitCap = 10; //???
	
	private Region territory; // used to assign protection units?
	
	
	public void update() {
		
//		reviewMemory();		
		reviewGoal();
//		reviewMaterials();
//		reviewUnits();
		
		Action next = chooseNextAction();
		next.effect();
		
		
		counter++;
	}
	
	public Action chooseNextAction() {
		if( this.counter > 30) {
			reviewGoal();
		}
		
		
		
		return null;
	}
	
	
	public void reviewGoal() {
		boolean success = this.goal.tryToPerform( this.materials);
		if( success) {
			this.goal = this.chooseNewGoal();
		}else {
			this.assignUnits( this.goal.cost);
		}
	}
	
	
	public abstract Action chooseNewGoal();
	
	
	
	
	
	public void unitDestroyed( int id) {
		this.addToMemory( new Event());
	}
	
	
	public void addToMemory(Event e) {
		if( this.memory.size() > this.personality.memory) {
			this.memory.poll();
		}
		this.memory.add( e);
	}
	
	
	
	
	public class Action{
		public ResourceList cost;
		
		public boolean tryToPerform( ResourceList materials) {
			if( this.costMet(materials)) {
				this.perform();
				return true;
			}
			return false;
		}
		
		public void perform() {
			
		}
		
		public boolean costMet( ResourceList materials) {
			return false;
		}
		
	}

	
	
	public class Event{
		boolean outcome;
		ChunkPos location;
		
	}
	
	
	public class ResourceList {
		private HashMap<Block,Integer> materials;
	}
	
	
	public class Reaction{
		public Action action;
		public boolean trigger() {
			return false;
		}
	}
	
	
	//Would influence Action choice... so need more thought on Actions.
	public class Personality{
		int aggr;
		int curiosity;
		int memory = 20;
		int persistence;
	}
	
}


/**

Leaning towards centralized logic right now, bypassing tile entities (maybe).

Would that work for all faction implementations though?

This would necessitate two "versions" of each faction, for loaded/unloaded.
Or, two logic paths for each unit / faction act.
Maybe just for each unit.


What actions can a faction take?
	- build structure
	- build unit
	- assign unit
		- to collect resources
		- to build structure
	- activate alert ( set combat units to repel invaders )
	
	
	
	
	When a faction spawns, what would they do first?
	They spawn as a core block with minimal resources.
	First thing you want to do is spawn resource drones.
	What resource do you want first?
	More drones = stone and redstone?
	Would the Terralith even have Drones?
	Collection points? - Would solve issue of allowing players to raid, if we had
		timed deliveries of resources.
		
		
		
		
		
	Types of buildings will be faction-dependent....
	And Terraliths won't need a lot of this...
	Even Void probably won't.
	Maybe a generic Faction isn't possible.
	Might want to just write Aleph, and then if anything's general, move it out.
	
	
	
	
        		EntityX entity = new EntityX(worldIn);
        		entity.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
        		worldIn.spawnEntityInWorld(entity);
	
	
	
	
	
	
	
	
	


**/