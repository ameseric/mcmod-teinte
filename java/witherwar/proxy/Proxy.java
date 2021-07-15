package witherwar.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface Proxy {
	
	abstract public boolean isDedicatedServer();
	
	void preInit();
	
	void init();
	
	void postInit();	
	
	void onWorldLoad( WorldEvent.Load event);
	
    
    void openGui( int ID ,IMessage msg);
    
    
    public boolean playerIsDashing();
	
	
}