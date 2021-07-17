package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.tilelogic.GrowingTile;
import witherwar.tilelogic.MonumentCell;
import witherwar.tilelogic.TileLogic;
import witherwar.tilelogic.TileLogicContainer;





public class DebugBlock extends Block implements TileLogicContainer{

	
	
	
	public DebugBlock() {
		super( Material.ROCK);
		setUnlocalizedName("debug_block");
		setRegistryName("debug_block");
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerTileLogic( new MonumentCell( pos));
		System.out.println( "Placed debug block.");
	}


	
	
}
