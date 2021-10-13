package teinte.network;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import teinte.proxy.ClientOnlyProxy;



public class MessageRegionOverlayOn implements IMessage {
	public String regionName;
	
	public MessageRegionOverlayOn(){}

	public MessageRegionOverlayOn(String toSend) {
		this.regionName = toSend;
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeCharSequence( this.regionName ,Charset.defaultCharset());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.regionName = buf.readCharSequence( buf.readableBytes() ,Charset.defaultCharset()).toString();
	}
	  
	//For server-to-client direction ONLY. RMHandler references client code.
	public static class HandleMessageRegionOverlayOn implements IMessageHandler< MessageRegionOverlayOn ,IMessage> {

		@Override 
		public IMessage onMessage( MessageRegionOverlayOn message ,MessageContext ctx) {				
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> 
				ClientOnlyProxy.teinteGUI.triggerDraw( message.regionName));
			return null;
		}
	}
}
