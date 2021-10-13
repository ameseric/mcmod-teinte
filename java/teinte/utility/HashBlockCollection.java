package teinte.utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

/**
 * 
 * @author Guiltygate
 *
 * Class for O(n) comparison for groups of block types
 *
 */

public class HashBlockCollection implements BlockTypeCollection{
	
	
	private HashSet<Block> blocks;
	
	public HashBlockCollection() {
		this.blocks = new HashSet<Block>();
	};
	
	
	public HashBlockCollection( Block b) {
		this.blocks = new HashSet<Block>();
		this.blocks.add( b);
	};
	
	
	public HashBlockCollection( Block[] blocks) {
		for( Block b : blocks) {
			this.blocks.add( b);
		}
	}	
	
	
	public HashBlockCollection( HashSet<Block> blocks) {
		this.blocks = blocks;
	}
	
	
	public HashBlockCollection( ArrayList<Block> blocks) {
		this.blocks = new HashSet<Block>( blocks);
	}
	
	
	public void add( Block b) {
		this.blocks.add(b);
	}	


	@Override
	public boolean includes(Block b) {
		if( this.blocks == null) return false;
		return this.blocks.contains(b);
	}		
}

