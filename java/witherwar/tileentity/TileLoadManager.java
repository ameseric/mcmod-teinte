package witherwar.tileentity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.disk.NBTSaveFormat;
import witherwar.disk.NBTSaveObject;
import witherwar.disk.TeinteWorldSavedData;




public class TileLoadManager extends NBTSaveObject{

	private HashMap<BlockPos ,TileLogic> tiles;
	private HashMap<BlockPos ,TileLogic> tileclone;
	//private ArrayList<HashMap<BlockPos,TileLogic>> tilebuckets;
	private World world; //TODO: Storing world locally for access during readNBT, will change if it's a problem
	
	private final double LIMIT = 20.0;
	
	private int tilesPerTick = -1;
	private int tickNumber = 1;
	private int ticksPerCycle = 1;
	private boolean performReset = false;
	
	
	
	public TileLoadManager( TeinteWorldSavedData savedata ,World world) {
		super( savedata);
//		this.tilebuckets = new ArrayList<>();
//		this.tilebuckets.add( new HashMap<BlockPos ,TileLogic>());
		this.tiles = new HashMap<>();
		this.tileclone = (HashMap<BlockPos, TileLogic>) this.tiles.clone();
		this.world = world;
	}

	
	
	
	public String getDataName() {
		return "TileLogicSaveData";
	}
	
	
	
//	private void balanceLoad() {
//		if( this.CHANGES_TIL_BALANCE > this.changesSinceLastBalance) { return;}
//		
//		HashMap<BlockPos,TileLogic> tiles = this.tilebuckets.get(0);
//		int size = tiles.size();
//		if( size > this.LIMIT) {
//			int amountToRemove = size - (LIMIT - LIMIT/2);
//			
//			int index = 0;
//			while( amountToRemove > 0) {
//				if( tiles.size() > LIMIT) {
//					index++;
//					if( index == this.tilebuckets.size()) {
//						this.tilebuckets.add( new HashMap<BlockPos,TileLogic>());
//					}
//					
//					tiles = this.tilebuckets.get( index); 
//				}
//				
//				
//							
//				amountToRemove--;
//			}
//			
//		}		
//		
//		
//		this.changesSinceLastBalance = 0;
//	}
	
	
	private void resetCycle() {
		double callsPerCycle = Math.ceil(this.tiles.size() / LIMIT);
		this.tilesPerTick = (int) Math.floor( this.tiles.size() / callsPerCycle);
		this.tickNumber = 1;
		this.ticksPerCycle = (int) callsPerCycle;
		this.tileclone = (HashMap<BlockPos, TileLogic>) this.tiles.clone();
		this.performReset = false;
		
//		System.out.println( "Ticks per Cycle: " + callsPerCycle);
//		System.out.println( "Tiles per Tick: " + this.tilesPerTick);
//		System.out.println( "" + 0);
	}
	


	
	
	//calls all entities every tick, and trusts them to distribute their workload
	public void tick( int tickcount ,World world) {
		
		if( this.performReset) {
			this.resetCycle();
			return;
		}
		
		ArrayList<BlockPos> toRemove = new ArrayList<>();
		
		//We iterate over a shallow copy to allow insertion of new Tiles to the original
		//during TileLogic ticking.
		//HashMap<BlockPos ,TileLogic> clone = (HashMap<BlockPos, TileLogic>) this.tiles.clone();
		int tilesThisTick = 0;	
		
		
		//Set<Map.Entry<BlockPos ,TileLogic>> tilecloneSet = this.tileclone.entrySet();
		Iterator< Map.Entry<BlockPos ,TileLogic>> iter;
		
		for( iter = this.tileclone.entrySet().iterator(); iter.hasNext();) { //TODO this should support deletion, try it			
			Map.Entry< BlockPos ,TileLogic> tl = iter.next();
			if( !(this.tickNumber == this.ticksPerCycle)) {
				if( tilesThisTick == this.tilesPerTick) {
					break;
				}
			}
			
			if( tl.getValue().isDead()) {
				toRemove.add( tl.getKey()); //TODO might be able to phase this out / removal from original / main
			}else {
				tl.getValue().tick( tickcount ,world);
			}
			

			iter.remove();
//			tilecloneSet.remove( tl);
//			this.tileclone.remove( tl.getKey()); //removal from clone
			tilesThisTick++;
		}
		
		
		for( BlockPos pos : toRemove) {
			this.remove( pos);
		}
		
		
		this.tickNumber++;
		if( this.tickNumber > this.ticksPerCycle) {
			this.performReset = true;
		}
		
	}
	
	
	
	
	
	public void add( TileLogic be) {
		this.tiles.put( be.getPos() ,be);
		this.markDirty();
	}
	
	
	/**
	 * 
	 * @param pos
	 */
	public void remove( BlockPos pos) {
		
		this.tiles.remove( pos);
		this.markDirty();
	}
	
	
	
	
	
	public TileLogic get( BlockPos pos) {
		return this.tiles.get( pos);
	}
	
	
	



	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = new NBTTagCompound(); //TODO?: if this becomes intensive, save local copy for comparison
		
		
		Integer i = 1;
		for( Map.Entry< BlockPos ,TileLogic> tl : this.tiles.entrySet()) {
			String generatedName = "tilelogic" + i.toString(); 
			compound.setTag( generatedName ,tl.getValue().writeToNBT( new NBTTagCompound()));
			i++;
		}
		
		compound.setInteger( "numOfTLs" ,i);
		this.setDirty( false);
		return compound;
	}



	@Override
	public void readFromNBT(NBTTagCompound compound) {
		int tlCount = compound.getInteger( "numOfTLs");
		
//		HashMap<BlockPos,TileLogic> tiles = this.tilebuckets.get(0);
		
		for(Integer i=0; i<tlCount; i++) {
			NBTTagCompound beTag = compound.getCompoundTag( "tilelogic" + i.toString());
			BlockPos pos = new BlockPos( beTag.getInteger( "x") ,beTag.getInteger( "y") ,beTag.getInteger( "z") );
			this.tiles.put( pos ,TileLogic.createTileLogicFromID( beTag.getInteger( "id") ,pos ,this.world));
			this.tiles.get(pos).readFromNBT( beTag);			
		}
		
	}


	
	
	
}
