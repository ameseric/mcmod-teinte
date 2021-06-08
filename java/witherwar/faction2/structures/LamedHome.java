package witherwar.faction2.structures;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import witherwar.utility.Pair;
import witherwar.utility.Vec2;

public class LamedHome extends Home{

	
	
	public LamedHome( BlockPos center ,int size) {
		super( center ,size);
	}
	
	
	
	
	
	
	
	
	
	@Override
	public ArrayList<BlockInfo> getNextSegment() {
		return null;
	}





	@Override
	public boolean isValidPosition(BlockPos pos) {
		return (isValidPattern( pos) && withinBoundary( pos)) || pos.getY() == pyramid( pos.getX() ,pos.getZ() ,80);
	}
	
	
	private static int pyramid( int x ,int z ,int c) {
		x = Math.abs( x);
		z = Math.abs( z);
		int xoz = x < z ? z : x;
		return c - xoz;
	}
	private boolean withinBoundary( BlockPos pos) {
//		double distance = Vec2.distance( pos.getX() ,pos.getZ() ,this.center.getX() ,this.center.getZ());
		return pos.getY() < pyramid( pos.getX() ,pos.getZ() ,80);
	}




	private boolean isValidPattern( BlockPos pos) {
		 return emptyBoxPattern( pos ,6);
	}
	
	
	

	
	
	
}
