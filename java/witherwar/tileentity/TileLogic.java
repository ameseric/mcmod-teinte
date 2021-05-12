package witherwar.tileentity;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.disk.NBTSaveFormat;




/**
 * 
 * @author Guiltygate
 *
 * Custom version of minecraft's TileEntities.
 * Was formerly BlockEntity, but made class names difficult to parse due to similarity with Block class.
 * Under this, a "tile" is really just a position in the world, analogous to a BlockPos.
 * A BlockPos will correspond to a BlockState, a Block, and possibly a TileEntity or a TileLogic.
 * This is cumbersome, but I have to work with what's given. And Blocks are stateless, so...
 *
 */
public abstract class TileLogic implements NBTSaveFormat{
	
	public static final int SERPENTMIND_ID = 0;
	public static final int RITUALBLOCK_ID = 1;
	public static final int CONDUIT_ID = 2;
	public static final int GEYSER_ID = 3;
	public static final int REPLICATE_ID = 4;
	
	
	private Block homeBlock;
	private BlockPos pos;
	private boolean amIDead = false;
	
	private boolean dirty = false;
	
	private boolean updatedOnTick = false;
	
	protected int ticksUntilUpdate = 1;
	private int tickcountAtLastUpdate = 0;
	
	private int id = -1;
	
	
	
	
	public TileLogic( BlockPos pos ,Block homeblock ,int id ,boolean active ,int ticksUntilUpdate){
		this.pos = pos;
		this.homeBlock = homeblock;
		this.id = id;
		this.updatedOnTick = active;
		this.ticksUntilUpdate = ticksUntilUpdate;
	}
	
	
	
	public void tick( int tickcount ,World world) {
		
		if( homeBlockRemoved( world)) {
			this.amIDead = true;
		}
		
		if(this.tickcountAtLastUpdate == 0) {
			this.tickcountAtLastUpdate = tickcount;
		}
		
		int delta = tickcount - this.tickcountAtLastUpdate;
//		System.out.println( delta + " | " + this.ticksUntilUpdate + " | " + this.tickcountAtLastUpdate);

		if( !this.amIDead && this.updatedOnTick && delta >= this.ticksUntilUpdate ) {
			this.tickcountAtLastUpdate = tickcount;
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
	public double getX() {
		return this.pos.getX();
	}
	public double getY() {
		return this.pos.getY();
	}
	public double getZ() {
		return this.pos.getZ();
	}
	public Block getHomeBlock() {
		return this.homeBlock;
	}
	public int getID() {
		return this.id;
	}
	public boolean isActive() {
		return this.updatedOnTick;
	}
	protected void iAmDead() {
		this.amIDead = true;
	}
	protected void setActive( boolean isActive) {
		this.updatedOnTick = isActive;
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
	
	
	

	public static TileLogic createTileLogicFromID(int id ,BlockPos pos ,World world) {
		switch(id) {
			case SERPENTMIND_ID: return new KaliCoreTile( pos);
			case RITUALBLOCK_ID: return new RitualBlockTile( pos ,world);
			case CONDUIT_ID: return new ConduitTile( pos);
			case GEYSER_ID: return new AlchemyGeyserTile( pos);
			case REPLICATE_ID: return new ReplicatingTile( pos);
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
