package witherwar.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**
 * Credit to EMAX2000, whose code I used below: https://emxtutorials.wordpress.com/simple-in-game-gui-overlay/ 
 */


/***
 * 
 *  Rough GUI outline. Right now written in bad practice, will be modified later to accomodate different GUI elements.
 */
public class RegionGUI{
	public RenderHandler renderHandler;
	
	public RegionGUI() {
		renderHandler = new RenderHandler();
	}
	
	
	public class RenderHandler{
	    @SubscribeEvent
	    public void onRenderGui( RenderGameOverlayEvent.Post event)
	    {
			 if ( event.getType() != ElementType.EXPERIENCE) return;
			new ActualRegionGUI( Minecraft.getMinecraft());
	    }
	}
	
	
	
	private class ActualRegionGUI extends Gui{
		String text = "Sample";
		
		public ActualRegionGUI( Minecraft mc){
	        ScaledResolution scaled = new ScaledResolution( mc);
	        int width = scaled.getScaledWidth();
	        int height = scaled.getScaledHeight();
	 
	        drawCenteredString( mc.fontRenderer ,text ,width / 2 ,(height / 2) - 4 ,Integer.parseInt("FFAA00", 16));		
		}
	}
	
	
	
}