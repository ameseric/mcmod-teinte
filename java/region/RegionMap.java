package region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import com.google.common.collect.Sets;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import witherwar.WitherWar;
import witherwar.network.MessageEditGuidestone;
import witherwar.network.MessageRegionOverlayOn;
import witherwar.util.Symbol;

public class RegionMap {
	private WorldSavedData data;
	private HashMap<ChunkPos ,Integer> map;
	public HashMap<Integer ,String> nameMap;
	public HashMap<EntityPlayer ,String> playerMap;
	private HashSet<Integer> rejectedBiomes = Sets.newHashSet( 16 ,25 ,26);
	private HashMap<Integer ,HashSet<Integer>> similarBiomes = new HashMap<Integer ,HashSet<Integer>>();
	public static ExecutorService saveExecutor = Executors.newFixedThreadPool(1);
	
	private final int MAX_SEARCH_DEPTH = 30;

	
	public RegionMap( WorldSavedData data) {
		this.data = data;
		this.initDataStructs();
	
	}
	
	
	private void initDataStructs() {
		this.map = new HashMap<>();
		this.nameMap = new HashMap<>();
		this.playerMap = new HashMap<>();
		
		//messy, but allows us O(1) lookup, and we'll have 10k+ lookups in one tick.
		List< HashSet<Integer>> biomeGroups = new ArrayList<>();		
		HashSet<Integer> oceans = Sets.newHashSet( 0 ,24 ,46 ,49 ,45 ,48 ,44 ,47);
		biomeGroups.add( Sets.newHashSet( 0 ,24 ,46 ,49 ,45 ,48 ,44 ,47)); //oceans
		biomeGroups.add( Sets.newHashSet( 4 ,18 ,132)); //forest
		biomeGroups.add( Sets.newHashSet( 27 ,28 ,155 ,156)); //bforest
		biomeGroups.add( Sets.newHashSet( 29 ,157)); //dforest
		biomeGroups.add( Sets.newHashSet( 21 ,22 ,149 ,168 ,169)); //jungle
		biomeGroups.add( Sets.newHashSet( 5 ,19)); //taiga
		biomeGroups.add( Sets.newHashSet( 30 ,31)); //staiga
		biomeGroups.add( Sets.newHashSet( 32 ,33 ,160 ,161)); //giants
		biomeGroups.add( Sets.newHashSet( 14 ,15)); //mushroom
		biomeGroups.add( Sets.newHashSet( 6 ,134)); //swamp
		biomeGroups.add( Sets.newHashSet( 35 ,36)); //savanna
		biomeGroups.add( Sets.newHashSet( 163 ,164)); //shattered
		biomeGroups.add( Sets.newHashSet( 1 ,129)); //plains
		biomeGroups.add( Sets.newHashSet( 2 ,17 ,130)); //desert
		biomeGroups.add( Sets.newHashSet( 3 ,34 ,131 ,162)); //mountains
		biomeGroups.add( Sets.newHashSet( 37 ,38 ,39 ,165 ,166 ,167)); //badlands

		for( HashSet<Integer> biomeGroup : biomeGroups) {
			System.out.println( "Adding group: " + biomeGroup);
			for( Integer i : biomeGroup) {
				System.out.print( "Adding biome: " + i);
				this.similarBiomes.put( i ,biomeGroup);
			}
		}

	}
	
	
	
