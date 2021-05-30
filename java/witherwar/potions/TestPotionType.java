package witherwar.potions;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;

public class TestPotionType extends PotionType{

	public TestPotionType( Potion potion) {
		super( "noidea" ,new PotionEffect[] {new PotionEffect( potion ,3600)});
		this.setRegistryName( "noidea");
	}
	
}
