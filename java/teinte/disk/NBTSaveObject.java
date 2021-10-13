package teinte.disk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

//TODO refactor to WorldSave being ticked, children will lose access
public abstract class NBTSaveObject implements NBTSaveFormat{ 
	
	private boolean dirty;
	
	
	
	
	public abstract NBTTagCompound writeToNBT( NBTTagCompound compound);
	
	public abstract void readFromNBT( NBTTagCompound compound);
	

	public abstract String getDataName();
	
	
    /**
     * Marks this MapDataBase dirty, to be saved to disk when the level next saves.
     */
    protected void markDirty(){
        this.dirty = true;
    }
    
    protected void markClean() {
    	this.dirty = false;
    }


    /**
     * Whether this obj has data to save.
     */
    public boolean isDirty(){
        return this.dirty;
    }
	
}
