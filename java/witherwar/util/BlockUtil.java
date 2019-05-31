package witherwar.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import witherwar.util.SearchBlock.FilterBlock;



/**
 * 
 * @author Guiltygate
 *
 *
 * Basic block utilities, but poorly structures (and implemented?).
 *
 * Probably going to be removed in the future.
 */
public class BlockUtil {	
	
	public static boolean onlyTouchingBlockTypes( BlockPos currentPos ,FilterBlock acceptedBlocks ,World world) {
		HashMap<BlockPos,Block> map = getNeighborBlocks( currentPos ,world);
		
		for( Block b : map.values()) {
			if( !acceptedBlocks.allows( b)) {
				return false;
			}
		}
		return true;
	}
	

	
	public static HashMap<BlockPos,Block> getNeighborBlocks( BlockPos pos ,World world){
		return getNeighborBlocks( pos ,Symbol.values() ,world);
	}
	
	public static HashMap<BlockPos,Block> getNeighborBlocks( BlockPos pos ,Symbol[] array ,World world){
		HashMap<BlockPos,Block> map = new HashMap<BlockPos,Block>();
		
		for( Symbol direction : array) {
			BlockPos currentPos = pos.add( direction.mod);
			map.put( currentPos ,world.getBlockState( currentPos).getBlock());
		}
		return map;
	}
	
	
	public static HashSet<ChunkPos> getNeighborChunks( BlockPos pos ,World world){
		ChunkPos cpos = new ChunkPos(pos);
		return getNeighborChunks( cpos ,Symbol.values() ,world);
	}
	
	public static HashSet<ChunkPos> getNeighborChunks( ChunkPos pos ,Symbol[] array ,World world){
		HashSet<ChunkPos> set = new HashSet<ChunkPos>();
		
		for( Symbol direction : array) {
			set.add( new ChunkPos( pos.x+direction.mod.getX() ,pos.z+direction.mod.getZ() ));
		}
		return set;
	}
	
	public static BlockPos chunkCenterPos( ChunkPos pos) {
		return new BlockPos( pos.getXStart()+7 ,0 ,pos.getZStart()+7);
	}
	
	
	protected Symbol[] getRandomizedPath( BlockPos a ,BlockPos b) {
		int x = b.getX() - a.getX();
		int y = b.getY() - a.getY();
		int z = b.getZ() - a.getZ();

		ArrayList<Symbol> path = new ArrayList<Symbol>();
		
		if( x > 0) {
			for( int i=0; i<x; i++) {
				path.add( Symbol.XP);
			}
		}else {
			for( int i=0; i>x; i--) {
				path.add( Symbol.XN);
			}			
		}
		
		if( y > 0) {
			for( int i=0; i<y; i++) {
				path.add( Symbol.YP);
			}
		}else {
			for( int i=0; i>y; i--) {
				path.add( Symbol.YN);
			}			
		}
		
		if( z > 0) {
			for( int i=0; i<z; i++) {
				path.add( Symbol.ZP);
			}
		}else {
			for( int i=0; i>z; i--) {
				path.add( Symbol.ZN);
			}			
		}

		Collections.shuffle( path);
		return  path.toArray( new Symbol[ path.size()]);
	}
}