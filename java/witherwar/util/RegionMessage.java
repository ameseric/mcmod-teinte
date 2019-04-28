package witherwar.util;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import witherwar.proxy.ClientOnlyProxy;



public class RegionMessage implements IMessage {
	public String regionName;
	
	public RegionMessage(){}

	public RegionMessage(String toSend) {
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
	public static class RegionMessageHandler implements IMessageHandler< RegionMessage ,IMessage> {

		@Override 
		public IMessage onMessage( RegionMessage message ,MessageContext ctx) {				
			FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> 
				//ClientOnlyProxy.messageHandler(message, ctx));
				ClientOnlyProxy.teinteGUI.triggerDraw( message.regionName));
			return null;
		}
	}
}
