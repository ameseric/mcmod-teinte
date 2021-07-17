package witherwar.tilelogic;

import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.hermetics.ElementalFluid;

public class AlchemyGeyserTile extends ElementalFluidContainerTile{
	
	
	static {
		TileLogicManager.registerClass( new AlchemyGeyserTile());
	}
	
	
	public AlchemyGeyserTile() {}

	public AlchemyGeyserTile(BlockPos pos) {
		super(pos ,ObjectCatalog.GEYSER ,false ,1);
		this.setContents( ElementalFluid.random() );
		System.out.println( this.peekAtContents().getElements());
		System.out.println( this.peekAtContents());
	}
	
	
	
	

	@Override
	public String getDataName() {
		return null;
	}

	@Override
	public void _ticklogic(World world) {
	}
	
	@Override
	public ElementalFluid _takeFluid( BlockPos requesterPos ,HashSet<BlockPos> traversed) {
		System.out.println( "Got to geyser...");
		traversed.add( getPos());
		return this.peekAtContents();
	}



	@Override
	protected NBTTagCompound __writeToNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void __readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
		
	}

}
