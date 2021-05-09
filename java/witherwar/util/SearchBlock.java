package witherwar.util;


/**
 * 
 * @author Guiltygate
 * 
 * General block searching class
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SearchBlock {
	
	private World world;
	private BlockTypeCollection returnBlockTypes;
	private BlockTypeCollection traversableBlockTypes;
	private final int MAX_DEPTH;


	
	
	public SearchBlock( World world ,BlockTypeCollection returnBlockTypes ,BlockTypeCollection traversableBlockTypes ,int searchDepth) {
		this.world = world;
		this.returnBlockTypes = returnBlockTypes;
		this.traversableBlockTypes = traversableBlockTypes;
		this.MAX_DEPTH = searchDepth;
	}
	
	
	
	public BlockPos search( BlockPos currentPos ,boolean random ) {
		
		ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
		positions.add( currentPos);
		
		Symbol[] values;
		if( random) {
			values = Symbol.randomValues();
		}else {
			values = Symbol.values();
		}
		
		return search( positions ,0 ,values);
	}	
	
	
	private BlockPos search( ArrayList<BlockPos> positions ,int currentDepth ,Symbol[] values) {
		BlockPos dead = new BlockPos(0,0,0);
		if( currentDepth >= this.MAX_DEPTH || positions.size() <= currentDepth) { 
			return dead;}
		
		HashMap<BlockPos,Block> map = getNeighborBlocks( positions.get( currentDepth) ,values);
		
		for( BlockPos pos : map.keySet()) {
			Block b = map.get(pos);
			if( this.traversableBlockTypes.includes(b)) {
				if( this.returnBlockTypes.includes( b)) {
					return pos;
				}else if( !positions.contains( pos)) { //inefficient, think about using HashSet?
					positions.add( pos);
				}
			}
		}
		
		return search( positions ,++currentDepth ,values);
	}
	
	
	
	public boolean onlyTouchingBlockTypes( BlockPos currentPos ,BlockTypeCollection acceptedBlocks) {
		HashMap<BlockPos,Block> map = getNeighborBlocks( currentPos);
		
		for( Block b : map.values()) {
			if( !acceptedBlocks.includes( b)) {
				return false;
			}
		}
		return true;
	}
	

	
	public HashMap<BlockPos,Block> getNeighborBlocks( BlockPos pos){
		return getNeighborBlocks( pos ,Symbol.values());
	}
	
	public HashMap<BlockPos,Block> getNeighborBlocks( BlockPos pos ,Symbol[] array){
		HashMap<BlockPos,Block> map = new HashMap<BlockPos,Block>();
		
		for( Symbol direction : array) {
			BlockPos currentPos = pos.add( direction.mod);
			map.put( currentPos ,this.world.getBlockState( currentPos).getBlock());
		}
		return map;
	}	
	

	
}
