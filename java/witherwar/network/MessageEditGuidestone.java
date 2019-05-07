package witherwar.network;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import witherwar.WitherWar;
import witherwar.tileentity.TileEntityGuidestone;

public class MessageEditGuidestone implements IMessage{
	public String regionName = "";
	public int x = 0;
	//public int y = 0;
	public int z = 0;
	//public int id;
	
	public MessageEditGuidestone() {}
	
	public MessageEditGuidestone( int x ,int z ,String regionName) {
		this.regionName = regionName;
		this.x = x;
		this.z = z;
	}
	
	/**public MessageEditGuidestone( String name ,int x ,int y ,int z) {
		this.regionName = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}**/
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int length = buf.readInt();
		this.regionName = buf.readCharSequence( length ,Charset.defaultCharset()).toString();
		//this.id = buf.readInt();
		this.x = buf.readInt();
		//this.y = buf.readInt();
		this.z = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		int length = this.regionName.getBytes().length;
		buf.writeInt( length);
		buf.writeCharSequence( this.regionName ,Charset.defaultCharset());
		buf.writeInt( this.x);
		//buf.writeInt( this.y);
		buf.writeInt( this.z);
	}	
	
	
	public static class HandleMessageEditGuidestone implements IMessageHandler<MessageEditGuidestone ,IMessage>{

		@Override
		public IMessage onMessage(MessageEditGuidestone msg, MessageContext ctx) {
			
			if( FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT ) { 
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
					WitherWar.proxy.openGui( 0 ,msg));
			}else {
				FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() ->
					WitherWar.instance.setRegionName( msg.regionName , new BlockPos( msg.x ,0 ,msg.z) ));
	//				this.serverHandler( msg));
			}
			return null;
		}
		
	
		
	}
	

}
