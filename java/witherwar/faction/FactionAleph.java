package witherwar.faction;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import witherwar.TEinTE;

public class FactionAleph extends Faction{

	
	public FactionAleph( World world) {
//		super( world ,TEinTE.blocks.get( "aleph_core").block);
		super( world ,Blocks.BEACON);
	}

	@Override
	public void setupJobWeights() {
		// TODO Auto-generated method stub
		
	}
	
	
	


}
