package witherwar.util;

import java.util.HashMap;
import java.util.HashSet;

import com.google.common.collect.HashBasedTable;

import net.minecraft.util.math.ChunkPos;
import witherwar.region.Region;

public class AnotherMultiKeyMap {

	public AnotherMultiKeyMap() {
		
	}
	
	
	// Probably not worth it, headed for discard.
	
	
	public class MultiKey {
		public HashMap< Object ,HashMap<String ,Region>> keys;
		
		public MultiKey( String... keys) {
			HashBasedTable a = new HashBasedTable();
			
			for( String s : keys) {
//				this.keys.put( new ChunkPos(0,0) ,value);
			}
		}
	}
	
}
