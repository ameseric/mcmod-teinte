package witherwar.faction;

import java.util.HashMap;
import java.util.List;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
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
	private ChunkPos center;
	private int boundary = 5;
	
	
	public ResourceMap( ChunkPos centerPos) {
		this.center = centerPos;
	}
	
	
	public void update() {
		//update map boundary?
	}
	
	
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
	
	public void record( ChunkPos pos ,World world) {
		RMChunk rmc = new RMChunk( pos);
		
		rmc.calculateYValues( pos);
		rmc.r = 
		
		this.add( rmc);
	}
	
	public boolean hasChunk( ChunkPos pos) {
		return this.chunkMap.containsKey( pos);
	}
	
	public boolean isExplorable( ChunkPos pos) {
		int xDiff = Math.abs( pos.x - this.center.x);
		int zDiff = Math.abs( pos.z - this.center.z);
		int r = xDiff > zDiff ? xDiff : zDiff;
		return !this.hasChunk(pos) && r < boundary; 
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
