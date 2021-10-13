package teinte.faction.aleph;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import teinte.TEinTE;
import teinte.faction.Faction;

public class FactionAleph extends Faction{

	
	public FactionAleph() {
//		super( world ,TEinTE.blocks.get( "aleph_core").block);
		super( Blocks.BEACON);
	}

	@Override
	public void setupJobWeights() {
		// TODO Auto-generated method stub
		
	}
	
	
	


}
