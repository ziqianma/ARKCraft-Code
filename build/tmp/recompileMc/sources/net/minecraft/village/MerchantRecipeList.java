package net.minecraft.village;

import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MerchantRecipeList extends ArrayList
{
    private static final String __OBFID = "CL_00000127";

    public MerchantRecipeList() {}

    public MerchantRecipeList(NBTTagCompound p_i1944_1_)
    {
        this.readRecipiesFromTags(p_i1944_1_);
    }

    /**
     * can par1,par2 be used to in crafting recipe par3
     */
    public MerchantRecipe canRecipeBeUsed(ItemStack p_77203_1_, ItemStack p_77203_2_, int p_77203_3_)
    {
        if (p_77203_3_ > 0 && p_77203_3_ < this.size())
        {
            MerchantRecipe merchantrecipe1 = (MerchantRecipe)this.get(p_77203_3_);
            return ItemStack.areItemsEqual(p_77203_1_, merchantrecipe1.getItemToBuy()) && (p_77203_2_ == null && !merchantrecipe1.hasSecondItemToBuy() || merchantrecipe1.hasSecondItemToBuy() && ItemStack.areItemsEqual(p_77203_2_, merchantrecipe1.getSecondItemToBuy())) && p_77203_1_.stackSize >= merchantrecipe1.getItemToBuy().stackSize && (!merchantrecipe1.hasSecondItemToBuy() || p_77203_2_.stackSize >= merchantrecipe1.getSecondItemToBuy().stackSize) ? merchantrecipe1 : null;
        }
        else
        {
            for (int j = 0; j < this.size(); ++j)
            {
                MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(j);

                if (ItemStack.areItemsEqual(p_77203_1_, merchantrecipe.getItemToBuy()) && p_77203_1_.stackSize >= merchantrecipe.getItemToBuy().stackSize && (!merchantrecipe.hasSecondItemToBuy() && p_77203_2_ == null || merchantrecipe.hasSecondItemToBuy() && ItemStack.areItemsEqual(p_77203_2_, merchantrecipe.getSecondItemToBuy()) && p_77203_2_.stackSize >= merchantrecipe.getSecondItemToBuy().stackSize))
                {
                    return merchantrecipe;
                }
            }

            return null;
        }
    }

    public void writeToBuf(PacketBuffer p_151391_1_)
    {
        p_151391_1_.writeByte((byte)(this.size() & 255));

        for (int i = 0; i < this.size(); ++i)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            p_151391_1_.writeItemStackToBuffer(merchantrecipe.getItemToBuy());
            p_151391_1_.writeItemStackToBuffer(merchantrecipe.getItemToSell());
            ItemStack itemstack = merchantrecipe.getSecondItemToBuy();
            p_151391_1_.writeBoolean(itemstack != null);

            if (itemstack != null)
            {
                p_151391_1_.writeItemStackToBuffer(itemstack);
            }

            p_151391_1_.writeBoolean(merchantrecipe.isRecipeDisabled());
            p_151391_1_.writeInt(merchantrecipe.getToolUses());
            p_151391_1_.writeInt(merchantrecipe.getMaxTradeUses());
        }
    }

    public void readRecipiesFromTags(NBTTagCompound p_77201_1_)
    {
        NBTTagList nbttaglist = p_77201_1_.getTagList("Recipes", 10);

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            this.add(new MerchantRecipe(nbttagcompound1));
        }
    }

    public NBTTagCompound getRecipiesAsTags()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.size(); ++i)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)this.get(i);
            nbttaglist.appendTag(merchantrecipe.writeToTags());
        }

        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }

    @SideOnly(Side.CLIENT)
    public static MerchantRecipeList readFromBuf(PacketBuffer p_151390_0_) throws IOException
    {
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        int i = p_151390_0_.readByte() & 255;

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = p_151390_0_.readItemStackFromBuffer();
            ItemStack itemstack1 = p_151390_0_.readItemStackFromBuffer();
            ItemStack itemstack2 = null;

            if (p_151390_0_.readBoolean())
            {
                itemstack2 = p_151390_0_.readItemStackFromBuffer();
            }

            boolean flag = p_151390_0_.readBoolean();
            int k = p_151390_0_.readInt();
            int l = p_151390_0_.readInt();
            MerchantRecipe merchantrecipe = new MerchantRecipe(itemstack, itemstack2, itemstack1, k, l);

            if (flag)
            {
                merchantrecipe.func_82785_h();
            }

            merchantrecipelist.add(merchantrecipe);
        }

        return merchantrecipelist;
    }
}