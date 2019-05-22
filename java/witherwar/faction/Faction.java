package witherwar.faction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import witherwar.entity.EntityFactionFlying;
import witherwar.region.Region;
import witherwar.region.RegionBiome;
import witherwar.util.WeightedHashMap;

/*
 * Note that, for this class, "materials" denote items collected from the world
 * (ores, wood, etc.) while "resources" refer to everything at the Factions' disposal,
 * e.g. materials, units, structures, etc.
 */

//one action per second, for now
public abstract class Faction {

	private MaterialList materials;
	private LinkedList<Event> memory;
	private List<Action> actions;
	private Personality personality;

//	private List<Reaction> reactions; //maybe?

	//managers
	private Home home; //structure manager, may become a Terralith extension.
	private UnitCategory scouts = new UnitCategory();
	private ResourceMap map;
	
	private Action masterGoal;
	
	private int updateCounter = 0;
	private int upkeepCost = 100; //meaningless right now
	private int scoutRadius = 4; //chunk radius
	private int anima = 10; //used to power structures, and perhaps units.

	
	//weights
	private WeightedHashMap scoutWeights = new WeightedHashMap();
	private WeightedHashMap materialWeights;
	
	
	public Faction( World world) {
		//still debating universal weight vs current weight....
		//because some Factions may need more of a resource than others...
		//but I don't know.
		Object[] os = new Block[] { Blocks.STONE ,Blocks.REDSTONE_ORE ,Blocks.LOG};
		this.materialWeights = new WeightedHashMap( os);

		this.scoutWeights.put( "patrol" ,0);
		this.scoutWeights.put( "explore" ,4);
		
		this.scouts.add( new EntityFactionFlying( world));
		
		/*
		 * note we're switching without thought between weighted probabilities
		 * and using those weights as priority in a queue.
		 * 
		 * THESE ARE NOT THE SAME THING. Figure out what you're doing!
		 * 
		 * We might want priority... weighted probabilities are an easy way to introduce
		 * unpredictability, but could also result in system killing itself, which is boring
		 * to players.
		 * 
		 * But we also want some fuzziness...
		 * 
		 * 
		 * Could we use weights to calculate a distribution, then assign our units to
		 * match that distribution? I mean, we can, but would that work any better?
		 * 
		 * 
		 */
	}
	
	
	public void update( World world) {
		
		
//		reviewAssignments
//		influencers:
//				current goal cost
//				current material stockpiles
//				current unit count
//				active reactions
//				weights? would be area-based, and triggered by reactions?
//		
//		chooseNextAction (weighted)
		
		
		switch( updateCounter) {
//			case 0: reviewMemory(); 		break;
//			case 1: reviewResourceAssignments(); break;
			case 0: reviewScoutingAssignments(); break;
//			case 2: reviewCombatAssignments(); break; //wait
//			case 3: updateWeights(); 		break;
//			case 4:{ chooseNextAction().perform(); break;}
//			case ?: review damage to buildings / units - may fall back to other cate.
		}		
		
		updateCounter = updateCounter > 4 ? 0 : updateCounter++;
	}
	
	
	/**
	 * Cycle through list of possible reactions and trigger appropriate items.
	 */
	protected void reviewMemory() {
		
	}
	
	
	

	protected void reviewResourceAssignments() {
		//review current drone distribution
		//do we have drones allocated for the current project materials?
		//are we out (or low) of a certain material? do we have a source to gather?
		//are we reacting (danger?)
		//!!!!! we could have the resources themselves weighted. THey could start near-equal,
		//then adjust from reactions and goals. Works like a request system.
		//need, at some point, to account for building structures as well?
		//Also repairing structures? need blueprints and comparison
		
		//how do we perform the calculation?

		//check weight of resource
		//check current stockpile of resource
		//high amount, lower weight, and vice versa
		
//		for( each resource) {
//			updateWeight( resource);			
//		}
//		
//		find highest- and lowest-weight resources
//		if free unit, reassign to highest
//		otherwise reassign lowest to highest
		
/**
 * if we do priority queue, you can end up with the lowest-prioirity still being fairly high
 * since everything's being segmented, you can't choose between them.
 * but you also want some momentum to a task, which that would automatically provide.
 * 
 * random weighted choice means we might assign back-and-forth between competing objectives.
 * either need to manually assign momentum, or...?
 */
		
	}
	
	
	protected void reviewScoutingAssignments() {
		
		boolean increaseRadius = true;
		int weight = this.map.size() / 9;
		this.scoutWeights.updateWeight( "patrol" ,weight);
		//this.scouts.updateJobWeight( "patrol" ,weight);
		
		//this.scoutWeights.updateWeight( "explore" ,weight);
		
		for( int i = this.scoutRadius; i>0; i--) {
			if( this.map.getRadial(i).size() < i*8) {
				increaseRadius = false;
				break;
			}
		}
		if( increaseRadius) {
			this.scoutRadius = this.scoutRadius + 3;
		}
		
		this.scoutWeights.allocate( this.scouts.size() ,this.scouts.jobAssignments);
		//this.scouts.assignJobs()
		
	}

	//---------- NBT Save / Load -------------------//
	
	public NBTTagCompound save( NBTTagCompound nbt) {
		/*
		 * will need to save:
		 * 		ResourceMap Chunks
		 * 		Unit count, and some stats (such as position)
		 * 			we'll want to save them from puppet state
		 * 		All weights, of course
		 * 		Home structures
		 */
		return nbt;
	}
	
	
	
	//---------- Utility Methods -------------------//
	public void addNewAction( Action a) {
		this.actions.add( a);
	}	
	
	public void addToMemory(Event e) {
		if( this.memory.size() > this.personality.memory) {
			this.memory.poll();
		}
		this.memory.add( e);
	}
	
	public void produce( ) {
//		this.architect.
	}
	
	
	
	//---------------- Structuring Classes -------------------//
	
	public abstract class Action{
		public MaterialList cost;
		public int level = 1;
		protected Faction faction;
		
		public Action( Faction f) {
			this.faction = f;
		}
		
		public boolean tryToPerform( MaterialList materials) {
			if( this.costMet(materials)) {
				this.perform();
				return true;
			}
			return false;
		}
		
		public abstract void perform();		
		public abstract boolean costMet( MaterialList materials);
		
	}

	
	
	public class Event{
		boolean outcome;
		ChunkPos location;
		
	}
	
	
	public class MaterialList {
		private HashMap<Block,Integer> materials;
		
		public void add( Block b ,Integer i) {
			this.materials.put( b ,i);
		}
		
		public boolean has( Block b ,Integer i) {
			Integer j = this.materials.get( b);
			return (j != null) && j >= i;
		}
		
		public boolean compare( MaterialList rl) {
			boolean costMet = true;
			for( Block b : this.materials.keySet()) {
				costMet = costMet && rl.has( b ,this.materials.get(b));
			}
			return costMet;
		}
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
	
	
	
	
	//---------- Instantiated Helper Classes -------------------//

	public class BuildCUnit extends Action{

		public BuildCUnit(Faction f) {
			super(f);
			this.cost.add( Blocks.COBBLESTONE ,8);
		}

		@Override
		public void perform() {
			//this.faction.produce( new Entity());
		}

		@Override
		public boolean costMet(MaterialList materials) {
			//return this.faction.units.combat.two.cost;
		}
		
		
		
	}
	
	
	
}


/**	
	
        		EntityX entity = new EntityX(worldIn);
        		entity.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
        		//DISCARD worldIn.spawnEntityInWorld(entity);
        		worldIn.spawnEntity( entity);
	
	
		
	
	
	
	


**/