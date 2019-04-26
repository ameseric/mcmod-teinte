package witherwar.tickhandler;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

final public class TickHandlerRegion extends TickHandler{
	private int tickcount = 0;
	private World world;
	
	
	public TickHandlerRegion() {
		world = DimensionManager.getWorld(0);
	}
	
	@Override
	public void onWorldTick( ) {
		
		//as far as indexing at placement, 
		//if( true && tickcount%2 == 0) {//allowRegionNames){
			//select next player in queue
			//check if they have proper item
			//get block position -- might index by chunks, actually. Fuzziness isn't a problem here.
			//if y < 60, abort. Lazy, but good enough for personal purposes, and speedier.
			//cycle through region dictionary and plug in block position
			//if hit, trigger text display event
			//iterate next player queue
		//}
				
		++tickcount;
		
		
		if( tickcount%4 == 0) {
			List<EntityPlayer> players = this.world.playerEntities;
			for( EntityPlayer player : players) {
				if( true) { //need to check for Transient Worm
					if( player.getPosition().getY() > 60) {
						//this.world.getChunkFromBlockCoords( player.getPosition());
						
					}
				}
			}
		}
		
		
	}
	
	
}