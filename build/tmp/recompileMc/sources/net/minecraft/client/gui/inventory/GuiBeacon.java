package net.minecraft.client.gui.inventory;

import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class GuiBeacon extends GuiContainer
{
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
    private IInventory tileBeacon;
    private GuiBeacon.ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;
    private static final String __OBFID = "CL_00000739";

    public GuiBeacon(InventoryPlayer p_i45507_1_, IInventory p_i45507_2_)
    {
        super(new ContainerBeacon(p_i45507_1_, p_i45507_2_));
        this.tileBeacon = p_i45507_2_;
        this.xSize = 230;
        this.ySize = 219;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(this.beaconConfirmButton = new GuiBeacon.ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107));
        this.buttonList.add(new GuiBeacon.CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        int i = this.tileBeacon.getField(0);
        int j = this.tileBeacon.getField(1);
        int k = this.tileBeacon.getField(2);

        if (this.buttonsNotDrawn && i >= 0)
        {
            this.buttonsNotDrawn = false;
            int i1;
            int j1;
            int k1;
            int l1;
            GuiBeacon.PowerButton powerbutton;

            for (int l = 0; l <= 2; ++l)
            {
                i1 = TileEntityBeacon.effectsList[l].length;
                j1 = i1 * 22 + (i1 - 1) * 2;

                for (k1 = 0; k1 < i1; ++k1)
                {
                    l1 = TileEntityBeacon.effectsList[l][k1].id;
                    powerbutton = new GuiBeacon.PowerButton(l << 8 | l1, this.guiLeft + 76 + k1 * 24 - j1 / 2, this.guiTop + 22 + l * 25, l1, l);
                    this.buttonList.add(powerbutton);

                    if (l >= i)
                    {
                        powerbutton.enabled = false;
                    }
                    else if (l1 == j)
                    {
                        powerbutton.func_146140_b(true);
                    }
                }
            }

            byte b0 = 3;
            i1 = TileEntityBeacon.effectsList[b0].length + 1;
            j1 = i1 * 22 + (i1 - 1) * 2;

            for (k1 = 0; k1 < i1 - 1; ++k1)
            {
                l1 = TileEntityBeacon.effectsList[b0][k1].id;
                powerbutton = new GuiBeacon.PowerButton(b0 << 8 | l1, this.guiLeft + 167 + k1 * 24 - j1 / 2, this.guiTop + 47, l1, b0);
                this.buttonList.add(powerbutton);

                if (b0 >= i)
                {
                    powerbutton.enabled = false;
                }
                else if (l1 == k)
                {
                    powerbutton.func_146140_b(true);
                }
            }

            if (j > 0)
            {
                GuiBeacon.PowerButton powerbutton1 = new GuiBeacon.PowerButton(b0 << 8 | j, this.guiLeft + 167 + (i1 - 1) * 24 - j1 / 2, this.guiTop + 47, j, b0);
                this.buttonList.add(powerbutton1);

                if (b0 >= i)
                {
                    powerbutton1.enabled = false;
                }
                else if (j == k)
                {
                    powerbutton1.func_146140_b(true);
                }
            }
        }

        this.beaconConfirmButton.enabled = this.tileBeacon.getStackInSlot(0) != null && j > 0;
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == -2)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (button.id == -1)
        {
            String s = "MC|Beacon";
            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.tileBeacon.getField(1));
            packetbuffer.writeInt(this.tileBeacon.getField(2));
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s, packetbuffer));
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (button instanceof GuiBeacon.PowerButton)
        {
            if (((GuiBeacon.PowerButton)button).func_146141_c())
            {
                return;
            }

            int j = button.id;
            int k = j & 255;
            int i = j >> 8;

            if (i < 3)
            {
                this.tileBeacon.setField(1, k);
            }
            else
            {
                this.tileBeacon.setField(2, k);
            }

            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
        Iterator iterator = this.buttonList.iterator();

        while (iterator.hasNext())
        {
            GuiButton guibutton = (GuiButton)iterator.next();

            if (guibutton.isMouseOver())
            {
                guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }

        RenderHelper.enableGUIStandardItemLighting();
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(beaconGuiTextures);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        this.itemRender.zLevel = 100.0F;
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.emerald), k + 42, l + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.diamond), k + 42 + 22, l + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.gold_ingot), k + 42 + 44, l + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.iron_ingot), k + 42 + 66, l + 109);
        this.itemRender.zLevel = 0.0F;
    }

    @SideOnly(Side.CLIENT)
    static class Button extends GuiButton
        {
            private final ResourceLocation field_146145_o;
            private final int field_146144_p;
            private final int field_146143_q;
            private boolean field_146142_r;
            private static final String __OBFID = "CL_00000743";

            protected Button(int p_i1077_1_, int p_i1077_2_, int p_i1077_3_, ResourceLocation p_i1077_4_, int p_i1077_5_, int p_i1077_6_)
            {
                super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
                this.field_146145_o = p_i1077_4_;
                this.field_146144_p = p_i1077_5_;
                this.field_146143_q = p_i1077_6_;
            }

            /**
             * Draws this button to the screen.
             */
            public void drawButton(Minecraft mc, int mouseX, int mouseY)
            {
                if (this.visible)
                {
                    mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                    short short1 = 219;
                    int k = 0;

                    if (!this.enabled)
                    {
                        k += this.width * 2;
                    }
                    else if (this.field_146142_r)
                    {
                        k += this.width * 1;
                    }
                    else if (this.hovered)
                    {
                        k += this.width * 3;
                    }

                    this.drawTexturedModalRect(this.xPosition, this.yPosition, k, short1, this.width, this.height);

                    if (!GuiBeacon.beaconGuiTextures.equals(this.field_146145_o))
                    {
                        mc.getTextureManager().bindTexture(this.field_146145_o);
                    }

                    this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
                }
            }

            public boolean func_146141_c()
            {
                return this.field_146142_r;
            }

            public void func_146140_b(boolean p_146140_1_)
            {
                this.field_146142_r = p_146140_1_;
            }
        }

    @SideOnly(Side.CLIENT)
    class CancelButton extends GuiBeacon.Button
    {
        private static final String __OBFID = "CL_00000740";

        public CancelButton(int p_i1074_2_, int p_i1074_3_, int p_i1074_4_)
        {
            super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.beaconGuiTextures, 112, 220);
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }

    @SideOnly(Side.CLIENT)
    class ConfirmButton extends GuiBeacon.Button
    {
        private static final String __OBFID = "CL_00000741";

        public ConfirmButton(int p_i1075_2_, int p_i1075_3_, int p_i1075_4_)
        {
            super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.beaconGuiTextures, 90, 220);
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
            GuiBeacon.this.drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
        }
    }

    @SideOnly(Side.CLIENT)
    class PowerButton extends GuiBeacon.Button
    {
        private final int field_146149_p;
        private final int field_146148_q;
        private static final String __OBFID = "CL_00000742";

        public PowerButton(int p_i1076_2_, int p_i1076_3_, int p_i1076_4_, int p_i1076_5_, int p_i1076_6_)
        {
            super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.inventoryBackground, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
            this.field_146149_p = p_i1076_5_;
            this.field_146148_q = p_i1076_6_;
        }

        public void drawButtonForegroundLayer(int mouseX, int mouseY)
        {
            String s = I18n.format(Potion.potionTypes[this.field_146149_p].getName(), new Object[0]);

            if (this.field_146148_q >= 3 && this.field_146149_p != Potion.regeneration.id)
            {
                s = s + " II";
            }

            GuiBeacon.this.drawCreativeTabHoveringText(s, mouseX, mouseY);
        }
    }
}