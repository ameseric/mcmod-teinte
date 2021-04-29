package witherwar.disk;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTSaveFormat {
	
//	@Retention(RetentionPolicy.RUNTIME)
//	@interface AutoSaveValue{}

	
	
	public NBTTagCompound writeToNBT( NBTTagCompound compound);
	
	public void readFromNBT( NBTTagCompound compound);
	
	public void markDirty();
	
	public String getDataName();

	
}
