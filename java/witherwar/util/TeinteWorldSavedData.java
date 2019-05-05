package witherwar.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import witherwar.RegionMap;
import witherwar.WitherWar;

/**
 * 
 * @author Guiltygate
 *
 *	Code based off of example at https://mcforge.readthedocs.io/en/latest/datastorage/worldsaveddata/
 *
 */




public class TeinteWorldSavedData extends WorldSavedData {
	private static final String DATA_NAME = WitherWar.MODID + "_SaveData";
	public RegionMap regionMap;
	

	public TeinteWorldSavedData() {
		super(DATA_NAME);
		this.regionMap = new RegionMap( this);
	}
	  
	public TeinteWorldSavedData( String s) {
		super(s);
		this.regionMap = new RegionMap( this);
	}
	  
	@Override
	public void readFromNBT(NBTTagCompound nbt) {	
		this.regionMap.readFromNBT( nbt);	
	}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = this.regionMap.writeToNBT( compound);
		return compound;
	}
	

	//meant for Overworld dimension (0) storage
	public static TeinteWorldSavedData get( World world) {
		MapStorage storage = world.getPerWorldStorage();
		TeinteWorldSavedData instance = (TeinteWorldSavedData) storage.getOrLoadData(TeinteWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new TeinteWorldSavedData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
	
	
	
	
	

}








