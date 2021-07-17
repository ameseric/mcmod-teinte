package witherwar.utility;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import witherwar.tilelogic.TileLogic;

public class Tile{
	
	private IBlockState blockstate;
	private TileLogic logic;
	
	
	public Tile( IBlockState blockstate ,@Nullable TileLogic logic) {
		this.blockstate = blockstate;
		this.logic = logic;
	}
	
	
	public TileLogic logic() {
		return this.logic;
	}
	
	
	public IBlockState blockstate() {
		return this.blockstate;
	}
	
	
	public Block block() {
		return this.blockstate().getBlock();
	}
	
	
	public boolean hasLogic() {
		return this.logic != null;
	}
	
	
}