	public void guidestoneActivated( World world ,BlockPos pos ,EntityPlayer playerIn) {
		int id = getRegionID( pos);
		if( id == -1) {
			id = ThreadLocalRandom.current().nextInt(0, 999999 + 1);
			//saveExecutor.submit( this.threadedFindRegionChunks(world, pos, id));
			List<ChunkPos> map = findRegionChunks( world ,pos);
			addRegion( id ,map);
			//this.addRegionID(id);
		}
		String regionName = getRegionName( id);
		WitherWar.snwrapper.sendTo( new MessageEditGuidestone( id ,regionName) ,(EntityPlayerMP)playerIn);
		

	}
	
	
	
	
	public void tick( int tickcount ,World world) {
		if( world.provider.getDimension() != 0 || this.map.isEmpty()) { return;}
		
		List<EntityPlayer> players = world.playerEntities;
		
		for( EntityPlayer player : players) {
			if( player.getPosition().getY() > 58) { //&& transient worm in effect
				ChunkPos cpos = new ChunkPos( player.getPosition());
				//String playerRegion = this.getPlayerRegionName( player);
				String regionName = this.getRegionName( cpos);
				
				//System.out.println( "Checking region name......n->p");
				//System.out.println( regionName);
				//System.out.println( playerRegion);
				if( regionName == null) {
					this.setPlayerRegionName( player ,"");
				}else if( regionName != this.getPlayerRegionName( player)) {
					WitherWar.snwrapper.sendTo( new MessageRegionOverlayOn( regionName) ,(EntityPlayerMP)player);
					this.setPlayerRegionName( player ,regionName);
				}
			}
		}
	}	
	
	
	
	
    @SubscribeEvent
    public void playerLoggedOn( PlayerLoggedInEvent event) {
    	this.playerMap.put( event.player ,"");
    }
    
    
    public void addRegionID( int id) {
    	this.setRegionName( id ,"");
    }

	
	public void addRegion( int id ,List<ChunkPos> map) {
		for( ChunkPos pos : map) {
			this.map.put( pos ,id);
		}
		this.setRegionName( id ,"");
	}
	
    public void removeFromRegionMap( BlockPos pos) {
    	removeFromRegionMap( new ChunkPos(pos));
    }
	
	public void removeFromRegionMap( ChunkPos cpos) {
		int id = this.getRegionID( cpos);
		this.map.entrySet().removeIf( e -> e.getValue() == id); //efficient?
		this.nameMap.remove( id);
		this.save();
    }
	
	public int getRegionID(BlockPos pos) {
		return getRegionID( new ChunkPos(pos));
	}
	
	public int getRegionID( ChunkPos pos) {
		if( this.map.containsKey( pos)) {
			return this.map.get( pos);
		}
		return -1;
	}	
	
	public String getRegionName( int id) { return this.nameMap.get(id);	}
	public String getRegionName( ChunkPos pos) { return this.getRegionName( this.getRegionID(pos)); }	
	public String getRegionName( BlockPos pos) { return this.getRegionName( this.getRegionID(pos));	}
	
	public String getPlayerRegionName( EntityPlayer player) {
		return this.playerMap.get( player);
	}
	
	public void setPlayerRegionName( EntityPlayer player ,String name) {
		this.playerMap.put( player ,name);
	}
	
	public void setRegionName( int id ,String name) {
		this.nameMap.put( id ,name);
		this.save();
	}
	
	
	
	public void save() {
		this.data.markDirty();
	}
	
	
	//should probably optimize saving, so that name updates don't trigger all ChunkPos to be re-set
	//and *might* want to break up this.map into a new Object. I don't know when this will impact server performance.
	public NBTTagCompound writeToNBT( NBTTagCompound nbt) {
		int i = 0;
		for( ChunkPos cpos : this.map.keySet()) {
			int[] arr = { cpos.x ,cpos.z ,this.map.get( cpos)};
			nbt.setIntArray( "Chunk"+i ,arr);
			i++;
		}
		nbt.setInteger( "NumOfChunks" ,i);
		
		int j = 0;
		for( Integer id : this.nameMap.keySet()) {
			nbt.setString( "Region"+j ,this.nameMap.get(id));
			nbt.setInteger( "ID"+j ,id);
			j++;
		}
		nbt.setInteger( "NumOfRegions" ,j);

		
		return nbt;
	}
	
	
	
