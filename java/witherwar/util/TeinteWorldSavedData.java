package witherwar.util;

import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayer;
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
	public HashMap<ChunkPos ,Integer> regionMap;
	public HashMap<Integer ,String> regionNameMap;
	public HashMap<EntityPlayer ,String> playerRegionMap;
	

	public TeinteWorldSavedData() {
		super(DATA_NAME);
		this.regionMap = new HashMap<>();
		this.regionNameMap = new HashMap<>();
		this.playerRegionMap = new HashMap<>();
	}
	  
	public TeinteWorldSavedData( String s) {
		super(s);
		this.regionMap = new HashMap<>();
		this.regionNameMap = new HashMap<>();
		this.playerRegionMap = new HashMap<>();
	}
	  
	@Override
		public void readFromNBT(NBTTagCompound nbt) {
			// TODO Auto-generated method stub
			this.regionMap = new HashMap<>();
			this.regionNameMap = new HashMap<>();
			this.playerRegionMap = new HashMap<>();
		}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return null;
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








