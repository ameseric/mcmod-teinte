package teinte.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import teinte.TEinTE;

public class TestFireball extends Item{ //TODO model, but no textures, looking into it.
	
	
	public TestFireball() {
		this.maxStackSize = 1;
		this.setCreativeTab( TEinTE.teinteTab);
		this.setRegistryName( "testfireball");
		this.setUnlocalizedName( "testfireball");
	}
	
	
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn)
    {
        ItemStack itemstack = player.getHeldItem(handIn);

        if (!player.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!world.isRemote)
        {
//            EntitySnowball entitysnowball = new EntitySnowball(world, player);
            
            EntityFireball fireball = new EntityLargeFireball(world ,player ,0.5 ,0.5 ,0.5);
//            entitysnowball.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity( fireball);
        }

        player.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }
	

}
