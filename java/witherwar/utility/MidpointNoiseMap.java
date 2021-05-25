package witherwar.utility;

import java.util.Arrays;
import java.util.Random;

import scala.Array;

/**
 * 
 * @author Guiltygate
 * 
 * built from Simon G's implementation https://stackoverflow.com/questions/5531019/perlin-noise-in-java
 * and modified according to https://tomlankhorst.nl/rmdf-in-matlab-or-octave/
 *
 *
 */
public class MidpointNoiseMap {

	private Random RNG;
	private float roughness;
	private float variance;
	private float[][] map;
	
	
	
	public MidpointNoiseMap( int size ,float roughness) {
//		this.roughness = roughness;
		
		int width = (int)Math.pow( size ,2) - 1;
		int height = (int)Math.pow( size ,2) - 1;
		this.roughness = roughness;
		this.variance = roughness / width;
		
		this.map = new float[width][height];
		for( float[] v : this.map) {
			Arrays.fill( v ,0);
		}	
		
		this.RNG = new Random();
		
		this.generate();
		
	}
	
	
	public float[][] getMap(){
		return this.map;
	}
	
	
	
	private void generate() {
		int xf = this.map.length - 1;
		int yf = xf;
		int xo = 0;
		int yo = 0;
		
		this.map[xo][yo] = this.RNG.nextFloat();
		this.map[xo][yf] = this.RNG.nextFloat();
		this.map[xf][yo] = this.RNG.nextFloat();
		this.map[xf][yf] = this.RNG.nextFloat();
		
		this.calculateMidpoints( xo ,yo ,xf ,yf);		
	}
	
	
	/**
	 *
	 * @param xo - x Origin
	 * @param yo - y Origin
	 * @param xf - x Final
	 * @param yf - y Final
	 * 
	 * Calculate averaged & roughened midpoints for grid segment defined by xy boundary points
	 * 
	 */
    private void calculateMidpoints( int xo ,int yo ,int xf ,int yf) {
    	
    	int xm = (xf + xo) / 2;
    	int ym = (yf + yo) / 2;
    	int halfwidth = (xf - xo) / 2;
    	if( xo==xm && yo==ym) return;
    	
    	
//    	float midpoint = (this.map[xo][yo] + this.map[xo][yf] + this.map[xf][yo] + this.map[xf][yf]) * 0.25f;
    	float midpoint = avg( new float[]{ this.map[xo][yo] ,this.map[xo][yf] ,this.map[xf][yo] ,this.map[xf][yf]});
    	midpoint = roughen( midpoint ,halfwidth);    	
    	this.map[xm][ym] = midpoint;
    	
    	if( this.map[xo][ym] == 0) {
    		this.map[xo][ym] = roughen( avg( new float[]{ this.map[xo][yo] ,this.map[xo][yf] ,midpoint}) ,halfwidth);
    	}if( this.map[xm][yo] == 0) {
    		this.map[xm][yo] = roughen( avg( new float[]{ this.map[xo][yo] ,this.map[xf][yo] ,midpoint}) ,halfwidth);
    	}if( this.map[xf][ym] == 0) {
    		this.map[xf][ym] = roughen( avg( new float[]{ this.map[xf][yf] ,this.map[xf][yo] ,midpoint}) ,halfwidth);
    	}if( this.map[xm][yf] == 0) {
    		this.map[xm][yf] = roughen( avg( new float[]{ this.map[xo][yf] ,this.map[xf][yf] ,midpoint}) ,halfwidth);
    	}
    	
        this.calculateMidpoints( xo ,yo ,xm ,ym);
        this.calculateMidpoints( xm ,yo ,xf ,ym);
        this.calculateMidpoints( xo ,ym ,xm ,yf);
        this.calculateMidpoints( xm ,ym ,xf ,yf);    	
    }
	
	
    private static float avg( float[] args) {
    	
    	float sum = 0;
    	for( float v : args) {
    		sum += v;
    	}
    	    	
    	return (sum / args.length);
    }
    
	
    
    private float roughen( float value ,int distance) {
		return value + ( this.variance * (float)this.RNG.nextGaussian() * distance); 
	}
	
	
	public int getHalfwidth() {
		return this.map.length / 2;
	}
	
	
	private static float[][] convolve( float[][] input ,float[][]kernel) { //assuming omni-symmetric kernel
		float[][] output = new float[3][3];
		for(float[] v : output) {
			Arrays.fill( v ,0);
		}
		
		for( int i=0; i<input.length; i++) {
			for( int j=0; j<input.length; j++) {
				
				float accumulation = 0;
				float eI = input[i][j];
				for( int h=0; h<kernel.length; h++) {
					for( int l=0; l<kernel.length; l++) {
						float eK = kernel[h][l];
						int a = i-1+h;//assuming 3x3
						int b = j-1+l;//assuming 3x3
						float oV = 0;
						if( a > -1 && a < input.length && b > -1 && b < input.length) {
							oV = input[a][b];
						}
						if( i==1 && j==1) {
							System.out.println(oV);
							System.out.println(eK);
						}
						accumulation += (oV * eK);
					}
				}
				output[i][j] = accumulation;
			}			
		}
		return output;
	}
	
	

	
	

    public static void printCSV( float[][] map) {
        for(int i = 0;i < map.length;i++) {
            for(int j = 0;j < map[0].length;j++) {
                System.out.print(map[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }
	
    
    
    
	
	public static void main(String args[]) {
		
		MidpointNoiseMap nm = new MidpointNoiseMap( 10 ,0.6f);
		GreyScaleNoisePrinter.greyWriteImage( nm.getMap());
		
		
//		float[][] test = new float[3][3];
//		float[][] kernel = new float[3][3];
//		
//		for(float[] v : test) {
//			Arrays.fill( v ,0);
//			Arrays.fill( v ,0);
//		}
//
//		
//		kernel[0][0] = 0.25f;
//		kernel[0][2] = 0.25f;
//		kernel[2][0] = 0.25f;
//		kernel[2][2] = 0.25f;
//		
//		test[0][0] = 0.75f;
//		test[0][2] = 0.5f;
//		test[2][0] = 0.5f;
//		test[2][2] = 0.25f;
//
//
//		printCSV( convolve( test ,kernel));
		
		
		

	}  
	
	
	
}








