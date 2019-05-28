package witherwar.faction;


/*
 * Job tells you what to do next - shouldn't dictate path logic? 
 */
public enum Job{
	PATROL{
		public void update( UnitEntity ue) {
			if( ue.moveTo == null) {
				calculate final position and return
			}else if( ue.moveTo == ue.pos) {
				ue.record();
			}
		}

	}
	,EXPLORE{
		public void update( UnitEntity ue) {
			
		}
	}
	,IDLE{
		public void update( UnitEntity ue) {
			return;
		}
	};
	
	
	
	public abstract void update( UnitEntity ue);
	
	
}	










