package witherwar.faction2.structures;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import witherwar.utility.Pair;


public class TestHome extends Home{

	
	
	
	
	public TestHome(BlockPos center) {
		super(center ,180);
		
//		District tall = new District( "tall_things" ,new int[] {1,2,3,4,5,6,7,8,9,10});
//		tall.addStructure( MuirSpike.getInertInstance());
//		
//		
//		districts.add( tall);
//		districts.add( new District( "short_things" ,new int[] {11,12,16,17,21,22}).setHeight( 8));
//		districts.add( new District( "center" ,new int[] {13}).setLayers(10).setHeight(10));
		
		
	}

	
	
	
	@Override
	public boolean isValidPosition(BlockPos pos) {
		return false;
	}
	
	
	



}
