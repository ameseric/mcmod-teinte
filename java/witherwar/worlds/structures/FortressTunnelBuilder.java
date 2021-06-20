package witherwar.worlds.structures;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;



public class FortressTunnelBuilder extends StructureBuilder {

	
	
	public FortressTunnelBuilder(BlockPos start, BlockPos end) {
		super(start, end);
		
		
		
		build();
	}
	
	

	@Override
	void postprocess_initial() {
		// TODO Auto-generated method stub
		
	}



	@Override
	void blockPostProcess(Block block) {
		buildIfWall( block ,Blocks.NETHER_BRICK.getDefaultState());
		buildIfSupport( block ,Blocks.NETHER_BRICK.getDefaultState());
	}
	
	
	
	

}





class MonumentGardenBuilder extends StructureBuilder{

	public MonumentGardenBuilder(BlockPos start, BlockPos end) {
		super(start, end);
		
		
		build();
	}
	
	

	@Override
	void postprocess_initial() {
		// TODO Auto-generated method stub
		
	}



	@Override
	void blockPostProcess(Block block) {
		// TODO Auto-generated method stub
		
	}
	
}