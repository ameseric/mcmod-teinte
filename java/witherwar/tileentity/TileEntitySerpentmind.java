package witherwar.tileentity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.system.SystemLindenmayer;
import witherwar.util.HashBlockFilter;
import witherwar.util.SearchBlock;
import witherwar.util.SearchBlock.FilterBlock;
import witherwar.util.Symbol;
import witherwar.util.WeightedChoice;



public class TileEntitySerpentmind extends TileEntityCustomTickingBase{
	
	private int ticks = 0;
	private SystemLindenmayer pattern;
	private Symbol[][] branches;  //convert to String[][] and back for writing/reading
	private int[] lastBlockPosition;
	private int numOfBranches = 0;
	private int fullCycleCount = 0;
	private HashBlockFilter nativeBlocks;
	private SearchBlock seeker;
	private Block terraformBlock;
	private int blocksPlaced = 0;
	private boolean isFullCycle() {
		return this.fullCycleCount >= 10;
	}
	
	
	private enum Layer{
		 SKIN( Blocks.OBSIDIAN)
		//,FLESH( TEinTE.blocks.get("flesh").block)
		,FLESH( ObjectCatalog.FLESH)
		,BONE( Blocks.BONE_BLOCK);
		
		private IBlockState bs;
		private Block block;
		
		private Layer( Block block) {
			this.block = block;
			this.bs = block.getDefaultState();
		}
		
		public void setBlockStateType( Block block) {
			this.bs = block.getDefaultState();
		}
	}
	

	
// MOB SPAWNER SUPPORT
	private CustomMobSpawner spawnerLogic;// = new CustomMobSpawner( "witherwar:serpent_wither_skeleton" ,this ,WitherWar.newBlocks.get("terra_kali").block);
//	private final CustomMobSpawner spawnerLogicB; = new CustomMobSpawner( "witherwar:motus_ghast" ,this ,WitherWar.newBlocks.get("terra_kali").block);
	
	
//    private final CustomMobSpawner spawnerLogic = new CustomMobSpawner( "witherwar:serpent_wither_skeleton")
//   {
//        public void broadcastEvent(int id) {
//        	TileEntitySerpentmind.this.world.addBlockEvent( TileEntitySerpentmind.this.pos 
//        			,WitherWar.newBlocks.get("terra_kali").block ,id ,0);
//        }
//        public World getSpawnerWorld(){			return TileEntitySerpentmind.this.world; }
//        public BlockPos getSpawnerPosition(){	return TileEntitySerpentmind.this.pos;   }
//        public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_) {
//            super.setNextSpawnData(p_184993_1_);
//
//            if (this.getSpawnerWorld() != null) 
//            {
//                IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
//                this.getSpawnerWorld().notifyBlockUpdate(TileEntitySerpentmind.this.pos, iblockstate, iblockstate, 4);
//            }
//        }
//    };
    
    
    
//    private final CustomMobSpawner spawnerLogicB = new CustomMobSpawner( "witherwar:motus_ghast" ,this ,WitherWar.newBlocks.get("terra_kali").block)
//   {
//        public void broadcastEvent(int id)
//        {
//        	TileEntitySerpentmind.this.world.addBlockEvent( TileEntitySerpentmind.this.pos 
//        			,WitherWar.newBlocks.get("terra_kali").block ,id ,0);
//        }
//        public World getSpawnerWorld()
//        {
//            return TileEntitySerpentmind.this.world;
//        }
//        public BlockPos getSpawnerPosition() 
//        {
//            return TileEntitySerpentmind.this.pos;
//        }
//        public void setNextSpawnData(WeightedSpawnerEntity p_184993_1_)   
//        {
//            super.setNextSpawnData(p_184993_1_);
//
//            if (this.getSpawnerWorld() != null) 
//            {
//                IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
//                this.getSpawnerWorld().notifyBlockUpdate(TileEntitySerpentmind.this.pos, iblockstate, iblockstate, 4);
//            }
//        }
//    };
  
	

    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound nbttagcompound = this.writeToNBT(new NBTTagCompound());
        nbttagcompound.removeTag("SpawnPotentials");
        return nbttagcompound;
    }
    
    public boolean receiveClientEvent(int id, int type)
    {
    	if( this.spawnerLogic == null) { return false;}
        if( this.spawnerLogic.setDelayToMin(id)){// || this.spawnerLogicB.setDelayToMin(id)) {
        	return true;
        }
        return false;
    }
    
    
	
	
	
