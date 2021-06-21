package witherwar.worlds.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;



public class FortressTunnelBuilder extends StructureBuilder {

	
	private static final IBlockState BRICKS = Blocks.STONEBRICK.getDefaultState();
	private static final IBlockState OBSIDIAN = Blocks.OBSIDIAN.getDefaultState();
//	private static final IBlockState DBRICK = Blocks.NETHER_BRICK.getDefaultState();

	//do some block testing in-game?
	
	
	public FortressTunnelBuilder(BlockPos start, BlockPos end) {
		super(start, end);
		
		
		
		build();
	}
	
	

	@Override
	protected void postprocess_initial() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void blockPostProcess(Block block) {
		buildIfWall( block ,BRICKS);
		buildIfSupport( block ,BRICKS);
	}
	
	
	
	@Override
	protected void roomPostProcess( Room room) {
		super.roomPostProcess(room);
		this.tryToBuildNewConnections( room ,BRICKS);
	}
	
	
	
	@Override
	protected void doorPostProcess( Door door) {
		super.doorPostProcess(door);
	}
	
	
	
	

}





class MonumentGardenBuilder extends StructureBuilder{

	public MonumentGardenBuilder(BlockPos start, BlockPos end) {
		super(start, end);
		
		
		build();
	}
	
	

	@Override
	protected void postprocess_initial() {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void blockPostProcess(Block block) {
		// TODO Auto-generated method stub
		
	}
	
}