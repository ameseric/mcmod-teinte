package witherwar.util;

import java.util.HashMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import witherwar.WitherWar;
import witherwar.tileentity.TileEntityGuidestone;

/**
 * 
 * @author Guiltygate
 *
 *	Code based off of example at https://mcforge.readthedocs.io/en/latest/datastorage/worldsaveddata/
 *
 */




public class TeinteWorldSavedData extends WorldSavedData {
	private static final String DATA_NAME = WitherWar.MODID + "_SaveData";
	public HashMap<ChunkPos ,BlockPos> regionMap = new HashMap<>(); 
	

	public TeinteWorldSavedData() {
		super(DATA_NAME);
	}
	  
	public TeinteWorldSavedData(String s) {
		super(s);
	}
	  
	@Override
		public void readFromNBT(NBTTagCompound nbt) {
			// TODO Auto-generated method stub
		}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return null;
	}
	

	//meant for Overworld dimension (0) storage
	public static TeinteWorldSavedData get( World world) {
		// The IS_GLOBAL constant is there for clarity, and should be simplified into the right branch.
		MapStorage storage = world.getPerWorldStorage();
		TeinteWorldSavedData instance = (TeinteWorldSavedData) storage.getOrLoadData(TeinteWorldSavedData.class, DATA_NAME);

		if (instance == null) {
			instance = new TeinteWorldSavedData();
			storage.setData(DATA_NAME, instance);
		}
		return instance;
	}
	
	
	
	public void saveRegionMap( HashMap<ChunkPos ,TileEntityGuidestone> map) {
		
	}
	
	public HashMap<ChunkPos ,BlockPos> loadRegionMap() {
		return this.regionMap;
	}


}








