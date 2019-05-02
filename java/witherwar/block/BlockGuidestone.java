package witherwar.block;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import witherwar.WitherWar;
import witherwar.network.MessageEditGuidestone;
import witherwar.tileentity.TileEntityGuidestone;
import witherwar.tileentity.TileEntityMaw;
import witherwar.util.Symbol;


public class BlockGuidestone extends Block{
	
	public BlockGuidestone() {
		super( Material.ROCK);
		setUnlocalizedName( "guidestone");
		setRegistryName( "guidestone");
		setCreativeTab( WitherWar.teinteTab);
	}

	@Override
	public boolean hasTileEntity( IBlockState state) {
		return false;
	}
/**
	@Override
	public TileEntity createTileEntity( World world ,IBlockState state) {
		return new TileEntityGuidestone();
	}**/

	
	@Override
    public EnumBlockRenderType getRenderType( IBlockState state){
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		//TileEntityGuidestone teg = (TileEntityGuidestone) worldIn.getTileEntity( pos);
		WitherWar.instance.removeFromRegionMap( pos);
		super.breakBlock( worldIn ,pos, state);
	}
	//import java.util.concurrent.ThreadLocalRandom;

	// nextInt is normally exclusive of the top value,
	// so add 1 to make it inclusive
	
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand
    		,EnumFacing facing ,float hitX ,float hitY ,float hitZ) {
    	if( !worldIn.isRemote) {
    		//TileEntityGuidestone teg = (TileEntityGuidestone) worldIn.getTileEntity( pos);
    		//String name = WitherWar.instance.getRegionName(pos);
    		int id = WitherWar.instance.getRegionID( pos);
    		if( id == -1) {
    			List<ChunkPos> map = findRegionChunks( worldIn ,pos);
    			id = ThreadLocalRandom.current().nextInt(0, 999999 + 1);
    			WitherWar.instance.addToRegionMap( id ,map);
    		}
    		String regionName = WitherWar.instance.getRegionName( id);
    		WitherWar.snwrapper.sendTo( new MessageEditGuidestone( id ,regionName) ,(EntityPlayerMP)playerIn);
    		
    	}
    	return true;
    }
    
    
    
    public List<ChunkPos> findRegionChunks( World world ,BlockPos pos) {
    	HashSet<ChunkPos> map = new HashSet<>();
    	//ChunkPos origin = new ChunkPos(pos); 
    	//map.add( origin);
    	Biome regionBiome = world.getBiome( pos);
    	
    	Symbol[] dirs = new Symbol[]{ Symbol.XP ,Symbol.ZP ,Symbol.ZN ,Symbol.XN};
    	for( Symbol s : dirs) {
    		findRegionChunks( map ,world ,pos ,s ,regionBiome);
    	}
    	
    	return new ArrayList<>(map);
    }
    
    
    private void findRegionChunks( HashSet<ChunkPos> map ,World world ,BlockPos pos ,Symbol direction ,Biome regionBiome) {
    	if( map.contains( new ChunkPos(pos))) { return;}
    	
    	if( world.getBiome( pos) == regionBiome) {
    		map.add( new ChunkPos( pos));
    	}

    	ArrayList<Symbol> arr = Symbol.compliment2D( direction);
    	arr.add( direction);
    	for( Symbol s : arr) {
    		BlockPos newpos = pos.add( s.mod);
    		findRegionChunks( map ,world ,newpos ,s ,regionBiome);
    	}    	
    }
    
    
    
    

}