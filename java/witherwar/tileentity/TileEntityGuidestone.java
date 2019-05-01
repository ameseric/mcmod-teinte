package witherwar.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import witherwar.WitherWar;
import witherwar.network.MessageEditGuidestone;


public class TileEntityGuidestone extends TileEntity{
	public String regionName = "";
	public List<ChunkPos> map = new ArrayList<ChunkPos>();
	

	public TileEntityGuidestone() {}
	

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	super.writeToNBT(compound);
    	return compound;
    }
    
    
    
    public void readFromNBT(NBTTagCompound compound) {
    	super.readFromNBT(compound);
    }
    
    
   
    
    //on the tile entity being loaded, access and modify the WitherWar Regional dictionary
    //method goes here


	public void findRegionChunks() {
		System.out.println( "Finding region chunks...");
		this.map.add( new ChunkPos( this.pos.getX()%16 ,this.pos.getZ()%16));
	}
	
	public boolean hasRegionChunks() {
		System.out.println( "Checking region chunks...");
		return ( this.map.size() > 0 );
	}
	
	public void setRegionName( String regionName) {
		this.regionName = regionName;
	}
	
	
	public void update( String regionName) {
		this.setRegionName(regionName);
		WitherWar.instance.setRegionMap( this);
	}
	
	
	public void onBlockActivated( World world ,BlockPos pos ,EntityPlayer player) {
		if( !world.isRemote) {
			if( !hasRegionChunks()) {
				findRegionChunks();
			}
			WitherWar.snwrapper.sendTo( new MessageEditGuidestone( this.regionName ,pos.getX() ,pos.getY() ,pos.getZ()) ,(EntityPlayerMP)player);
		}		
	}
	
	
	
}
