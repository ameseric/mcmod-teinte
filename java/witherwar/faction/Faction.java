package witherwar.faction;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import witherwar.faction.UnitEntity.Job;
import witherwar.faction.UnitEntity.UnitType;
import witherwar.util.BlockUtil;
import witherwar.util.WeightedHashMap;

/*
 * Note that, for this class, "materials" denote items collected from the world
 * (ores, wood, etc.) while "resources" refer to everything at the Factions' disposal,
 * e.g. materials, units, structures, etc.
 */

//one action per second, for now
public abstract class Faction {

	protected MaterialList materials;
	protected LinkedList<Event> memory;
	protected List<Action> actions;
	protected Personality personality;

//	private List<Reaction> reactions; //maybe?
	
	//group troops into a list, along with review logic (interface needed)

	//managers
	protected Home home; //structure manager, may become a Terralith extension.
	protected Troop scouts = new Troop( UnitType.SCOUT ,this);
	protected Troop gathers = new Troop( UnitType.GATHER ,this);
	//fighters
	//movers?
	protected ResourceMap map = new ResourceMap();
	
	protected Action masterGoal;
	
	protected int updateCounter = 0;
	protected int scoutRadius = 4; //chunk radius
	protected int anima = 10; //used to power structures, and perhaps units.
	
	protected BlockPos corePos;
	protected BlockPos oldCorePos; //only used for generating new cores after original is destroyed
	protected Block coreBlock;
	protected int respawnTimer = 600; //amount of time before trying to respawn a core block.
	
	
	protected HashMap<Block ,Job> materialJobs = new HashMap<>();
	
	
	
	public Faction( World world ,Block coreBlockType) {
		//I think I've decided: The weights will be independent of unit assignments.
		this.coreBlock = coreBlockType;
		this.setupMaterialJobs();		
	}
	
	
	public void update( World world) {		
		
		if( this.corePos == null) {
			boolean success = this.tryToPlaceCore( world);
			if( success) {
				this.setup(world);
			}
			return;
		} //TODO questions - do we want cores? Should they always pick new place?
		
		
		this.map.update();
		
		
		switch( updateCounter) {
//			case 0: reviewMemory(); 		break;
//			case 1: reviewResourceAssignments(); break;
			case 0: reviewScoutingAssignments(); break;
//			case 2: reviewCombatAssignments(); break; //wait
//			case ?: review damage to buildings / units - may fall back to other cate.
//			case ?: look through arraylist of additional reviews? Can't remove default...
		}
		
		this.scouts.updateMemberActions( world);
//		this.gathers.update();
		
		updateCounter = updateCounter > 4 ? 0 : updateCounter++;
	}
	
	
	private boolean tryToPlaceCore( World world) {
		//get random chunk x distance from player
		//examine surrounding chunks
		//check no buildings, villages, serious terrain elevation, etc.
		//world.setBlockState( this.corePos ,this.coreBlock.getDefaultState());
		
		this.corePos = BlockUtil.getAirYPos( new BlockPos(0,0,0) ,world);
		world.setBlockState( this.corePos ,this.coreBlock.getDefaultState());
		
		return true;
	}
	
	private void setup( World world) {
		this.map.setCenterPos( new ChunkPos( this.corePos));
		this.scouts.addUnit( this.corePos.add( 1,0,1), world);
		this.scouts.addUnit( this.corePos.add( -1,0,-1), world);
	}
	
	
	protected void setupMaterialJobs() {
		this.materialJobs.put( Blocks.LOG ,Job.HARVEST);
		this.materialJobs.put( Blocks.REDSTONE_ORE ,Job.MINE);
		this.materialJobs.put( Blocks.DIAMOND_ORE ,Job.MINE);
		this.materialJobs.put( Blocks.GOLD_ORE ,Job.MINE);
		this.materialJobs.put( Blocks.STONE ,Job.MINE);
		this.materialJobs.put( Blocks.COAL_ORE ,Job.MINE);
	}
	

	/*
	 * use to override the default job weights
	 */
	public abstract void setupJobWeights();
	
	
	
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
	
	//TODO
	protected void reviewScoutingAssignments() {
		
//		boolean increaseRadius = true; //until proven otherwise
		int weight = this.map.radialSize() / 9;
		this.scouts.weights.update( Job.PATROL ,weight);
		this.scouts.weights.update( Job.EXPLORE ,weight);
		
//		for( int i = this.map.boundary; i>0; i--) {
//			if( this.map.getRadial(i).size() < (i*8) - (i*2)) {
//				increaseRadius = false;
//				break;
//			}
//		}
//		if( increaseRadius) {
//			this.map.boundary = this.map.boundary + 2;
//		}
		
		this.scouts.updateJobAssignments();
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
	
	public ResourceMap getMap() {
		return this.map;
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
			return true; //TODO
		}
		
		
		
	}
	
	
	
}


/**	
	
        		EntityX entity = new EntityX(worldIn);
        		entity.setPosition(playerIn.posX, playerIn.posY, playerIn.posZ);
        		//DISCARD worldIn.spawnEntityInWorld(entity);
        		worldIn.spawnEntity( entity);
	
	
		
	
	
	
	


**/