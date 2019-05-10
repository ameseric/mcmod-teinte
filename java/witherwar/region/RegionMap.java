package witherwar.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
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
	//private HashMap<ChunkPos ,Integer> map;
	
	//SuperChunkPos HashMap -> ChunkPos HashMap -> Region //convoluted, but for saving purposees lets us break up ChunkPos HashMap
	
	//HashMap<SuperChunkPos ,HashMap< ChunkPos ,Region>> map; //convoluted, but allows for saving/updating to be O(n)
	HashMap<SCPos ,SuperChunk> map;
	HashSet<Region> regionSet = new HashSet<Region>();
//	SuperChunkMap map;
	//private List< List<SuperChunk>> map;

	NBTTagCompound nbt;
	
	//public HashMap<Integer ,String> nameMap;
	public HashMap<EntityPlayer ,String> playerMap;
	public static ExecutorService saveExecutor = Executors.newFixedThreadPool(1);
	private int nextID = 0;
	

	
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
//		this.map = new SuperChunkMap();
		//this.nameMap = new HashMap<>();
		//this.map = new ArrayList< List<SuperChunk>>();
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
		//int id = getRegionID( pos);
//		String name = getRegionName( pos); 
//		if( name == null) {
//			name = new Integer( ThreadLocalRandom.current().nextInt(0, 999999 + 1)).toString();
//			//saveExecutor.submit( this.threadedFindRegionChunks(world, pos, id));
//			List<ChunkPos> map = findRegionChunks( world ,pos);
//			addRegion( id ,map);
//			//this.addRegionID(id);
//		}
		String regionName = this.getRegionName( new ChunkPos(pos));
		WitherWar.snwrapper.sendTo( new MessageEditGuidestone( pos.getX() ,pos.getZ() ,regionName) ,(EntityPlayerMP)playerIn);
		

	}
	
	
//    public void addRegionID( int id) {
//    	this.setRegionName( id ,"");
//    }

	
//	private void addRegion( int id ,List<ChunkPos> map) {
//		for( ChunkPos pos : map) {
//			this.map.put( pos ,id);
//		}
//		this.setRegionName( id ,"");
//	}

	
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
		Region r = this.getRegion( new ChunkPos( startingPosition));
		if( r == null) {
			this.createNewRegion( name ,startingPosition);
		}else {
			r.name = name;
			r.dirty = true;  //perform internal to object
			this.save();
		}
	}
	
	
	private void createNewRegion( String regionName ,BlockPos startingPosition) {
		Region newRegion = new Region( regionName ,startingPosition ,this.world);
		this.addRegion( newRegion);
	}
	
	
	@Nullable
	private Region getRegion( ChunkPos pos) {
		return this.getSuperChunk( pos).getRegion(pos);
	}	

	private void addRegion( Region r) { this.addRegion( r ,false);}
	private void addRegion( Region r ,boolean skipSave) {
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
		Region r = this.getRegion( cpos);
		if( r != null) {
			for( ChunkPos pos : r.getChunks()) {
				this.getSuperChunk( pos).remove( pos);
			}
			r.prepareForRemoval();
			this.save();
		}
    }
	
//	public int getRegionID(BlockPos pos) {
//		return getRegionID( new ChunkPos(pos));
//	}
//	
//	public int getRegionID( ChunkPos pos) {
//		if( this.map.containsKey( pos)) {
//			return this.map.get( pos);
//		}
//		return -1;
//	}	
	
	//public String getRegionName( int id) { return this.nameMap.get(id);	}
	//public String getRegionName( ChunkPos pos) { return this.getRegionName( this.getRegionID(pos)); }	
	//public String getRegionName( BlockPos pos) { return this.getRegionName( this.getRegionID(pos));	}
	
	//private String getRegionName( BlockPos pos) { return this.getRegionName(new ChunkPos(pos));}
	
	/**
	 * 
	 * @param pos
	 * @return Returns empty string if no Region exists.
	 */
	private String getRegionName( ChunkPos pos) { 
		Region r = this.getRegion( pos);
		if( r != null) {
			return r.name;
		}
		return "";
	}
	//this.map.get( new SCPos( pos)).getRegion( pos).name;
	//this.map.get( x>>4).get( z>>4).getRegion( ChunkPos);

	
	public String getPlayerRegionName( EntityPlayer player) { return this.playerMap.get( player);	}	
	public void setPlayerRegionName( EntityPlayer player ,String name) {this.playerMap.put( player ,name);	}
	
	//public void setRegionName( int id ,String name) {
	//	this.nameMap.put( id ,name);
	//	this.save();
	//}
	
	
	
	public void save() {
		this.data.markDirty();
	}
	
	
	
	
	public NBTTagCompound writeToNBT( NBTTagCompound compound) {
		Iterator<Region> iter = this.regionSet.iterator();
		while( iter.hasNext()) {
			Region r = iter.next();
			if( r.dirty) {
				r.writeToNBT( this.nbt);
				if( r.isEmpty()) {
					iter.remove();
				}
			}
		}
		
		this.nbt.setInteger( "numOfRegions" ,this.regionSet.size());
		int i = 0;
		for( Region r : this.regionSet) {
			this.nbt.setString( "Region"+i ,r.id);
			i++;
		}		
		
		compound.setTag( "TeinteRegionMap" ,this.nbt);
		return compound;
	}
	
	
	
	public void readFromNBT(NBTTagCompound compound) {		
		this.nbt = compound.getCompoundTag( "TeinteRegionMap");
		System.out.println( compound);
		
		int numOfRegions = this.nbt.getInteger( "numOfRegions");
		for( int i=0; i<numOfRegions; i++) {
			String id = this.nbt.getString( "Region"+i);
			NBTTagCompound regionNBT = this.nbt.getCompoundTag( id);
			Region r = new Region( regionNBT);
			this.addRegion( r ,true);
		}
	}
	
	
//	public Runnable threadedFindRegionChunks( World world ,BlockPos pos ,int id) {
//		List<ChunkPos> map = findRegionChunks( world ,pos);
//		this.addRegion(id, map);
//		return null;
//	}
	

	
	
}
