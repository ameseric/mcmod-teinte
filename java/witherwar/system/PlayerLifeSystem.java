package witherwar.system;

import net.minecraft.entity.player.EntityPlayer;
import witherwar.TEinTE;

public class PlayerLifeSystem {

	private int numberOfSharedLives = 10;
	
	
	
	public PlayerLifeSystem() {
		
	}
	
	
	
	public void update( EntityPlayer player) {
		if( TEinTE.config.sharedLives) {
			if( this.numberOfSharedLives > 0) {
				this.subtractLife();
				return;
			}else {
				//TODO: inflict debuff or whatever for running out
			}
		}//TODO: non-shared life subtraction
	}
	
	
	
	public void subtractLife() {
		this.numberOfSharedLives--;
	}
	
	
	public void subtractLife( EntityPlayer player) {
		//TODO life pool for each player
	}
	
	
	
}
