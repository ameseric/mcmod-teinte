package teinte.utility.noise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import teinte.disk.NBTSaveFormat;
import teinte.utility.Pair;
import teinte.utility.Vec2;

public abstract class NoiseMap2D implements NBTSaveFormat{

	protected double[][] map;
	protected int size;
	
	
	public NoiseMap2D( int size) {
		
		this.size = (int)Math.pow( 2 ,size);
		
		this.map = new double[ this.size][ this.size];
		for( double[] list : this.map) {
			Arrays.fill( list ,0);
		}
		
	}
	
	
	
	
	//=============== Private Helpers
	
    protected static double avg( double[] args) {    	
    	float sum = 0;
    	for( double v : args) {
    		sum += v;
    	}    	    	
    	return (sum / args.length);
    }
    
    
    
    protected static double squashValue( double value ,float target ,float intensity) {
		double diff = target - value;
		return value + (intensity * diff );
	}
    
	

	protected static double squashToRange( double d) {
		if( d < 0.0) {
			return 0.0;
		}else if( d > 1.0) {
			return 1.0;
		}
		return d;
	}
	
	
	
	
	//================ Outer-Facing
	
	public double[][] getMap() {
		return this.map;
	}
	
	
	
	public abstract double getValue( int x ,int y);
	
	
	
	public int getHalfwidth() {
		return this.map.length / 2;
	}
	
	
	public void spikeRandomValues( int numberOfSpikes ,int radius) {
		this.spikeRandomValues(numberOfSpikes, this.map.length ,1.0 ,radius);
	}
	
	//expensive, for front-load only
	public void spikeRandomValues( int numberOfSpikes ,int range ,double intensityModifier ,int radius) {
		System.out.println( "======================= STARTING SPIKES");
		double dist = radius;
		ArrayList< Pair<Double,Vec2>> spikes = new ArrayList<>();
		Random r = new Random();
		for( int i=0; i<numberOfSpikes; i++) {			
			double intensity = r.nextDouble() * intensityModifier;
			int shiftfactor = this.getHalfwidth() - (range/2);
			int x = (int) Math.round( (r.nextFloat() * range-1) + shiftfactor);
			int y = (int) Math.round( (r.nextFloat() * range-1) + shiftfactor);
			Vec2 pos = new Vec2( x ,y);
			spikes.add( new Pair<>( intensity ,pos));
		}
		
		for(int x=0; x<this.size; x++) {
			for( int y=0; y<this.size; y++) {
				for( Pair<Double,Vec2> p : spikes) {
					Vec2 point = new Vec2( x ,y);
					if( point.distance( p.second()) < dist) {
						this.map[x][y] = squashToRange( this.map[x][y] + p.first());
					}
				}
			}
		}
		System.out.println( "======================= FINISHED SPIKES");
	}
	
	
	
    public void lowerValues( float intensity) {
		this.pullValues( 0f ,intensity);
	}
	
	
	
	
	public void raiseValues( float intensity) {
		this.pullValues( 1f ,intensity);
	}
	
	
	
	
    public void pullValues( float target ,float intensity) {
		for(int x = 0; x<this.map.length; x++) {
			for(int y=0; y<this.map.length; y++) {
				double value = this.map[x][y]; 
				this.map[x][y] = squashValue( value ,target ,intensity);
			}
		}	
    }
	
	
	
	
	public void pullValuesAwayFromMean( float positiveIntensity ,float negativeIntensity ,int iterations) {
		for(int x = 0; x<this.map.length; x++) {
			for(int y=0; y<this.map.length; y++) {
				double value = this.map[x][y];
				
				float target = 1.0f;
				float intensity = positiveIntensity;
				if( value < 0.5) {
					target = 0f;
					intensity = negativeIntensity;
				}
				
				for( int j=0; j<iterations; j++) {
					value = squashValue( value ,target ,intensity);
				}
				this.map[x][y] = value;
			}
		}
	}
	
	
	
    //For normalized maps
	public static void printCSV( double[][] map) {
        for(int i = 0;i < map.length;i++) {
            for(int j = 0;j < map[0].length;j++) {
                System.out.print(map[i][j]);
                System.out.print(",");
            }
            System.out.println();
        }
    }
	
	
}
