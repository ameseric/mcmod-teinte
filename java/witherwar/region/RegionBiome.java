package witherwar.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;

import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import witherwar.utility.Symbol;

/**
 * 
 * @author Guiltygate
 * 
 * Extension of Region. Allows for auto-mapping of Region chunks via similar
 * biome types.
 *
 */
public class RegionBiome extends Region{
	private static final HashMap<Integer ,HashSet<Integer>> similarBiomes = new HashMap<Integer ,HashSet<Integer>>();	
	
	private HashSet<Integer> rejectedBiomes = Sets.newHashSet( 16 ,25 ,26);
	private final int REGION_SIZE_LIMIT = 1000;	
	
	static {
		List< HashSet<Integer>> biomeGroups = new ArrayList<>();		
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
			for( Integer i : biomeGroup) {
				similarBiomes.put( i ,biomeGroup);
			}
		}		
	}
	
	
	public RegionBiome( String name ,BlockPos startingPosition ,World world) {
		super( name);
		this.chunks = this.findRegionChunks( world ,startingPosition);
	}
	

	public RegionBiome( NBTTagCompound nbt) {
		super( nbt);
	}
	
	
	
	
    private HashSet<ChunkPos> findRegionChunks( World world ,BlockPos pos) {
    	HashSet<ChunkPos> bmap;// = new HashSet<>();
    	ChunkPos origin = new ChunkPos(pos);
    	Biome regionBiome = world.getBiome( pos);
    	
   		//findRegionChunks( bmap ,world ,origin ,Symbol.XP ,regionBiome ,regionBiome ,0);
    	bmap = findRegionChunks( origin ,regionBiome ,world);
    	
    	return bmap; 
    }
    
    
    private static class SearchNode {
    	public ChunkPos pos;
    	public Symbol facing;
    	public Biome parentBiome;
    	
    	SearchNode( ChunkPos pos ,Symbol facing ,Biome parentBiome){
    		this.pos = pos;
    		this.facing = facing;
    		this.parentBiome = parentBiome;
    	}
    }
    
    
    //breadth-first
    //Not elegant, but should allow O(1) lookup for duplicates for smaller regions.
    //For Minecraft, better to take up memory than to take up CPU time.
    private HashSet<ChunkPos> findRegionChunks( ChunkPos origin ,Biome originBiome ,World world) {
    	HashSet<ChunkPos> map = new HashSet<>();
    	HashSet<ChunkPos> consideredPos = new HashSet<>();
    	ArrayList<SearchNode> queue = new ArrayList<>();
		//Biome currentBiome = getAverageBiome( world ,origin);
    	queue.add( new SearchNode( origin ,Symbol.XP ,originBiome));
    	
		//ListIterator<SearchNode> queueIter = queue.listIterator();
    	
		int i = 0;
    	while(true){
			//System.out.println( "Current queue: " + queue);			
			if( i >= queue.size() || map.size() > REGION_SIZE_LIMIT) {
				System.out.println( "----------------> Final Iteration Count: " + i);
				System.out.println( "----------------> Final Map Size: " + map.size());
				return map;
			}
			
    		SearchNode node = queue.get(i);
    		Biome currentBiome = getAverageBiome( world ,node.pos);
    		
    		if( isSimilarBiome( originBiome ,currentBiome) || isPassableRiver( currentBiome ,node.parentBiome ,i)) {
    			map.add( node.pos);
    			//debug viewing here
//    	   		int mod = i / 100;
//    			for( int y=120; y<mod+121; y++) {
//    	   	   		world.setBlockState( new BlockPos( node.pos.getXStart()+7 ,y ,node.pos.getZStart()+7) ,Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState());
//    	   		}
   		
	        	ArrayList<Symbol> arr = Symbol.compliment2D( node.facing);
	        	arr.add( node.facing); //redundant, but is sigf in literal edge-cases (guidestone is on edge of region) 
	    		for( Symbol s : arr) {
	    			ChunkPos childPos = new ChunkPos( node.pos.x+s.getX() ,node.pos.z+s.getZ());
	    			if( !consideredPos.contains( childPos)) {
	    				queue.add( new SearchNode( childPos ,s ,currentBiome));
	    				consideredPos.add( childPos);
	    			}
	    		}
    		}
    		i++;
    	}
    }

    
    
//    //depth-first
//    private void findRegionChunks( HashSet<ChunkPos> bmap ,World world ,ChunkPos pos ,Symbol direction ,Biome originBiome ,Biome lastBiome ,int depth) {
//    	if( bmap.contains( pos) || depth > this.REGION_SIZE_LIMIT ) { return;}
//
//    	Biome currentBiome = getAverageBiome( world ,pos);//world.getBiome( pos);
//    	if( !isSimilarBiome( originBiome ,currentBiome) && !isPassableRiver( currentBiome ,lastBiome ,depth)) {
//    		return;
//    	}
//    	
//   		bmap.add( pos);
//   		
//   		int mod = depth / 5;
//   		for( int y=120; y<mod+121; y++) {
//   	   		world.setBlockState( new BlockPos( pos.getXStart()+7 ,y ,pos.getZStart()+7) ,Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState());
//   		}
//   		
//    	ArrayList<Symbol> arr = Symbol.compliment2D( direction);
//    	arr.add( direction);
//    	for( Symbol s : arr) {
//    		ChunkPos newpos = new ChunkPos( pos.x+s.getX() ,pos.z+s.getZ() );
//    		findRegionChunks( bmap ,world ,newpos ,s ,originBiome ,currentBiome ,depth++);
//    	}
//    	return;
//    }
    
    
    private Biome getAverageBiome( World world ,ChunkPos pos) {
    	Biome b =  world.getBiome( new BlockPos( pos.getXStart()+7 ,0 ,pos.getZStart()+7 ));
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
    	if( idNew != 7 && idNew != 11) { return false;} //not river
    	
    	if( (float)depth/REGION_SIZE_LIMIT < 0.65) { return false;}
    
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
