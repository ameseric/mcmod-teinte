package witherwar.util;

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
	
//	public RegionManager regionMap;
//	public SystemBlockDegrade blockDegrade;
//	public SystemPower sysPower;
	
	private NBTSaveFormat[] objectsForReadWrite;
	
	//private HashMap<String,NBTTagCompound> systemDataToSave;
	
	private NBTTagCompound nbt;
	

	//DO NOT USE; Not actually for public use, but forge API needs to call it. 
	public TeinteWorldSavedData() {
		super(DATA_NAME);
		//this.initTeinteStructures();
	}

	public TeinteWorldSavedData( String s) {
		super(s);
		//this.initTeinteStructures();
	}
	
	
	private void initTeinteStructures() {
		//this.regionMap = new RegionManager( this);
		//this.blockDegrade = new SystemBlockDegrade();
		//this.sysPower = new SystemPower();
	}
	
	
	
	public void setDirty( boolean isDirty ,String field) {
        //TODO: 
		super.setDirty(isDirty);
    }
	
	
	  
	@Override
	public void readFromNBT(NBTTagCompound nbt) {	
		//this.regionMap.readFromNBT( nbt);
		for( NBTSaveFormat systemToSave : this.objectsForReadWrite){
			systemToSave.readFromNBT( nbt);
		}
	}
	
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		for( NBTSaveFormat systemToSave : this.objectsForReadWrite){
			compound = systemToSave.writeToNBT( compound);
		}
		return compound;
	}
	
	

	//meant for Overworld dimension (0) storage only (for now) (but is using global MapStorage)
	public static TeinteWorldSavedData getInstance( World world ,NBTSaveFormat[] saveObjects) {
		MapStorage storage = world.getMapStorage();//getPerWorldStorage(); //TODO: Switch perWorld to Global
		TeinteWorldSavedData instance = (TeinteWorldSavedData) storage.getOrLoadData(TeinteWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new TeinteWorldSavedData();
			storage.setData(DATA_NAME, instance);
		}
		
		instance.objectsForReadWrite = saveObjects;
		
		return instance;
	}
	
	
	
	
	

}








