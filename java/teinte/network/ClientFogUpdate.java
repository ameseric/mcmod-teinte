package teinte.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teinte.TEinTE;



public class ClientFogUpdate implements IMessage {
	private float red;
	private float green;
	private float blue;
	private float density;
	private float scale;
	
	public ClientFogUpdate(){}

	public ClientFogUpdate( Vec3d unadjColor ,float colorScale ,float density) {
		this.density = density;
		this.red = (float) unadjColor.x;
		this.green = (float) unadjColor.y;
		this.blue = (float) unadjColor.z;
		this.scale = colorScale;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat( this.density);
		buf.writeFloat( this.scale);
		buf.writeFloat( this.red);
		buf.writeFloat( this.green);
		buf.writeFloat( this.blue);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.density = buf.readFloat();
		this.scale = buf.readFloat();
		this.red = buf.readFloat();
		this.green = buf.readFloat();
		this.blue = buf.readFloat();
	}
	  
	//For server-to-client direction ONLY. RMHandler references client code.
	public static class HandleClientFogUpdate implements IMessageHandler< ClientFogUpdate ,IMessage> {

		@Override 
		public IMessage onMessage( ClientFogUpdate message ,MessageContext ctx) {				
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> 
				TEinTE.instance.updatePlayerFogValues( message.red ,message.green ,message.blue ,message.density ,message.scale));
//				ClientOnlyProxy.teinteGUI.triggerDraw( message.regionName));
			return null;
		}
	}
}
