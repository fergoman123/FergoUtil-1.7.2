package io.github.fergoman123.fergoutil.block;

import io.github.fergoman123.fergoutil.helper.NameHelper;
import io.github.fergoman123.fergoutil.info.BlockInfo;
import net.minecraft.block.BlockLog;
import net.minecraft.creativetab.CreativeTabs;

public class BlockLogFergo extends BlockLog
{
    private int mod;
    private BlockInfo info;

    public BlockLogFergo(int mod, CreativeTabs tabs, BlockInfo info)
    {
        super();
        this.setMod(mod);
        this.setCreativeTab(tabs);
        this.setUnlocalizedName(info.getName());
        this.info = info;
    }

    @Override
    public String getUnlocalizedName() {
        return NameHelper.formatBlockName(NameHelper.getModString(this.getMod()), NameHelper.getUnlocalizedName(super.getUnlocalizedName()));
    }

    public void setMod(int mod) {
        this.mod = mod;
    }

    public int getMod() {
        return mod;
    }
}