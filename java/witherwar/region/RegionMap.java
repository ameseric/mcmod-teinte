package witherwar.region;

import java.util.HashMap;
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
	//private HashMap<ChunkPos ,Integer> map;
	
	//SuperChunkPos HashMap -> ChunkPos HashMap -> Region //convoluted, but for saving purposees lets us break up ChunkPos HashMap
	
	//HashMap<SuperChunkPos ,HashMap< ChunkPos ,Region>> map; //convoluted, but allows for saving/updating to be O(n)
	HashMap<SCPos ,SuperChunk> map;
//	SuperChunkMap map;
	//private List< List<SuperChunk>> map;

	
	//public HashMap<Integer ,String> nameMap;
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
				
				if( regionName != this.getPlayerRegionName( player)) {
					this.setPlayerRegionName( player ,regionName);
					if( regionName != "") {
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
//		List<SuperChunk> a;
//		try{
//			 a = this.map.get( (pos.x>>5)+2000);
//		}catch( IndexOutOfBoundsException e){
//			a = new ArrayList<SuperChunk>();
//			this.map.add( (pos.x>>5)+2000 ,a);
//		}
//		
//		try {
//			return a.get( (pos.z>>5)+2000 );
//		}catch( IndexOutOfBoundsException e){
//			SuperChunk newSC = new SuperChunk();
//			a.add( (pos.z>>5)+2000 ,newSC);
//			return newSC;
//		}
		
		SuperChunk sc = this.map.get( new SCPos( pos));
		if( sc == null) {
			sc = new SuperChunk( pos);
			this.map.put( sc.pos ,sc);
		}
		return sc;
	}
	
	
	
	
	public void updateRegionName( String name ,BlockPos startingPosition) {
		ChunkPos pos = new ChunkPos( startingPosition);
		//String oldName = this.getRegionName( pos);
		Region r = this.getRegion(pos);
		if( r == null) {
			this.createNewRegion( name ,startingPosition);
		}else {
			r.name = name;
		}
	}
	
	
	private void createNewRegion( String regionName ,BlockPos startingPosition) {
		Region newRegion = new Region( regionName ,startingPosition ,this.world);
		for( ChunkPos pos : newRegion.getChunks()) {
			this.getSuperChunk( pos).add( pos ,newRegion);			
		}
	}
	
	
	@Nullable
	private Region getRegion( ChunkPos pos) {
		return this.getSuperChunk( pos).getRegion(pos);
	}	

	
    public void removeRegion( BlockPos pos) {  	removeRegion( new ChunkPos(pos));  }	
	public void removeRegion( ChunkPos cpos) {
		//int id = this.getRegionID( cpos);
		//this.map.entrySet().removeIf( e -> e.getValue() == id); //efficient?
		//this.nameMap.remove( id);
		Region toRemove = this.getRegion( cpos);
		if( toRemove != null) {
			for( ChunkPos pos : toRemove.getChunks()) {
				this.getSuperChunk( pos).remove( pos);
			}
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
	
	
	
	
	public NBTTagCompound writeToNBT( NBTTagCompound nbt) {
//		int i = 0;
//		for( ChunkPos cpos : this.map.keySet()) {
//			int[] arr = { cpos.x ,cpos.z ,this.map.get( cpos)};
//			nbt.setIntArray( "Chunk"+i ,arr);
//			i++;
//		}
//		nbt.setInteger( "NumOfChunks" ,i);
		
//		int j = 0;
//		for( Integer id : this.nameMap.keySet()) {
//			nbt.setString( "Region"+j ,this.nameMap.get(id));
//			nbt.setInteger( "ID"+j ,id);
//			j++;
//		}
//		nbt.setInteger( "NumOfRegions" ,j);

		
		return nbt;
	}
	
	
	
	public void readFromNBT(NBTTagCompound nbt) {
		
//		int numOfChunks = nbt.getInteger( "NumOfChunks");
//		for( int i = 0; i<numOfChunks; i++) {
//			int[] arr = nbt.getIntArray( "Chunk"+i);
//			this.map.put( new ChunkPos(arr[0] ,arr[1]) ,arr[2]);
//		}
//		
//		int numOfRegions = nbt.getInteger( "NumOfRegions");
//		for( int j=0; j<numOfRegions; j++) {
//			this.nameMap.put( nbt.getInteger( "ID"+j) ,nbt.getString( "Region"+j));
//		}
		
	}
	
	
//	public Runnable threadedFindRegionChunks( World world ,BlockPos pos ,int id) {
//		List<ChunkPos> map = findRegionChunks( world ,pos);
//		this.addRegion(id, map);
//		return null;
//	}
	
	

    
    
    

	
	
}
