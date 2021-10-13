package teinte.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import teinte.MCForge;
import teinte.TEinTE;
import teinte.hermetics.ElementalFluidContainer;
import teinte.hermetics.Muir;
import teinte.tilelogic.MuirGasContainerTile;
import teinte.tilelogic.RitualBlockTile;
import teinte.tilelogic.TileLogic;
import teinte.tilelogic.TileLogicContainer;


public class RitualBlock extends DirectionalBlock{

	
	
	
	public RitualBlock() {
		super(Material.ROCK);
		setUnlocalizedName( "ritualblock");  // unlocalized name ties block to resources.lang file, I.e. "Serpentmind" or what have you
		setRegistryName( "ritualblock");		   //registry name ties block to the resources.blockstates json file. I.e. our blockstate json file is named after the registry name
		setCreativeTab( TEinTE.teinteTab);
		setResistance( 10.0f);
	}

	
	
	
	@Override
	public void onBlockAdded( World world ,BlockPos pos ,IBlockState state) {
		EnumFacing face = state.getValue( DirectionalBlock.FACING);
		TEinTE.instance.registerTileLogic( new RitualBlockTile( pos ,face));
	}
	
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	MuirGasContainerTile mc = (MuirGasContainerTile) MCForge.getTileLogic(pos);
    	System.out.println( mc.contents());
    	return true;
    }



}
