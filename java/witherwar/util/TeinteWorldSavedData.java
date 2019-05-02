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
		
			int numOfChunks = nbt.getInteger( "NumOfChunks");
			for( int i = 0; i<numOfChunks; i++) {
				int[] arr = nbt.getIntArray( "Chunk"+i);
				this.regionMap.put( new ChunkPos(arr[0] ,arr[1]) ,arr[2]);
			}
			
			int numOfRegions = nbt.getInteger( "NumOfRegions");
			for( int j=0; j<numOfRegions; j++) {
				this.regionNameMap.put( nbt.getInteger( "ID"+j) ,nbt.getString( "Region"+j));
			}

		}
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {

		int i = 0;
		for( ChunkPos cpos : this.regionMap.keySet()) {
			int[] arr = { cpos.x ,cpos.z ,this.regionMap.get( cpos)};
			compound.setIntArray( "Chunk"+i ,arr);
			i++;
		}
		compound.setInteger( "NumOfChunks" ,i);
		
		int j = 0;
		for( Integer id : this.regionNameMap.keySet()) {
			compound.setString( "Region"+j ,this.regionNameMap.get(id));
			compound.setInteger( "ID"+j ,id);
			j++;
		}
		compound.setInteger( "NumOfRegions" ,j);
		
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








