package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.alchemy.Element;
import witherwar.alchemy.Fluid;

public class AlchemyGeyserBlockEntity extends FluidContainerBlockEntity{

	public AlchemyGeyserBlockEntity(BlockPos pos) {
		super(pos ,ObjectCatalog.GEYSER ,BlockEntity.GEYSER_ID ,false);
		this.setContents( new Fluid( new Element[] {Element.A}));
	}

	@Override
	public String getDataName() {
		return null;
	}

	@Override
	public void ticklogic(World world) {
	}
	
	@Override
	public Fluid pullFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		System.out.println("Calling Geyser...");
		return this.getContents();
	}

}
