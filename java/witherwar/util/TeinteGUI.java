package witherwar.util;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Credit to EMAX2000, whose code started this class: https://emxtutorials.wordpress.com/simple-in-game-gui-overlay/ 
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
		final int MAX_ALPHA = 255;
		final float SCALE = 1.6F;
		final float ISCALE = 1/SCALE;
		final int DELAY = 60;
		
		String text = "Plains of Reprieve";
		int tick = 0;
		int delayCounter = 0;
		boolean asc = true;
		
		public void draw( Minecraft mc){
	        ScaledResolution scaled = new ScaledResolution( mc);
	        int width = scaled.getScaledWidth();
	        int height = scaled.getScaledHeight();
	        
	        int alpha = getFadeAlpha();
	        
	        GlStateManager.scale( SCALE ,SCALE ,SCALE);
	        mc.fontRenderer.drawString( text ,width/(24*SCALE) ,height/(SCALE*14) ,Integer.parseInt("EEEEEE" ,16) | (alpha << 24) ,false);
	        GlStateManager.scale( ISCALE ,ISCALE ,ISCALE);
	        
		}
		
		private int getFadeAlpha() {
			
			int alpha = Math.min( (int) Math.floor( (1.0F/16.0F) * Math.pow(tick ,2)) + 4 ,MAX_ALPHA);
			
			if (asc) {
				if( alpha == MAX_ALPHA) {
					++delayCounter;
					if( delayCounter == DELAY) {
						asc = false;
						delayCounter = 0;
					}
				}
				++tick;
			}else {
				if( alpha == 4) {
					asc = true;
				}else {
					--tick;
				}
			}
			
						
			return alpha;
		}
		
		

	}
	
	
	
}