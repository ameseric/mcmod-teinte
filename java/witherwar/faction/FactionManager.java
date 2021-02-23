package witherwar.faction;

import java.util.ArrayList;

import net.minecraft.world.World;
import witherwar.faction.tet.FactionTet;


//TODO: Consider having each living faction calculate generic travel routes while loading a game world, then updating periodically
//during play session? Leave in-moment pathfinding to entity interactions.

public class FactionManager {

	private ArrayList<Faction> livingFactions = new ArrayList<>();
	
	public void tick( World world) {
		
		//TODO update factions
		
		if( this.livingFactions.size() == 0) { //Testing Placeholder
			this.createNewFaction();
		}
		
		
		
	}
	
	
	
	
	
	private void createNewFaction() {
		Faction newFaction = new FactionTet();
		
		this.livingFactions.add( newFaction);
		this.placeFactionHome( newFaction); //TODO: Evaluate whether this will be Faction-dependent (Faction) or independent (Manager).
	}
	
	
	private void placeFactionHome( Faction faction) {
		
			
		
	}
	
	
}
