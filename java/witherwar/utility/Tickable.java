package witherwar.utility;

import net.minecraft.world.WorldServer;

public interface Tickable {

	
	public boolean isDead();
	
	
	public void _tick( int tickcount ,WorldServer world);
	
	
}
