package witherwar.tileentity;

import java.util.HashMap;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;
import witherwar.utility.BlockTypeCollection;
import witherwar.utility.BlockUtil;
import witherwar.utility.HashBlockCollection;

public class ReplicatingTile extends TileLogic{

	
	private final BlockTypeCollection dontChange = new HashBlockCollection();
	private TileLogic parent;
	private static Random rand = new Random();
	
	private boolean firstrun = true;

	
	public ReplicatingTile(BlockPos pos) { //(int)(Math.random() * 500)+20
		super(pos, ObjectCatalog.ASH_REPL_BLOCK, TileLogic.REPLICATE_ID, true ,getRandomTickAmount(100));
		this.dontChange.add( Blocks.OBSIDIAN);
		this.dontChange.add( ObjectCatalog.TERRA_KALI);
		this.dontChange.add( Blocks.AIR);
		this.dontChange.add( ObjectCatalog.ASH_REPL_BLOCK);
		this.dontChange.add( Blocks.BEDROCK);
	}
	
	
	

	@Override
	public String getDataName() {
		return null;
	}
	
	
	public void newBlockNeighbors() {
		this.setActive( true);
	}
	
	
	public void setParent( TileLogic parent) {
		this.parent = parent;
	}
	
	

	public static int getRandomTickAmount( int multiplier) {
		return (int)(Math.random() * multiplier) + 20;
	}
	

	@Override
	public void ticklogic(World worldIn) {
		WorldServer world = (WorldServer) worldIn;
		
		if( firstrun && BlockUtil.notTouchingAir( this.getPos() ,world)) {
			this.ticksUntilUpdate = this.ticksUntilUpdate * 10;				
		}
		firstrun = false;
		

		if(this.parent != null && this.parent.isDead()) {
			this.iAmDead();
			return;
		}
		
		
		//particle test
    	world.spawnParticle( EnumParticleTypes.EXPLOSION_NORMAL ,this.getX() ,this.getY() ,this.getZ() ,3 
    			,0 ,2 ,0 ,0 ,null);

		
		
		HashMap<BlockPos, Block> neighbors = BlockUtil.getNeighborBlocks( this.getPos() ,world ,true);
		
		boolean nochanges = true;
		for( BlockPos neighborPos : neighbors.keySet()) {
			Block b = world.getBlockState( neighborPos).getBlock();
			if( !this.dontChange.includes(b)) {
				world.setBlockState( neighborPos ,ObjectCatalog.ASH_REPL_BLOCK.getDefaultState());
				ReplicatingTile newChild = (ReplicatingTile) TEinTE.instance.getTileLogic( neighborPos);
				newChild.setParent( this.parent);
				nochanges = false;
				break;
			}
		}
		
		if( nochanges) {
			//this.setActive( false);
			this.iAmDead();
		}
		
		
	}
	
	
	
	

}
