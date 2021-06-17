package witherwar.tileentity;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import witherwar.ObjectCatalog;
import witherwar.TEinTE;




public abstract class GrowingTile extends TileLogic{
	
	private int lifetime = 0;
	private int height = 0;
	private int parentsBefore = 0;
	protected CellType celltype;
	
	private Block deadCell;
	private Block liveCell;
	
	protected EnumFacing directionOfGrowth;
	
	
	
	
	public enum CellType{
		STEM(0)
		,PETAL(1)
		,LEAF(2)
		,HEAD(3)
		,ROOT(4)
		,SEED(5);
		
		
		private int id;
		
		private CellType( int id) {
			this.id = id;
		}
		
		public int getint() {
			return this.id;
		}
		
		
	}
	
	
	
	

	public GrowingTile(BlockPos pos ,Block b ,int id ,int ticksUntilUpdate) {
		super( pos, b, id, true, ticksUntilUpdate);
	}
	


	
	
	
	@Override
	public String getDataName() {
		return null;
	}
	
	
	
	protected abstract void grow( World world);
	
	
	protected abstract boolean canGrow();
	
	
	@Override
	public void ticklogic(World world) {
		
		if(this.parentsBefore > 20) {
			this.iAmDead();
			
		}else if( this.isBeingBuilt()) {
//			BlockInfo bi = this.getNextBlock( world);
			if( this.canGrow()) {
//				world.setBlockState( bi.pos ,bi.blockState);
//				GrowingTile child = (GrowingTile) TEinTE.instance.getTileLogic( bi.pos);
//				if( child != null) {
//					child.setParent( this.parentsBefore);
//				}
				this.grow(world);
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
	
	
	protected void setCellType( CellType ct) {
		this.celltype = ct;
	}
	

	protected void setGrowthDirection( EnumFacing growth) {
		this.directionOfGrowth = growth;
	}
	
	
	public boolean isBeingBuilt() {
		return true;
	}
	

	
	public boolean isSeed() {
		return this.celltype == CellType.SEED;
	}
	

	
	
}





