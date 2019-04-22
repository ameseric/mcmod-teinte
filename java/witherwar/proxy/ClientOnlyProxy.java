package witherwar.proxy;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import witherwar.WitherWar;
import witherwar.block.BlockRefHolder;
import witherwar.entity.EntityMotusGhast;
import witherwar.entity.EntitySerpentWither;

public class ClientOnlyProxy extends IProxy{
	
	public void preInit() {
		
		for( BlockRefHolder brh : WitherWar.newBlocks.values()) {
			brh.setModelResourceLocation();
		}		
	}
	
	public void init() {
		Minecraft mc = Minecraft.getMinecraft();
        mc.getRenderManager().entityRenderMap.put( EntitySerpentWither.class, new RenderWitherSkeleton( mc.getRenderManager()));
        mc.getRenderManager().entityRenderMap.put( EntityMotusGhast.class, new RenderGhast( mc.getRenderManager()));
	}
	
	public void postInit() {

	}

	@Override
	public boolean isDedicatedServer() {
		// TODO Auto-generated method stub
		return false;
	}

	
	
	

	

}