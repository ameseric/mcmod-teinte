package witherwar.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import witherwar.util.SearchBlock.FilterBlock;

/**
 * 
 * @author Guiltygate
 *
 * Class for O(n) comparison for groups of block types
 *
 */

public class HashBlockFilter implements FilterBlock{
	private HashSet<Block> blocks;
	
	public HashBlockFilter() {
		this.blocks = new HashSet<Block>();
	};
	
	
	public HashBlockFilter( Block[] blocks) {
		for( Block b : blocks) {
			this.blocks.add( b);
		}
	}	
	
	
	public HashBlockFilter( HashSet<Block> blocks) {
		this.blocks = blocks;
	}
	
	
	public HashBlockFilter( ArrayList<Block> blocks) {
		this.blocks = new HashSet<Block>( blocks);
	}
	
	
	public void add( Block b) {
		this.blocks.add(b);
	}
	

	@Override
	public boolean allows(Block bs) {
		if( this.blocks == null) return false;
		return this.blocks.contains(bs);
	}		
}

