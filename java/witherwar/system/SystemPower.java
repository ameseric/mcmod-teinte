package witherwar.system;

import java.util.HashMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import witherwar.util.NBTSaveFormat;

public class SystemPower implements NBTSaveFormat{

	private HashMap<ItemStack,Integer> weaponPowers;
	private HashMap<EntityLiving,Integer> entityPowers;
	private HashMap<EntityPlayer,Integer> playerPowers;
	
	
	
	
	//TODO: Need method for sussing out which dimensions have Power automatically (i.e. dungeons)	
	
	
	
	
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
	public void save() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
