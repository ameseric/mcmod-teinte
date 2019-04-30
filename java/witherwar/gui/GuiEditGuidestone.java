package witherwar.gui;

import java.io.IOException;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;


public class GuiEditGuidestone extends GuiScreen{
	private GuiTextField text;
	private String plaintext;
	
	
	public GuiEditGuidestone( String regionName) {
		this.plaintext = regionName;
	}
	 

	public void initGui(){
        this.text = new GuiTextField( 0 ,this.fontRenderer ,this.width / 2 - 68 ,this.height/2-46 ,137 ,20);
        text.setMaxStringLength(23);
        text.setText( this.plaintext);
        this.text.setFocused(true);
	}
	
	
	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
	 

	protected void keyTyped(char par1, int par2){
		try {
			super.keyTyped(par1, par2);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.text.textboxKeyTyped(par1, par2);
    }
	 

    public void updateScreen(){
        super.updateScreen();
        this.text.updateCursorCounter();
    }
	 

    public void drawScreen(int par1, int par2, float par3){
        this.drawDefaultBackground();
        this.text.drawTextBox();
        super.drawScreen(par1, par2, par3);
    }
 

    protected void mouseClicked(int x, int y, int btn) {
        try {
			super.mouseClicked(x, y, btn);
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.text.mouseClicked(x, y, btn);
	    }
}
