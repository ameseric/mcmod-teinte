package teinte.tileentity;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;

public class CustomMobSpawner extends MobSpawnerBaseLogic{
	
	//private final List< WeightedSpawnerEntity>
	
	private int maxSpawnDelay = 400;
	private int spawnCount = 8;
	private int spawnRange = 20;
	private int activatingRangeFromPlayer = 24;
	private int spawnDelay = 20;
	private double prevMobRotation;
	private double mobRotation;
	private int minSpawnDelay = 200;
    private final List<WeightedSpawnerEntity> potentialSpawns = Lists.<WeightedSpawnerEntity>newArrayList();
    private WeightedSpawnerEntity spawnData;// = new WeightedSpawnerEntity();
	private int maxNearbyEntities = 8;
	
	private World world;
	private BlockPos pos;
	private Block block;
	

	
	public CustomMobSpawner( String mobType ,World world ,BlockPos pos ,Block block) {
		NBTTagCompound nbt =  new NBTTagCompound();
		nbt.setString("id", mobType);
        this.spawnData = new WeightedSpawnerEntity( 1 ,nbt);

        this.world = world;
        this.pos = pos;
        this.block = block;
	}
	
	

	@Override
	public void broadcastEvent(int id) {
    	this.world.addBlockEvent( this.pos ,this.block ,id ,0);		
	}

	@Override
	public World getSpawnerWorld() {
		return this.world;
	}

	@Override
	public BlockPos getSpawnerPosition() {
		return this.pos;
	}
	
	@Override
	public void setNextSpawnData( WeightedSpawnerEntity p_184993_1_) {
        super.setNextSpawnData(p_184993_1_);

        if (this.getSpawnerWorld() != null) 
        {
            IBlockState iblockstate = this.getSpawnerWorld().getBlockState(this.getSpawnerPosition());
            this.getSpawnerWorld().notifyBlockUpdate( this.pos, iblockstate, iblockstate, 4);
        }
	}
	
    private boolean isActivated()
    {
        BlockPos blockpos = this.getSpawnerPosition();
        System.out.println( "---------------------> " + this.getSpawnerWorld() );
        return this.getSpawnerWorld().isAnyPlayerWithinRangeAt((double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D, (double)this.activatingRangeFromPlayer);
    }
    
    private void resetTimer()
    {
        if (this.maxSpawnDelay <= this.minSpawnDelay)
        {
            this.spawnDelay = this.minSpawnDelay;
        }
        else
        {
            int i = this.maxSpawnDelay - this.minSpawnDelay;
            this.spawnDelay = this.minSpawnDelay + this.getSpawnerWorld().rand.nextInt(i);
        }

        if (!this.potentialSpawns.isEmpty())
        {
            this.setNextSpawnData((WeightedSpawnerEntity)WeightedRandom.getRandomItem(this.getSpawnerWorld().rand, this.potentialSpawns));
        }

        this.broadcastEvent(1);
    }
	
	
	@Override
    public void updateSpawner()
    {
        if (!this.isActivated())
        {
            this.prevMobRotation = this.mobRotation;
        }
        else
        {
            BlockPos blockpos = this.getSpawnerPosition();

            if (this.getSpawnerWorld().isRemote)
            {

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                }

                this.prevMobRotation = this.mobRotation;
                this.mobRotation = (this.mobRotation + (double)(1000.0F / ((float)this.spawnDelay + 200.0F))) % 360.0D;
            }
            else
            {
                if (this.spawnDelay == -1)
                {
                    this.resetTimer();
                }

                if (this.spawnDelay > 0)
                {
                    --this.spawnDelay;
                    return;
                }

                boolean flag = false;

                for (int i = 0; i < this.spawnCount; ++i)
                {
                    NBTTagCompound nbttagcompound = this.spawnData.getNbt();
                    NBTTagList nbttaglist = nbttagcompound.getTagList("Pos", 6);
                    World world = this.getSpawnerWorld();
                    int j = nbttaglist.tagCount();
                    double d0 = j >= 1 ? nbttaglist.getDoubleAt(0) : (double)blockpos.getX() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    double d1 = j >= 2 ? nbttaglist.getDoubleAt(1) : (double)(blockpos.getY() + world.rand.nextInt(3) - 1);
                    double d2 = j >= 3 ? nbttaglist.getDoubleAt(2) : (double)blockpos.getZ() + (world.rand.nextDouble() - world.rand.nextDouble()) * (double)this.spawnRange + 0.5D;
                    Entity entity = AnvilChunkLoader.readWorldEntityPos(nbttagcompound, world, d0, d1, d2, false);

                    if (entity == null)
                    {
                        return;
                    }

                    int k = world.getEntitiesWithinAABB(entity.getClass(), (new AxisAlignedBB((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), (double)(blockpos.getX() + 1), (double)(blockpos.getY() + 1), (double)(blockpos.getZ() + 1))).grow((double)this.spawnRange)).size();

                    if (k >= this.maxNearbyEntities)
                    {
                        this.resetTimer();
                        return;
                    }

                    EntityLiving entityliving = entity instanceof EntityLiving ? (EntityLiving)entity : null;
                    entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, world.rand.nextFloat() * 360.0F, 0.0F);

                    if (entityliving == null || net.minecraftforge.event.ForgeEventFactory.canEntitySpawnSpawner(entityliving, getSpawnerWorld(), (float)entity.posX, (float)entity.posY, (float)entity.posZ, this))
                    {
                        if (this.spawnData.getNbt().getSize() == 1 && this.spawnData.getNbt().hasKey("id", 8) && entity instanceof EntityLiving)
                        {
                            if (!net.minecraftforge.event.ForgeEventFactory.doSpecialSpawn(entityliving, this.getSpawnerWorld(), (float)entity.posX, (float)entity.posY, (float)entity.posZ, this))
                            ((EntityLiving)entity).onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entity)), (IEntityLivingData)null);
                        }

                        AnvilChunkLoader.spawnEntity(entity, world);
                        world.playEvent(2004, blockpos, 0);


                        flag = true;
                    }
                }

                if (flag)
                {
                    this.resetTimer();
                }
            }
        }
    }
	

	
}