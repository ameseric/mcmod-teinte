package witherwar.util;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSaveFormat {

	
	public NBTTagCompound writeToNBT( NBTTagCompound compound);
	
	public void readFromNBT( NBTTagCompound compound);
	
	public void save();
	
}
