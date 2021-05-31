package witherwar.utility.noise;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Guiltygate
 * 
 * built from Simon G's implementation https://stackoverflow.com/questions/5531019/perlin-noise-in-java
 * and modified according to https://tomlankhorst.nl/rmdf-in-matlab-or-octave/
 *
 * Range is *roughly* 0 -> 1, not -1 -> 1, with some minor outliers due to roughening (not normalized)
 */
public class MidpointNoiseMap extends NoiseMap {

	private Random RNG;
	private float roughness;
	private float variance;
	
	
	
	public MidpointNoiseMap( int size ,float roughness) {
//		this.roughness = roughness;
		super( size);
		this.roughness = roughness;
		this.variance = roughness / this.size;
		
		this.RNG = new Random();
		
		this.generate();
		
	}
	
	
	
	public static MidpointNoiseMap getLowMapWithHighAccent( int numberOfAccents) {
		return null;
	}
	
	
	
	
	private void generate() {
		int xf = this.map.length - 1;
		int yf = xf;
		int xo = 0;
		int yo = 0;
		
		this.map[xo][yo] = this.RNG.nextDouble();
		this.map[xo][yf] = this.RNG.nextDouble();
		this.map[xf][yo] = this.RNG.nextDouble();
		this.map[xf][yf] = this.RNG.nextDouble();
		
		this.calculateMidpoints( xo ,yo ,xf ,yf ,true);		
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
    private void calculateMidpoints( int xo ,int yo ,int xf ,int yf ,boolean firstIteration) {
    	
    	int xm = (xf + xo) / 2;
    	int ym = (yf + yo) / 2;
    	int halfwidth = (xf - xo) / 2;
    	if( xo==xm && yo==ym) return;
    	
    	
    	double midpoint;
    	if( firstIteration) {
    		midpoint = this.RNG.nextDouble();
    	}
    	else{
    		midpoint = avg( new double[]{ this.map[xo][yo] ,this.map[xo][yf] ,this.map[xf][yo] ,this.map[xf][yf]});
        	midpoint = roughen( midpoint ,halfwidth);
    	}
    	this.map[xm][ym] = midpoint;
    	
    	if( this.map[xo][ym] == 0) {
    		this.map[xo][ym] = roughen( avg( new double[]{ this.map[xo][yo] ,this.map[xo][yf] ,midpoint}) ,halfwidth);
    	}if( this.map[xm][yo] == 0) {
    		this.map[xm][yo] = roughen( avg( new double[]{ this.map[xo][yo] ,this.map[xf][yo] ,midpoint}) ,halfwidth);
    	}if( this.map[xf][ym] == 0) {
    		this.map[xf][ym] = roughen( avg( new double[]{ this.map[xf][yf] ,this.map[xf][yo] ,midpoint}) ,halfwidth);
    	}if( this.map[xm][yf] == 0) {
    		this.map[xm][yf] = roughen( avg( new double[]{ this.map[xo][yf] ,this.map[xf][yf] ,midpoint}) ,halfwidth);
    	}
    	
        this.calculateMidpoints( xo ,yo ,xm ,ym ,false);
        this.calculateMidpoints( xm ,yo ,xf ,ym ,false);
        this.calculateMidpoints( xo ,ym ,xm ,yf ,false);
        this.calculateMidpoints( xm ,ym ,xf ,yf ,false);    	
    }
	
    
    
    
	
    
    //========== Private helpers ==============
    
    protected double roughen( double value ,int distance) {
		return value + ( (float)this.RNG.nextGaussian() * distance * this.variance); 
	}
    
    
    
	
    
    
    //============ Public Access ==================	
	
	public double getValue( int x ,int y) {
		return this.map[x][y];
	}
    
    
    

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void readFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void markDirty() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public String getDataName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
	
//	private static float[][] convolve( float[][] input ,float[][]kernel) { //assuming omni-symmetric kernel
//		float[][] output = new float[3][3];
//		for(float[] v : output) {
//			Arrays.fill( v ,0);
//		}
//		
//		for( int i=0; i<input.length; i++) {
//			for( int j=0; j<input.length; j++) {
//				
//				float accumulation = 0;
//				float eI = input[i][j];
//				for( int h=0; h<kernel.length; h++) {
//					for( int l=0; l<kernel.length; l++) {
//						float eK = kernel[h][l];
//						int a = i-1+h;//assuming 3x3
//						int b = j-1+l;//assuming 3x3
//						float oV = 0;
//						if( a > -1 && a < input.length && b > -1 && b < input.length) {
//							oV = input[a][b];
//						}
//						if( i==1 && j==1) {
//							System.out.println(oV);
//							System.out.println(eK);
//						}
//						accumulation += (oV * eK);
//					}
//				}
//				output[i][j] = accumulation;
//			}			
//		}
//		return output;
//	}
	
	
 
	
	
	
}








