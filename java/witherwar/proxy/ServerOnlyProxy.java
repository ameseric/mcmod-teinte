package witherwar.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ServerOnlyProxy implements IProxy{
	
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
	public void openGui(int ID, TileEntity tileentity) {
		// TODO Auto-generated method stub
		
	}



}