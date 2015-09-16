package net.minecraft.client.renderer;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemModelMesher
{
    private final Map simpleShapes = Maps.newHashMap();
    private final Map simpleShapesCache = Maps.newHashMap();
    private final Map shapers = Maps.newHashMap();
    private final ModelManager modelManager;
    private static final String __OBFID = "CL_00002536";

    public ItemModelMesher(ModelManager modelManager)
    {
        this.modelManager = modelManager;
    }

    public TextureAtlasSprite getParticleIcon(Item item)
    {
        return this.getParticleIcon(item, 0);
    }

    public TextureAtlasSprite getParticleIcon(Item item, int meta)
    {
        return this.getItemModel(new ItemStack(item, 1, meta)).getTexture();
    }

    public IBakedModel getItemModel(ItemStack stack)
    {
        Item item = stack.getItem();
        IBakedModel ibakedmodel = this.getItemModel(item, this.getMetadata(stack));

        if (ibakedmodel == null)
        {
            ItemMeshDefinition itemmeshdefinition = (ItemMeshDefinition)this.shapers.get(item);

            if (itemmeshdefinition != null)
            {
                ibakedmodel = this.modelManager.getModel(itemmeshdefinition.getModelLocation(stack));
            }
        }

        if(ibakedmodel instanceof net.minecraftforge.client.model.ISmartItemModel)
        {
            ibakedmodel = ((net.minecraftforge.client.model.ISmartItemModel)ibakedmodel).handleItemState(stack);
        }

        if (ibakedmodel == null)
        {
            ibakedmodel = this.modelManager.getMissingModel();
        }

        return ibakedmodel;
    }

    protected int getMetadata(ItemStack stack)
    {
        return stack.isItemStackDamageable() ? 0 : stack.getMetadata();
    }

    protected IBakedModel getItemModel(Item item, int meta)
    {
        return (IBakedModel)this.simpleShapesCache.get(Integer.valueOf(this.getIndex(item, meta)));
    }

    private int getIndex(Item item, int meta)
    {
        return Item.getIdFromItem(item) << 16 | meta;
    }

    public void register(Item item, int meta, ModelResourceLocation location)
    {
        this.simpleShapes.put(Integer.valueOf(this.getIndex(item, meta)), location);
        this.simpleShapesCache.put(Integer.valueOf(this.getIndex(item, meta)), this.modelManager.getModel(location));
    }

    public void register(Item item, ItemMeshDefinition definition)
    {
        this.shapers.put(item, definition);
    }

    public ModelManager getModelManager()
    {
        return this.modelManager;
    }

    public void rebuildCache()
    {
        this.simpleShapesCache.clear();
        Iterator iterator = this.simpleShapes.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry entry = (Entry)iterator.next();
            this.simpleShapesCache.put(entry.getKey(), this.modelManager.getModel((ModelResourceLocation)entry.getValue()));
        }
    }
}