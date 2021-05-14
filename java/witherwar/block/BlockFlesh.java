package witherwar.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.TEinTE;
import witherwar.tileentity.AlchemyGeyserTile;
import witherwar.tileentity.ReplicatingTile;
import witherwar.tileentity.RitualBlockTile;
import witherwar.tileentity.TileLogic;
import witherwar.tileentity.TileLogicContainer;


public class BlockFlesh extends DirectionalBlock{
	
	public static final MaterialFlesh matFlesh = new MaterialFlesh( MapColor.TNT);
	
	public BlockFlesh() {
        super( matFlesh); //try making a block that can use Material.WATER ?
	    setHardness(8F);
		setUnlocalizedName( "flesh");
		setSoundType( SoundType.SLIME);
		setRegistryName( "flesh");
//        this.setDefaultState( this.blockState.getBaseState());
        this.setHarvestLevel("axe" ,1);
		//setCreativeTab( CreativeTabs.BUILDING_BLOCKS);
		setCreativeTab( TEinTE.teinteTab);
//"nether_wart_block", (new Block(Material.GRASS, MapColor.RED)).setCreativeTab(CreativeTabs.BUILDING_BLOCKS).setHardness(1.0F).setSoundType(SoundType.WOOD).setUnlocalizedName("netherWartBlock"));
	}
	
	
	@Override
	public EnumBlockRenderType getRenderType( IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}
	
	@SideOnly( Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}
	
    @Override
	public Vec3d modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3d motion)
    {
        return motion.add(new Vec3d(1,0,0));
    }
	
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    
    @Override
    public boolean isCollidable()
    {
        return false;
    }
	
	@Override
	public boolean hasTileEntity( IBlockState state) {
		return false;
	}
	
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
    {
        return true;
    }


    @Override
    @SideOnly (Side.CLIENT)
    public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor, float partialTicks)
    {
//        Vec3d viewport = net.minecraft.client.renderer.ActiveRenderInfo.projectViewFromEntity(entity, partialTicks);
//
//        if (state.getMaterial().isLiquid())
//        {
//            float height = 0.0F;
//            if (state.getBlock() instanceof BlockLiquid)
//            {
//                height = getLiquidHeightPercent(5.0) - 0.11111111F;
//            }
//            float f1 = (float) (pos.getY() + 1) - height;
//            if (viewport.y > (double)f1)
//            {
//                BlockPos upPos = pos.up();
//                IBlockState upState = world.getBlockState(upPos);
//                return upState.getBlock().getFogColor(world, upPos, upState, entity, originalColor, partialTicks);
//            }
//        }

        return new Vec3d(0.6F, 0.8F, 0.6F); //RGB
    }
	

	



}



//NOTE: Materials that block movement throw an overlay render when clipping.
// Materials that don't block movement don't through an overlay, unless water or lava?
//Must be hard-coded somewhere else...
class MaterialFlesh extends Material{

	public MaterialFlesh(MapColor color) {
		super(color);
		this.setRequiresTool();
	}
	
	
	@Override
    public boolean blocksMovement(){
        return false;
    }
	
}