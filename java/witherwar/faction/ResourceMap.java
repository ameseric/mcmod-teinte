package witherwar.faction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import witherwar.utility.BlockUtil;

public class ResourceMap {
	
	private HashMap<ChunkPos ,RMChunk> chunkMap;
	private HashMap<Integer ,HashSet<RMChunk>> radialMap;
	private ChunkPos center;
	public int boundary = 5;
	
	
	public ResourceMap() {
	}
	
	
	public void setCenterPos( ChunkPos pos) {
		this.center = pos;
	}
	
	public void update() {
		//update map boundary?
	}
	
	
	public void add( RMChunk chunk) {
		this.chunkMap.put( chunk.pos ,chunk);
		this.radialMap.get( chunk.r).add( chunk);
	}
	
	public RMChunk getChunk( ChunkPos pos) {
		return this.chunkMap.get(pos);
	}
	
	public int chunkSize() {
		return this.chunkMap.size();
	}
	
	public int radialSize() {
		return this.radialMap.size();
	}
	
	public HashSet<RMChunk> getRadial( int index){
		return this.radialMap.get( index);
	}
	
	public void record( ChunkPos pos ,World world) {
		RMChunk rmc = new RMChunk( pos);
		
		
		this.add( rmc);
	}
	
	public boolean hasChunk( ChunkPos pos) {
		return this.chunkMap.containsKey( pos);
	}
	
	public int calcR( ChunkPos pos) {
		return BlockUtil.calcR( pos ,this.center);
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
			this.r = BlockUtil.calcR( pos ,ResourceMap.this.center);
		}
		
		public RMChunk( ChunkPos pos) {
			this.pos = pos;
		}
		
		

		
		
		
	}
	
	
}
