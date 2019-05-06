package region;

import java.util.HashSet;

import net.minecraft.util.math.ChunkPos;

/**
 * 
 * @author Guiltygate
 * 
 * A consecutive series of ChunkPos that share an identifier (name).
 * The name is displayed to the player when first traversing the region.
 * 
 * The Region contains its own ChunkPos HashSet because, while being already contained by the RegionMap HashMap, it allows for O(n) removal
 * from the HashMap.
 *
 */
public class Region {
	HashSet<ChunkPos> chunks;
	String name;
	
	public Region( String name ,HashSet<ChunkPos> chunks) {
		this.chunks = chunks;
		this.name = name;
	}
	
	
	@Override
	public boolean equals( Object o) {
		if( !(o instanceof Region)) {
			return false;
		}
		Region r = (Region) o;
		
		return this.chunks == r.chunks;
	}
	
	@Override
	public int hashCode() {
		return chunks.hashCode();
	}
	
	
}
