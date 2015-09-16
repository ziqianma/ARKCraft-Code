package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class FallbackResourceManager implements IResourceManager
{
    private static final Logger logger = LogManager.getLogger();
    protected final List resourcePacks = Lists.newArrayList();
    private final IMetadataSerializer frmMetadataSerializer;
    private static final String __OBFID = "CL_00001074";

    public FallbackResourceManager(IMetadataSerializer p_i1289_1_)
    {
        this.frmMetadataSerializer = p_i1289_1_;
    }

    public void addResourcePack(IResourcePack p_110538_1_)
    {
        this.resourcePacks.add(p_110538_1_);
    }

    public Set getResourceDomains()
    {
        return null;
    }

    public IResource getResource(ResourceLocation p_110536_1_) throws IOException
    {
        IResourcePack iresourcepack = null;
        ResourceLocation resourcelocation1 = getLocationMcmeta(p_110536_1_);

        for (int i = this.resourcePacks.size() - 1; i >= 0; --i)
        {
            IResourcePack iresourcepack1 = (IResourcePack)this.resourcePacks.get(i);

            if (iresourcepack == null && iresourcepack1.resourceExists(resourcelocation1))
            {
                iresourcepack = iresourcepack1;
            }

            if (iresourcepack1.resourceExists(p_110536_1_))
            {
                InputStream inputstream = null;

                if (iresourcepack != null)
                {
                    inputstream = this.getInputStream(resourcelocation1, iresourcepack);
                }

                return new SimpleResource(iresourcepack1.getPackName(), p_110536_1_, this.getInputStream(p_110536_1_, iresourcepack1), inputstream, this.frmMetadataSerializer);
            }
        }

        throw new FileNotFoundException(p_110536_1_.toString());
    }

    @SuppressWarnings("resource")
    protected InputStream getInputStream(ResourceLocation p_177245_1_, IResourcePack p_177245_2_) throws IOException
    {
        InputStream inputstream = p_177245_2_.getInputStream(p_177245_1_);
        return (InputStream)(logger.isDebugEnabled() ? new FallbackResourceManager.ImputStreamLeakedResourceLogger(inputstream, p_177245_1_, p_177245_2_.getPackName()) : inputstream);
    }

    public List getAllResources(ResourceLocation p_135056_1_) throws IOException
    {
        ArrayList arraylist = Lists.newArrayList();
        ResourceLocation resourcelocation1 = getLocationMcmeta(p_135056_1_);
        Iterator iterator = this.resourcePacks.iterator();

        while (iterator.hasNext())
        {
            IResourcePack iresourcepack = (IResourcePack)iterator.next();

            if (iresourcepack.resourceExists(p_135056_1_))
            {
                InputStream inputstream = iresourcepack.resourceExists(resourcelocation1) ? this.getInputStream(resourcelocation1, iresourcepack) : null;
                arraylist.add(new SimpleResource(iresourcepack.getPackName(), p_135056_1_, this.getInputStream(p_135056_1_, iresourcepack), inputstream, this.frmMetadataSerializer));
            }
        }

        if (arraylist.isEmpty())
        {
            throw new FileNotFoundException(p_135056_1_.toString());
        }
        else
        {
            return arraylist;
        }
    }

    static ResourceLocation getLocationMcmeta(ResourceLocation p_110537_0_)
    {
        return new ResourceLocation(p_110537_0_.getResourceDomain(), p_110537_0_.getResourcePath() + ".mcmeta");
    }

    @SideOnly(Side.CLIENT)
    static class ImputStreamLeakedResourceLogger extends InputStream
        {
            private final InputStream field_177330_a;
            private final String field_177328_b;
            private boolean field_177329_c = false;
            private static final String __OBFID = "CL_00002395";

            public ImputStreamLeakedResourceLogger(InputStream p_i46093_1_, ResourceLocation p_i46093_2_, String p_i46093_3_)
            {
                this.field_177330_a = p_i46093_1_;
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                (new Exception()).printStackTrace(new PrintStream(bytearrayoutputstream));
                this.field_177328_b = "Leaked resource: \'" + p_i46093_2_ + "\' loaded from pack: \'" + p_i46093_3_ + "\'\n" + bytearrayoutputstream.toString();
            }

            public void close() throws IOException
            {
                this.field_177330_a.close();
                this.field_177329_c = true;
            }

            protected void finalize() throws Throwable
            {
                if (!this.field_177329_c)
                {
                    FallbackResourceManager.logger.warn(this.field_177328_b);
                }

                super.finalize();
            }

            public int read() throws IOException
            {
                return this.field_177330_a.read();
            }
        }
}