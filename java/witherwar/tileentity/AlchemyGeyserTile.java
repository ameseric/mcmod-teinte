package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.hermetics.Element;
import witherwar.hermetics.ElementalFluid;

public class AlchemyGeyserTile extends ElementalFluidContainerTile{

	public AlchemyGeyserTile(BlockPos pos) {
		super(pos ,ObjectCatalog.GEYSER ,TileLogic.GEYSER_ID ,false ,1);
		this.setContents( ElementalFluid.random() );
		System.out.println( this.peekAtContents());
		System.out.println( );
	}

	@Override
	public String getDataName() {
		return null;
	}

	@Override
	public void _ticklogic(World world) {
	}
	
	@Override
	public ElementalFluid _takeFluid( BlockPos requesterPos ,BlockPos myPos ,World world) {
		return this.peekAtContents();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}

}