// NBT TAG SUPPORT
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		
		compound = super.writeToNBT(compound);
		if( this.spawnerLogic != null){
			this.spawnerLogic.writeToNBT( compound);
		}
//		this.spawnerLogicB.writeToNBT( compound);

		if( this.branches != null) {
			for( int i=0; i<branches.length; i++) {
				int[] newChain = new int[ branches[i].length];
				for( int j=0; j<branches[i].length; j++) {
					newChain[j] = branches[i][j].toInt();
				}
				compound.setIntArray( "Branch"+i, newChain);
			}
		
			compound.setIntArray( "lastBlockPosition" ,this.lastBlockPosition);
			compound.setInteger( "numOfBranches", numOfBranches);
		}
		
		return compound;
	}
	
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		
		if( this.spawnerLogic != null) {
			this.spawnerLogic.readFromNBT( compound);
		}
//		this.spawnerLogicB.readFromNBT( compound);
		
		this.numOfBranches = compound.getInteger( "numOfBranches");		
		this.lastBlockPosition = compound.getIntArray( "lastBlockPosition");
		
		this.branches = new Symbol[ numOfBranches][];
		
		for( int i=0; i<numOfBranches; i++) {
			int[] intBranch = compound.getIntArray( "Branch"+i);
			Symbol[] symBranch = new Symbol[intBranch.length];
			
			for( int j=0; j < intBranch.length; j++) {
				symBranch[j] = Symbol.intToSymbol( intBranch[j]);
			}
			this.branches[i] = symBranch;
		}
		
	}
	
	
	
	
	
	
// Core Serpentmind code
	@Override
	public void update() {
//		this.spawnerLogic.updateSpawner();
//		this.spawnerLogicB.updateSpawner();
		//super.update(); //for chunkloading
		
		if( this.hasWorld()) {
			if ( !this.world.isRemote) {
			
				if( this.numOfBranches == 0) {
					setup();
					this.markDirty();
				}
				
				++ticks;
				if( ticks == (20)) {
					ticks = 0;
					//BlockPos.getAllInBoxMutable(from, to) use for mouth?
					//buildSelf();
					terraform();
					
				}
			}
			
			if( this.spawnerLogic == null) {
				this.spawnerLogic = new CustomMobSpawner( "witherwar:serpent_wither_skeleton" ,this.world ,this.pos ,ObjectCatalog.TERRA_KALI);
			}else {
				this.spawnerLogic.updateSpawner();
			}
		}


	}
	

	private void setup() {
		
		//this.spawnerLogic.setup( this.world ,this.pos ,WitherWar.newBlocks.get("terra_kali").block);
		
		//numOfBranches = 10; //tie to family later, but also save in TE reference for NBTTag
		//int recursionDepth = 3; //tie to family later
		
		
		//branches = new Symbol[ numOfBranches][];
		//lastBlockPosition = new int[ numOfBranches];
		//pattern = new LSystem( LSystem.Rulesets.WEEPING.ruleset ,recursionDepth);
		
		
		//Layer.SKIN.setBlockStateType( Blocks.OBSIDIAN);
		//Layer.FLESH.setBlockStateType( WitherWar.newBlocks.get("flesh").block);
		//Layer.BONE.setBlockStateType( Blocks.BONE_BLOCK);
		
		Block[] list = {
				 Blocks.OBSIDIAN
				,Blocks.BONE_BLOCK
				,ObjectCatalog.FLESH
				,this.getBlockType()};
		this.nativeBlocks = new HashBlockFilter( list);

		
		this.terraformBlock = ObjectCatalog.DEAD_ASH;
		
		FilterBlock filterReturnBlock = ( b) -> {
			//return !isOfBlockSet( b ,this.nativeBlocks) && b != this.terraformBlock && b != Blocks.BEDROCK;
			return b != this.terraformBlock;
		};
		
		FilterBlock filterTraversableBlock = ( b) -> {
			return b != Blocks.AIR && this.nativeBlocks.allows(b) && b != Blocks.BEDROCK;
		};
		
		this.seeker = new SearchBlock( this.world ,filterReturnBlock ,filterTraversableBlock ,1000);
		
		//for( int i=0; i<numOfBranches; i++) {
//			branches[ i] = this.pattern.grow();
		//}
		
	}
	
	
	private void terraform() {
		

		
//TODO
		//BlockPos pos = searchForTouchingBlock( this.pos ,filterReturnBlock ,filterTraversableBlock ,10000 ,true);
		BlockPos pos = this.seeker.search( this.pos ,true);
		if( !pos.equals( new BlockPos(0,0,0))) {
			++blocksPlaced;
			System.out.println( "Blocks placed: " + blocksPlaced);
			this.world.setBlockState( pos ,this.terraformBlock.getDefaultState());
		}
	}

	
