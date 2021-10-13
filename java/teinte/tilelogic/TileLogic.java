package teinte.tilelogic;


import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import teinte.disk.NBTSaveFormat;
import teinte.disk.NBTSaveObject;
import teinte.utility.Tickable;




/**
 * 
 * @author Guiltygate
 *
 * Custom version of minecraft's TileEntities.
 * Was formerly BlockEntity, but made class names difficult to (visually) parse due to similarity with Block class.
 * Under this, a "tile" is really just a position in the world, analogous to a BlockPos.
 * This naming convention is used in our MCForge API wrapper.
 * A BlockPos will correspond to a Tile, which will have a BlockState and possibly a TileEntity or a TileLogic.
 * This is non-ideal, but I have to work within the design pattern given. And Blocks are stateless, so...
 *
 */
public abstract class TileLogic extends NBTSaveObject implements Tickable{
	
	
	private Block homeBlock;
	private BlockPos pos;
	private boolean amIDead = false;	
	private boolean updatedOnTick = false;	
	protected int ticksUntilUpdate = 1;
	private int tickcountAtLastUpdate = 0;	
	protected static final Random RNG = new Random();
	
	
	
	public TileLogic() {}	//should be private, but then access difficulties with Reflection?	

	public TileLogic( BlockPos pos ,Block homeblock ,boolean active ,int ticksUntilUpdate){
		if( !TileLogicManager.hasClassRegistered( this)) {
			warning();  //TODO check performance at later date, given loop call to TLM
		}
		this.pos = pos;
		this.homeBlock = homeblock;
		this.updatedOnTick = active;
		this.ticksUntilUpdate = ticksUntilUpdate;
	}
	
	
	
	private void warning() { 
		System.out.println( "CLASS NOT REGISTERED WITH MANAGER");
	}
	
	
	
	@Override
	public void _tick( int tickcount ,WorldServer world) {
		
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
			_ticklogic( world);
		}
	}
	
	
	
	public abstract void _ticklogic( World world);
	
	
	
	private boolean homeBlockRemoved( World world) {
		return world.getBlockState( this.pos).getBlock() != this.homeBlock;
	}
	
	
	@Override
	public boolean isDead() {
		return this.amIDead;
	}	
	public BlockPos pos() {
		return this.pos;
	}
	public int posX() {
		return this.pos.getX();
	}
	public int absX() {
		return Math.abs( this.posX());
	}
	public int posY() {
		return this.pos.getY();
	}
	public int absY() {
		return Math.abs( this.posY());
	}
	public int posZ() {
		return this.pos.getZ();
	}
	public int absZ() {
		return Math.abs( this.posZ());
	}
	public Block getHomeBlock() {
		return this.homeBlock;
	}
	public String getID() {
		return this.getClass().getName();
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
	public boolean hasThisID( String id) {
		return getID().equals( id);
	}
	
	
	
	
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger( "x" ,this.pos.getX());
		nbt.setInteger( "y" ,this.pos.getY());
		nbt.setInteger( "z" ,this.pos.getZ());
		nbt.setString( "id" ,getID());
		NBTTagCompound childResult = __writeToNBT(nbt);
		this.markClean();
		return (childResult != null) ? childResult : nbt;
	}
	
	protected abstract NBTTagCompound __writeToNBT( NBTTagCompound nbt);
	
	
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		BlockPos pos = new BlockPos( nbt.getInteger( "x") ,nbt.getInteger( "y") ,nbt.getInteger( "z") );
		this.pos = pos;
		__readFromNBT( nbt);
	}
	
	protected abstract void __readFromNBT( NBTTagCompound nbt);
	
	


	
	
	
//	public static TileLogic createTileLogicFromID(int id ,BlockPos pos ,World world) {
//		switch(id) {
//			case SERPENTMIND_ID: return new KaliCoreTile( pos);
//			case RITUALBLOCK_ID: return new RitualBlockTile( pos ,world);
//			case CONDUIT_ID: return new ConduitTile( pos);
//			case GEYSER_ID: return new AlchemyGeyserTile( pos);
//			case REPLICATE_ID: return new ReplicatingTile( pos);
////			case MONUMENT_ID: return new MonumentCell( pos);
//		}
//		throw new UnsupportedOperationException();
//	}
//	
//	
//	public static TileLogic createTileLogicFromNBT( NBTTagCompound nbt ,World world) {
//		BlockPos pos = new BlockPos( nbt.getInteger( "x") ,nbt.getInteger( "y") ,nbt.getInteger( "z") );
//		TileLogic tl = TileLogic.createTileLogicFromID( nbt.getInteger( "id"), pos, world);
//		tl.readFromNBT( nbt);
//		return tl;
//	}
	

	
	
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
