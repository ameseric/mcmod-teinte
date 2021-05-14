package witherwar.network;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.server.FMLServerHandler;
import witherwar.proxy.ClientOnlyProxy;



public class PlayerDashMessage implements IMessage {
	
	public PlayerDashMessage(){}

	public PlayerDashMessage(String toSend) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}
	  
	//For client-to-server direction ONLY. RMHandler references client code.
	public static class HandlePlayerDashMessage implements IMessageHandler< PlayerDashMessage ,IMessage> {

		@Override 
		public IMessage onMessage( PlayerDashMessage message ,MessageContext ctx) {	
			EntityPlayerMP player  = ctx.getServerHandler().player;
			WorldServer world = player.getServerWorld();
			
		    world.addScheduledTask(() -> {
		    	
		    	
		    	System.out.println( player.prevPosX);
		    	System.out.println( player.posX);
		    	System.out.println( player.lastTickPosX);
		    	
		    	if( player.motionX == 0 && player.motionZ == 0.0) {
			        Vec3d facing = player.getForward();
			    	player.motionX = facing.x * 1.5;
			    	player.motionZ = facing.z * 1.5;			        
		    	}else {
			    	player.motionX *= 1.5;
			    	player.motionZ *= 1.5;		    		
		    	}
	            player.connection.sendPacket(new SPacketEntityVelocity(player));
		      });
			
			return null;
		}
	}
}
