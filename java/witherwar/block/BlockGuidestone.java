package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import witherwar.WitherWar;


public class BlockGuidestone extends Block{
	
	public BlockGuidestone() {
		super( Material.ROCK);
		setUnlocalizedName( "guidestone");
		setRegistryName( "guidestone");
		setCreativeTab( WitherWar.wwCreativeTab);
	}

}



/**
class MaterialFlesh extends Material{

	public MaterialFlesh(MapColor color) {
		super(color);
		this.setRequiresTool();
	}
	
}**/