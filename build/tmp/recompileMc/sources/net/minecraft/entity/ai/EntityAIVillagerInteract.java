package net.minecraft.entity.ai;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class EntityAIVillagerInteract extends EntityAIWatchClosest2
{
    private int field_179478_e;
    private EntityVillager field_179477_f;
    private static final String __OBFID = "CL_00002251";

    public EntityAIVillagerInteract(EntityVillager p_i45886_1_)
    {
        super(p_i45886_1_, EntityVillager.class, 3.0F, 0.02F);
        this.field_179477_f = p_i45886_1_;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();

        if (this.field_179477_f.func_175555_cq() && this.closestEntity instanceof EntityVillager && ((EntityVillager)this.closestEntity).func_175557_cr())
        {
            this.field_179478_e = 10;
        }
        else
        {
            this.field_179478_e = 0;
        }
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();

        if (this.field_179478_e > 0)
        {
            --this.field_179478_e;

            if (this.field_179478_e == 0)
            {
                InventoryBasic inventorybasic = this.field_179477_f.func_175551_co();

                for (int i = 0; i < inventorybasic.getSizeInventory(); ++i)
                {
                    ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    ItemStack itemstack1 = null;

                    if (itemstack != null)
                    {
                        Item item = itemstack.getItem();
                        int j;

                        if ((item == Items.bread || item == Items.potato || item == Items.carrot) && itemstack.stackSize > 3)
                        {
                            j = itemstack.stackSize / 2;
                            itemstack.stackSize -= j;
                            itemstack1 = new ItemStack(item, j, itemstack.getMetadata());
                        }
                        else if (item == Items.wheat && itemstack.stackSize > 5)
                        {
                            j = itemstack.stackSize / 2 / 3 * 3;
                            int k = j / 3;
                            itemstack.stackSize -= j;
                            itemstack1 = new ItemStack(Items.bread, k, 0);
                        }

                        if (itemstack.stackSize <= 0)
                        {
                            inventorybasic.setInventorySlotContents(i, (ItemStack)null);
                        }
                    }

                    if (itemstack1 != null)
                    {
                        double d0 = this.field_179477_f.posY - 0.30000001192092896D + (double)this.field_179477_f.getEyeHeight();
                        EntityItem entityitem = new EntityItem(this.field_179477_f.worldObj, this.field_179477_f.posX, d0, this.field_179477_f.posZ, itemstack1);
                        float f = 0.3F;
                        float f1 = this.field_179477_f.rotationYawHead;
                        float f2 = this.field_179477_f.rotationPitch;
                        entityitem.motionX = (double)(-MathHelper.sin(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI) * f);
                        entityitem.motionZ = (double)(MathHelper.cos(f1 / 180.0F * (float)Math.PI) * MathHelper.cos(f2 / 180.0F * (float)Math.PI) * f);
                        entityitem.motionY = (double)(-MathHelper.sin(f2 / 180.0F * (float)Math.PI) * f + 0.1F);
                        entityitem.setDefaultPickupDelay();
                        this.field_179477_f.worldObj.spawnEntityInWorld(entityitem);
                        break;
                    }
                }
            }
        }
    }
}