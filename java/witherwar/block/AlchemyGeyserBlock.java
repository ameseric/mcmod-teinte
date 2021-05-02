package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.TEinTE;
import witherwar.alchemy.Element;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;
import witherwar.tileentity.AlchemyGeyserTile;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.RitualBlockTile;




public class AlchemyGeyserBlock extends FluidContainerBlock{

	
	public AlchemyGeyserBlock() {
		super(Material.ROCK);
		setUnlocalizedName("geyser");
		setRegistryName("geyser");
		setCreativeTab( TEinTE.teinteTab);
	}


	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerBlockEntity( new AlchemyGeyserTile( pos));
	}


}
