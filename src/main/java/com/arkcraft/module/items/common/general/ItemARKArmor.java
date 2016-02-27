package com.arkcraft.module.items.common.general;

import com.arkcraft.module.core.ARKCraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class ItemARKArmor extends ItemArmor
{

    public String texName;
    public boolean golden;

    public ItemARKArmor(ArmorMaterial mat, String texName, int type, boolean golden)
    {
        super(mat, 0, type);
        this.golden = golden;
        this.texName = texName;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, int slot, String type)
    {
        return ARKCraft.MODID + ":textures/armor/" + this.texName + "_" + (this.armorType == 2 ? "2" : "1") + ".png";
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        if (golden)
        {
            return EnumChatFormatting.GOLD + super.getItemStackDisplayName(stack);
        }
        return super.getItemStackDisplayName(stack);
    }
}
