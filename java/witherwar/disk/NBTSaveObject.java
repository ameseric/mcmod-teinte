package witherwar.disk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public abstract class NBTSaveObject implements NBTSaveFormat{
	
	private boolean dirty;
	public WorldSavedData savedata;
	
	
	public NBTSaveObject( WorldSavedData savedata) {
		this.savedata = savedata;
	}
	
	public NBTSaveObject() {
		throw new UnsupportedOperationException();
	}
	
	
	
	public abstract NBTTagCompound writeToNBT( NBTTagCompound compound);
	
	public abstract void readFromNBT( NBTTagCompound compound);
	

	public abstract String getDataName();
	
	
    /**
     * Marks this MapDataBase dirty, to be saved to disk when the level next saves.
     */
    public void markDirty(){
    	System.out.println("Marking saveobj dirty,....");
        this.setDirty(true);
        this.savedata.markDirty();
    }

    /**
     * Sets the dirty state of this MapDataBase, whether it needs saving to disk.
     */
    public void setDirty(boolean isDirty){
        this.dirty = isDirty;
    }

    /**
     * Whether this MapDataBase needs saving to disk.
     */
    public boolean isDirty(){
        return this.dirty;
    }
	
}
