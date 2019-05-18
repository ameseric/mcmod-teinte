package witherwar.region;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import witherwar.WitherWar;
import witherwar.network.MessageEditGuidestone;
import witherwar.network.MessageRegionOverlayOn;
import witherwar.region.SuperChunk.SCPos;

public class RegionMap {
	private WorldSavedData data;
	private World world;
	private HashMap<SCPos ,SuperChunk> map; //for allowing O(1) player region lookup
	private HashSet<RegionBiome> regionSet = new HashSet<RegionBiome>(); //for tracking Regions
	private NBTTagCompound nbt;
	public HashMap<EntityPlayer ,String> playerMap;
	public static ExecutorService saveExecutor = Executors.newFixedThreadPool(1);
	

	
	public RegionMap( WorldSavedData data) {
		this.data = data;
		this.initDataStructs();
	}	
	
	// Bad idea? Used for delaying findRegion call until after Guidestone activation.
	public void setWorld( World world) {
		this.world = world;
	}	
	
	private void initDataStructs() {
		this.map = new HashMap<>();
		this.playerMap = new HashMap<>();
	}
	
	
	
	public void tick( int tickcount ,World world) {
		if( world.provider.getDimension() != 0 || this.map.isEmpty()) { return;}
		
		List<EntityPlayer> players = world.playerEntities;
		
		for( EntityPlayer player : players) {
			if( player.getPosition().getY() > 58) { //&& transient worm in effect
				ChunkPos cpos = new ChunkPos( player.getPosition());
				//String playerRegion = this.getPlayerRegionName( player);
				String regionName = this.getRegionName( cpos);
				
				if( !regionName.equals( this.getPlayerRegionName( player) ) ) {
					this.setPlayerRegionName( player ,regionName);
					if( !regionName.equals("")) {
						WitherWar.snwrapper.sendTo( new MessageRegionOverlayOn( regionName) ,(EntityPlayerMP)player);
					}
				}
			}
		}
	}	
	
	
	
	
    @SubscribeEvent
    public void playerLoggedOn( PlayerLoggedInEvent event) {
    	this.playerMap.put( event.player ,"");
    }
    
    
    
	public void guidestoneActivated( World world ,BlockPos pos ,EntityPlayer playerIn) {
		String regionName = this.getRegionName( new ChunkPos(pos));
		WitherWar.snwrapper.sendTo( new MessageEditGuidestone( pos.getX() ,pos.getZ() ,regionName) ,(EntityPlayerMP)playerIn);
		

	}
	
	
	@Nullable
	private SuperChunk getSuperChunk( ChunkPos pos) {
		SuperChunk sc = this.map.get( new SCPos( pos));
		if( sc == null) {
			sc = new SuperChunk( pos);
			this.map.put( sc.pos ,sc);
		}
		return sc;
	}
	
	
	
	
	public void updateRegionName( String name ,BlockPos startingPosition) {
		RegionBiome r = this.getRegion( new ChunkPos( startingPosition));
		if( r == null) {
			this.createNewRegion( name ,startingPosition);
		}else {
			r.name = name;
			r.dirty = true;  //perform internal to object
			this.save();
		}
	}
	
	
	private void createNewRegion( String regionName ,BlockPos startingPosition) {
		RegionBiome newRegion = new RegionBiome( regionName ,startingPosition ,this.world);
		this.addRegion( newRegion);
	}
	
	
	@Nullable
	private RegionBiome getRegion( ChunkPos pos) {
		return this.getSuperChunk( pos).getRegion(pos);
	}	

	private void addRegion( RegionBiome r) { this.addRegion( r ,false);}
	private void addRegion( RegionBiome r ,boolean skipSave) {
		this.regionSet.add( r);
		for( ChunkPos pos : r.getChunks()) {
			this.getSuperChunk(pos).add(pos, r);
		}
		if( !skipSave) {
			this.save();
		}
	}
	
	
    public void removeRegion( BlockPos pos) {  	removeRegion( new ChunkPos(pos));  }	
	public void removeRegion( ChunkPos cpos) {
		RegionBiome r = this.getRegion( cpos);
		if( r != null) {
			for( ChunkPos pos : r.getChunks()) {
				this.getSuperChunk( pos).remove( pos);
			}
			r.prepareForRemoval();
			this.save();
		}
    }
	
	
	/**
	 * 
	 * @param pos
	 * @return Returns empty string if no Region exists.
	 */
	private String getRegionName( ChunkPos pos) { 
		RegionBiome r = this.getRegion( pos);
		if( r != null) {
			return r.name;
		}
		return "";
	}

	
	public String getPlayerRegionName( EntityPlayer player) { return this.playerMap.get( player);	}	
	public void setPlayerRegionName( EntityPlayer player ,String name) {this.playerMap.put( player ,name);	}
	

	
	public void save() {
		this.data.markDirty();
	}
	
	
	
	
	public NBTTagCompound writeToNBT( NBTTagCompound compound) {
		if( this.nbt == null) {
			this.nbt = new NBTTagCompound();
		}
		
		Iterator<RegionBiome> iter = this.regionSet.iterator();
		while( iter.hasNext()) {
			RegionBiome r = iter.next();
			if( r.dirty) {
				r.writeToNBT( this.nbt);
				if( r.isEmpty()) {
					iter.remove();
				}
			}
		}
		
		this.nbt.setInteger( "numOfRegions" ,this.regionSet.size());
		int i = 0;
		for( RegionBiome r : this.regionSet) {
			this.nbt.setString( "Region"+i ,r.id);
			i++;
		}		
		
		compound.setTag( "TeinteRegionMap" ,this.nbt);
		return compound;
	}
	
	
	
	public void readFromNBT(NBTTagCompound compound) {		
		this.nbt = compound.getCompoundTag( "TeinteRegionMap");
		
		int numOfRegions = this.nbt.getInteger( "numOfRegions");
		for( int i=0; i<numOfRegions; i++) {
			String id = this.nbt.getString( "Region"+i);
			NBTTagCompound regionNBT = this.nbt.getCompoundTag( id);
			RegionBiome r = new RegionBiome( regionNBT);
			this.addRegion( r ,true);
		}
	}
	
	
//	public Runnable threadedFindRegionChunks( World world ,BlockPos pos ,int id) {
//		List<ChunkPos> map = findRegionChunks( world ,pos);
//		this.addRegion(id, map);
//		return null;
//	}
	

	
	
}
