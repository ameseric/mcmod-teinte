package witherwar.tileentity;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.util.BlockTypeCollection;
import witherwar.util.BlockUtil;
import witherwar.util.HashBlockCollection;

public class ReplicatingTile extends TileLogic{

	
	private final BlockTypeCollection dontChange = new HashBlockCollection();
	private TileLogic parent;

	
	public ReplicatingTile(BlockPos pos) {
		super(pos, ObjectCatalog.ASH_REPL_BLOCK, TileLogic.REPLICATE_ID, true ,(int)(Math.random() * 500)+20);
		this.dontChange.add( Blocks.OBSIDIAN);
		this.dontChange.add( ObjectCatalog.TERRA_KALI);
		this.dontChange.add( Blocks.AIR);
		this.dontChange.add( ObjectCatalog.ASH_REPL_BLOCK);
	}
	
	
	

	@Override
	public String getDataName() {
		return null;
	}
	
	
	public void newBlockNeighbors() {
		this.setActive( true);
	}
	
	
	public void setParent( TileLogic parent) {
		this.parent = parent;
	}
	

	@Override
	public void ticklogic(World world) {

		if(this.parent != null && this.parent.isDead()) {
			this.iAmDead();
			return;
		}
		
		HashMap<BlockPos, Block> neighbors = BlockUtil.getNeighborBlocks( this.getPos() ,world ,true);
		
		boolean nochanges = true;
		for( BlockPos neighborPos : neighbors.keySet()) {
			Block b = world.getBlockState( neighborPos).getBlock();
			if( !this.dontChange.includes(b)) {
				world.setBlockState( neighborPos ,ObjectCatalog.ASH_REPL_BLOCK.getDefaultState());
				ReplicatingTile newChild = (ReplicatingTile) TEinTE.instance.getTileLogic( neighborPos);
				newChild.setParent( this.parent);
				nochanges = false;
				break;
			}
		}
		
		if( nochanges) {
//			System.out.println("Killing...");
			//this.setActive( false);
			this.iAmDead();
		}
		
		
	}
	
	
	
	

}
