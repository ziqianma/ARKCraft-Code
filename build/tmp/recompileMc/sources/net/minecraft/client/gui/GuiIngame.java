package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.border.WorldBorder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiIngame extends Gui
{
    protected static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    protected static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    protected static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
    protected final Random rand = new Random();
    protected final Minecraft mc;
    protected final RenderItem itemRenderer;
    /** ChatGUI instance that retains all previous chat data */
    protected final GuiNewChat persistantChatGUI;
    protected final GuiStreamIndicator streamIndicator;
    protected int updateCounter;
    /** The string specifying which record music is playing */
    protected String recordPlaying = "";
    /** How many ticks the record playing message will be displayed */
    protected int recordPlayingUpFor;
    protected boolean recordIsPlaying;
    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    public float prevVignetteBrightness = 1.0F;
    /** Remaining ticks the item highlight should be visible */
    protected int remainingHighlightTicks;
    /** The ItemStack that is currently being highlighted */
    protected ItemStack highlightingItemStack;
    protected final GuiOverlayDebug overlayDebug;
    /** The spectator GUI for this in-game GUI instance */
    protected final GuiSpectator spectatorGui;
    protected final GuiPlayerTabOverlay overlayPlayerList;
    protected int field_175195_w;
    protected String field_175201_x = "";
    protected String field_175200_y = "";
    protected int field_175199_z;
    protected int field_175192_A;
    protected int field_175193_B;
    protected int field_175194_C = 0;
    protected int field_175189_D = 0;
    /** The last recorded system time */
    protected long lastSystemTime = 0L;
    protected long field_175191_F = 0L;
    private static final String __OBFID = "CL_00000661";

    public GuiIngame(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.itemRenderer = mcIn.getRenderItem();
        this.overlayDebug = new GuiOverlayDebug(mcIn);
        this.spectatorGui = new GuiSpectator(mcIn);
        this.persistantChatGUI = new GuiNewChat(mcIn);
        this.streamIndicator = new GuiStreamIndicator(mcIn);
        this.overlayPlayerList = new GuiPlayerTabOverlay(mcIn, this);
        this.func_175177_a();
    }

    public void func_175177_a()
    {
        this.field_175199_z = 10;
        this.field_175192_A = 70;
        this.field_175193_B = 20;
    }

    public void renderGameOverlay(float partialTicks)
    {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        this.mc.entityRenderer.setupOverlayRendering();
        GlStateManager.enableBlend();

        if (Minecraft.isFancyGraphicsEnabled())
        {
            this.func_180480_a(this.mc.thePlayer.getBrightness(partialTicks), scaledresolution);
        }
        else
        {
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        }

        ItemStack itemstack = this.mc.thePlayer.inventory.armorItemInSlot(3);

        if (this.mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
        {
            this.func_180476_e(scaledresolution);
        }

        if (!this.mc.thePlayer.isPotionActive(Potion.confusion))
        {
            float f1 = this.mc.thePlayer.prevTimeInPortal + (this.mc.thePlayer.timeInPortal - this.mc.thePlayer.prevTimeInPortal) * partialTicks;

            if (f1 > 0.0F)
            {
                this.func_180474_b(f1, scaledresolution);
            }
        }

        if (this.mc.playerController.isSpectator())
        {
            this.spectatorGui.renderTooltip(scaledresolution, partialTicks);
        }
        else
        {
            this.renderTooltip(scaledresolution, partialTicks);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(icons);
        GlStateManager.enableBlend();

        if (this.showCrosshair())
        {
            GlStateManager.tryBlendFuncSeparate(775, 769, 1, 0);
            GlStateManager.enableAlpha();
            this.drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
        }

        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        this.mc.mcProfiler.startSection("bossHealth");
        this.renderBossHealth();
        this.mc.mcProfiler.endSection();

        if (this.mc.playerController.shouldDrawHUD())
        {
            this.func_180477_d(scaledresolution);
        }

        GlStateManager.disableBlend();
        float f2;
        int k;
        int j1;

        if (this.mc.thePlayer.getSleepTimer() > 0)
        {
            this.mc.mcProfiler.startSection("sleep");
            GlStateManager.disableDepth();
            GlStateManager.disableAlpha();
            j1 = this.mc.thePlayer.getSleepTimer();
            f2 = (float)j1 / 100.0F;

            if (f2 > 1.0F)
            {
                f2 = 1.0F - (float)(j1 - 100) / 10.0F;
            }

            k = (int)(220.0F * f2) << 24 | 1052704;
            drawRect(0, 0, i, j, k);
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            this.mc.mcProfiler.endSection();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        j1 = i / 2 - 91;

        if (this.mc.thePlayer.isRidingHorse())
        {
            this.func_175186_a(scaledresolution, j1);
        }
        else if (this.mc.playerController.gameIsSurvivalOrAdventure())
        {
            this.func_175176_b(scaledresolution, j1);
        }

        if (this.mc.gameSettings.heldItemTooltips && !this.mc.playerController.isSpectator())
        {
            this.func_175182_a(scaledresolution);
        }
        else if (this.mc.thePlayer.isSpectator())
        {
            this.spectatorGui.func_175263_a(scaledresolution);
        }

        if (this.mc.isDemo())
        {
            this.func_175185_b(scaledresolution);
        }

        if (this.mc.gameSettings.showDebugInfo)
        {
            this.overlayDebug.renderDebugInfo(scaledresolution);
        }

        int l;

        if (this.recordPlayingUpFor > 0)
        {
            this.mc.mcProfiler.startSection("overlayMessage");
            f2 = (float)this.recordPlayingUpFor - partialTicks;
            k = (int)(f2 * 255.0F / 20.0F);

            if (k > 255)
            {
                k = 255;
            }

            if (k > 8)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j - 68), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                l = 16777215;

                if (this.recordIsPlaying)
                {
                    l = Color.HSBtoRGB(f2 / 50.0F, 0.7F, 0.6F) & 16777215;
                }

                this.func_175179_f().drawString(this.recordPlaying, -this.func_175179_f().getStringWidth(this.recordPlaying) / 2, -4, l + (k << 24 & -16777216));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        if (this.field_175195_w > 0)
        {
            this.mc.mcProfiler.startSection("titleAndSubtitle");
            f2 = (float)this.field_175195_w - partialTicks;
            k = 255;

            if (this.field_175195_w > this.field_175193_B + this.field_175192_A)
            {
                float f3 = (float)(this.field_175199_z + this.field_175192_A + this.field_175193_B) - f2;
                k = (int)(f3 * 255.0F / (float)this.field_175199_z);
            }

            if (this.field_175195_w <= this.field_175193_B)
            {
                k = (int)(f2 * 255.0F / (float)this.field_175193_B);
            }

            k = MathHelper.clamp_int(k, 0, 255);

            if (k > 8)
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)(i / 2), (float)(j / 2), 0.0F);
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.pushMatrix();
                GlStateManager.scale(4.0F, 4.0F, 4.0F);
                l = k << 24 & -16777216;
                this.func_175179_f().drawString(this.field_175201_x, (float)(-this.func_175179_f().getStringWidth(this.field_175201_x) / 2), -10.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.pushMatrix();
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
                this.func_175179_f().drawString(this.field_175200_y, (float)(-this.func_175179_f().getStringWidth(this.field_175200_y) / 2), 5.0F, 16777215 | l, true);
                GlStateManager.popMatrix();
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }

            this.mc.mcProfiler.endSection();
        }

        Scoreboard scoreboard = this.mc.theWorld.getScoreboard();
        ScoreObjective scoreobjective = null;
        ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(this.mc.thePlayer.getName());

        if (scoreplayerteam != null)
        {
            int i1 = scoreplayerteam.func_178775_l().getColorIndex();

            if (i1 >= 0)
            {
                scoreobjective = scoreboard.getObjectiveInDisplaySlot(3 + i1);
            }
        }

        ScoreObjective scoreobjective1 = scoreobjective != null ? scoreobjective : scoreboard.getObjectiveInDisplaySlot(1);

        if (scoreobjective1 != null)
        {
            this.func_180475_a(scoreobjective1, scaledresolution);
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, (float)(j - 48), 0.0F);
        this.mc.mcProfiler.startSection("chat");
        this.persistantChatGUI.drawChat(this.updateCounter);
        this.mc.mcProfiler.endSection();
        GlStateManager.popMatrix();
        scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(0);

        if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || this.mc.thePlayer.sendQueue.func_175106_d().size() > 1 || scoreobjective1 != null))
        {
            this.overlayPlayerList.func_175246_a(true);
            this.overlayPlayerList.func_175249_a(i, scoreboard, scoreobjective1);
        }
        else
        {
            this.overlayPlayerList.func_175246_a(false);
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableLighting();
        GlStateManager.enableAlpha();
    }

    protected void renderTooltip(ScaledResolution sr, float partialTicks)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(widgetsTexPath);
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = sr.getScaledWidth() / 2;
            float f1 = this.zLevel;
            this.zLevel = -90.0F;
            this.drawTexturedModalRect(i - 91, sr.getScaledHeight() - 22, 0, 0, 182, 22);
            this.drawTexturedModalRect(i - 91 - 1 + entityplayer.inventory.currentItem * 20, sr.getScaledHeight() - 22 - 1, 0, 22, 24, 22);
            this.zLevel = f1;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < 9; ++j)
            {
                int k = sr.getScaledWidth() / 2 - 90 + j * 20 + 2;
                int l = sr.getScaledHeight() - 16 - 3;
                this.renderHotbarItem(j, k, l, partialTicks, entityplayer);
            }

            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
        }
    }

    public void func_175186_a(ScaledResolution p_175186_1_, int p_175186_2_)
    {
        this.mc.mcProfiler.startSection("jumpBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        float f = this.mc.thePlayer.getHorseJumpPower();
        short short1 = 182;
        int j = (int)(f * (float)(short1 + 1));
        int k = p_175186_1_.getScaledHeight() - 32 + 3;
        this.drawTexturedModalRect(p_175186_2_, k, 0, 84, short1, 5);

        if (j > 0)
        {
            this.drawTexturedModalRect(p_175186_2_, k, 0, 89, j, 5);
        }

        this.mc.mcProfiler.endSection();
    }

    public void func_175176_b(ScaledResolution p_175176_1_, int p_175176_2_)
    {
        this.mc.mcProfiler.startSection("expBar");
        this.mc.getTextureManager().bindTexture(Gui.icons);
        int j = this.mc.thePlayer.xpBarCap();
        int l;

        if (j > 0)
        {
            short short1 = 182;
            int k = (int)(this.mc.thePlayer.experience * (float)(short1 + 1));
            l = p_175176_1_.getScaledHeight() - 32 + 3;
            this.drawTexturedModalRect(p_175176_2_, l, 0, 64, short1, 5);

            if (k > 0)
            {
                this.drawTexturedModalRect(p_175176_2_, l, 0, 69, k, 5);
            }
        }

        this.mc.mcProfiler.endSection();

        if (this.mc.thePlayer.experienceLevel > 0)
        {
            this.mc.mcProfiler.startSection("expLevel");
            int j1 = 8453920;
            String s = "" + this.mc.thePlayer.experienceLevel;
            l = (p_175176_1_.getScaledWidth() - this.func_175179_f().getStringWidth(s)) / 2;
            int i1 = p_175176_1_.getScaledHeight() - 31 - 4;
            boolean flag = false;
            this.func_175179_f().drawString(s, l + 1, i1, 0);
            this.func_175179_f().drawString(s, l - 1, i1, 0);
            this.func_175179_f().drawString(s, l, i1 + 1, 0);
            this.func_175179_f().drawString(s, l, i1 - 1, 0);
            this.func_175179_f().drawString(s, l, i1, j1);
            this.mc.mcProfiler.endSection();
        }
    }

    public void func_175182_a(ScaledResolution p_175182_1_)
    {
        this.mc.mcProfiler.startSection("toolHighlight");

        if (this.remainingHighlightTicks > 0 && this.highlightingItemStack != null)
        {
            String s = this.highlightingItemStack.getDisplayName();

            if (this.highlightingItemStack.hasDisplayName())
            {
                s = EnumChatFormatting.ITALIC + s;
            }

            int i = (p_175182_1_.getScaledWidth() - this.func_175179_f().getStringWidth(s)) / 2;
            int j = p_175182_1_.getScaledHeight() - 59;

            if (!this.mc.playerController.shouldDrawHUD())
            {
                j += 14;
            }

            int k = (int)((float)this.remainingHighlightTicks * 256.0F / 10.0F);

            if (k > 255)
            {
                k = 255;
            }

            if (k > 0)
            {
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                this.func_175179_f().drawStringWithShadow(s, (float)i, (float)j, 16777215 + (k << 24));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
            }
        }

        this.mc.mcProfiler.endSection();
    }

    public void func_175185_b(ScaledResolution p_175185_1_)
    {
        this.mc.mcProfiler.startSection("demo");
        String s = "";

        if (this.mc.theWorld.getTotalWorldTime() >= 120500L)
        {
            s = I18n.format("demo.demoExpired", new Object[0]);
        }
        else
        {
            s = I18n.format("demo.remainingTime", new Object[] {StringUtils.ticksToElapsedTime((int)(120500L - this.mc.theWorld.getTotalWorldTime()))});
        }

        int i = this.func_175179_f().getStringWidth(s);
        this.func_175179_f().drawStringWithShadow(s, (float)(p_175185_1_.getScaledWidth() - i - 10), 5.0F, 16777215);
        this.mc.mcProfiler.endSection();
    }

    protected boolean showCrosshair()
    {
        if (this.mc.gameSettings.showDebugInfo && !this.mc.thePlayer.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo)
        {
            return false;
        }
        else if (this.mc.playerController.isSpectator())
        {
            if (this.mc.pointedEntity != null)
            {
                return true;
            }
            else
            {
                if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
                {
                    BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

                    if (this.mc.theWorld.getTileEntity(blockpos) instanceof IInventory)
                    {
                        return true;
                    }
                }

                return false;
            }
        }
        else
        {
            return true;
        }
    }

    public void renderStreamIndicator(ScaledResolution p_180478_1_)
    {
        this.streamIndicator.render(p_180478_1_.getScaledWidth() - 10, 10);
    }

    protected void func_180475_a(ScoreObjective p_180475_1_, ScaledResolution p_180475_2_)
    {
        Scoreboard scoreboard = p_180475_1_.getScoreboard();
        Collection collection = scoreboard.getSortedScores(p_180475_1_);
        ArrayList arraylist = Lists.newArrayList(Iterables.filter(collection, new Predicate()
        {
            private static final String __OBFID = "CL_00001958";
            public boolean func_178903_a(Score p_178903_1_)
            {
                return p_178903_1_.getPlayerName() != null && !p_178903_1_.getPlayerName().startsWith("#");
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_178903_a((Score)p_apply_1_);
            }
        }));
        ArrayList arraylist1;

        if (arraylist.size() > 15)
        {
            arraylist1 = Lists.newArrayList(Iterables.skip(arraylist, collection.size() - 15));
        }
        else
        {
            arraylist1 = arraylist;
        }

        int i = this.func_175179_f().getStringWidth(p_180475_1_.getDisplayName());
        String s;

        for (Iterator iterator = arraylist1.iterator(); iterator.hasNext(); i = Math.max(i, this.func_175179_f().getStringWidth(s)))
        {
            Score score = (Score)iterator.next();
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            s = ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName()) + ": " + EnumChatFormatting.RED + score.getScorePoints();
        }

        int i1 = arraylist1.size() * this.func_175179_f().FONT_HEIGHT;
        int j1 = p_180475_2_.getScaledHeight() / 2 + i1 / 3;
        byte b0 = 3;
        int k1 = p_180475_2_.getScaledWidth() - i - b0;
        int j = 0;
        Iterator iterator1 = arraylist1.iterator();

        while (iterator1.hasNext())
        {
            Score score1 = (Score)iterator1.next();
            ++j;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = EnumChatFormatting.RED + "" + score1.getScorePoints();
            int k = j1 - j * this.func_175179_f().FONT_HEIGHT;
            int l = p_180475_2_.getScaledWidth() - b0 + 2;
            drawRect(k1 - 2, k, l, k + this.func_175179_f().FONT_HEIGHT, 1342177280);
            this.func_175179_f().drawString(s1, k1, k, 553648127);
            this.func_175179_f().drawString(s2, l - this.func_175179_f().getStringWidth(s2), k, 553648127);

            if (j == arraylist1.size())
            {
                String s3 = p_180475_1_.getDisplayName();
                drawRect(k1 - 2, k - this.func_175179_f().FONT_HEIGHT - 1, l, k - 1, 1610612736);
                drawRect(k1 - 2, k - 1, l, k, 1342177280);
                this.func_175179_f().drawString(s3, k1 + i / 2 - this.func_175179_f().getStringWidth(s3) / 2, k - this.func_175179_f().FONT_HEIGHT, 553648127);
            }
        }
    }

    protected void func_180477_d(ScaledResolution p_180477_1_)
    {
        if (this.mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)this.mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean flag = this.field_175191_F > (long)this.updateCounter && (this.field_175191_F - (long)this.updateCounter) / 3L % 2L == 1L;

            if (i < this.field_175194_C && entityplayer.hurtResistantTime > 0)
            {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.field_175191_F = (long)(this.updateCounter + 20);
            }
            else if (i > this.field_175194_C && entityplayer.hurtResistantTime > 0)
            {
                this.lastSystemTime = Minecraft.getSystemTime();
                this.field_175191_F = (long)(this.updateCounter + 10);
            }

            if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
            {
                this.field_175194_C = i;
                this.field_175189_D = i;
                this.lastSystemTime = Minecraft.getSystemTime();
            }

            this.field_175194_C = i;
            int j = this.field_175189_D;
            this.rand.setSeed((long)(this.updateCounter * 312871));
            boolean flag1 = false;
            FoodStats foodstats = entityplayer.getFoodStats();
            int k = foodstats.getFoodLevel();
            int l = foodstats.getPrevFoodLevel();
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = p_180477_1_.getScaledWidth() / 2 - 91;
            int j1 = p_180477_1_.getScaledWidth() / 2 + 91;
            int k1 = p_180477_1_.getScaledHeight() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int k2 = entityplayer.getTotalArmorValue();
            int l2 = -1;

            if (entityplayer.isPotionActive(Potion.regeneration))
            {
                l2 = this.updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
            }

            this.mc.mcProfiler.startSection("armor");
            int i3;
            int j3;

            for (i3 = 0; i3 < 10; ++i3)
            {
                if (k2 > 0)
                {
                    j3 = i1 + i3 * 8;

                    if (i3 * 2 + 1 < k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 34, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 == k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 25, 9, 9, 9);
                    }

                    if (i3 * 2 + 1 > k2)
                    {
                        this.drawTexturedModalRect(j3, j2, 16, 9, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endStartSection("health");
            int k3;
            int l3;
            int i4;

            for (i3 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i3 >= 0; --i3)
            {
                j3 = 16;

                if (entityplayer.isPotionActive(Potion.poison))
                {
                    j3 += 36;
                }
                else if (entityplayer.isPotionActive(Potion.wither))
                {
                    j3 += 72;
                }

                byte b0 = 0;

                if (flag)
                {
                    b0 = 1;
                }

                k3 = MathHelper.ceiling_float_int((float)(i3 + 1) / 10.0F) - 1;
                l3 = i1 + i3 % 10 * 8;
                i4 = k1 - k3 * i2;

                if (i <= 4)
                {
                    i4 += this.rand.nextInt(2);
                }

                if (i3 == l2)
                {
                    i4 -= 2;
                }

                byte b1 = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled())
                {
                    b1 = 5;
                }

                this.drawTexturedModalRect(l3, i4, 16 + b0 * 9, 9 * b1, 9, 9);

                if (flag)
                {
                    if (i3 * 2 + 1 < j)
                    {
                        this.drawTexturedModalRect(l3, i4, j3 + 54, 9 * b1, 9, 9);
                    }

                    if (i3 * 2 + 1 == j)
                    {
                        this.drawTexturedModalRect(l3, i4, j3 + 63, 9 * b1, 9, 9);
                    }
                }

                if (f2 > 0.0F)
                {
                    if (f2 == f1 && f1 % 2.0F == 1.0F)
                    {
                        this.drawTexturedModalRect(l3, i4, j3 + 153, 9 * b1, 9, 9);
                    }
                    else
                    {
                        this.drawTexturedModalRect(l3, i4, j3 + 144, 9 * b1, 9, 9);
                    }

                    f2 -= 2.0F;
                }
                else
                {
                    if (i3 * 2 + 1 < i)
                    {
                        this.drawTexturedModalRect(l3, i4, j3 + 36, 9 * b1, 9, 9);
                    }

                    if (i3 * 2 + 1 == i)
                    {
                        this.drawTexturedModalRect(l3, i4, j3 + 45, 9 * b1, 9, 9);
                    }
                }
            }

            Entity entity = entityplayer.ridingEntity;
            int i5;

            if (entity == null)
            {
                this.mc.mcProfiler.endStartSection("food");

                for (j3 = 0; j3 < 10; ++j3)
                {
                    i5 = k1;
                    k3 = 16;
                    byte b4 = 0;

                    if (entityplayer.isPotionActive(Potion.hunger))
                    {
                        k3 += 36;
                        b4 = 13;
                    }

                    if (entityplayer.getFoodStats().getSaturationLevel() <= 0.0F && this.updateCounter % (k * 3 + 1) == 0)
                    {
                        i5 = k1 + (this.rand.nextInt(3) - 1);
                    }

                    if (flag1)
                    {
                        b4 = 1;
                    }

                    i4 = j1 - j3 * 8 - 9;
                    this.drawTexturedModalRect(i4, i5, 16 + b4 * 9, 27, 9, 9);

                    if (flag1)
                    {
                        if (j3 * 2 + 1 < l)
                        {
                            this.drawTexturedModalRect(i4, i5, k3 + 54, 27, 9, 9);
                        }

                        if (j3 * 2 + 1 == l)
                        {
                            this.drawTexturedModalRect(i4, i5, k3 + 63, 27, 9, 9);
                        }
                    }

                    if (j3 * 2 + 1 < k)
                    {
                        this.drawTexturedModalRect(i4, i5, k3 + 36, 27, 9, 9);
                    }

                    if (j3 * 2 + 1 == k)
                    {
                        this.drawTexturedModalRect(i4, i5, k3 + 45, 27, 9, 9);
                    }
                }
            }
            else if (entity instanceof EntityLivingBase)
            {
                this.mc.mcProfiler.endStartSection("mountHealth");
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
                i5 = (int)Math.ceil((double)entitylivingbase.getHealth());
                float f3 = entitylivingbase.getMaxHealth();
                l3 = (int)(f3 + 0.5F) / 2;

                if (l3 > 30)
                {
                    l3 = 30;
                }

                i4 = k1;

                for (int j5 = 0; l3 > 0; j5 += 20)
                {
                    int j4 = Math.min(l3, 10);
                    l3 -= j4;

                    for (int k4 = 0; k4 < j4; ++k4)
                    {
                        byte b2 = 52;
                        byte b3 = 0;

                        if (flag1)
                        {
                            b3 = 1;
                        }

                        int l4 = j1 - k4 * 8 - 9;
                        this.drawTexturedModalRect(l4, i4, b2 + b3 * 9, 9, 9, 9);

                        if (k4 * 2 + 1 + j5 < i5)
                        {
                            this.drawTexturedModalRect(l4, i4, b2 + 36, 9, 9, 9);
                        }

                        if (k4 * 2 + 1 + j5 == i5)
                        {
                            this.drawTexturedModalRect(l4, i4, b2 + 45, 9, 9, 9);
                        }
                    }

                    i4 -= 10;
                }
            }

            this.mc.mcProfiler.endStartSection("air");

            if (entityplayer.isInsideOfMaterial(Material.water))
            {
                j3 = this.mc.thePlayer.getAir();
                i5 = MathHelper.ceiling_double_int((double)(j3 - 2) * 10.0D / 300.0D);
                k3 = MathHelper.ceiling_double_int((double)j3 * 10.0D / 300.0D) - i5;

                for (l3 = 0; l3 < i5 + k3; ++l3)
                {
                    if (l3 < i5)
                    {
                        this.drawTexturedModalRect(j1 - l3 * 8 - 9, j2, 16, 18, 9, 9);
                    }
                    else
                    {
                        this.drawTexturedModalRect(j1 - l3 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }

            this.mc.mcProfiler.endSection();
        }
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    protected void renderBossHealth()
    {
        if (BossStatus.bossName != null && BossStatus.statusBarTime > 0)
        {
            --BossStatus.statusBarTime;
            FontRenderer fontrenderer = this.mc.fontRendererObj;
            ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            int i = scaledresolution.getScaledWidth();
            short short1 = 182;
            int j = i / 2 - short1 / 2;
            int k = (int)(BossStatus.healthScale * (float)(short1 + 1));
            byte b0 = 12;
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);
            this.drawTexturedModalRect(j, b0, 0, 74, short1, 5);

            if (k > 0)
            {
                this.drawTexturedModalRect(j, b0, 0, 79, k, 5);
            }

            String s = BossStatus.bossName;
            this.func_175179_f().drawStringWithShadow(s, (float)(i / 2 - this.func_175179_f().getStringWidth(s) / 2), (float)(b0 - 10), 16777215);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.mc.getTextureManager().bindTexture(icons);
        }
    }

    protected void func_180476_e(ScaledResolution p_180476_1_)
    {
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableAlpha();
        this.mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV(0.0D, (double)p_180476_1_.getScaledHeight(), -90.0D, 0.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)p_180476_1_.getScaledWidth(), (double)p_180476_1_.getScaledHeight(), -90.0D, 1.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)p_180476_1_.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
        worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void func_180480_a(float p_180480_1_, ScaledResolution p_180480_2_)
    {
        p_180480_1_ = 1.0F - p_180480_1_;
        p_180480_1_ = MathHelper.clamp_float(p_180480_1_, 0.0F, 1.0F);
        WorldBorder worldborder = this.mc.theWorld.getWorldBorder();
        float f1 = (float)worldborder.getClosestDistance(this.mc.thePlayer);
        double d0 = Math.min(worldborder.getResizeSpeed() * (double)worldborder.getWarningTime() * 1000.0D, Math.abs(worldborder.getTargetSize() - worldborder.getDiameter()));
        double d1 = Math.max((double)worldborder.getWarningDistance(), d0);

        if ((double)f1 < d1)
        {
            f1 = 1.0F - (float)((double)f1 / d1);
        }
        else
        {
            f1 = 0.0F;
        }

        this.prevVignetteBrightness = (float)((double)this.prevVignetteBrightness + (double)(p_180480_1_ - this.prevVignetteBrightness) * 0.01D);
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(0, 769, 1, 0);

        if (f1 > 0.0F)
        {
            GlStateManager.color(0.0F, f1, f1, 1.0F);
        }
        else
        {
            GlStateManager.color(this.prevVignetteBrightness, this.prevVignetteBrightness, this.prevVignetteBrightness, 1.0F);
        }

        this.mc.getTextureManager().bindTexture(vignetteTexPath);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV(0.0D, (double)p_180480_2_.getScaledHeight(), -90.0D, 0.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)p_180480_2_.getScaledWidth(), (double)p_180480_2_.getScaledHeight(), -90.0D, 1.0D, 1.0D);
        worldrenderer.addVertexWithUV((double)p_180480_2_.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
        worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    protected void func_180474_b(float p_180474_1_, ScaledResolution p_180474_2_)
    {
        if (p_180474_1_ < 1.0F)
        {
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ *= p_180474_1_;
            p_180474_1_ = p_180474_1_ * 0.8F + 0.2F;
        }

        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(1.0F, 1.0F, 1.0F, p_180474_1_);
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        TextureAtlasSprite textureatlassprite = this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(Blocks.portal.getDefaultState());
        float f1 = textureatlassprite.getMinU();
        float f2 = textureatlassprite.getMinV();
        float f3 = textureatlassprite.getMaxU();
        float f4 = textureatlassprite.getMaxV();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV(0.0D, (double)p_180474_2_.getScaledHeight(), -90.0D, (double)f1, (double)f4);
        worldrenderer.addVertexWithUV((double)p_180474_2_.getScaledWidth(), (double)p_180474_2_.getScaledHeight(), -90.0D, (double)f3, (double)f4);
        worldrenderer.addVertexWithUV((double)p_180474_2_.getScaledWidth(), 0.0D, -90.0D, (double)f3, (double)f2);
        worldrenderer.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)f1, (double)f2);
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void renderHotbarItem(int index, int xPos, int yPos, float partialTicks, EntityPlayer p_175184_5_)
    {
        ItemStack itemstack = p_175184_5_.inventory.mainInventory[index];

        if (itemstack != null)
        {
            float f1 = (float)itemstack.animationsToGo - partialTicks;

            if (f1 > 0.0F)
            {
                GlStateManager.pushMatrix();
                float f2 = 1.0F + f1 / 5.0F;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), 0.0F);
                GlStateManager.scale(1.0F / f2, (f2 + 1.0F) / 2.0F, 1.0F);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0F);
            }

            this.itemRenderer.renderItemAndEffectIntoGUI(itemstack, xPos, yPos);

            if (f1 > 0.0F)
            {
                GlStateManager.popMatrix();
            }

            this.itemRenderer.renderItemOverlays(this.mc.fontRendererObj, itemstack, xPos, yPos);
        }
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick()
    {
        if (this.recordPlayingUpFor > 0)
        {
            --this.recordPlayingUpFor;
        }

        if (this.field_175195_w > 0)
        {
            --this.field_175195_w;

            if (this.field_175195_w <= 0)
            {
                this.field_175201_x = "";
                this.field_175200_y = "";
            }
        }

        ++this.updateCounter;
        this.streamIndicator.func_152439_a();

        if (this.mc.thePlayer != null)
        {
            ItemStack itemstack = this.mc.thePlayer.inventory.getCurrentItem();

            if (itemstack == null)
            {
                this.remainingHighlightTicks = 0;
            }
            else if (this.highlightingItemStack != null && itemstack.getItem() == this.highlightingItemStack.getItem() && ItemStack.areItemStackTagsEqual(itemstack, this.highlightingItemStack) && (itemstack.isItemStackDamageable() || itemstack.getMetadata() == this.highlightingItemStack.getMetadata()))
            {
                if (this.remainingHighlightTicks > 0)
                {
                    --this.remainingHighlightTicks;
                }
            }
            else
            {
                this.remainingHighlightTicks = 40;
            }

            this.highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String p_73833_1_)
    {
        this.setRecordPlaying(I18n.format("record.nowPlaying", new Object[] {p_73833_1_}), true);
    }

    public void setRecordPlaying(String p_110326_1_, boolean p_110326_2_)
    {
        this.recordPlaying = p_110326_1_;
        this.recordPlayingUpFor = 60;
        this.recordIsPlaying = p_110326_2_;
    }

    public void func_175178_a(String p_175178_1_, String p_175178_2_, int p_175178_3_, int p_175178_4_, int p_175178_5_)
    {
        if (p_175178_1_ == null && p_175178_2_ == null && p_175178_3_ < 0 && p_175178_4_ < 0 && p_175178_5_ < 0)
        {
            this.field_175201_x = "";
            this.field_175200_y = "";
            this.field_175195_w = 0;
        }
        else if (p_175178_1_ != null)
        {
            this.field_175201_x = p_175178_1_;
            this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
        }
        else if (p_175178_2_ != null)
        {
            this.field_175200_y = p_175178_2_;
        }
        else
        {
            if (p_175178_3_ >= 0)
            {
                this.field_175199_z = p_175178_3_;
            }

            if (p_175178_4_ >= 0)
            {
                this.field_175192_A = p_175178_4_;
            }

            if (p_175178_5_ >= 0)
            {
                this.field_175193_B = p_175178_5_;
            }

            if (this.field_175195_w > 0)
            {
                this.field_175195_w = this.field_175199_z + this.field_175192_A + this.field_175193_B;
            }
        }
    }

    public void func_175188_a(IChatComponent p_175188_1_, boolean p_175188_2_)
    {
        this.setRecordPlaying(p_175188_1_.getUnformattedText(), p_175188_2_);
    }

    /**
     * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
     */
    public GuiNewChat getChatGUI()
    {
        return this.persistantChatGUI;
    }

    public int getUpdateCounter()
    {
        return this.updateCounter;
    }

    public FontRenderer func_175179_f()
    {
        return this.mc.fontRendererObj;
    }

    public GuiSpectator func_175187_g()
    {
        return this.spectatorGui;
    }

    public GuiPlayerTabOverlay getTabList()
    {
        return this.overlayPlayerList;
    }
}