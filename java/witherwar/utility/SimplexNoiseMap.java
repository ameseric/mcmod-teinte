package witherwar.utility;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.util.math.Vec3i;





public class SimplexNoiseMap {

	private Random RNG;
	private float[][] map;

	
	public SimplexNoiseMap( int size) {
		int width = (int)Math.pow( size ,2) - 1;
		int height = (int)Math.pow( size ,2) - 1;
		
		this.map = new float[width][height];
		for( float[] v : this.map) {
			Arrays.fill( v ,0);
		}	
		
		this.RNG = new Random();
		
		
	}
	
	
	
	
}
