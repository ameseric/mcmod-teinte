package witherwar.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGuidestone extends TileEntity{

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	return compound;
    }
    
    
    
    public void readFromNBT(NBTTagCompound compound) {
    	super.readFromNBT(compound);
    }


	
	
}
