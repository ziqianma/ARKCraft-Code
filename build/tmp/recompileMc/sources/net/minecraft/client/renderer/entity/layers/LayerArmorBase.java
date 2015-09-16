package net.minecraft.client.renderer.entity.layers;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class LayerArmorBase implements LayerRenderer
{
    protected static final ResourceLocation field_177188_b = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected ModelBase field_177189_c;
    protected ModelBase field_177186_d;
    private final RendererLivingEntity field_177190_a;
    private float field_177187_e = 1.0F;
    private float field_177184_f = 1.0F;
    private float field_177185_g = 1.0F;
    private float field_177192_h = 1.0F;
    private boolean field_177193_i;
    private static final Map field_177191_j = Maps.newHashMap();
    private static final String __OBFID = "CL_00002428";

    public LayerArmorBase(RendererLivingEntity p_i46125_1_)
    {
        this.field_177190_a = p_i46125_1_;
        this.func_177177_a();
    }

    public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 4);
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 3);
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 2);
        this.func_177182_a(p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_, 1);
    }

    public boolean shouldCombineTextures()
    {
        return false;
    }

    private void func_177182_a(EntityLivingBase p_177182_1_, float p_177182_2_, float p_177182_3_, float p_177182_4_, float p_177182_5_, float p_177182_6_, float p_177182_7_, float p_177182_8_, int p_177182_9_)
    {
        ItemStack itemstack = this.func_177176_a(p_177182_1_, p_177182_9_);

        if (itemstack != null && itemstack.getItem() instanceof ItemArmor)
        {
            ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
            ModelBase modelbase = this.func_177175_a(p_177182_9_);
            modelbase.setModelAttributes(this.field_177190_a.getMainModel());
            modelbase.setLivingAnimations(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_4_);
            modelbase = net.minecraftforge.client.ForgeHooksClient.getArmorModel(p_177182_1_, itemstack, p_177182_9_, modelbase);
            this.func_177179_a(modelbase, p_177182_9_);
            boolean flag = this.func_177180_b(p_177182_9_);
            this.field_177190_a.bindTexture(this.getArmorResource(p_177182_1_, itemstack, flag ? 2 : 1, null));

            {
                int j = itemarmor.getColor(itemstack);
                if (j != -1) //Allow this for anything, not only cloth.
                {
                    float f7 = (float)(j >> 16 & 255) / 255.0F;
                    float f8 = (float)(j >> 8 & 255) / 255.0F;
                    float f9 = (float)(j & 255) / 255.0F;
                    GlStateManager.color(this.field_177184_f * f7, this.field_177185_g * f8, this.field_177192_h * f9, this.field_177187_e);
                    modelbase.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    this.field_177190_a.bindTexture(this.getArmorResource(p_177182_1_, itemstack, flag ? 2 : 1, "overlay"));
                }
                { // Non-cloth
                    GlStateManager.color(this.field_177184_f, this.field_177185_g, this.field_177192_h, this.field_177187_e);
                    modelbase.render(p_177182_1_, p_177182_2_, p_177182_3_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                }
                { // Default, Why is this a switch? there were no breaks.
                    if (!this.field_177193_i && itemstack.isItemEnchanted())
                    {
                        this.func_177183_a(p_177182_1_, modelbase, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_);
                    }
                }
            }
        }
    }

    public ItemStack func_177176_a(EntityLivingBase p_177176_1_, int p_177176_2_)
    {
        return p_177176_1_.getCurrentArmor(p_177176_2_ - 1);
    }

    public ModelBase func_177175_a(int p_177175_1_)
    {
        return this.func_177180_b(p_177175_1_) ? this.field_177189_c : this.field_177186_d;
    }

    private boolean func_177180_b(int p_177180_1_)
    {
        return p_177180_1_ == 2;
    }

    private void func_177183_a(EntityLivingBase p_177183_1_, ModelBase p_177183_2_, float p_177183_3_, float p_177183_4_, float p_177183_5_, float p_177183_6_, float p_177183_7_, float p_177183_8_, float p_177183_9_)
    {
        float f7 = (float)p_177183_1_.ticksExisted + p_177183_5_;
        this.field_177190_a.bindTexture(field_177188_b);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        float f8 = 0.5F;
        GlStateManager.color(f8, f8, f8, 1.0F);

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.disableLighting();
            GlStateManager.blendFunc(768, 1);
            float f9 = 0.76F;
            GlStateManager.color(0.5F * f9, 0.25F * f9, 0.8F * f9, 1.0F);
            GlStateManager.matrixMode(5890);
            GlStateManager.loadIdentity();
            float f10 = 0.33333334F;
            GlStateManager.scale(f10, f10, f10);
            GlStateManager.rotate(30.0F - (float)i * 60.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.translate(0.0F, f7 * (0.001F + (float)i * 0.003F) * 20.0F, 0.0F);
            GlStateManager.matrixMode(5888);
            p_177183_2_.render(p_177183_1_, p_177183_3_, p_177183_4_, p_177183_6_, p_177183_7_, p_177183_8_, p_177183_9_);
        }

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
    }

    @Deprecated //Use the more sensitive version getArmorResource below
    private ResourceLocation getArmorResource(ItemArmor p_177181_1_, boolean p_177181_2_)
    {
        return this.getArmorResource(p_177181_1_, p_177181_2_, (String)null);
    }

    @Deprecated //Use the more sensitive version getArmorResource below
    private ResourceLocation getArmorResource(ItemArmor p_177178_1_, boolean p_177178_2_, String p_177178_3_)
    {
        String s1 = String.format("textures/models/armor/%s_layer_%d%s.png", new Object[] {p_177178_1_.getArmorMaterial().getName(), Integer.valueOf(p_177178_2_ ? 2 : 1), p_177178_3_ == null ? "" : String.format("_%s", new Object[]{p_177178_3_})});
        ResourceLocation resourcelocation = (ResourceLocation)field_177191_j.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            field_177191_j.put(s1, resourcelocation);
        }

        return resourcelocation;
    }

    protected abstract void func_177177_a();

    protected abstract void func_177179_a(ModelBase p_177179_1_, int p_177179_2_);

    /*=================================== FORGE START =========================================*/
    /**
     * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
     *
     * @param entity Entity wearing the armor
     * @param stack ItemStack for the armor
     * @param slot Slot ID that the item is in
     * @param type Subtype, can be null or "overlay"
     * @return ResourceLocation pointing at the armor's texture
     */
    public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, int slot, String type)
    {
        ItemArmor item = (ItemArmor)stack.getItem();
        String texture = ((ItemArmor)stack.getItem()).getArmorMaterial().getName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1)
        {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture, (slot == 2 ? 2 : 1), type == null ? "" : String.format("_%s", type));

        s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
        ResourceLocation resourcelocation = (ResourceLocation)field_177191_j.get(s1);

        if (resourcelocation == null)
        {
            resourcelocation = new ResourceLocation(s1);
            field_177191_j.put(s1, resourcelocation);
        }

        return resourcelocation;
    }
    /*=================================== FORGE END ===========================================*/

    @SideOnly(Side.CLIENT)

    static final class SwitchArmorMaterial
        {
            static final int[] field_178747_a = new int[ItemArmor.ArmorMaterial.values().length];
            private static final String __OBFID = "CL_00002427";

            static
            {
                try
                {
                    field_178747_a[ItemArmor.ArmorMaterial.LEATHER.ordinal()] = 1;
                }
                catch (NoSuchFieldError var5)
                {
                    ;
                }

                try
                {
                    field_178747_a[ItemArmor.ArmorMaterial.CHAIN.ordinal()] = 2;
                }
                catch (NoSuchFieldError var4)
                {
                    ;
                }

                try
                {
                    field_178747_a[ItemArmor.ArmorMaterial.IRON.ordinal()] = 3;
                }
                catch (NoSuchFieldError var3)
                {
                    ;
                }

                try
                {
                    field_178747_a[ItemArmor.ArmorMaterial.GOLD.ordinal()] = 4;
                }
                catch (NoSuchFieldError var2)
                {
                    ;
                }

                try
                {
                    field_178747_a[ItemArmor.ArmorMaterial.DIAMOND.ordinal()] = 5;
                }
                catch (NoSuchFieldError var1)
                {
                    ;
                }
            }
        }
}