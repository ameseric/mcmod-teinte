package witherwar.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Sets;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import witherwar.util.Symbol;

/**
 * 
 * @author Guiltygate
 * 
 * A consecutive series of ChunkPos that share an identifier (name).
 * The name is displayed to the player when first traversing the region.
 * 
 * The Region contains its own ChunkPos HashSet because, while being already contained by the RegionMap HashMap, it allows for O(n) removal
 * from the HashMap.
 *
 */
public class Region {
	private HashSet<ChunkPos> chunks;	
	public String name;
	private HashSet<Integer> rejectedBiomes = Sets.newHashSet( 16 ,25 ,26);
	private HashMap<Integer ,HashSet<Integer>> similarBiomes = new HashMap<Integer ,HashSet<Integer>>();

	private final int MAX_SEARCH_DEPTH = 30;
	
	public Region( String name ,BlockPos startingPosition ,World world) {
		//this.chunks = chunks;
		this.name = name;		
		this.buildBiomeGroups();		
		this.chunks = this.findRegionChunks( world ,startingPosition);
	}
	
	public HashSet<ChunkPos> getChunks(){
		return this.chunks;
	}
	
	
	@Override
	public boolean equals( Object o) {
		if( !(o instanceof Region)) {
			return false;
		}
		Region r = (Region) o;
		
		return this.chunks == r.chunks;
	}
	
	@Override
	public int hashCode() {
		return chunks.hashCode();
	}
	
	
	private void buildBiomeGroups() {
		//messy, but allows us O(1) lookup, and we'll have 1k+ lookups in one tick.
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
				this.similarBiomes.put( i ,biomeGroup);
			}
		}		
	}
	
	
    private HashSet<ChunkPos> findRegionChunks( World world ,BlockPos pos) {
    	HashSet<ChunkPos> bmap = new HashSet<>();
    	ChunkPos origin = new ChunkPos(pos);
    	//map.add( origin);
    	Biome regionBiome = world.getBiome( pos);
    	
    	Symbol[] dirs = new Symbol[]{ Symbol.XP ,Symbol.ZP ,Symbol.ZN ,Symbol.XN};
    	//for( Symbol s : dirs) {
    		findRegionChunks( bmap ,world ,origin ,Symbol.XP ,regionBiome ,regionBiome ,0);
    		//bmap.remove( origin);
    	//}
    	
//    	return new ArrayList<>(bmap);
    	return bmap; 
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
