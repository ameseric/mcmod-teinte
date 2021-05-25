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
	private float[][] map;
	
	
	
	public MidpointNoiseMap( int size ,float roughness) {
//		this.roughness = roughness;
		
		int width = (int)Math.pow( size ,2) - 1;
		int height = (int)Math.pow( size ,2) - 1;
		this.roughness = roughness / width;
		
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
		int yf = this.map[0].length - 1;
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
    private void calculateMidpoints(int xl, int yl, int xh, int yh) {
        int xm = (xl + xh) / 2;
        int ym = (yl + yh) / 2;
        if ((xl == xm) && (yl == ym)) return;

//        if( this.map[xm][yl] == 0) {
        	this.map[xm][yl] = 0.5f * (this.map[xl][yl] + this.map[xh][yl]);
        	this.map[xm][yl] = roughen(this.map[xm][yl], xl, xh);
//        }
//        if( this.map[xm][yh] == 0) {
        	this.map[xm][yh] = 0.5f * (this.map[xl][yh] + this.map[xh][yh]);
        	this.map[xm][yh] = roughen(this.map[xm][yh], xl, xh);
//        }
//        if( this.map[xl][ym] == 0) {
        	this.map[xl][ym] = 0.5f * (this.map[xl][yl] + this.map[xl][yh]);
        	this.map[xl][ym] = roughen(this.map[xl][ym], yl, yh);
//        }
//        if( this.map[xh][ym] == 0) {
        	this.map[xh][ym] = 0.5f * (this.map[xh][yl] + this.map[xh][yh]);
        	this.map[xh][ym] = roughen(this.map[xh][ym], yl, yh);
//        }

        float v = roughen(0.5f * (this.map[xm][yl] + this.map[xm][yh]), yh
                + xh ,xl + yl);
        
        this.map[xm][ym] = v;
//        this.map[xm][yl] = roughen(this.map[xm][yl], xl, xh);
//        this.map[xm][yh] = roughen(this.map[xm][yh], xl, xh);
//        this.map[xl][ym] = roughen(this.map[xl][ym], yl, yh);
//        this.map[xh][ym] = roughen(this.map[xh][ym], yl, yh);

        calculateMidpoints(xl, yl, xm, ym);
        calculateMidpoints(xm, yl, xh, ym);
        calculateMidpoints(xl, ym, xm, yh);
        calculateMidpoints(xm, ym, xh, yh);
    }
	
	
	
	private float roughen( float value ,int upperBound ,int lowerBound) {
		return value + ( this.roughness * (float)this.RNG.nextGaussian() * (upperBound - lowerBound)); 
	}
	
	
	
	private static float[][] convolve( float[][] input ,float[][]kernel) { //assuming omni-symmetric kernel
		
		for( int i=0; i<input.length; i++) {
			for( int j=0; j<input.length; j++) {
				
				float accumulation = 0;
				float eI = input[i][j];
				for( int h=0; h<kernel.length; h++) {
					for( int l=0; l<kernel.length; l++) {
						float eK = kernel[h][l];
						int a = h - 1;//assuming 3x3
						int b = l - 1;//assuming 3x3
						float output = 0;
						if( a > -1 && a < input.length && b > -1 && b < input.length) {
							output = input[a][b];
						}
						accumulation += (output * eK);
					}
				}
				input[i][j] = accumulation;
			}			
		}
		return input;
	}
	
	
	private static void singleConvolve() { //Not possible? Still need both matricies...
		
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
		
//		MidpointNoiseMap nm = new MidpointNoiseMap( 8 ,0.1f);
//		GreyScaleNoisePrinter.greyWriteImage( nm.getMap());
		
		
		float[][] test = new float[3][3];
		float[][] kernel = new float[3][3];
		
		for(float[] v : test) {
			Arrays.fill( v ,0);
			Arrays.fill( v ,0);
		}

		
		kernel[0][0] = 0.25f;
		kernel[0][2] = 0.25f;
		kernel[2][0] = 0.25f;
		kernel[2][2] = 0.25f;
		
		test[0][0] = 0.75f;
		test[0][2] = 0.5f;
		test[2][0] = 0.5f;
		test[2][2] = 0.25f;

		printCSV( kernel);
		printCSV( test);
		printCSV( convolve( test ,kernel));

	}  
	
	
	
}








