package teinte.disk;


import net.minecraft.nbt.NBTTagCompound;

public interface NBTSaveFormat {
	
//	@Retention(RetentionPolicy.RUNTIME)
//	@interface AutoSaveValue{}

	
	
	public NBTTagCompound writeToNBT( NBTTagCompound compound);
	
	public void readFromNBT( NBTTagCompound compound) throws InstantiationException, IllegalAccessException;
	
	public String getDataName();

	
}
