package witherwar.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.tileentity.ConduitBlockEntity;
import witherwar.tileentity.RitualBlockEntity;

public class ConduitBlock extends FluidContainerBlock{

	public ConduitBlock() {
		super( Material.ROCK);
		setUnlocalizedName("conduit");
		setRegistryName("conduit");
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerBlockEntity( new ConduitBlockEntity( pos));
	}

}
