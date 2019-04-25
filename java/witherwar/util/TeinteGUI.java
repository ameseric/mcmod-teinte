package witherwar.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Credit to EMAX2000, whose code I used below: https://emxtutorials.wordpress.com/simple-in-game-gui-overlay/ 
 */


/***
 * 
 *  Rough GUI outline. Right now written in bad practice, will be modified later to accomodate different GUI elements.
 */
public class TeinteGUI{
	public RenderHandler renderHandler;
	private RegionOverlay regionGUI;
	
	public TeinteGUI() {
		renderHandler = new RenderHandler();
		regionGUI = new RegionOverlay();
	}
	
	
	public class RenderHandler{
	    @SubscribeEvent
	    public void onRenderGui( RenderGameOverlayEvent.Post event){
			 if ( event.getType() == ElementType.EXPERIENCE) {
				 regionGUI.draw( Minecraft.getMinecraft());
			 }
	    }
			 
	}
	
	@SideOnly( Side.CLIENT)
	public class RegionOverlay extends Gui{
		String text = "SAMPLE";
		int tick = 0;
		
		public void draw( Minecraft mc){
	        ScaledResolution scaled = new ScaledResolution( mc);
	        int width = scaled.getScaledWidth();
	        int height = scaled.getScaledHeight();
	        
	        int timer = tick/20;
	        String textNow = text.substring( 0 ,timer);
	        if( timer == text.length()) {
	        	tick = 0;
	        }
	 
	        drawString( mc.fontRenderer ,textNow ,width / 20 ,height / 10 ,Integer.parseInt("FFAA00", 16));
	        tick++;
		}
	}
	
	
	
}