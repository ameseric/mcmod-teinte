package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.tileentity.ReplicatingTile;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;

public class AshReplicatingBlock extends Block implements TileLogicContainer{
	
	public AshReplicatingBlock() {
		super( Material.SAND);
		setUnlocalizedName( "dead_ash");
		setRegistryName( "ash_repl");
		setCreativeTab( TEinTE.teinteTab);
	}

	@Override
	public TileLogic getTileLogic(BlockPos pos) {
		return TEinTE.instance.getTileLogic(pos);
	}
	
	
	
//	@Override
//    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
//		
//		ReplicatingTile tile = (ReplicatingTile) this.getTileLogic( pos);
//		if(tile != null) {
//			tile.newBlockNeighbors();
//		}
//		
//	}
	
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerBlockEntity( new ReplicatingTile( pos)); //TODO consider comparing classes for TileLogic
	}
	
	
	

}

