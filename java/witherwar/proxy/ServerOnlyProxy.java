package witherwar.proxy;



public class ServerOnlyProxy extends IProxy{
	
	public void preInit() {
	}
	
	public void init() {
		//MinecraftForge.EVENT_BUS.register( new WitherWar( mc));
	}
	
	public void postInit() {
	}
	
	


	@Override
	public boolean isDedicatedServer() {
		// TODO Auto-generated method stub
		return true;
	}
	

}