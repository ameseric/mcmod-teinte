package witherwar.faction;

import net.minecraft.world.World;

public abstract class UnitEntity {
	private boolean isPuppet = true;
	
	
	public void update( World world) {
//		this.job.update();
	}
	
	public boolean isPuppet() {
		return this.isPuppet;
	}
	
	public static UEScout getNewScout() {
		return new UEScout();
	}

	
	
}


/*
 * Not sure how to treat units or jobs.
 * 
 * Jobs could be just an enum title for id, or
 * full classes similar to Faction Actions/Goals.
 * 
 * So a UnitEntity class might be implemented, and hard-coded to do certain tasks.
 * Or we could have tasks/jobs independent of UnitEntity.
 * 
 * I lean towards the latter, since this allows more flexibility later.
 * 
 * 
 * 
 */


class UEScout extends UnitEntity{
	
}