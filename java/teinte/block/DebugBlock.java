package teinte.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.TEinTE;
import teinte.tilelogic.GrowingTile;
import teinte.tilelogic.MonumentCell;
import teinte.tilelogic.TileLogic;
import teinte.tilelogic.TileLogicContainer;





public class DebugBlock extends Block implements TileLogicContainer{

	
	
	
	public DebugBlock() {
		super( Material.ROCK);
		setUnlocalizedName("debug_block");
		setRegistryName("debug_block");
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
//		TEinTE.instance.registerTileLogic( new MonumentCell( pos));
		System.out.println( "Placed debug block.");
	}
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	
    	return true;
    }


	
	
}
