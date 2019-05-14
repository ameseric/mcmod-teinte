package witherwar.faction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.ChunkPos;
import witherwar.region.Region;

//one action per second, for now
public abstract class Faction {

	private ResourceList materials;
	private List<Entity> units;
	private LinkedList<Event> memory;
	private List<Action> actions;
	private List<Reaction> reactions;
	
	private Action masterGoal;
	private Action tempGoal; //trumps masterGoal for period of time? Reaction, basically....
	
	private Personality personality;
	
	private int counter = 0;
//	private int unitCap = 10; //rather than hard-coding, we'll have an upkeep cost, and force the AI to scrap.
	private int upkeepCost = 100; //meaningless right now
	
	private Region territory; // used to assign protection units?
	
	
	public Faction() {

	}
	
	
	public void update() {
		
//		reviewMemory();		
		reviewGoal();
//		reviewMaterials();
//		reviewUnits();
		
		Action next = chooseNextAction();
		next.perform();
		
		
		counter++;
	}
	
	public Action chooseNextAction() {
				
		
		
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
		public int level = 1;
		
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
	
	
	
	

	
	
Take a step back.
What is the goal of a Faction?
To expand without being stopped.
So, acquisiton of resources and protection of 1) core, 2) resources & resource gatherers.
Resources are used to gather more resources and protect core... more or less. Aleph may have another goal. Ignore for now.

Maybe another way to look at it - 
Our resources or units go up - good (units are judged by their r.cost).
Their resources / units go down - neutral / good.
Evaluate by tradeoffs.

If a bad tradeoff occurs, either invest in more c.units, or different c.units?

Maybe two logic trains - balance resource tradoff with players while also achieving Master Goal?
Aleph Prime Goal - best c.unit produced at Heart.
Aleph Post Goal - Meet 

Goals will be lists of Major Actions. Aleph:
	- best c.unit at Heart
	- expand housing to accomodate Aleph villagers (reach housing value?)
	- collect resources for city upkeep
	- Build anti-rot mechanism? Floating machines for city? TAI


OK, so:
	Aleph are going to be independent of Terraliths. Use Monument Core (or Artifical Anocortex?).
	Jodh are going to become Terralith / Individual symbiotes. Use Slave Anocortex?
	Kalimmacinus will be Terralith faction. Use Anocortex.
	Rot mobs will not be faction-based.
	
Decision-process may differ, but underlying mechanics should be same, and located here.

Ages:
	
	Breath
	
	Silence / Sleep / Repose / Dreams
	
	
	
        		EntityX entity = new EntityX(worldIn);
        		entity.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
        		worldIn.spawnEntityInWorld(entity);
	
	
	
	
	
	
	
	
	


**/