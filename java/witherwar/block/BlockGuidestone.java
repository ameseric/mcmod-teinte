package witherwar.block;



import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.WitherWar;
import witherwar.gui.GuiEditGuidestone;
import witherwar.tileentity.TileEntityGuidestone;


public class BlockGuidestone extends BlockContainer{
	
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityGuidestone();
	}
	
	
	@Override
    public EnumBlockRenderType getRenderType( IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
	

    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
        //if (!worldIn.isRemote){
        	//playerIn.openGui( WitherWar.instance ,0 ,worldIn ,pos.getX() ,pos.getY() ,pos.getZ());
        	//playerIn.openEditSign( new TileEntitySign());
        	
        //}  
    	TileEntityGuidestone teg = (TileEntityGuidestone) worldIn.getTileEntity( pos);
    	if( worldIn.isRemote) { 	//logical client
    		WitherWar.proxy.openGui( 0 ,teg);
    		//new GuiEditGuidestone( (TileEntityGuidestone) worldIn.getTileEntity( pos));
    		//new GuiEditGuidestone( new TileEntitySign());
    		//String regionName = new GuiEditGuidestone();
    		//GuidestoneEditMessage message = new GuidestoneEditMessage( regionName); 
    		//WitherWar.snwrapper.sendTo( ... ,message);
    		
    	}else { 					//logical server
    		if( !teg.hasRegionChunks()) {
    			teg.findRegionChunks();
    		}
    	}

    	return true;
    }

}