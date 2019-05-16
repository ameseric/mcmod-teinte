package witherwar.faction;

public class FactionAleph extends Faction{

	
	public FactionAleph() {
		
	}
	
	
	
	@Override
	public Action chooseNewGoal() {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	
	
	
	
	public class PrimeGoal extends Action{
		
		@Override
		public void perform() {
			
		}

		@Override
		public boolean costMet(ResourceList materials) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	


}
