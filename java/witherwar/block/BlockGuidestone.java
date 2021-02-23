package witherwar.block;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.TEinTE;


public class BlockGuidestone extends Block{
	
	public BlockGuidestone() {
		super( Material.ROCK);
		setUnlocalizedName( "guidestone");
		setRegistryName( "guidestone");
		setCreativeTab( TEinTE.teinteTab);
	}

	@Override
	public boolean hasTileEntity( IBlockState state) {
		return false;
	}
/**
	@Override
	public TileEntity createTileEntity( World world ,IBlockState state) {
		return new TileEntityGuidestone();
	}**/

	
	@Override
    public EnumBlockRenderType getRenderType( IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
	
	
	
	@Override
	public boolean isFullCube( IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube( IBlockState state) {
		return false;
	}

	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TEinTE.instance.removeRegion( pos);
		super.breakBlock( worldIn ,pos, state);
	}
	//import java.util.concurrent.ThreadLocalRandom;

	// nextInt is normally exclusive of the top value,
	// so add 1 to make it inclusive
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	if( !worldIn.isRemote) {
    		TEinTE.instance.guidestoneActivated( worldIn ,pos ,playerIn);
    	}
    	return true;
    } 
    
    
    

}