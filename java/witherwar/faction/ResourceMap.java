package witherwar.faction;

import java.util.HashMap;
import java.util.List;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;

public class ResourceMap {
	/*
	 * Index options:
	 * 		ChunkPos
	 * 		Materials (discard)
	 * 		Player events
	 * 		radial distance
	 */
	
	private HashMap<ChunkPos ,RMChunk> chunkMap;
	private HashMap<Integer ,List<RMChunk>> radialMap;
	
	
	
	public void add( RMChunk chunk) {
		this.chunkMap.put( chunk.pos ,chunk);
		this.radialMap.get( chunk.r).add( chunk);
	}
	
	public int size() {
		return this.chunkMap.size();
	}
	
	public List<RMChunk> getRadial( int index){
		return this.radialMap.get( index);
	}
	
	
	
	
	
	public class RMChunk{
		public ChunkPos pos;
		public int r;
		public int numOfPlayerAppearances; //event list?
		public boolean hasWood;
		public boolean claimedForBuilding;
		public int averageY;
		public int gradientY;
		public Biome biome;
		
		public RMChunk( int x ,int z) {
			this.pos = new ChunkPos( x ,z);
		}
		
		public RMChunk( ChunkPos pos) {
			this.pos = pos;
		}
		
		
		
	}
	
	
}
