package witherwar.faction;


public enum Job{
	PATROL{
		public void update() {
			if( moveTo == null) {
				getMoveToPos()
			}			
			if( puppet) {
				if( moveTo != currentPos) {
					if( path == null) {
						createPath()
					}
					path.increment()
				}
			}else {
				//not implementing yet? unit.entity.moveToPos();
			}
		}
	}
	,EXPLORE{
		public void update() {
			
		}
	}
	,IDLE{
		public void update() {
			
		}
	};
	
	
	public abstract void update();
	
	public 
	
}	
