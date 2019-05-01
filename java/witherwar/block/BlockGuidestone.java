package witherwar.block;



import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.WitherWar;
import witherwar.tileentity.TileEntityGuidestone;
import witherwar.tileentity.TileEntityMaw;


public class BlockGuidestone extends Block{
	
	public BlockGuidestone() {
		super( Material.ROCK);
		setUnlocalizedName( "guidestone");
		setRegistryName( "guidestone");
		setCreativeTab( WitherWar.teinteTab);
	}

	@Override
	public boolean hasTileEntity( IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity( World world ,IBlockState state) {
		return new TileEntityGuidestone();
	}

	
	@Override
    public EnumBlockRenderType getRenderType( IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		TileEntityGuidestone teg = (TileEntityGuidestone) worldIn.getTileEntity( pos);
		teg.removeFromRegionMap();
		super.breakBlock( worldIn ,pos, state);
	}
	
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	if( !worldIn.isRemote) {
    		TileEntityGuidestone teg = (TileEntityGuidestone) worldIn.getTileEntity( pos);
    		teg.onBlockActivated(worldIn ,pos ,playerIn);
    	}
    	return true;
    }

}