package witherwar.faction;

public abstract class UnitEntity {
	private boolean isPuppet = true;
	
	
	public void act() {
		
	}
	
	public boolean isPuppet() {
		return this.isPuppet;
	}
	
	public static UEScout getNewScout() {
		return new UEScout();
	}

	
	
}



class UEScout extends UnitEntity{
	
}