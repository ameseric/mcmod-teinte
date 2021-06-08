package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;




public class GrowingTile extends TileLogic{
	
	private int lifetime = 0;
	private int height = 0;
	private int parentsBefore = 0;
	
	private Block deadCell = ObjectCatalog.DEAD_ASH;
	private Block liveCell = ObjectCatalog.DEBUG_BLOCK;
	
	

	public GrowingTile(BlockPos pos) {
		super( pos, ObjectCatalog.DEBUG_BLOCK, TileLogic.GROWING_ID, true, 40);
	}

	
	
	
	@Override
	public String getDataName() {
		return null;
	}

	
	
	
	@Override
	public void ticklogic(World world) {
		System.out.println( this.parentsBefore);
		
		if(this.parentsBefore > 20) {
			this.iAmDead();
			
		}else if( this.isBeingBuilt()) {
			BlockInfo bi = this.getNextBlock( world);
			if( bi != null) {
				world.setBlockState( bi.pos ,bi.blockState);
				GrowingTile child = (GrowingTile) TEinTE.instance.getTileLogic( bi.pos);
				if( child != null) {
					child.setParent( this.parentsBefore);
				}
			}else {
				this.iAmDead();
			}
		}
		
		this.lifetime++;
	}
	
	
	
	private BlockInfo getNextBlock( World world) {
		if( world.getBlockState( this.getPos().up()) == Blocks.AIR.getDefaultState()) {
			if( RNG.nextDouble() < 0.65) {
				return new BlockInfo( this.getPos().up() ,this.liveCell.getDefaultState() ,null);
			}else {				
				return new BlockInfo( getRandomHorizontal( this.getPos()) ,this.liveCell.getDefaultState() ,null);
			}
		}
		return null;
	}
	
	
	private static BlockPos getRandomHorizontal( BlockPos pos) {
		double r = RNG.nextDouble();
		if( r < 0.25) { return pos.north();}
		if( r < 0.5) { return pos.south();}
		if( r < 0.75) { return pos.east();}
		return pos.west();
	}
	
	
	public void setParent( int n) {
		this.parentsBefore = n + 1;
	}
	
	
	
	public boolean isBeingBuilt() {
		return true;
	}

	
	
}





