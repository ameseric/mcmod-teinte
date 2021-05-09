package witherwar.tileentity;


import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.util.BlockTypeCollection;
import witherwar.util.BlockUtil;
import witherwar.util.HashBlockCollection;




public class KaliCoreTile extends TileLogic{

	private final IBlockState SKIN = Blocks.OBSIDIAN.getDefaultState();
	
	private final BlockTypeCollection dontChange = new HashBlockCollection();
	
	
	public KaliCoreTile(BlockPos pos) {
		super(pos ,ObjectCatalog.TERRA_KALI ,0 ,true ,10);
		this.dontChange.add( Blocks.OBSIDIAN);
		this.dontChange.add( Blocks.AIR);
		this.dontChange.add( ObjectCatalog.TERRA_KALI);
		this.dontChange.add( ObjectCatalog.ASH_REPL_BLOCK);
	}


	
	
	@Override
	public void ticklogic( World world) {
		HashMap<BlockPos,Block> neighbors = BlockUtil.getNeighborBlocks( this.getPos() ,world);
		
		for( BlockPos pos : neighbors.keySet() ) {
			Block neighbor = neighbors.get( pos); 
			if( !this.dontChange.includes( neighbor)) {
				world.setBlockState( pos ,ObjectCatalog.ASH_REPL_BLOCK.getDefaultState());
				ReplicatingTile child = (ReplicatingTile) TEinTE.instance.getTileLogic( pos);
				child.setParent( this);
			}
		}
		
	}



	public String getDataName() {
		return "Serpentmind";
	}






	
	
}
