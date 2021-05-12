package witherwar.system;

import java.util.ArrayList;

import net.minecraft.util.EnumFacing;

public class GrowthSystem {

	private ArrayList<ArrayList<EnumFacing>> pattern = new ArrayList<>();
	
	
	
	public GrowthSystem() {}
	
	
	
	public void add( ArrayList<EnumFacing> patternSegment) {
		this.pattern.add( patternSegment);		
	}
	
	
	
	public void add( EnumFacing ef) {
		int i = this.pattern.size();
		this.pattern.add( new ArrayList<>());
		this.pattern.get(i).add( ef);
	}
	
	
	
	
	
	
}
