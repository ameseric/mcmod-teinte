package witherwar.proxy;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import witherwar.TEinTE;
import witherwar.block.BlockRefHolder;
import witherwar.entity.EntityMotusGhast;
import witherwar.entity.EntitySerpentWither;
import witherwar.gui.GuiEditGuidestone;
import witherwar.gui.TeinteGUI;
import witherwar.tileentity.TileEntityGuidestone;

public class ClientOnlyProxy implements IProxy{
	public static TeinteGUI teinteGUI;
	
	public void preInit() {
		
		for( BlockRefHolder brh : TEinTE.blocks.values()) {
			brh.setModelResourceLocation();
		}		
	}
	
	public void init() {
		Minecraft mc = Minecraft.getMinecraft();
        mc.getRenderManager().entityRenderMap.put( EntitySerpentWither.class, new RenderWitherSkeleton( mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put( EntityMotusGhast.class, new RenderGhast( mc.getRenderManager()));
	}
	
	public void postInit() {
    	teinteGUI = new TeinteGUI();
    	MinecraftForge.EVENT_BUS.register( teinteGUI.renderHandler);
	}

	@Override
	public boolean isDedicatedServer() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	@Override
	// code taken from Jabelar, but seems to be an endless loop? Also doesn't seem useful.
	public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
		return null;
		//return (ctx.side.isClient() ? Minecraft.getMinecraft().player : WitherWar.proxy.getPlayerEntityFromContext(ctx));
	}

	@Override
	public void openGui(int ID ,IMessage msg) {
		if( ID == 0) {
			Minecraft.getMinecraft().displayGuiScreen( new GuiEditGuidestone( msg));
		}
		
	}

	

}