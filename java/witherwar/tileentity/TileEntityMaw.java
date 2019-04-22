package witherwar.tileentity;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import scala.actors.threadpool.Arrays;
import witherwar.WitherWar;
import witherwar.util.Symbol;
import witherwar.util.WeightedChoice;

//Needs cleaning, too many flag variables.
public class TileEntityMaw extends TileEntityCustomTickingBase{
	
	private int ticks = 0;
	private ArrayList<BlockPos> branch;
	private Block skinBlock = Blocks.OBSIDIAN;
	private int hRadius = 8;
	private int vRadius = 8;
	private boolean reachedTarget = false;
	private BlockPos target = null;
	private int maxRange = 60;
	boolean retracting = false;
	
	Block deadSkin = WitherWar.newBlocks.get("dead_ash").block;
	
	
// NBT TAG SUPPORT
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		super.writeToNBT(compound);
		
		if( this.target != null) {
			compound.setLong( "target", this.target.toLong());
			compound.setInteger( "size" ,this.branch.size());
			for( int i=0; i<this.branch.size(); i++) {
				compound.setLong( "BPos"+i ,this.branch.get(i).toLong());
			}
			compound.setBoolean( "reached" ,reachedTarget);
		}
		
		return compound;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		Long target = compound.getLong("target"); 
		if( target != 0) {
			this.target = BlockPos.fromLong( target);
			this.reachedTarget = compound.getBoolean( "reached");
			
			Integer size = compound.getInteger( "size");
			this.branch = new ArrayList<BlockPos>();
			for( int i=0; i<size; i++) {
				Long bPos = compound.getLong( "BPos"+i);
				this.branch.add( BlockPos.fromLong( bPos));
			}			
		}		
	}	
	
	
	
	@Override
	public void update() {		
		if (!this.hasWorld() || world.isRemote) return; // no client-side work
		
		++ticks;
		if( ticks == 10) { //ticks modulo of five?
			ticks = 0;
			castParticle();
			
			if( this.target == null) {
				getTargetBlock();
			}else {
				buildSelf();
			}
		}
	}
	
	
	
	
	private void getTargetBlock() {
		Block[] set = new Block[] { Blocks.AIR ,Blocks.STONE ,Blocks.SAND ,Blocks.DIRT ,Blocks.WATER ,Blocks.ICE ,this.getBlockType()};
		Iterator<MutableBlockPos> itr = BlockPos.getAllInBoxMutable( this.pos.add( new BlockPos( hRadius ,0 ,hRadius)) ,this.pos.add( new BlockPos( -hRadius ,-vRadius ,-hRadius))).iterator();
		
		BlockPos pos = null;
		while( itr.hasNext()) {
			pos = itr.next();
			if( !isOfBlockSet( this.world.getBlockState(pos).getBlock() ,set) ) {
				this.target = pos;
				break;
			}
		}
		
		if( this.target != null) {
			Symbol[] path = getPath( this.pos ,pos);
			BlockPos currentPos = this.pos;
			this.branch = new ArrayList<BlockPos>();
			for( Symbol direction : path) {
				currentPos = currentPos.add( direction.mod);
				this.branch.add( currentPos);
			}
		}
	}
	
	
	
	private void buildSelf() {		
		
		int airBlock = -1;
		Block currentBlock;
		BlockPos currentPos = this.pos;
		BlockPos possibleFillPos = null;

		if( this.maxRange < this.branch.size() || reachedTarget) {
			retracting = true;
		}
		if( retracting) {
			retractBranch();
			return;
			//Symbol newDirection = Symbol.randomValues(1)[0];
			//BlockPos newPos = this.branch.get( this.branch.size()-1).add( newDirection.mod);
			//this.branch.add( newPos);
		}
		
		
		for( int i=0; i<this.branch.size(); i++) {
			currentPos = this.branch.get(i);
			currentBlock = this.world.getBlockState( currentPos).getBlock();
			
			if( currentBlock == this.skinBlock) {
				if( airBlock != -1) {
					destroyBranch( airBlock);
					return;
				}else if( currentPos.equals( this.target)) {
					reachedTarget = true;
				}
				
			}else {
				if( possibleFillPos == null) {
					possibleFillPos = currentPos;
				}
				if( currentBlock == Blocks.AIR) {
					airBlock = i;
				}
			}
		}
		
		if( possibleFillPos != null) {
			this.world.destroyBlock( possibleFillPos ,false);
			this.world.setBlockState( possibleFillPos ,this.skinBlock.getDefaultState());
		}
	}


	public void destroyBranch() {
		destroyBranch( 0);
	}
	
	public void destroyBranch( int index) {
		Block currentBlock;
		BlockPos currentPos;
		for( int i=index; i<this.branch.size(); i++) {
			currentPos = this.branch.get( i);
			currentBlock = this.world.getBlockState( currentPos).getBlock();
			if( currentBlock == this.skinBlock) {
				world.setBlockState( currentPos ,this.deadSkin.getDefaultState());
			}
		}
		this.retracting = true;
	}
	
	
	
	private void retractBranch() {
		Block currentBlock;
		BlockPos currentPos;
		for( int i=this.branch.size()-1; i >= 0; i--) {			
			currentPos = this.branch.get(i);
			currentBlock = this.world.getBlockState( currentPos).getBlock();
			if( currentBlock == this.skinBlock) {
				this.world.setBlockState( currentPos ,Blocks.AIR.getDefaultState());
				if( i == 0) {
					clearBranch();
				}
				break;
			}
		}
	}
	
	
	public void retract() {
		if( !branchless()){
			this.retracting = true;
		}
	}
	
	
	private void clearBranch() {
		this.target = null;
		reachedTarget = false;
		this.branch = null;
		retracting = false;
	}
	
	
	public boolean branchless() {
		return this.branch == null;
	}
	
}




