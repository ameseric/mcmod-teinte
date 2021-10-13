package teinte.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import teinte.TEinTE;
import teinte.network.MessageEditGuidestone;


public class GuidestoneGUI extends GuiScreen{
	private GuiTextField text;
	private String plaintext;
	private MessageEditGuidestone msg;
    private GuiButton doneBtn;
	
	
	public GuidestoneGUI( IMessage message) {
		this.msg = (MessageEditGuidestone) message;
		this.plaintext = msg.regionName;
	}
	 

	public void initGui(){
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.doneBtn = this.addButton(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done")));
		
        this.text = new GuiTextField( 0 ,this.fontRenderer ,this.width / 2 - 68 ,this.height/2-46 ,137 ,20);
        text.setMaxStringLength(24);
        text.setText( this.plaintext);
        this.text.setFocused(true);
	}
	
	
	@Override
	public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
		this.plaintext = this.text.getText();
		TEinTE.networkwrapper.sendToServer( new MessageEditGuidestone( msg.x ,msg.z ,this.plaintext));
	}
	 

	protected void keyTyped(char typedChar ,int keyCode){
		try {
			super.keyTyped(typedChar , keyCode);
	        if (keyCode == 1 || keyCode == 28){
	            this.actionPerformed(this.doneBtn);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.text.textboxKeyTyped(typedChar , keyCode);
    }
	

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }
	 

    public void updateScreen(){
        super.updateScreen();
        this.text.updateCursorCounter();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
    	return false;
    }
	 

    public void drawScreen(int mouseX, int mouseY ,float partialTicks){
        this.drawDefaultBackground();
        this.text.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
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
