package witherwar.utility.noise;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.math.BlockPos;



public class NoiseMap3D {
	
	protected HashMap<BlockPos,Double> map;
	protected int width;
	protected int height;
	
	
	
	public NoiseMap3D( int width, int height) {
		
		this.width = (int)Math.pow( 2 ,width);
		this.height = (int)Math.pow( 2 ,height);		
		this.map = new HashMap<>();
		
//		Iterator< Map.Entry<Integer ,Double>> iter;		
//		for( iter = this.map.entrySet().iterator(); iter.hasNext();) {
//			
//		}
		
	}
	
	
	
	
	
//	protected static int hashInt( int x ,int y ,int z) {
//		int hash = 17;
//		hash = hash * 31 + x;
//		hash = hash * 31 + y;
//		hash = hash * 23 + z;
//		return hash;
//	}
	
	
	
	
	public int getHalfwidth() {
		return this.width / 2;
	}	
	
	
	public int getHalfheight() {
		return this.height / 2;
	}
	
	
	
	public Double get( int x ,int y ,int z ,boolean shiftOriginToCenter) {
		return this.get( new BlockPos(x,y,z) ,shiftOriginToCenter);		
	}
	
	
	
	public Double get( BlockPos pos ,boolean shiftOriginToCenter) {
		
		if( shiftOriginToCenter) {
			pos = pos.add( this.getHalfwidth() ,0 ,this.getHalfwidth());
		}
		
		return this.map.get( pos);	
	}
	
}





