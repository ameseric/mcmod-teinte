package witherwar.faction.tet;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.faction.Faction;

public class FactionTet extends Faction{

	
	public FactionTet() {
//		super( world ,TEinTE.blocks.get( "aleph_core").block);
		super( Blocks.BEACON);
	}

	@Override
	public void setupJobWeights() {
		// TODO Auto-generated method stub
		
	}
	
	
	


}
