package io.github.fergoman123.fergoutil.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.*;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlockStairFergo extends BlockFergo
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum HALF = PropertyEnum.create("half", BlockStairFergo.EnumHalf.class);
    public static final PropertyEnum SHAPE = PropertyEnum.create("shape", BlockStairFergo.EnumShape.class);
    private static final int[][] field_150150_a = new int[][] {{4, 5}, {5, 7}, {6, 7}, {4, 6}, {0, 1}, {1, 3}, {2, 3}, {0, 2}};

    public final Block modelBlock;
    public final IBlockState modelState;
    public boolean hasRaytraced;
    public int rayTracePass;

    public BlockStairFergo(IBlockState modelState, int mod, CreativeTabs tab, String name) {
        super(modelState.getBlock().getMaterial(), mod, tab, 2.0f, 5.0f, name);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockStairFergo.EnumHalf.BOTTOM).withProperty(SHAPE, BlockStairFergo.EnumShape.STRAIGHT));
        this.modelBlock = modelState.getBlock();
        this.modelState = modelState;
        this.setStepSound(this.modelBlock.stepSound);
        this.setLightOpacity(255);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
    {
        if (this.hasRaytraced)
        {
            this.setBlockBounds(0.5F * (float)(this.rayTracePass % 2), 0.5F * (float)(this.rayTracePass / 4 % 2), 0.5F * (float)(this.rayTracePass / 2 % 2), 0.5F + 0.5F * (float)(this.rayTracePass % 2), 0.5F + 0.5F * (float)(this.rayTracePass / 4 % 2), 0.5F + 0.5F * (float)(this.rayTracePass / 2 % 2));
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean isFullCube()
    {
        return false;
    }

    public void setBaseCollisionBounds(IBlockAccess worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getValue(HALF) == BlockStairFergo.EnumHalf.TOP)
        {
            this.setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
    }

    public static boolean isBlockStairs(Block block)
    {
        return block instanceof BlockStairFergo;
    }

    public static boolean isSameStair(IBlockAccess worldIn, BlockPos pos, IBlockState state)
    {
        IBlockState iblockstate1 = worldIn.getBlockState(pos);
        Block block = iblockstate1.getBlock();
        return isBlockStairs(block) && iblockstate1.getValue(HALF) == state.getValue(HALF) && iblockstate1.getValue(FACING) == state.getValue(FACING);
    }

    public int func_176307_f(IBlockAccess p_176307_1_, BlockPos p_176307_2_)
    {
        IBlockState iblockstate = p_176307_1_.getBlockState(p_176307_2_);
        EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
        BlockStairFergo.EnumHalf enumhalf = (BlockStairFergo.EnumHalf)iblockstate.getValue(HALF);
        boolean flag = enumhalf == BlockStairFergo.EnumHalf.TOP;
        IBlockState iblockstate1;
        Block block;
        EnumFacing enumfacing1;

        if (enumfacing == EnumFacing.EAST)
        {
            iblockstate1 = p_176307_1_.getBlockState(p_176307_2_.east());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176307_1_, p_176307_2_.south(), iblockstate))
                {
                    return flag ? 1 : 2;
                }

                if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176307_1_, p_176307_2_.north(), iblockstate))
                {
                    return flag ? 2 : 1;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST)
        {
            iblockstate1 = p_176307_1_.getBlockState(p_176307_2_.west());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176307_1_, p_176307_2_.south(), iblockstate))
                {
                    return flag ? 2 : 1;
                }

                if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176307_1_, p_176307_2_.north(), iblockstate))
                {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH)
        {
            iblockstate1 = p_176307_1_.getBlockState(p_176307_2_.south());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176307_1_, p_176307_2_.east(), iblockstate))
                {
                    return flag ? 2 : 1;
                }

                if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176307_1_, p_176307_2_.west(), iblockstate))
                {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH)
        {
            iblockstate1 = p_176307_1_.getBlockState(p_176307_2_.north());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176307_1_, p_176307_2_.east(), iblockstate))
                {
                    return flag ? 1 : 2;
                }

                if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176307_1_, p_176307_2_.west(), iblockstate))
                {
                    return flag ? 2 : 1;
                }
            }
        }

        return 0;
    }

    public int func_176305_g(IBlockAccess p_176305_1_, BlockPos p_176305_2_)
    {
        IBlockState iblockstate = p_176305_1_.getBlockState(p_176305_2_);
        EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
        BlockStairFergo.EnumHalf enumhalf = (BlockStairFergo.EnumHalf)iblockstate.getValue(HALF);
        boolean flag = enumhalf == BlockStairFergo.EnumHalf.TOP;
        IBlockState iblockstate1;
        Block block;
        EnumFacing enumfacing1;

        if (enumfacing == EnumFacing.EAST)
        {
            iblockstate1 = p_176305_1_.getBlockState(p_176305_2_.west());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176305_1_, p_176305_2_.north(), iblockstate))
                {
                    return flag ? 1 : 2;
                }

                if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176305_1_, p_176305_2_.south(), iblockstate))
                {
                    return flag ? 2 : 1;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST)
        {
            iblockstate1 = p_176305_1_.getBlockState(p_176305_2_.east());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176305_1_, p_176305_2_.north(), iblockstate))
                {
                    return flag ? 2 : 1;
                }

                if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176305_1_, p_176305_2_.south(), iblockstate))
                {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH)
        {
            iblockstate1 = p_176305_1_.getBlockState(p_176305_2_.north());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176305_1_, p_176305_2_.west(), iblockstate))
                {
                    return flag ? 2 : 1;
                }

                if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176305_1_, p_176305_2_.east(), iblockstate))
                {
                    return flag ? 1 : 2;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH)
        {
            iblockstate1 = p_176305_1_.getBlockState(p_176305_2_.south());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176305_1_, p_176305_2_.west(), iblockstate))
                {
                    return flag ? 1 : 2;
                }

                if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176305_1_, p_176305_2_.east(), iblockstate))
                {
                    return flag ? 2 : 1;
                }
            }
        }

        return 0;
    }

    public boolean func_176306_h(IBlockAccess p_176306_1_, BlockPos p_176306_2_)
    {
        IBlockState iblockstate = p_176306_1_.getBlockState(p_176306_2_);
        EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
        BlockStairFergo.EnumHalf enumhalf = (BlockStairFergo.EnumHalf)iblockstate.getValue(HALF);
        boolean flag = enumhalf == BlockStairFergo.EnumHalf.TOP;
        float f = 0.5F;
        float f1 = 1.0F;

        if (flag)
        {
            f = 0.0F;
            f1 = 0.5F;
        }

        float f2 = 0.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float f5 = 0.5F;
        boolean flag1 = true;
        IBlockState iblockstate1;
        Block block;
        EnumFacing enumfacing1;

        if (enumfacing == EnumFacing.EAST)
        {
            f2 = 0.5F;
            f5 = 1.0F;
            iblockstate1 = p_176306_1_.getBlockState(p_176306_2_.east());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176306_1_, p_176306_2_.south(), iblockstate))
                {
                    f5 = 0.5F;
                    flag1 = false;
                }
                else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176306_1_, p_176306_2_.north(), iblockstate))
                {
                    f4 = 0.5F;
                    flag1 = false;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST)
        {
            f3 = 0.5F;
            f5 = 1.0F;
            iblockstate1 = p_176306_1_.getBlockState(p_176306_2_.west());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176306_1_, p_176306_2_.south(), iblockstate))
                {
                    f5 = 0.5F;
                    flag1 = false;
                }
                else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176306_1_, p_176306_2_.north(), iblockstate))
                {
                    f4 = 0.5F;
                    flag1 = false;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH)
        {
            f4 = 0.5F;
            f5 = 1.0F;
            iblockstate1 = p_176306_1_.getBlockState(p_176306_2_.south());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176306_1_, p_176306_2_.east(), iblockstate))
                {
                    f3 = 0.5F;
                    flag1 = false;
                }
                else if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176306_1_, p_176306_2_.west(), iblockstate))
                {
                    f2 = 0.5F;
                    flag1 = false;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH)
        {
            iblockstate1 = p_176306_1_.getBlockState(p_176306_2_.north());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176306_1_, p_176306_2_.east(), iblockstate))
                {
                    f3 = 0.5F;
                    flag1 = false;
                }
                else if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176306_1_, p_176306_2_.west(), iblockstate))
                {
                    f2 = 0.5F;
                    flag1 = false;
                }
            }
        }

        this.setBlockBounds(f2, f, f4, f3, f1, f5);
        return flag1;
    }

    public boolean func_176304_i(IBlockAccess p_176304_1_, BlockPos p_176304_2_)
    {
        IBlockState iblockstate = p_176304_1_.getBlockState(p_176304_2_);
        EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
        BlockStairFergo.EnumHalf enumhalf = (BlockStairFergo.EnumHalf)iblockstate.getValue(HALF);
        boolean flag = enumhalf == BlockStairFergo.EnumHalf.TOP;
        float f = 0.5F;
        float f1 = 1.0F;

        if (flag)
        {
            f = 0.0F;
            f1 = 0.5F;
        }

        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = 0.5F;
        float f5 = 1.0F;
        boolean flag1 = false;
        IBlockState iblockstate1;
        Block block;
        EnumFacing enumfacing1;

        if (enumfacing == EnumFacing.EAST)
        {
            iblockstate1 = p_176304_1_.getBlockState(p_176304_2_.west());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176304_1_, p_176304_2_.north(), iblockstate))
                {
                    f4 = 0.0F;
                    f5 = 0.5F;
                    flag1 = true;
                }
                else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176304_1_, p_176304_2_.south(), iblockstate))
                {
                    f4 = 0.5F;
                    f5 = 1.0F;
                    flag1 = true;
                }
            }
        }
        else if (enumfacing == EnumFacing.WEST)
        {
            iblockstate1 = p_176304_1_.getBlockState(p_176304_2_.east());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                f2 = 0.5F;
                f3 = 1.0F;
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.NORTH && !isSameStair(p_176304_1_, p_176304_2_.north(), iblockstate))
                {
                    f4 = 0.0F;
                    f5 = 0.5F;
                    flag1 = true;
                }
                else if (enumfacing1 == EnumFacing.SOUTH && !isSameStair(p_176304_1_, p_176304_2_.south(), iblockstate))
                {
                    f4 = 0.5F;
                    f5 = 1.0F;
                    flag1 = true;
                }
            }
        }
        else if (enumfacing == EnumFacing.SOUTH)
        {
            iblockstate1 = p_176304_1_.getBlockState(p_176304_2_.north());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                f4 = 0.0F;
                f5 = 0.5F;
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176304_1_, p_176304_2_.west(), iblockstate))
                {
                    flag1 = true;
                }
                else if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176304_1_, p_176304_2_.east(), iblockstate))
                {
                    f2 = 0.5F;
                    f3 = 1.0F;
                    flag1 = true;
                }
            }
        }
        else if (enumfacing == EnumFacing.NORTH)
        {
            iblockstate1 = p_176304_1_.getBlockState(p_176304_2_.south());
            block = iblockstate1.getBlock();

            if (isBlockStairs(block) && enumhalf == iblockstate1.getValue(HALF))
            {
                enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);

                if (enumfacing1 == EnumFacing.WEST && !isSameStair(p_176304_1_, p_176304_2_.west(), iblockstate))
                {
                    flag1 = true;
                }
                else if (enumfacing1 == EnumFacing.EAST && !isSameStair(p_176304_1_, p_176304_2_.east(), iblockstate))
                {
                    f2 = 0.5F;
                    f3 = 1.0F;
                    flag1 = true;
                }
            }
        }

        if (flag1)
        {
            this.setBlockBounds(f2, f, f4, f3, f1, f5);
        }

        return flag1;
    }

    public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List list, Entity collidingEntity)
    {
        this.setBaseCollisionBounds(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        boolean flag = this.func_176306_h(worldIn, pos);
        super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);

        if (flag && this.func_176304_i(worldIn, pos))
        {
            super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
        }

        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn)
    {
        this.modelBlock.onBlockClicked(worldIn, pos, playerIn);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.modelBlock.randomDisplayTick(worldIn, pos, state, rand);
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        this.modelBlock.onBlockDestroyedByPlayer(worldIn, pos, state);
    }

    @SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess worldIn, BlockPos pos)
    {
        return this.modelBlock.getMixedBrightnessForBlock(worldIn, pos);
    }

    public float getExplosionResistance(Entity exploder)
    {
        return this.modelBlock.getExplosionResistance(exploder);
    }

    public int tickRate(World worldIn)
    {
        return this.modelBlock.tickRate(worldIn);
    }

    public Vec3 modifyAcceleration(World worldIn, BlockPos pos, Entity entityIn, Vec3 motion)
    {
        return this.modelBlock.modifyAcceleration(worldIn, pos, entityIn, motion);
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return this.modelBlock.getBlockLayer();
    }

    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
    {
        return this.modelBlock.getSelectedBoundingBox(worldIn, pos);
    }

    public boolean isCollidable()
    {
        return this.modelBlock.isCollidable();
    }

    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid)
    {
        return this.modelBlock.canCollideCheck(state, hitIfLiquid);
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        return this.modelBlock.canPlaceBlockAt(worldIn, pos);
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        this.onNeighborBlockChange(worldIn, pos, this.modelState, Blocks.air);
        this.modelBlock.onBlockAdded(worldIn, pos, this.modelState);
    }

    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        this.modelBlock.breakBlock(worldIn, pos, this.modelState);
    }

    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn)
    {
        this.modelBlock.onEntityCollidedWithBlock(worldIn, pos, entityIn);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.modelBlock.updateTick(worldIn, pos, state, rand);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        return this.modelBlock.onBlockActivated(worldIn, pos, this.modelState, playerIn, EnumFacing.DOWN, 0.0F, 0.0F, 0.0F);
    }

    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn)
    {
        this.modelBlock.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
    }

    public MapColor getMapColor(IBlockState state)
    {
        return this.modelBlock.getMapColor(this.modelState);
    }

    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        IBlockState iblockstate = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing()).withProperty(SHAPE, BlockStairFergo.EnumShape.STRAIGHT);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || (double)hitY <= 0.5D) ? iblockstate.withProperty(HALF, BlockStairFergo.EnumHalf.BOTTOM) : iblockstate.withProperty(HALF, BlockStairFergo.EnumHalf.TOP);
    }

    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
    {
        MovingObjectPosition[] amovingobjectposition = new MovingObjectPosition[8];
        IBlockState iblockstate = worldIn.getBlockState(pos);
        int i = ((EnumFacing)iblockstate.getValue(FACING)).getHorizontalIndex();
        boolean flag = iblockstate.getValue(HALF) == BlockStairFergo.EnumHalf.TOP;
        int[] aint = field_150150_a[i + (flag?4:0)];
        this.hasRaytraced = true;

        for (int j = 0; j < 8; ++j)
        {
            this.rayTracePass = j;

            if (Arrays.binarySearch(aint, j) < 0)
            {
                amovingobjectposition[j] = super.collisionRayTrace(worldIn, pos, start, end);
            }
        }

        int[] aint1 = aint;
        int k = aint.length;

        for (int l = 0; l < k; ++l)
        {
            int i1 = aint1[l];
            amovingobjectposition[i1] = null;
        }

        MovingObjectPosition movingobjectposition1 = null;
        double d1 = 0.0D;
        MovingObjectPosition[] amovingobjectposition1 = amovingobjectposition;
        int j1 = amovingobjectposition.length;

        for (int k1 = 0; k1 < j1; ++k1)
        {
            MovingObjectPosition movingobjectposition = amovingobjectposition1[k1];

            if (movingobjectposition != null)
            {
                double d0 = movingobjectposition.hitVec.squareDistanceTo(end);

                if (d0 > d1)
                {
                    movingobjectposition1 = movingobjectposition;
                    d1 = d0;
                }
            }
        }

        return movingobjectposition1;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        IBlockState iblockstate = this.getDefaultState().withProperty(HALF, (meta & 4) > 0 ? BlockStairFergo.EnumHalf.TOP : BlockStairFergo.EnumHalf.BOTTOM);
        iblockstate = iblockstate.withProperty(FACING, EnumFacing.getFront(5 - (meta & 3)));
        return iblockstate;
    }

    public int getMetaFromState(IBlockState state)
    {
        int i = 0;

        if (state.getValue(HALF) == BlockStairFergo.EnumHalf.TOP)
        {
            i |= 4;
        }

        i |= 5 - ((EnumFacing)state.getValue(FACING)).getIndex();
        return i;
    }

    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        if (this.func_176306_h(worldIn, pos))
        {
            switch (this.func_176305_g(worldIn, pos))
            {
                case 0:
                    state = state.withProperty(SHAPE, BlockStairFergo.EnumShape.STRAIGHT);
                    break;
                case 1:
                    state = state.withProperty(SHAPE, BlockStairFergo.EnumShape.INNER_RIGHT);
                    break;
                case 2:
                    state = state.withProperty(SHAPE, BlockStairFergo.EnumShape.INNER_LEFT);
            }
        }
        else
        {
            switch (this.func_176307_f(worldIn, pos))
            {
                case 0:
                    state = state.withProperty(SHAPE, BlockStairFergo.EnumShape.STRAIGHT);
                    break;
                case 1:
                    state = state.withProperty(SHAPE, BlockStairFergo.EnumShape.OUTER_RIGHT);
                    break;
                case 2:
                    state = state.withProperty(SHAPE, BlockStairFergo.EnumShape.OUTER_LEFT);
            }
        }

        return state;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, HALF, SHAPE});
    }

    public static enum EnumHalf implements IStringSerializable
    {
        TOP("top"),
        BOTTOM("bottom");
        private final String name;

        private EnumHalf(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }

    public static enum EnumShape implements IStringSerializable
    {
        STRAIGHT("straight"),
        INNER_LEFT("inner_left"),
        INNER_RIGHT("inner_right"),
        OUTER_LEFT("outer_left"),
        OUTER_RIGHT("outer_right");
        private final String name;

        private EnumShape(String name)
        {
            this.name = name;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
    }
}