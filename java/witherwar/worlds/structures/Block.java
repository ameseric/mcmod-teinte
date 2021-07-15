package witherwar.worlds.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;




public class Block {

	
	BlockSegment parent;
	IBlockState blockstate = Blocks.AIR.getDefaultState();
	BlockPos position;
	public boolean isWall = false;
	public boolean isFloor = false;
	public boolean isCeiling = false;
	public boolean isDoor = false;
	public boolean isStairwell = false; //blocks surrounding an up/down door
	
	
	public Block( BlockPos pos) {
		this.position = pos;
	}
	

	public boolean isSolid() {
		return this.blockstate != Blocks.AIR.getDefaultState();
	}


	public IBlockState getState() {
		return this.blockstate;
	}
	
	
	public void claim( BlockSegment bs) {
		this.parent = bs;
//		this.blockstate = Blocks.COBBLESTONE.getDefaultState();
	}
	
	public boolean isClaimed() {
		return this.parent != null;
	}
	
	public void setState( IBlockState bs) {
		this.blockstate = bs;
	}
	
	
	public boolean isSolidEdge() {
		return (this.isCeiling || this.isFloor || this.isWall) && !this.isDoor && !this.isStairwell;
	}
	
	
	public Block markAsDoor() {
		this.isDoor = true;
		return this;
	}
	
	
	public Block markAsStairwell() {
		this.isStairwell = true;
		return this;
	}
	

	
	
	public int x() {
		return this.position.getX();
	}
	public int y() {
		return this.position.getY();
	}
	public int z() {
		return this.position.getZ();
	}
	
	
}
