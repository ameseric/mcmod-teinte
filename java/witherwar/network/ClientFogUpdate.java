package witherwar.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import witherwar.TEinTE;



public class ClientFogUpdate implements IMessage {
	private float red;
	private float green;
	private float blue;
	private float density;
	
	public ClientFogUpdate(){}

	public ClientFogUpdate( Vec3d colors ,float density) {
		this.density = density;
		this.red = (float) colors.x;
		this.green = (float) colors.y;
		this.blue = (float) colors.z;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat( this.density);
		buf.writeFloat( this.red);
		buf.writeFloat( this.green);
		buf.writeFloat( this.blue);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.density = buf.readFloat();
		this.red = buf.readFloat();
		this.green = buf.readFloat();
		this.blue = buf.readFloat();
	}
	  
	//For server-to-client direction ONLY. RMHandler references client code.
	public static class HandleClientFogUpdate implements IMessageHandler< ClientFogUpdate ,IMessage> {

		@Override 
		public IMessage onMessage( ClientFogUpdate message ,MessageContext ctx) {				
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> 
				TEinTE.instance.updatePlayerFogValues( message.red ,message.green ,message.blue ,message.density));
//				ClientOnlyProxy.teinteGUI.triggerDraw( message.regionName));
			return null;
		}
	}
}
