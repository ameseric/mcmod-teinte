package teinte.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.MCForge;
import teinte.TEinTE;
import teinte.hermetics.Muir;
import teinte.hermetics.MuirContainer;
import teinte.hermetics.MuirGasContainer;
import teinte.tilelogic.ConduitFluidTile;
import teinte.tilelogic.ConduitGasTile;
import teinte.tilelogic.ConduitPlasmaTile;
import teinte.tilelogic.MuirGasContainerTile;
import teinte.tilelogic.RitualBlockTile;

public class ConduitBlock extends Block{

	public ConduitBlock() {
		super( Material.ROCK);
		setUnlocalizedName("conduit");
		setRegistryName("conduit");
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		TEinTE.instance.registerTileLogic( new ConduitGasTile( pos));
	}
	
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	MuirGasContainerTile mc = (MuirGasContainerTile) MCForge.getTileLogic(pos);
    	System.out.println( mc.contents());
    	return true;
    }



}
