package witherwar.proxy;


import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import witherwar.ObjectCatalog;
import witherwar.ObjectCatalog.NewBlock;
import witherwar.entity.DroneEntity;
import witherwar.entity.GhastTestEntity;
import witherwar.entity.WitherSkeletonTestEntity;
import witherwar.gui.GuiEditGuidestone;
import witherwar.gui.TeinteGUI;
import witherwar.models.DroneRender;

public class ClientOnlyProxy implements Proxy{
	public static TeinteGUI teinteGUI;
	public static KeyBinding dashKeybind = new KeyBinding("key.dash.desc", Keyboard.KEY_P, "key.magicbeans.teinte");

	
//	private HashMap< Class<? extends Entity> ,Class<? extends Render>> entityRenderTable = new HashMap<>();
//	{
//		this.entityRenderTable.put( EntitySerpentWither.class ,RenderWitherSkeleton.class);
//	}
	
	
	
	
	public void preInit() {		
		
		for( NewBlock nb : ObjectCatalog.getNewBlocks()) {
			this.setModelResourceLocation( nb.item ,nb.resourceLocation);
		}
	}
	
	
	
	
	public void init() {
		Minecraft mc = Minecraft.getMinecraft();
		
//		for( NewEntity ne : TEinTE.catalog.getNewEntities()) {
//			mc.getRenderManager().entityRenderMap.put( ne.entityClass ,)
//		}
		
        mc.getRenderManager().entityRenderMap.put( WitherSkeletonTestEntity.class, new RenderWitherSkeleton( mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put( GhastTestEntity.class, new RenderGhast( mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put( DroneEntity.class, new DroneRender( mc.getRenderManager()));		
	}
	
	
	
		
	public void postInit() {
    	teinteGUI = new TeinteGUI();
    	MinecraftForge.EVENT_BUS.register( teinteGUI.renderHandler);
    	ClientRegistry.registerKeyBinding( dashKeybind);
	}

	
	
		
	@Override
	public boolean isDedicatedServer() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	
	@Override
	public void openGui(int ID ,IMessage msg) {
		if( ID == 0) {
			Minecraft.getMinecraft().displayGuiScreen( new GuiEditGuidestone( msg));
		}
		
	}
	
	
	
	
	private void setModelResourceLocation( ItemBlock item ,ResourceLocation rl){
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation( rl ,"inventory");
		ModelLoader.setCustomModelResourceLocation( item ,DEFAULT_ITEM_SUBTYPE ,itemModelResourceLocation);
	}


	

	@Override
	public void onWorldLoad(Load event) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public boolean playerIsDashing() {
		return dashKeybind.isPressed();
		
	}

	

}