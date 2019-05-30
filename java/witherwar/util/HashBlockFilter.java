package witherwar.util;

import java.util.ArrayList;
import java.util.HashSet;

import net.minecraft.block.Block;
import witherwar.util.SearchBlock.FilterBlock;

public class HashBlockFilter implements FilterBlock{
	private HashSet<Block> allowedBlocks;
	
	public HashBlockFilter() {};
	public HashBlockFilter( Block[] allowedBlocks) {
		for( Block b : allowedBlocks) {
			this.allowedBlocks.add( b);
		}
	}	
	public HashBlockFilter( HashSet<Block> allowedBlocks) {
		this.allowedBlocks = allowedBlocks;
	}
	public HashBlockFilter( ArrayList<Block> allowedBlocks) {
		this.allowedBlocks = new HashSet<Block>( allowedBlocks);
	}
	
	public void add( Block b) {
		this.allowedBlocks.add(b);
	}

	@Override
	public boolean allows(Block bs) {
		if( this.allowedBlocks == null) return false;
		return this.allowedBlocks.contains(bs);
	}		
}

