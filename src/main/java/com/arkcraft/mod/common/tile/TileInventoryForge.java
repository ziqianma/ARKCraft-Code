package com.arkcraft.mod.common.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.arkcraft.mod.common.items.ARKCraftItems;

/***
 * 
 * @author wildbill22
 *
 */
public class TileInventoryForge extends TileEntity implements IInventory, IUpdatePlayerListBox {
	public static final int FORGE_SLOTS_COUNT = 8;
	public static final int TOTAL_SLOTS_COUNT = FORGE_SLOTS_COUNT;
	public static final int LAST_INVENTORY_SLOT = TOTAL_SLOTS_COUNT - 1; 

	public static final int FIRST_FORGE_SLOT = 0;
    private int furnaceBurnTime;
    private int currentItemBurnTime;
    private int cookTime;
    private int totalCookTime;

	// Create and initialize the itemStacks variable that will store store the itemStacks
	private ItemStack[] itemStacks = new ItemStack[TOTAL_SLOTS_COUNT];
	

		
    public boolean isBurning()
    {
        return this.furnaceBurnTime > 0;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isBurning(IInventory p_174903_0_)
    {
        return p_174903_0_.getField(0) > 0;
    }

	@Override
    public void update()
    {
		
        boolean flag = this.isBurning();
        boolean flag1 = false;

        if (this.isBurning())
        {
            --this.furnaceBurnTime;
        }

        if (!this.worldObj.isRemote)
        {
            if (!this.isBurning() && (this.itemStacks[1] == null || this.itemStacks[0] == null))
            {
                if (!this.isBurning() && this.cookTime > 0)
                {
                    this.cookTime = MathHelper.clamp_int(this.cookTime - 2, 0, this.totalCookTime);
                }
            }
            else
            {
                if (!this.isBurning() && this.canSmelt())
                {
                    this.currentItemBurnTime = this.furnaceBurnTime = getItemBurnTime(this.itemStacks[1]);

                    if (this.isBurning())
                    {
                        flag1 = true;

                        if (this.itemStacks[1] != null)
                        {
                            --this.itemStacks[1].stackSize;

                            if (this.itemStacks[1].stackSize == 0)
                            {
                                this.itemStacks[1] = itemStacks[1].getItem().getContainerItem(itemStacks[1]);
                            }
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt())
                {
                    ++this.cookTime;

                    if (this.cookTime == this.totalCookTime)
                    {
                        this.cookTime = 0;
                        this.totalCookTime = this.func_174904_a(this.itemStacks[0]);
                        this.smeltItem();
                        flag1 = true;
                    }
                }
                else
                {
                    this.cookTime = 0;
                }
            }

            if (flag != this.isBurning())
            {
                flag1 = true;
                BlockFurnace.setState(this.isBurning(), this.worldObj, this.pos);
            }
        }

        if (flag1)
        {
            this.markDirty();
        }
    }
	
	  public int func_174904_a(ItemStack p_174904_1_)
	    {
	        return 200;
	    }

	  private boolean canSmelt()
	    {
		  	Integer firstSuitableOutputSlot = null;

			// find the first suitable output slot- either empty, or with identical item that has enough space
			for (int outputSlot = LAST_INVENTORY_SLOT; outputSlot > FIRST_FORGE_SLOT; outputSlot--) {
				ItemStack outputStack = itemStacks[outputSlot];
				// Empty slot?
				if (outputStack == null) {
					firstSuitableOutputSlot = outputSlot;
					break;
				}
				// Fertilizer item and with enough space? 
				if (itemStacks[outputSlot].getItem() == ARKCraftItems.metal) {
					int combinedSize = itemStacks[outputSlot].stackSize + 1;
					if (combinedSize <= getInventoryStackLimit() && combinedSize <= itemStacks[outputSlot].getMaxStackSize()) {
						firstSuitableOutputSlot = outputSlot;
						break;
					}
				}
			}	
		
			// If no suitable output slot, return false
			if (firstSuitableOutputSlot == null) return false;

			// alter output slot
			if (itemStacks[firstSuitableOutputSlot] == null) {
				
				ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.itemStacks[0]);
	            if (itemstack == null) return false;
	            if (this.itemStacks[2] == null) return true;
	            if (!this.itemStacks[2].isItemEqual(itemstack)) return false;
	            int result = itemStacks[2].stackSize + itemstack.stackSize;
	            return result <= getInventoryStackLimit() && result <= this.itemStacks[2].getMaxStackSize(); 
	            //Forge BugFix: Make it respect stack sizes properly.	        
			
			} else {
				itemStacks[firstSuitableOutputSlot].stackSize += 1;
			}
			markDirty();
			return true;
	            
	    }

	    /**
	     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
	     */
	    public void smeltItem()
	    {	    	
	        if (this.canSmelt())
	        {
	            ItemStack itemstack = FurnaceRecipes.instance().getSmeltingResult(this.itemStacks[0]);

	            if (this.itemStacks[2] == null)
	            {
	                this.itemStacks[2] = itemstack.copy();
	            }
	            else if (this.itemStacks[2].getItem() == itemstack.getItem())
	            {
	                this.itemStacks[2].stackSize += itemstack.stackSize; // Forge BugFix: Results may have multiple items
	            }

	            if (this.itemStacks[0].getItem() == Item.getItemFromBlock(Blocks.sponge) && this.itemStacks[0].getMetadata() == 1 && this.itemStacks[1] != null && this.itemStacks[1].getItem() == Items.bucket)
	            {
	                this.itemStacks[1] = new ItemStack(Items.water_bucket);
	            }

	            --this.itemStacks[0].stackSize;

	            if (this.itemStacks[0].stackSize <= 0)
	            {
	                this.itemStacks[0] = null;
	            }
	        }
	    }

	 public static int getItemBurnTime(ItemStack p_145952_0_)
	    {
	        if (p_145952_0_ == null)
	        {
	            return 0;
	        }
	        else
	        {
	            Item item = p_145952_0_.getItem();

	            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
	            {
	                Block block = Block.getBlockFromItem(item);

	                if (block == Blocks.wooden_slab)
	                {
	                    return 150;
	                }

	                if (block.getMaterial() == Material.wood)
	                {
	                    return 300;
	                }

	                if (block == Blocks.coal_block)
	                {
	                    return 16000;
	                }
	            }

	            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD")) return 200;
	            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD")) return 200;
	            if (item instanceof ItemHoe && ((ItemHoe)item).getMaterialName().equals("WOOD")) return 200;
	            if (item == Items.stick) return 100;
	            if (item == Items.coal) return 1600;
	            if (item == Items.lava_bucket) return 20000;
	            if (item == Item.getItemFromBlock(Blocks.sapling)) return 100;
	            if (item == Items.blaze_rod) return 2400;
	            return net.minecraftforge.fml.common.registry.GameRegistry.getFuelValue(p_145952_0_);
	        }
	    }
	 
	  public static boolean isItemFuel(ItemStack p_145954_0_)
	    {
	        /**
	         * Returns the number of ticks that the supplied fuel item will keep the furnace burning, or 0 if the item isn't
	         * fuel
	         */
	        return getItemBurnTime(p_145954_0_) > 0;
	    }

	@Override
	public String getName() {
		return "tile.forge.name";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public IChatComponent getDisplayName() {
		return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName());
	}

	@Override
	public int getSizeInventory() {
		return itemStacks.length;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return itemStacks[index];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int count) {
		ItemStack itemStackInSlot = getStackInSlot(slotIndex);
		if (itemStackInSlot == null) return null;

		ItemStack itemStackRemoved;
		if (itemStackInSlot.stackSize <= count) {
			itemStackRemoved = itemStackInSlot;
			setInventorySlotContents(slotIndex, null);
		} else {
			itemStackRemoved = itemStackInSlot.splitStack(count);
			if (itemStackInSlot.stackSize == 0) {
				setInventorySlotContents(slotIndex, null);
			}
		}
		markDirty();
		return itemStackRemoved;
	}

	
	// Nothing to do, this is a furnace type inventory
	@Override
	public ItemStack getStackInSlotOnClosing(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack itemstack) {
		itemStacks[slotIndex] = itemstack;
		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
		markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		if (this.worldObj.getTileEntity(this.pos) != this) return false;
		final double X_CENTRE_OFFSET = 0.5;
		final double Y_CENTRE_OFFSET = 0.5;
		final double Z_CENTRE_OFFSET = 0.5;
		final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
		return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
    {
        return index == 2 ? false : (index != 1 ? true : isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack));
    }

    /**
     * Called from Chunk.setBlockIDWithMetadata, determines if this tile entity should be re-created when the ID, or Metadata changes.
     * Use with caution as this will leave straggler TileEntities, or create conflicts with other TileEntities if not used properly.
     *
     * @param world Current world
     * @param pos Tile's world position
     * @param oldID The old ID of the block
     * @param newID The new ID of the block (May be the same)
     * @return True to remove the old tile entity, false to keep it in tact {and create a new one if the new values specify to}
     */
	@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate){
        return false;
    }
	
	//------------------------------

	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        NBTTagList nbttaglist = compound.getTagList("Items", 10);
        this.itemStacks = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.itemStacks.length)
            {
                this.itemStacks[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }

        this.furnaceBurnTime = compound.getShort("BurnTime");
        this.cookTime = compound.getShort("CookTime");
        this.totalCookTime = compound.getShort("CookTimeTotal");
        this.currentItemBurnTime = getItemBurnTime(this.itemStacks[1]);

    }

    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setShort("BurnTime", (short)this.furnaceBurnTime);
        compound.setShort("CookTime", (short)this.cookTime);
        compound.setShort("CookTimeTotal", (short)this.totalCookTime);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.itemStacks.length; ++i)
        {
            if (this.itemStacks[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.itemStacks[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }
    }

	// When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getDescriptionPacket() and onDataPacket() to do this
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		writeToNBT(nbtTagCompound);
		final int METADATA = 0;
		return new S35PacketUpdateTileEntity(this.pos, METADATA, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	 public int getField(int id)
	    {
	        switch (id)
	        {
	            case 0:
	                return this.furnaceBurnTime;
	            case 1:
	                return this.currentItemBurnTime;
	            case 2:
	                return this.cookTime;
	            case 3:
	                return this.totalCookTime;
	            default:
	                return 0;
	        }
	    }
		
		@Override
	    public void setField(int id, int value)
	    {
	        switch (id)
	        {
	            case 0:
	                this.furnaceBurnTime = value;
	                break;
	            case 1:
	                this.currentItemBurnTime = value;
	                break;
	            case 2:
	                this.cookTime = value;
	                break;
	            case 3:
	                this.totalCookTime = value;
	        }
	    }
		
		@Override
	    public int getFieldCount()
	    {
	        return 4;
	    }
		
	    public void clear()
	    {
	        for (int i = 0; i < this.itemStacks.length; ++i)
	        {
	            this.itemStacks[i] = null;
	        }
	    }
}
