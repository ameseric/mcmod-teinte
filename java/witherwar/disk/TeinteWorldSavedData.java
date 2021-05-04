package witherwar.disk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import witherwar.TEinTE;
import witherwar.region.RegionManager;
import witherwar.system.SystemBlockDegrade;
import witherwar.system.SystemPower;

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




public class TeinteWorldSavedData extends WorldSavedData {
	
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
	
	
	
	public void setObjectsToSave( NBTSaveObject[] a ) {
		this.objectsForReadWrite = a;
	}
	

	
	
	

	
	
	
	
	@Override
	//called by getOrLoadData
	//we defer the actual read until manually called in forceReadFromNBT()
	public void readFromNBT(NBTTagCompound nbt) {
		System.out.println( "=================" + nbt.toString() + "================");

		//this.regionMap.readFromNBT( nbt);
		
//		for( NBTSaveObject systemToSave : this.objectsForReadWrite){
//			NBTTagCompound objectNBT = nbt.getCompoundTag( systemToSave.getDataName());
//			systemToSave.readFromNBT( objectNBT);
//		}
		this.nbtcopy = nbt.copy();
	}
	
	
	public void forceReadFromNBT() {
		System.out.println( "Copy of nbt: " + this.nbtcopy);
		for( NBTSaveObject systemToSave : this.objectsForReadWrite){
			NBTTagCompound objectNBT = this.nbtcopy.getCompoundTag( systemToSave.getDataName());
			systemToSave.readFromNBT( objectNBT);
		}
	}
	
	
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		System.out.println( "writing to compound? =================" + compound.toString() + "================");
		
		for( NBTSaveObject systemToSave : this.objectsForReadWrite){			
			
			NBTTagCompound objectNBT = this.nbtcopy.getCompoundTag( systemToSave.getDataName());
			if( systemToSave.isDirty()) {
				objectNBT = systemToSave.writeToNBT( objectNBT);
				this.nbtcopy.setTag( systemToSave.getDataName() ,objectNBT);
			}			
			//compound.setTag( systemToSave.getDataName() ,objectNBT);
		}
		
		System.out.println( "=================" + this.nbtcopy.toString() + "================");
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
	public static TeinteWorldSavedData getInstance( World world) {
		MapStorage storage = world.getMapStorage();//getPerWorldStorage(); //TODO: Switch perWorld to Global
		TeinteWorldSavedData instance = (TeinteWorldSavedData) storage.getOrLoadData(TeinteWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new TeinteWorldSavedData();
			storage.setData(DATA_NAME, instance);
			instance.nbtcopy = new NBTTagCompound();
		}
		
		return instance;
	}
	
	
	
	
	

}








