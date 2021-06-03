package witherwar.faction2.structures;




public class District {
	
	private int[] segments;
	private int height;
	private String name;
	
	
	public District( String name ,int[] segments ,int height) {
		this.segments = segments;
		this.height = height;
		this.name = name;
	}
	
	
	
	
	public int getHeight() {
		return this.height;
	}
	
	
	public int[] getSegments() {
		return this.segments;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	
//	public boolean containsSegment( int n) {
//		return this.segments[n] == 1;
//	}
	
	
	

}
