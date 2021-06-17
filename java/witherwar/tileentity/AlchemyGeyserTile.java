package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.alchemy.Element;
import witherwar.alchemy.Fluid;

public class AlchemyGeyserTile extends FluidContainerTile{

	public AlchemyGeyserTile(BlockPos pos) {
		super(pos ,ObjectCatalog.GEYSER ,TileLogic.GEYSER_ID ,false ,1);
		this.setContents( Fluid.random() );
		System.out.println( this.getContents());
		System.out.println( );
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
		return this.getContents();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

}
