package teinte.utility.noise;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;

public class SimplexNoiseMap2D extends NoiseMap2D{

	protected double scale;
	protected int shift;
	protected boolean normalize;

	
	public SimplexNoiseMap2D( int size ,double scale ,int shift ,boolean normalize) {
		super( size);
		this.scale = scale;
		this.shift = shift;
		this.normalize = normalize;
		
				
		this.generate(); //TODO: make optional?
	}
	
	public SimplexNoiseMap2D( int size ,double scale ,boolean normalize) {
		this( size ,scale ,new Random().nextInt() ,normalize);
	}
	
	
	
	public void generate() {
		for( int x=0; x<this.size; x++) {
			for( int y=0; y<this.size; y++) {
				double i = (x+shift)*scale;
				double j = (y+shift)*scale; 
				this.map[x][y] =  ( SimplexNoise.noise( i ,j) + 1) / 2.0;
			}
		}
	}
	
	
	
	public double getValue( int x ,int y) {
//		return SimplexNoise.noise( x ,y);  //TODO for now saving in map
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
	public String getDataName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	
	
}
