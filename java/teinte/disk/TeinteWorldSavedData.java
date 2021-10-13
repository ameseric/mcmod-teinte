package teinte.disk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import teinte.TEinTE;
import teinte.region.RegionManager;
import teinte.system.SystemBlockDegrade;
import teinte.system.SystemPower;
import teinte.utility.Tickable;

/**
 * 
 * @author Guiltygate
 *
 *	Code based off of example at https://mcforge.readthedocs.io/en/latest/datastorage/worldsaveddata/
 *
 *	All TEinTE mod data that needs to be "manually" saved is done here, through an instance of this object.
 *  
 *  Includes RegionMap, BlockDegradation, Power, ...
 */




public class TeinteWorldSavedData extends WorldSavedData implements Tickable{
	
	private static final String DATA_NAME = TEinTE.MODID + "_SaveData";
	
	
	//private ArrayList<NBTSaveFormat> objectsForReadWrite = new ArrayList<>();
	private NBTSaveObject[] objectsForReadWrite;
	
	private NBTTagCompound nbtcopy;
	
	//private HashMap<String,NBTTagCompound> systemDataToSave;
	

	//DO NOT USE; Not actually for public use, but forge API needs to call it. 
	public TeinteWorldSavedData() {
		super(DATA_NAME);
	}

	public TeinteWorldSavedData( String s) {
		super(s);
	}
	

	

	
	
	
	
	@Override
	//called by getOrLoadData (triggered in getInstance) (might actually be earlier, during forge init?)
	public void readFromNBT(NBTTagCompound nbt) {
		this.nbtcopy = nbt.copy();
	}
	
	
	
	private void lazyReadFromNBT(){
		for( NBTSaveObject systemToSave : this.objectsForReadWrite){
			NBTTagCompound objectNBT = this.nbtcopy.getCompoundTag( systemToSave.getDataName());
			systemToSave.readFromNBT( objectNBT);
		}
	}
	
	
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		for( NBTSaveObject systemToSave : this.objectsForReadWrite){				
			NBTTagCompound objectNBT = this.nbtcopy.getCompoundTag( systemToSave.getDataName());
			if( systemToSave.isDirty()) {
				objectNBT = systemToSave.writeToNBT( objectNBT);
				this.nbtcopy.setTag( systemToSave.getDataName() ,objectNBT);
			}			
			//compound.setTag( systemToSave.getDataName() ,objectNBT);
		}
		
		this.setDirty( false);
		return this.nbtcopy;
	}
	
	
	
	
	public <T> boolean storeGenericValue( T a) {
		if( a instanceof Object) {
			return true;
		}
		
		
		return false;
		//TODO: Come back later.
		//For now, save everything manually and ignore Annotation.
	}
	
	
	

	//meant for Overworld dimension (0) storage only (for now) (but is using global MapStorage)
	public static TeinteWorldSavedData getInstance( World world ,NBTSaveObject[] objs) {
		MapStorage storage = world.getMapStorage();//getPerWorldStorage();
		TeinteWorldSavedData instance = (TeinteWorldSavedData) storage.getOrLoadData(TeinteWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new TeinteWorldSavedData();
			storage.setData(DATA_NAME, instance);
			instance.nbtcopy = new NBTTagCompound();
		}
		
		instance.objectsForReadWrite = objs;
		instance.lazyReadFromNBT();
		
		return instance;
	}

	
	@Override
	public boolean isDead() {
		return false;
	}

	
	@Override
	public void _tick(int tickcount, WorldServer world) {
		for( NBTSaveObject systemToSave : this.objectsForReadWrite){
			if( systemToSave.isDirty()) {
				markDirty();
			}
		}
	}

	
	
	
	
	

}








