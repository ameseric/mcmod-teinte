package witherwar.tileentity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.disk.NBTSaveFormat;
import witherwar.disk.NBTSaveObject;
import witherwar.disk.TeinteWorldSavedData;




public class BlockEntityManager extends NBTSaveObject{

	private HashMap<BlockPos ,BlockEntity> blockEntities;
	private World world; //TODO: Storing world locally for access during readNBT, will change if it's a problem
	
	
	public BlockEntityManager( TeinteWorldSavedData savedata ,World world) {
		super( savedata);
		this.blockEntities = new HashMap<>();
		this.world = world;
	}

	
	
	
	public String getDataName() {
		return "BlockEntitySaveData";
	}
	
	
	
	//calls all entities every tick, and trusts them to distribute their workload
	public void tick( int tickcount ,World world) {
		ArrayList<BlockPos> toRemove = new ArrayList<>();
		
		for( Map.Entry<BlockPos ,BlockEntity> be : this.blockEntities.entrySet()) {
			if( be.getValue().isDead()) {
				toRemove.add( be.getKey());				
			}else {
				be.getValue().tick( tickcount ,world);				
			}
		}
		
		for( BlockPos pos : toRemove) {
			this.remove( pos);
		}
	}
	
	
	
	
	
	public void add( BlockEntity be) {
		this.blockEntities.put( be.getPos() ,be);
		this.markDirty();
	}
	
	
	/**
	 * 
	 * @param pos
	 */
	public void remove( BlockPos pos) {
		this.blockEntities.remove( pos);
		this.markDirty();
	}
	
	
	
	public BlockEntity get( BlockPos pos) {
		return this.blockEntities.get( pos);
	}
	
	
	



	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		System.out.println( "Saving block entities...");
		compound = new NBTTagCompound(); //TODO?: if this becomes intensive, save local copy for comparison
		compound.setInteger( "numOfBEs" ,this.blockEntities.size());
		Integer i = 0;
		for( Map.Entry< BlockPos ,BlockEntity> be : this.blockEntities.entrySet()) {
			String generatedName = "blockentity" + i.toString(); 
			compound.setTag( generatedName ,be.getValue().writeToNBT( new NBTTagCompound()));
			i++;
		}
		this.setDirty( false);
		return compound;
	}



	@Override
	public void readFromNBT(NBTTagCompound compound) {
		int beCount = compound.getInteger( "numOfBEs");
		for(Integer i=0; i<beCount; i++) {
			System.out.println( "========== Reading NBT for BEs");
			NBTTagCompound beTag = compound.getCompoundTag( "blockentity" + i.toString());
			BlockPos pos = new BlockPos( beTag.getInteger( "x") ,beTag.getInteger( "y") ,beTag.getInteger( "z") );
			this.blockEntities.put( pos ,BlockEntity.createBlockEntityFromID( beTag.getInteger( "id") ,pos ,this.world));
			this.blockEntities.get(pos).readFromNBT( beTag);			
		}
		
	}


	
	
	
}
