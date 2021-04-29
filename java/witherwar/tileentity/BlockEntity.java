package witherwar.tileentity;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.disk.NBTSaveFormat;

public abstract class BlockEntity implements NBTSaveFormat{
	private Block homeBlock;
	private BlockPos pos;
	private boolean amIDead = false;
	
	private boolean dirty = false;
	
	private int id = -1;
	
	
	
	
	public BlockEntity( BlockPos pos ,Block homeblock ,int id){
		this.pos = pos;
		this.homeBlock = homeblock;
		this.id = id;
	}
	
	
	
	public void tick( int tickcount ,World world) {
		if( homeBlockRemoved( world)) {
			this.amIDead = true;
		}
		
		if( !this.amIDead) {
			ticklogic( world);
		}
	}
	
	
	
	public abstract void ticklogic( World world);
	
	
	
	private boolean homeBlockRemoved( World world) {
		return world.getBlockState( this.pos).getBlock() != this.homeBlock;
	}
	
	
	
	public boolean isDead() {
		return this.amIDead;
	}	
	public BlockPos getPos() {
		return this.pos;
	}	
	public Block getHomeBlock() {
		return this.homeBlock;
	}
	public int getID() {
		return this.id;
	}
	
	
	
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger( "x" ,this.pos.getX());
		compound.setInteger( "y" ,this.pos.getY());
		compound.setInteger( "z" ,this.pos.getZ());
		compound.setInteger( "id" ,this.id);
		this.dirty = false;
		return compound;
	}
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
	}
	public void markDirty() {
		this.dirty = true;
	}
//	public String getDataName() {
//		return java.util.UUID.randomUUID().toString(); //TODO: give data name based on hashCode for obj
//	}
	
	
	

	public static BlockEntity createBlockEntityFromID(int id ,BlockPos pos ,World world) {
		switch(id) {
			case 0: return new SerpentmindBlockEntity( pos);
			case 1: return new RitualBlockEntity( pos ,world);
		}
		throw new UnsupportedOperationException();
	}
	

	
	
//	public static int getID( BlockEntity be) {
//		return BlockEntity.classLookup.get( be.getClass());
//	}
//	
//	public static Class<?> getClass( int id){
//		for( Map.Entry< Class<?> ,Integer> entry : BlockEntity.classLookup.entrySet()) {
//			if( entry.getValue() == id) {
//				return entry.getKey();
//			}
//		}
//		return null;
//	}
	
	
}
