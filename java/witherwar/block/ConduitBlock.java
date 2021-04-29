package witherwar.block;

import net.minecraft.block.material.Material;
import witherwar.TEinTE;

public class ConduitBlock extends FluidContainerBlock{

	public ConduitBlock() {
		super( Material.ROCK);
		setUnlocalizedName("conduit");
		setRegistryName("conduit");
		setCreativeTab( TEinTE.teinteTab);
	}

}
