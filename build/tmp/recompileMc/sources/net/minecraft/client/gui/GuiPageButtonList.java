package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IntHashMap;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPageButtonList extends GuiListExtended
{
    private final List field_178074_u = Lists.newArrayList();
    private final IntHashMap field_178073_v = new IntHashMap();
    private final List field_178072_w = Lists.newArrayList();
    private final GuiPageButtonList.GuiListEntry[][] field_178078_x;
    private int field_178077_y;
    private GuiPageButtonList.GuiResponder field_178076_z;
    private Gui field_178075_A;
    private static final String __OBFID = "CL_00001950";

    public GuiPageButtonList(Minecraft mcIn, int p_i45536_2_, int p_i45536_3_, int p_i45536_4_, int p_i45536_5_, int p_i45536_6_, GuiPageButtonList.GuiResponder p_i45536_7_, GuiPageButtonList.GuiListEntry[] ... p_i45536_8_)
    {
        super(mcIn, p_i45536_2_, p_i45536_3_, p_i45536_4_, p_i45536_5_, p_i45536_6_);
        this.field_178076_z = p_i45536_7_;
        this.field_178078_x = p_i45536_8_;
        this.field_148163_i = false;
        this.func_178069_s();
        this.func_178055_t();
    }

    private void func_178069_s()
    {
        GuiPageButtonList.GuiListEntry[][] aguilistentry = this.field_178078_x;
        int i = aguilistentry.length;

        for (int j = 0; j < i; ++j)
        {
            GuiPageButtonList.GuiListEntry[] aguilistentry1 = aguilistentry[j];

            for (int k = 0; k < aguilistentry1.length; k += 2)
            {
                GuiPageButtonList.GuiListEntry guilistentry = aguilistentry1[k];
                GuiPageButtonList.GuiListEntry guilistentry1 = k < aguilistentry1.length - 1 ? aguilistentry1[k + 1] : null;
                Gui gui = this.func_178058_a(guilistentry, 0, guilistentry1 == null);
                Gui gui1 = this.func_178058_a(guilistentry1, 160, guilistentry == null);
                GuiPageButtonList.GuiEntry guientry = new GuiPageButtonList.GuiEntry(gui, gui1);
                this.field_178074_u.add(guientry);

                if (guilistentry != null && gui != null)
                {
                    this.field_178073_v.addKey(guilistentry.func_178935_b(), gui);

                    if (gui instanceof GuiTextField)
                    {
                        this.field_178072_w.add((GuiTextField)gui);
                    }
                }

                if (guilistentry1 != null && gui1 != null)
                {
                    this.field_178073_v.addKey(guilistentry1.func_178935_b(), gui1);

                    if (gui1 instanceof GuiTextField)
                    {
                        this.field_178072_w.add((GuiTextField)gui1);
                    }
                }
            }
        }
    }

    private void func_178055_t()
    {
        this.field_178074_u.clear();

        for (int i = 0; i < this.field_178078_x[this.field_178077_y].length; i += 2)
        {
            GuiPageButtonList.GuiListEntry guilistentry = this.field_178078_x[this.field_178077_y][i];
            GuiPageButtonList.GuiListEntry guilistentry1 = i < this.field_178078_x[this.field_178077_y].length - 1 ? this.field_178078_x[this.field_178077_y][i + 1] : null;
            Gui gui = (Gui)this.field_178073_v.lookup(guilistentry.func_178935_b());
            Gui gui1 = guilistentry1 != null ? (Gui)this.field_178073_v.lookup(guilistentry1.func_178935_b()) : null;
            GuiPageButtonList.GuiEntry guientry = new GuiPageButtonList.GuiEntry(gui, gui1);
            this.field_178074_u.add(guientry);
        }
    }

    public int func_178059_e()
    {
        return this.field_178077_y;
    }

    public int func_178057_f()
    {
        return this.field_178078_x.length;
    }

    public Gui func_178056_g()
    {
        return this.field_178075_A;
    }

    public void func_178071_h()
    {
        if (this.field_178077_y > 0)
        {
            int i = this.field_178077_y--;
            this.func_178055_t();
            this.func_178060_e(i, this.field_178077_y);
            this.amountScrolled = 0.0F;
        }
    }

    public void func_178064_i()
    {
        if (this.field_178077_y < this.field_178078_x.length - 1)
        {
            int i = this.field_178077_y++;
            this.func_178055_t();
            this.func_178060_e(i, this.field_178077_y);
            this.amountScrolled = 0.0F;
        }
    }

    public Gui func_178061_c(int p_178061_1_)
    {
        return (Gui)this.field_178073_v.lookup(p_178061_1_);
    }

    private void func_178060_e(int p_178060_1_, int p_178060_2_)
    {
        GuiPageButtonList.GuiListEntry[] aguilistentry = this.field_178078_x[p_178060_1_];
        int k = aguilistentry.length;
        int l;
        GuiPageButtonList.GuiListEntry guilistentry;

        for (l = 0; l < k; ++l)
        {
            guilistentry = aguilistentry[l];

            if (guilistentry != null)
            {
                this.func_178066_a((Gui)this.field_178073_v.lookup(guilistentry.func_178935_b()), false);
            }
        }

        aguilistentry = this.field_178078_x[p_178060_2_];
        k = aguilistentry.length;

        for (l = 0; l < k; ++l)
        {
            guilistentry = aguilistentry[l];

            if (guilistentry != null)
            {
                this.func_178066_a((Gui)this.field_178073_v.lookup(guilistentry.func_178935_b()), true);
            }
        }
    }

    private void func_178066_a(Gui p_178066_1_, boolean p_178066_2_)
    {
        if (p_178066_1_ instanceof GuiButton)
        {
            ((GuiButton)p_178066_1_).visible = p_178066_2_;
        }
        else if (p_178066_1_ instanceof GuiTextField)
        {
            ((GuiTextField)p_178066_1_).setVisible(p_178066_2_);
        }
        else if (p_178066_1_ instanceof GuiLabel)
        {
            ((GuiLabel)p_178066_1_).visible = p_178066_2_;
        }
    }

    private Gui func_178058_a(GuiPageButtonList.GuiListEntry p_178058_1_, int p_178058_2_, boolean p_178058_3_)
    {
        return (Gui)(p_178058_1_ instanceof GuiPageButtonList.GuiSlideEntry ? this.func_178067_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.GuiSlideEntry)p_178058_1_) : (p_178058_1_ instanceof GuiPageButtonList.GuiButtonEntry ? this.func_178065_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.GuiButtonEntry)p_178058_1_) : (p_178058_1_ instanceof GuiPageButtonList.EditBoxEntry ? this.func_178068_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.EditBoxEntry)p_178058_1_) : (p_178058_1_ instanceof GuiPageButtonList.GuiLabelEntry ? this.func_178063_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiPageButtonList.GuiLabelEntry)p_178058_1_, p_178058_3_) : null))));
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent)
    {
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
        int l = this.getSlotIndexFromScreenCoords(mouseX, mouseY);

        if (l >= 0)
        {
            GuiPageButtonList.GuiEntry guientry = this.func_178070_d(l);

            if (this.field_178075_A != guientry.field_178028_d && this.field_178075_A != null && this.field_178075_A instanceof GuiTextField)
            {
                ((GuiTextField)this.field_178075_A).setFocused(false);
            }

            this.field_178075_A = guientry.field_178028_d;
        }

        return flag;
    }

    private GuiSlider func_178067_a(int p_178067_1_, int p_178067_2_, GuiPageButtonList.GuiSlideEntry p_178067_3_)
    {
        GuiSlider guislider = new GuiSlider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g(), p_178067_3_.func_178945_a());
        guislider.visible = p_178067_3_.func_178934_d();
        return guislider;
    }

    private GuiListButton func_178065_a(int p_178065_1_, int p_178065_2_, GuiPageButtonList.GuiButtonEntry p_178065_3_)
    {
        GuiListButton guilistbutton = new GuiListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
        guilistbutton.visible = p_178065_3_.func_178934_d();
        return guilistbutton;
    }

    private GuiTextField func_178068_a(int p_178068_1_, int p_178068_2_, GuiPageButtonList.EditBoxEntry p_178068_3_)
    {
        GuiTextField guitextfield = new GuiTextField(p_178068_3_.func_178935_b(), this.mc.fontRendererObj, p_178068_1_, p_178068_2_, 150, 20);
        guitextfield.setText(p_178068_3_.func_178936_c());
        guitextfield.func_175207_a(this.field_178076_z);
        guitextfield.setVisible(p_178068_3_.func_178934_d());
        guitextfield.func_175205_a(p_178068_3_.func_178950_a());
        return guitextfield;
    }

    private GuiLabel func_178063_a(int p_178063_1_, int p_178063_2_, GuiPageButtonList.GuiLabelEntry p_178063_3_, boolean p_178063_4_)
    {
        GuiLabel guilabel;

        if (p_178063_4_)
        {
            guilabel = new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1);
        }
        else
        {
            guilabel = new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
        }

        guilabel.visible = p_178063_3_.func_178934_d();
        guilabel.func_175202_a(p_178063_3_.func_178936_c());
        guilabel.setCentered();
        return guilabel;
    }

    public void func_178062_a(char p_178062_1_, int p_178062_2_)
    {
        if (this.field_178075_A instanceof GuiTextField)
        {
            GuiTextField guitextfield = (GuiTextField)this.field_178075_A;
            int j;

            if (!GuiScreen.func_175279_e(p_178062_2_))
            {
                if (p_178062_2_ == 15)
                {
                    guitextfield.setFocused(false);
                    int j1 = this.field_178072_w.indexOf(this.field_178075_A);

                    if (GuiScreen.isShiftKeyDown())
                    {
                        if (j1 == 0)
                        {
                            j1 = this.field_178072_w.size() - 1;
                        }
                        else
                        {
                            --j1;
                        }
                    }
                    else if (j1 == this.field_178072_w.size() - 1)
                    {
                        j1 = 0;
                    }
                    else
                    {
                        ++j1;
                    }

                    this.field_178075_A = (Gui)this.field_178072_w.get(j1);
                    guitextfield = (GuiTextField)this.field_178075_A;
                    guitextfield.setFocused(true);
                    int k1 = guitextfield.yPosition + this.slotHeight;
                    j = guitextfield.yPosition;

                    if (k1 > this.bottom)
                    {
                        this.amountScrolled += (float)(k1 - this.bottom);
                    }
                    else if (j < this.top)
                    {
                        this.amountScrolled = (float)j;
                    }
                }
                else
                {
                    guitextfield.textboxKeyTyped(p_178062_1_, p_178062_2_);
                }
            }
            else
            {
                String s = GuiScreen.getClipboardString();
                String[] astring = s.split(";");
                j = this.field_178072_w.indexOf(this.field_178075_A);
                int k = j;
                String[] astring1 = astring;
                int l = astring.length;

                for (int i1 = 0; i1 < l; ++i1)
                {
                    String s1 = astring1[i1];
                    ((GuiTextField)this.field_178072_w.get(k)).setText(s1);

                    if (k == this.field_178072_w.size() - 1)
                    {
                        k = 0;
                    }
                    else
                    {
                        ++k;
                    }

                    if (k == j)
                    {
                        break;
                    }
                }
            }
        }
    }

    public GuiPageButtonList.GuiEntry func_178070_d(int p_178070_1_)
    {
        return (GuiPageButtonList.GuiEntry)this.field_178074_u.get(p_178070_1_);
    }

    public int getSize()
    {
        return this.field_178074_u.size();
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth()
    {
        return 400;
    }

    protected int getScrollBarX()
    {
        return super.getScrollBarX() + 32;
    }

    /**
     * Gets the IGuiListEntry object for the given index
     */
    public GuiListExtended.IGuiListEntry getListEntry(int p_148180_1_)
    {
        return this.func_178070_d(p_148180_1_);
    }

    @SideOnly(Side.CLIENT)
    public static class EditBoxEntry extends GuiPageButtonList.GuiListEntry
        {
            private final Predicate field_178951_a;
            private static final String __OBFID = "CL_00001948";

            public EditBoxEntry(int p_i45534_1_, String p_i45534_2_, boolean p_i45534_3_, Predicate p_i45534_4_)
            {
                super(p_i45534_1_, p_i45534_2_, p_i45534_3_);
                this.field_178951_a = (Predicate)Objects.firstNonNull(p_i45534_4_, Predicates.alwaysTrue());
            }

            public Predicate func_178950_a()
            {
                return this.field_178951_a;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class GuiButtonEntry extends GuiPageButtonList.GuiListEntry
        {
            private final boolean field_178941_a;
            private static final String __OBFID = "CL_00001949";

            public GuiButtonEntry(int p_i45535_1_, String p_i45535_2_, boolean p_i45535_3_, boolean p_i45535_4_)
            {
                super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
                this.field_178941_a = p_i45535_4_;
            }

            public boolean func_178940_a()
            {
                return this.field_178941_a;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class GuiEntry implements GuiListExtended.IGuiListEntry
        {
            private final Minecraft field_178031_a = Minecraft.getMinecraft();
            private final Gui field_178029_b;
            private final Gui field_178030_c;
            private Gui field_178028_d;
            private static final String __OBFID = "CL_00001947";

            public GuiEntry(Gui p_i45533_1_, Gui p_i45533_2_)
            {
                this.field_178029_b = p_i45533_1_;
                this.field_178030_c = p_i45533_2_;
            }

            public Gui func_178022_a()
            {
                return this.field_178029_b;
            }

            public Gui func_178021_b()
            {
                return this.field_178030_c;
            }

            public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
            {
                this.func_178017_a(this.field_178029_b, y, mouseX, mouseY, false);
                this.func_178017_a(this.field_178030_c, y, mouseX, mouseY, false);
            }

            private void func_178017_a(Gui p_178017_1_, int p_178017_2_, int p_178017_3_, int p_178017_4_, boolean p_178017_5_)
            {
                if (p_178017_1_ != null)
                {
                    if (p_178017_1_ instanceof GuiButton)
                    {
                        this.func_178024_a((GuiButton)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                    }
                    else if (p_178017_1_ instanceof GuiTextField)
                    {
                        this.func_178027_a((GuiTextField)p_178017_1_, p_178017_2_, p_178017_5_);
                    }
                    else if (p_178017_1_ instanceof GuiLabel)
                    {
                        this.func_178025_a((GuiLabel)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                    }
                }
            }

            private void func_178024_a(GuiButton p_178024_1_, int p_178024_2_, int p_178024_3_, int p_178024_4_, boolean p_178024_5_)
            {
                p_178024_1_.yPosition = p_178024_2_;

                if (!p_178024_5_)
                {
                    p_178024_1_.drawButton(this.field_178031_a, p_178024_3_, p_178024_4_);
                }
            }

            private void func_178027_a(GuiTextField p_178027_1_, int p_178027_2_, boolean p_178027_3_)
            {
                p_178027_1_.yPosition = p_178027_2_;

                if (!p_178027_3_)
                {
                    p_178027_1_.drawTextBox();
                }
            }

            private void func_178025_a(GuiLabel p_178025_1_, int p_178025_2_, int p_178025_3_, int p_178025_4_, boolean p_178025_5_)
            {
                p_178025_1_.field_146174_h = p_178025_2_;

                if (!p_178025_5_)
                {
                    p_178025_1_.drawLabel(this.field_178031_a, p_178025_3_, p_178025_4_);
                }
            }

            public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_)
            {
                this.func_178017_a(this.field_178029_b, p_178011_3_, 0, 0, true);
                this.func_178017_a(this.field_178030_c, p_178011_3_, 0, 0, true);
            }

            /**
             * Returns true if the mouse has been pressed on this control.
             */
            public boolean mousePressed(int p_148278_1_, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
            {
                boolean flag = this.func_178026_a(this.field_178029_b, p_148278_2_, p_148278_3_, p_148278_4_);
                boolean flag1 = this.func_178026_a(this.field_178030_c, p_148278_2_, p_148278_3_, p_148278_4_);
                return flag || flag1;
            }

            private boolean func_178026_a(Gui p_178026_1_, int p_178026_2_, int p_178026_3_, int p_178026_4_)
            {
                if (p_178026_1_ == null)
                {
                    return false;
                }
                else if (p_178026_1_ instanceof GuiButton)
                {
                    return this.func_178023_a((GuiButton)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
                }
                else
                {
                    if (p_178026_1_ instanceof GuiTextField)
                    {
                        this.func_178018_a((GuiTextField)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
                    }

                    return false;
                }
            }

            private boolean func_178023_a(GuiButton p_178023_1_, int p_178023_2_, int p_178023_3_, int p_178023_4_)
            {
                boolean flag = p_178023_1_.mousePressed(this.field_178031_a, p_178023_2_, p_178023_3_);

                if (flag)
                {
                    this.field_178028_d = p_178023_1_;
                }

                return flag;
            }

            private void func_178018_a(GuiTextField p_178018_1_, int p_178018_2_, int p_178018_3_, int p_178018_4_)
            {
                p_178018_1_.mouseClicked(p_178018_2_, p_178018_3_, p_178018_4_);

                if (p_178018_1_.isFocused())
                {
                    this.field_178028_d = p_178018_1_;
                }
            }

            /**
             * Fired when the mouse button is released. Arguments: index, x, y, mouseEvent, relativeX, relativeY
             */
            public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
            {
                this.func_178016_b(this.field_178029_b, x, y, mouseEvent);
                this.func_178016_b(this.field_178030_c, x, y, mouseEvent);
            }

            private void func_178016_b(Gui p_178016_1_, int p_178016_2_, int p_178016_3_, int p_178016_4_)
            {
                if (p_178016_1_ != null)
                {
                    if (p_178016_1_ instanceof GuiButton)
                    {
                        this.func_178019_b((GuiButton)p_178016_1_, p_178016_2_, p_178016_3_, p_178016_4_);
                    }
                }
            }

            private void func_178019_b(GuiButton p_178019_1_, int p_178019_2_, int p_178019_3_, int p_178019_4_)
            {
                p_178019_1_.mouseReleased(p_178019_2_, p_178019_3_);
            }
        }

    @SideOnly(Side.CLIENT)
    public static class GuiLabelEntry extends GuiPageButtonList.GuiListEntry
        {
            private static final String __OBFID = "CL_00001946";

            public GuiLabelEntry(int p_i45532_1_, String p_i45532_2_, boolean p_i45532_3_)
            {
                super(p_i45532_1_, p_i45532_2_, p_i45532_3_);
            }
        }

    @SideOnly(Side.CLIENT)
    public static class GuiListEntry
        {
            private final int field_178939_a;
            private final String field_178937_b;
            private final boolean field_178938_c;
            private static final String __OBFID = "CL_00001945";

            public GuiListEntry(int p_i45531_1_, String p_i45531_2_, boolean p_i45531_3_)
            {
                this.field_178939_a = p_i45531_1_;
                this.field_178937_b = p_i45531_2_;
                this.field_178938_c = p_i45531_3_;
            }

            public int func_178935_b()
            {
                return this.field_178939_a;
            }

            public String func_178936_c()
            {
                return this.field_178937_b;
            }

            public boolean func_178934_d()
            {
                return this.field_178938_c;
            }
        }

    @SideOnly(Side.CLIENT)
    public interface GuiResponder
    {
        void func_175321_a(int p_175321_1_, boolean p_175321_2_);

        void func_175320_a(int p_175320_1_, float p_175320_2_);

        void func_175319_a(int p_175319_1_, String p_175319_2_);
    }

    @SideOnly(Side.CLIENT)
    public static class GuiSlideEntry extends GuiPageButtonList.GuiListEntry
        {
            private final GuiSlider.FormatHelper field_178949_a;
            private final float field_178947_b;
            private final float field_178948_c;
            private final float field_178946_d;
            private static final String __OBFID = "CL_00001944";

            public GuiSlideEntry(int p_i45530_1_, String p_i45530_2_, boolean p_i45530_3_, GuiSlider.FormatHelper p_i45530_4_, float p_i45530_5_, float p_i45530_6_, float p_i45530_7_)
            {
                super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
                this.field_178949_a = p_i45530_4_;
                this.field_178947_b = p_i45530_5_;
                this.field_178948_c = p_i45530_6_;
                this.field_178946_d = p_i45530_7_;
            }

            public GuiSlider.FormatHelper func_178945_a()
            {
                return this.field_178949_a;
            }

            public float func_178943_e()
            {
                return this.field_178947_b;
            }

            public float func_178944_f()
            {
                return this.field_178948_c;
            }

            public float func_178942_g()
            {
                return this.field_178946_d;
            }
        }
}