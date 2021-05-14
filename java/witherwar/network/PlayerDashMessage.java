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
import witherwar.TEinTE;
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
		    	Vec3d lastPos = TEinTE.instance.lastPlayerPos.get( player).poll();
		    	Vec3d currentPos = player.getPositionVector();
		    	Vec3d traj = currentPos.subtract( lastPos);
		    	traj = new Vec3d( traj.x ,0 ,traj.z).normalize();
//		    	System.out.println( lastPos);
//		    	System.out.println( currentPos);
//		    	System.out.println( traj);
//		    	System.out.println( player.getForward());
//		    	traj = traj.normalize();
//		    	System.out.println( traj);
		    	
		    	if( traj.x == 0 && traj.z == 0) {
			        Vec3d facing = player.getForward();
			    	player.motionX = facing.x;// * 1.2;
			    	player.motionZ = facing.z;// * 1.2;			        
		    	}else {
			    	player.motionX = traj.x;//* 1.2;
			    	player.motionZ = traj.z;//* 1.2;    		
		    	}
		    	player.getFoodStats().addExhaustion( (float)TEinTE.config.dashHungerCost);
	            player.connection.sendPacket(new SPacketEntityVelocity(player));
		      });
			
			return null;
		}
	}
}
