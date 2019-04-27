package witherwar.util;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import witherwar.WitherWar;

/**
 * 
 * @author Guiltygate
 * 
 * Code written via Forge tutorial: https://mcforge.readthedocs.io/en/latest/networking/simpleimpl/
 *
 */


public class TeintePacketHandler {
	public static final SimpleNetworkWrapper snwrapper = NetworkRegistry.INSTANCE.newSimpleChannel("teinte");
	
	
	public static class RegionMessage implements IMessage {
		private String regionName;
		
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
		  
		  
		public static class RegionMessageHandler implements IMessageHandler< RegionMessage ,IMessage> {

			@Override 
			public IMessage onMessage( RegionMessage message ,MessageContext ctx) {				
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> 
					handle( message ,ctx));
				return null;
			}
			
			
			private void handle( RegionMessage message , MessageContext ctx) {
				WitherWar.instance.teinteGUI.triggerDraw( message.regionName);
			}
		}
	}
	
	

	
}
