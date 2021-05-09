package witherwar.util;

import net.minecraft.block.Block;

public interface BlockTypeCollection {

	public boolean includes( Block b);
	
	public void add( Block b);
	
}
