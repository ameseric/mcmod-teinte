package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import witherwar.TEinTE;
import witherwar.tileentity.TileEntitySerpentmind;

public class BlockSerpentmind extends Block{
	
	public BlockSerpentmind() {
		super( Material.ROCK);
	    setHardness(30F);
		setUnlocalizedName( "terra_kali");  // unlocalized name ties block to resources.lang file, I.e. "Serpentmind" or what have you
		setRegistryName( "terra_kali");		   //registry name ties block to the resources.blockstates json file. I.e. our blockstate json file is named after the registry name
		this.setHarvestLevel("pickaxe" ,2);
		setLightLevel(0.8F);
		//setCreativeTab( CreativeTabs.BUILDING_BLOCKS);
		setCreativeTab( TEinTE.teinteTab);
	}
	
	
	@SideOnly( Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}
	
	
	@Override
	public EnumBlockRenderType getRenderType( IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean hasTileEntity( IBlockState state) {
		return true;
	}
	
//	@Override
//	public TileEntity createTileEntity( World world ,IBlockState state) {
//		return new TileEntitySerpentmind();
//	}


}



/**
public BlockChunkLoader() {
    super(Material.ROCK);
    setHardness(20F);
    setDefaultState(getDefaultState().withProperty(TYPE, Type.BLOCK));
    setResistance(100F);
    setSoundType(SoundType.STONE);
    setUnlocalizedName(ChickenChunks.MOD_ID + ":chunk_loader");
    setCreativeTab(CreativeTabs.MISC);
}**/