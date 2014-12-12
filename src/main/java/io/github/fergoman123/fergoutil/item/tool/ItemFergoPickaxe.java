package io.github.fergoman123.fergoutil.item.tool;

import io.github.fergoman123.fergoutil.helper.NameHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemFergoPickaxe extends ItemPickaxe
{
    public int mod;

    public ItemFergoPickaxe(ToolMaterial material, int mod, String itemName, CreativeTabs tab)
    {
        super(material);
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(tab);
        this.mod = mod;
    }

    public String getUnlocalizedName()
    {
        return String.format("item.%s%s", getModString(this.mod), NameHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        return String.format("item.%s%s", getModString(this.mod), NameHelper.getUnwrappedUnlocalizedName(super.getUnlocalizedName(stack)));
    }

    public void registerIcons(IIconRegister register)
    {
        itemIcon = register.registerIcon(String.format("%s", NameHelper.getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b)
    {
        list.add("Durability: " + NameHelper.getDurabilityString(stack));
    }

    public String getModString(int mod)
    {
        if (mod == 0)
        {
            return "FergoTools:";
        }
        else if (mod == 1)
        {
            return "MSB:";
        }
        else
        {
            return "null:";
        }
    }
}
