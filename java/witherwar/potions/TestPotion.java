package witherwar.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import witherwar.TEinTE;

public class TestPotion extends Potion{
	
	
//	private static final ResourceLocation resource = new ResourceLocation(TEinTE.MODID ,"textures/no_asset_default.png");

	public TestPotion() {
		super( false ,8171462); //TODO change color
		this.setPotionName( "effect.noidea");
		this.setIconIndex( 0 ,0);
//		this.setRegistryName( "noidea");
		this.setRegistryName( new ResourceLocation( TEinTE.MODID + ":noidea"));
		this.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, MathHelper.getRandomUUID().toString(), 0.10000000149011612D, 2);

	}
	
	
	
	
//	@Override
//	@SideOnly(Side.CLIENT)
//    public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) { 
//		System.out.println( "How does this get called?");
//	}

	
	
//	@Override
//	@SideOnly(Side.CLIENT)
//	public int getStatusIconIndex() {
//		Minecraft.getMinecraft().renderEngine.bindTexture( resource);
//		return super.getStatusIconIndex();
//	}
	
//    private static final ResourceLocation field_110839_f = new ResourceLocation("witherwar","textures/no_asset_default.png");
//
	@Override
	public boolean hasStatusIcon() 	{
//	    Minecraft.getMinecraft().renderEngine.bindTexture(field_110839_f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation( "witherwar:textures/no_asset_default_large.png"));
	    return true;
	}

	
	
	
}

/**
 REGISTRY.register(
 	3
 	,new ResourceLocation("haste")
 	,( new Potion(false, 14270531) )
 			.setPotionName("effect.digSpeed")
 			.setIconIndex(2, 0)
 			.setEffectiveness(1.5D)
 			.setBeneficial()
 			.registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "AF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3", 0.10000000149011612D, 2));
 			
 REGISTRY.register(
 	17
 	,new ResourceLocation("hunger")
 	,(new Potion(true, 5797459))
 			.setPotionName("effect.hunger")
 			.setIconIndex(1, 1));


**/