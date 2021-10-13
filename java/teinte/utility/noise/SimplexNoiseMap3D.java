package teinte.utility.noise;

import net.minecraft.util.math.BlockPos;

public class SimplexNoiseMap3D extends NoiseMap3D{

	
	protected double scale;
	protected int shift;
	
	
	public SimplexNoiseMap3D(int width, int height ,int shift ,double scale) {
		super(width, height);
		
		this.scale = scale;
		this.shift = shift;
		
		for( int x=0; x<this.width; x++) {
			for( int z=0; z<this.width; z++) {
				for( int y=0; y<this.height; y++) {
					double value = getNormalizedSimplexValue( x ,y ,z);
//					System.out.println( hash +": " + value);
					this.map.put( new BlockPos(x,y,z) ,value);
				}
			}
		}
		
	}
	
	
	
	private double getSimplexValue( int x ,int y ,int z) {
		double xt = ( x + this.shift) * this.scale;
		double zt = ( z + this.shift) * this.scale;
		double yt = ( y + this.shift) * this.scale;
		return SimplexNoise.noise( xt ,yt ,zt);
	}
	
	
	private double getNormalizedSimplexValue( int x ,int y ,int z) {
		return ( this.getSimplexValue(x, y, z) + 1) / 2.0;
	}
	
	
	
	
	

}
