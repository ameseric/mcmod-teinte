package witherwar.hermetics;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import witherwar.tilelogic.RitualBlockTile;

public class RitualOperations {

	//TODO consider changing to RuleBook class, adding BINDER and FOCI collections
	
	
	private RitualOperations() {}
	
	
	
	
	public Muir execute( Muir m ,Block type ,int magnitude) {
		
		if( !RitualBlockTile.FOCI.contains( type)) {
			return m;
		}
		
		if( type == Blocks.EMERALD_BLOCK) {
			
		}
		
		return m;
	}
	
	
	
	
//	private Muir expel( Muir m) {
//		return m;
//	}
	
	
	
	private Muir transform( Muir m) {
		return m;
	}
	
	
	
	private Muir filterNoble( Muir m ,int amount) {
		for( MuirElement e : MuirElement.values()) {
			
		}
		return m;		
		
	}
	
	
	private Muir filterQuiet( Muir m ,int amount) {
		return m;
	}
	
	
	private Muir store( Muir m) {
		return m;		
		
	}
	
	
//	private Muir collect() {
//				
//		
//	}
	
	
	
	
}
