package witherwar.block;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;


/*
 * Aggregate class for performing a number of standard registry actions on new block types.
 * 
 * registerBlock() and setModelResourceLocation() are split and called separately due to the structure of Forge
 * Both must be called in preInit(), to be available for init() usage.
 * setModel...() must be callled client-side only, and is so relegated to the client-side proxy.
 * registerBlock() has no limitations, and is thus called in the master class.
 * 
 */
public class BlockRefHolder{
	public Block block;
	public ItemBlock item;
	public String modelResourceLocation;
	
	
	public BlockRefHolder( Block block ,String modelResourceLocation) {
		this.block = block;
		this.modelResourceLocation = modelResourceLocation;
	}
	
	//registry data setup, client and server need this, so common.
	public void registerBlock() {
		ForgeRegistries.BLOCKS.register( this.block);
		this.item = new ItemBlock( this.block);
		this.item.setRegistryName( this.block.getRegistryName());
		ForgeRegistries.ITEMS.register( this.item);
	}
	
	//model-texture setup, only client needs to do this
	public void setModelResourceLocation(){
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation( this.modelResourceLocation ,"inventory");
		ModelLoader.setCustomModelResourceLocation( this.item ,DEFAULT_ITEM_SUBTYPE ,itemModelResourceLocation);
	}

}


