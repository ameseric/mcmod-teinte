package witherwar.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerOnlyProxy implements Proxy{
	
	public void preInit() {
	}
	
	public void init() {

	}
	
	public void postInit() {
	}
	
	


	public boolean isDedicatedServer() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		return ctx.getServerHandler().player;
	}

	@Override
	public void openGui(int ID, IMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWorldLoad( WorldEvent.Load event) {
		
	}

	@Override
	public boolean isDashing() {
		// TODO Auto-generated method stub
		return false;
	}



}