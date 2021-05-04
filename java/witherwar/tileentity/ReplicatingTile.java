package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.util.BlockUtil;

public class ReplicatingTile extends TileLogic{

	
	private int lifetime = 2;
	
	public ReplicatingTile(BlockPos pos) {
		super(pos, ObjectCatalog.FLESH, TileLogic.REPLICATE_ID, true ,1);
	}
	
	
	

	@Override
	public String getDataName() {
		return null;
	}

	@Override
	public void ticklogic(World world) {
		if( this.lifetime == 0) {
			this.iAmDead();
		}
		
		HashMap<BlockPos, Block> neighbors = BlockUtil.getNeighborBlocks( this.getPos() ,world ,true);
		
		for( BlockPos neighborPos : neighbors.keySet()) {
			Block b = world.getBlockState( neighborPos).getBlock();
			if( b != Blocks.AIR && this.lifetime > 0) {
				world.setBlockState( neighborPos ,ObjectCatalog.FLESH.getDefaultState());
				this.lifetime--;
			}
		}
		
	}
	
	
	
	

}
