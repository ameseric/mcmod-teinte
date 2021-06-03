package witherwar.utility;

import net.minecraft.world.WorldServer;

public interface Tickable {

	
	public boolean isDead();
	
	
	public void tick( int tickcount ,WorldServer world);
	
	
}
