package teinte.system;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import teinte.disk.NBTSaveFormat;
import teinte.disk.TeinteWorldSavedData;

public class SystemBlockDegrade implements NBTSaveFormat{

	static final int FAST = 2;
	static final int NEUTRAL = 1;
	static final int SLOW = 0;
	
	
	
	
	
	public void tick( TickEvent.WorldTickEvent event ,int tickcount) {
		
		//check if blocks degrade
		
	}


	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}
	
	


	@Override
	public String getDataName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
