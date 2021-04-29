package witherwar.block;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.alchemy.Element;
import witherwar.alchemy.Fluid;
import witherwar.alchemy.FluidContainer;




public class AlchemyGeyser extends Block implements FluidContainer{
	private Fluid contents = new Fluid( new Element[]{Element.A} );
	
	
	public AlchemyGeyser(Material materialIn) {
		super(materialIn);
	}

	
	@Override
	public Fluid pullFluid(FluidContainer requester, BlockPos pos, World world) {
		return this.contents;
	}

	
	@Override
	public boolean hasFluid() {
		return true;
	}

}
