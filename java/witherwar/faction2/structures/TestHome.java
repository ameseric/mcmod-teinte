package witherwar.faction2.structures;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import witherwar.utility.Pair;


public class TestHome extends Home{

	
	private List<District> districts = new ArrayList<>();
	
	
	
	
	public TestHome(BlockPos center) {
		super(center);
		districts.add( new District( "tall_things" ,new int[] {1,2,3,4,5,6,7,8,9,10} ,1));
		districts.add( new District( "short_things" ,new int[] {11,12,16,17,21,22} ,1));
		districts.add( new District( "center" ,new int[] {13} ,1));
	}

	
	
	
	@Override
	public boolean isValidPosition(BlockPos pos) {
		return false;
	}
	
	
	

	@Override
	public ArrayList<Pair<BlockPos, Block>> getNextSegment() {
		return null;
	}

}
