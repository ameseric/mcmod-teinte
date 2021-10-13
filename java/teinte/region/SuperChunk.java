package teinte.region;

import java.util.HashMap;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

/**
 * 
 * @author Guiltygate
 * 
 * Where 1 Chunk is a 16x16 grid of Blocks, a SuperChunk is a 32x32 collection of Chunks.
 * 
 * Used for ease of reading when indexing RegionMap, which is initially divided into SuperChunks.
 *
 */
public class SuperChunk{
	private HashMap< ChunkPos ,RegionBiome> map;
	public SCPos pos;
	
	public SuperChunk( ChunkPos pos) {	
		this.map = new HashMap<>();
		this.pos = new SCPos( pos);
	}
	
	
	public void add( ChunkPos pos ,RegionBiome r) {
		this.map.put( pos, r);
	}
	
	
	public void remove( ChunkPos pos) {
		this.map.remove( pos);
	}
	
	
	@Nullable
	public RegionBiome getRegion( ChunkPos pos) {
		return this.map.get( pos);
	}
	
	
	
	public static class SCPos {
		public int x;
		public int z;
		
		
		public SCPos( BlockPos pos) {
			this.x = pos.getX() >> 9;
			this.z = pos.getZ() >> 9;
		}
		
		public SCPos( ChunkPos pos) {
			this.x = pos.x >> 5;
			this.z = pos.z >> 5;		
		}
		
		
		@Override
		public boolean equals( Object o) {
			if( !(o instanceof SCPos)) {
				return false;
			}
			SCPos scp = (SCPos) o;
			
			return (scp.x == this.x) && ( scp.z == this.z);
		}
		
		
		//Written with help from https://stackoverflow.com/questions/11742593/what-is-the-hashcode-for-a-custom-class-having-just-two-int-properties
		@Override
		public int hashCode() {
			int hash = 17;
			hash = hash * 31 + this.x;
			hash = hash * 31 + this.z;
			return hash;
		}
		
		
	}
}
