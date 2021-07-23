package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.MCForge;
import witherwar.TEinTE;
import witherwar.hermetics.MuirElement;
import witherwar.hermetics.Muir;
import witherwar.hermetics.ElementalFluidContainer;
import witherwar.tilelogic.MuirGasContainerTile;
import witherwar.tilelogic.MuirGeyserTile;
import witherwar.tilelogic.MuirGeyserTile;
import witherwar.tilelogic.RitualBlockTile;
import witherwar.tilelogic.TileLogic;




public class MuirGeyserBlock extends Block{

	
	public MuirGeyserBlock() {
		super(Material.ROCK);
		setUnlocalizedName("geyser");
		setRegistryName("geyser");
		setCreativeTab( TEinTE.teinteTab);
	}


	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerTileLogic( new MuirGeyserTile( pos));
	}
	
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	MuirGasContainerTile mc = (MuirGasContainerTile) MCForge.getTileLogic(pos);
    	System.out.println( mc.contents());
    	return true;
    }


}