	public void readFromNBT(NBTTagCompound nbt) {
		//this.initDataStructs();
		
		int numOfChunks = nbt.getInteger( "NumOfChunks");
		for( int i = 0; i<numOfChunks; i++) {
			int[] arr = nbt.getIntArray( "Chunk"+i);
			this.map.put( new ChunkPos(arr[0] ,arr[1]) ,arr[2]);
		}
		
		int numOfRegions = nbt.getInteger( "NumOfRegions");
		for( int j=0; j<numOfRegions; j++) {
			this.nameMap.put( nbt.getInteger( "ID"+j) ,nbt.getString( "Region"+j));
		}
		
	}
	
	
	public Runnable threadedFindRegionChunks( World world ,BlockPos pos ,int id) {
		List<ChunkPos> map = findRegionChunks( world ,pos);
		this.addRegion(id, map);
		return null;
	}
	
	
    public List<ChunkPos> findRegionChunks( World world ,BlockPos pos) {
    	HashSet<ChunkPos> bmap = new HashSet<>();
    	ChunkPos origin = new ChunkPos(pos);
    	//map.add( origin);
    	Biome regionBiome = world.getBiome( pos);
    	
    	Symbol[] dirs = new Symbol[]{ Symbol.XP ,Symbol.ZP ,Symbol.ZN ,Symbol.XN};
    	//for( Symbol s : dirs) {
    		findRegionChunks( bmap ,world ,origin ,Symbol.XP ,regionBiome ,regionBiome ,0);
    		//bmap.remove( origin);
    	//}
    	
    	return new ArrayList<>(bmap);
    }
    
    
    private void findRegionChunks( HashSet<ChunkPos> bmap ,World world ,ChunkPos pos ,Symbol direction ,Biome originBiome ,Biome lastBiome ,int depth) {
    	//if( bmap.contains( pos) || bmap.size() > 400) {  return;}
    	if( bmap.contains( pos) || depth > this.MAX_SEARCH_DEPTH ) { return;}

    	
    	//if( bmap.contains( pos)) { System.out.println( "Already have it."); return;}
    	//if( bmap.size() > 400) { System.out.println( "Hit cap."); return;}
    	
    	Biome currentBiome = getAverageBiome( world ,pos);//world.getBiome( pos);
    	if( !isSimilarBiome( originBiome ,currentBiome) && !isPassableRiver( currentBiome ,lastBiome ,depth)) {
    		return;
    	}
    	
   		bmap.add( pos);
   		
   		int mod = depth / 5;
   		for( int y=120; y<mod+121; y++) {
   	   		world.setBlockState( new BlockPos( pos.getXStart()+7 ,y ,pos.getZStart()+7) ,Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState());
   		}
   		

    	ArrayList<Symbol> arr = Symbol.compliment2D( direction);
    	arr.add( direction);
    	for( Symbol s : arr) {
    		ChunkPos newpos = new ChunkPos( pos.x+s.getX() ,pos.z+s.getZ() );
    		findRegionChunks( bmap ,world ,newpos ,s ,originBiome ,currentBiome ,depth++);
    	}
    	return;
    }
    
    
    private Biome getAverageBiome( World world ,ChunkPos pos) {
    	Biome b =  world.getBiome( new BlockPos( pos.getXStart()+7 ,0 ,pos.getZStart()+7 ));
    	//System.out.println( "----------------------> " + b.getBiomeName() + " " + Biome.getIdForBiome( b));
    	return b;
    }
    
    
    private boolean isSimilarBiome( Biome originBiome ,Biome newBiome) {
    	if( isRejectBiome( newBiome)) { return false; }
    	
    	HashSet<Integer> map = this.similarBiomes.get( Biome.getIdForBiome( originBiome));
    	if( map != null) {
    		return map.contains( Biome.getIdForBiome( newBiome));
    	}    	
    	return originBiome == newBiome;
    }
    
    private boolean isPassableRiver( Biome newBiome ,Biome lastBiome ,int depth) {
    	int idNew = Biome.getIdForBiome( newBiome); 
    	if( idNew != 7 || idNew != 11) { return false;} //not river
    	
    	if( (float)depth/MAX_SEARCH_DEPTH < 0.65) { return false;}
    
    	int idOld = Biome.getIdForBiome( lastBiome);
    	if( (idOld == 7 && idNew == 7) || ( idOld == 11 && idNew == 11)){
    		return false;
    	}
    	
    	return true;
    }

 /**   
    private boolean followingRiver( Biome oldBiome ,Biome newBiome) {
    	int id1 = Biome.getIdForBiome( oldBiome);
    	int id2 = Biome.getIdForBiome( newBiome);
    	return ( id1 == 7 && id2 == 7) || ( id1 == 11 && id2 == 11);
    }
    
    private boolean isRiver( Biome biome) {
    	int id = Biome.getIdForBiome( biome);
    	return id == 7 || id == 11;
    }**/
    
    private boolean isRejectBiome( Biome biome) {    	
    	return this.rejectedBiomes.contains( Biome.getIdForBiome(biome)); 
    }
	
	
}