/**	
	private void buildSelf() {
		++fullCycleCount;
		
		for( Layer layer : Layer.values()) {
			
			if( !isFullCycle() && layer != Layer.SKIN) {
				continue;
			}
			
			for( int i=0; i<this.branches.length; i++) {
				
				BlockPos origin =  this.pos;
				BlockPos currentPos = origin;
				IBlockState blockstate;

				int lengthOfBranch = branches[i].length;
				for( int j=0; j<lengthOfBranch; j++) {

					Symbol currentSym = branches[i][j];
					currentPos = currentPos.add( currentSym.mod);
					blockstate = world.getBlockState( currentPos);
					
					if( isFullCycle()) {
						if( stopBlockSearch( layer ,blockstate)) {
							break;
						}
						if(	validBlockPosition( layer ,blockstate ,currentPos) ) {
							if( tryPlacingBlockPiece( currentPos ,layer ,lengthOfBranch ,j)) {
								break;
							}
						}

					}else if( j == lastBlockPosition[i]){
						if( tryPlacingBlockPiece( currentPos ,layer ,lengthOfBranch ,j)) {
							++lastBlockPosition[i];
							break;
						}
					}				
				}				
			}			
		}		
		
		if( isFullCycle()) { fullCycleCount = 0;}
	}
	
	
	private boolean tryPlacingBlockPiece( BlockPos currentPos ,Layer layer ,int lengthOfBranch ,int index) {
		double maxThickness = 3.0;
		double branchCompletion = ((double) lengthOfBranch-index) / (double) lengthOfBranch;
		int thickness = (int) Math.ceil( maxThickness * branchCompletion);
		
		if( layer != Layer.SKIN) { --thickness;}
		
		return tryPlacingBlock( thickness ,currentPos ,layer);
	}
	
	private boolean tryPlacingBlock( int thickness ,BlockPos currentPos ,Layer layer) {
		boolean placed = false;
		if( thickness > 0) {
			IBlockState currentBS = world.getBlockState( currentPos);
			
			if( currentBS.getBlock() != this.getBlockType()) {// && ( validBlockPosition( layer ,currentBS ,currentPos))) {
				if( isFullCycle()) {
					world.destroyBlock(currentPos ,false);
				}
				world.setBlockState( currentPos ,layer.bs);
				placed = true;
			}
				
			--thickness;
			for(int i=0; i<6; i++) {
				BlockPos newPos = currentPos.add( Symbol.intToSymbol( i).mod);
				placed = tryPlacingBlock( thickness ,newPos ,layer) || placed;
			}
		}
		return placed;
	}
	
	

	
	
	
	private boolean stopBlockSearch( Layer layer ,IBlockState blockstate) {
		if( layer == Layer.FLESH || layer == Layer.BONE) {
			if( !nativeBlock( blockstate)) {
				return true;
			}
		}
		return false;
	}
	
			
	private boolean validBlockPosition( Layer layer ,IBlockState blockstate ,BlockPos currentPos) {
		IBlockState currentBS = blockstate.getBlock().getDefaultState();
		if( layer == Layer.SKIN && ( isFullCycle() || !nativeBlock( currentBS))) {
			return true;
		}else if( layer == Layer.FLESH && currentBS != Layer.FLESH.bs) {
			IBlockState[] acceptedTypes = new IBlockState[] { Layer.SKIN.bs ,Layer.FLESH.bs ,Layer.BONE.bs};
			return ( onlyTouchingBlockTypes( currentPos ,acceptedTypes));				
		}else if( layer == Layer.BONE && currentBS != Layer.BONE.bs) {
			return ( onlyTouchingBlockTypes( currentPos ,new IBlockState[] {Layer.FLESH.bs ,Layer.BONE.bs} ));
		}
		return false;
	}
	**/

}


	


	



	








