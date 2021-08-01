package witherwar.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public interface Proxy {
	
	abstract public boolean isDedicatedServer();
	
	public void preInit();
	
	public void init();
	
	public void postInit();	
	
	public void onWorldLoad( WorldEvent.Load event);	
    
    public void openGui( int ID ,IMessage msg);    
    
    public boolean playerIsDashing();
    
    public void renderMuirParticles( float density ,Vec3d color);
	
	
}