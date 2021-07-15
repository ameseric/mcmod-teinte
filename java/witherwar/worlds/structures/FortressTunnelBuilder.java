package witherwar.worlds.structures;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;



//TODO: build entry rooms for SBs
//TODO: work on decorators
//TODO: add "tower" SB, use tower, tunnel, and garden for rough draft
//TODO: break up SB from block/segment/room/door?


public class FortressTunnelBuilder extends StructureBuilder {

	
	private static final IBlockState BRICKS = Blocks.STONEBRICK.getDefaultState();
	private static final IBlockState OBSIDIAN = Blocks.OBSIDIAN.getDefaultState();
//	private static final IBlockState DBRICK = Blocks.NETHER_BRICK.getDefaultState();

	
	public FortressTunnelBuilder(BlockPos start, BlockPos end) {
		super(start, end 
				,new EnumFacing[] {EnumFacing.NORTH ,EnumFacing.SOUTH ,EnumFacing.EAST ,EnumFacing.WEST} 
				,null);
		
		
		build();
	}
	
	

	@Override
	protected void postprocess_initial() {
		
	}
	
	


	@Override
	protected void blockPostProcess(Block block) {
		IBlockState material = BRICKS;
		Room room = (Room) block.parent;
		if( room != null && room.isEntrance()) {
			material = Blocks.YELLOW_GLAZED_TERRACOTTA.getDefaultState();
		}
		
		buildIfWall( block ,material);
//		buildIfSupport( block ,material);
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




//
//class MonumentGardenBuilder extends StructureBuilder{
//
//	public MonumentGardenBuilder(BlockPos start, BlockPos end) {
//		super(start, end);
//		
//		
//		build();
//	}
//	
//	
//
//	@Override
//	protected void postprocess_initial() {
//		// TODO Auto-generated method stub
//		
//	}
//
//
//
//	@Override
//	protected void blockPostProcess(Block block) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//}