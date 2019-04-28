package witherwar.block;


import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import witherwar.WitherWar;


public class BlockAsh extends BlockFalling{
	
	public BlockAsh() {
		super( Material.SAND);
		setUnlocalizedName( "dead_ash");
		setRegistryName( "dead_ash");
		setCreativeTab( WitherWar.teinteTab);
	}

}



/**
class MaterialFlesh extends Material{

	public MaterialFlesh(MapColor color) {
		super(color);
		this.setRequiresTool();
	}
	
}**/