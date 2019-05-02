package witherwar.gui;


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
@SideOnly( Side.CLIENT)
public class TeinteGUI{
	public RenderHandler renderHandler;
	private RegionOverlay regionGUI;
	private boolean running = false;
	private String regionName = "NULL";
	
	
	public TeinteGUI() {
		renderHandler = new RenderHandler();
		regionGUI = new RegionOverlay();
	}
	
	
	
	public void triggerDraw( String regionName) {
		this.running = true;
		this.regionName = regionName;
	}
	
	
	
	public class RenderHandler{
	    @SubscribeEvent
	    public void onRenderGui( RenderGameOverlayEvent.Post event){
			 if ( event.getType() == ElementType.EXPERIENCE && running) {
				 regionGUI.draw( Minecraft.getMinecraft());
			 }
	    }			 
	}
	
	
	
	public class RegionOverlay extends Gui{
		final int MAX_ALPHA = 255;
		final float SCALE = 1.6F;
		final float ISCALE = 1/SCALE;
		final int DELAY = 100;
		
		int tick = 0;
		int delayCounter = 0;
		boolean asc = true;
		
		
		public void draw( Minecraft mc){
	        ScaledResolution scaled = new ScaledResolution( mc);
	        int width = scaled.getScaledWidth();
	        int height = scaled.getScaledHeight();
	        
	        int alpha = getFadeAlpha();
	        
	        GlStateManager.scale( SCALE ,SCALE ,SCALE);
	        mc.fontRenderer.drawString( regionName ,width/(30*SCALE) ,height/(SCALE*20) ,Integer.parseInt("EEEEEE" ,16) | (alpha << 24) ,true);
	        GlStateManager.scale( ISCALE ,ISCALE ,ISCALE);
	        
		}
		
		private int getFadeAlpha() {
			
			int alpha = Math.min( (int) Math.floor( (1.0F/18.0F) * Math.pow(tick ,2)) + 4 ,MAX_ALPHA);
			
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
					running = false;
					asc = true;
				}else {
					--tick;
				}
			}
			
						
			return alpha;
		}
		
		

	}
	
	
	
}