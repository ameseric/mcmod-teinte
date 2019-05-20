package witherwar.region;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.ChunkPos;

/**
 * 
 * @author Guiltygate
 * 
 * A consecutive series of ChunkPos that share an identifier (name).
 * The name is displayed to the player when first traversing the region.
 * (If used with a RegionMap object)
 * 
 * The Region contains its own ChunkPos HashSet because, while being already contained by the RegionMap HashMap, it allows for O(n) removal
 * from the HashMap.
 *
 */
public class Region {
	public String name;
	protected boolean dirty = false;
	public String id;
	protected HashSet<ChunkPos> chunks = new HashSet<ChunkPos>();	


	
	public Region( String name) {
		this.name = name;		
		this.id = UUID.randomUUID().toString();
		this.dirty = true;
	}
	
	public Region( NBTTagCompound nbt) {
		this.id = nbt.getString( "id");
		this.name = nbt.getString( "name");
		this.chunks = new HashSet<ChunkPos>();
		
		int numOfChunks = nbt.getInteger( "numOfChunks");
		for( int i=0; i<numOfChunks; i++) {
			int[] coord = nbt.getIntArray( "Chunk"+i);
			this.chunks.add( new ChunkPos( coord[0] ,coord[1]));
		}
	}
	
	public HashSet<ChunkPos> getChunks(){
		return this.chunks;
	}	

	
	@Override
	public boolean equals( Object o) {
		if( !(o instanceof RegionBiome)) {
			return false;
		}
		RegionBiome r = (RegionBiome) o;
		
		return this.chunks == r.chunks;
	}
	
	@Override
	public int hashCode() {
		return chunks.hashCode();
	}
	
	
	public boolean isEmpty() {
		return this.chunks.isEmpty();
	}
	
	
	public void prepareForRemoval() {
		this.chunks.clear();
		this.dirty = true;
	}
	
	public void writeToNBT( NBTTagCompound nbt) {
		if( this.isEmpty()) {
			nbt.removeTag( this.id);
			return;
		}
		
		NBTTagCompound rnbt = nbt.getCompoundTag( this.id);
		
		rnbt.setString( "id" ,this.id);
		rnbt.setString( "name" ,this.name);
		rnbt.setInteger( "numOfChunks" ,this.chunks.size());
		
		int j = 0;
		for( ChunkPos pos : this.chunks) {
			rnbt.setIntArray( "Chunk"+j ,new int[]{ pos.x ,pos.z});
			j++;
		}
		nbt.setTag( this.id ,rnbt);
		
		this.dirty = false;
	}
	
}
