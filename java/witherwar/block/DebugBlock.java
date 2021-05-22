package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;





public class DebugBlock extends Block{

	
	
	
	public DebugBlock() {
		super( Material.ROCK);
		setUnlocalizedName("debug_block");
		setRegistryName("debug_block");
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
//		TEinTE.instance.registerBlockEntity( new AlchemyGeyserTile( pos));
		System.out.println( "Placed debug block.");
	}

	
	
}
