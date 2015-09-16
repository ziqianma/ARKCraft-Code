package net.minecraft.client.resources;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ResourcePackRepository
{
    private static final Logger logger = LogManager.getLogger();
    private static final FileFilter resourcePackFilter = new FileFilter()
    {
        private static final String __OBFID = "CL_00001088";
        public boolean accept(File p_accept_1_)
        {
            boolean flag = p_accept_1_.isFile() && p_accept_1_.getName().endsWith(".zip");
            boolean flag1 = p_accept_1_.isDirectory() && (new File(p_accept_1_, "pack.mcmeta")).isFile();
            return flag || flag1;
        }
    };
    private final File dirResourcepacks;
    public final IResourcePack rprDefaultResourcePack;
    private final File dirServerResourcepacks;
    public final IMetadataSerializer rprMetadataSerializer;
    private IResourcePack field_148532_f;
    private final ReentrantLock field_177321_h = new ReentrantLock();
    private ListenableFuture field_177322_i;
    private List repositoryEntriesAll = Lists.newArrayList();
    private List repositoryEntries = Lists.newArrayList();
    private static final String __OBFID = "CL_00001087";

    public ResourcePackRepository(File p_i45101_1_, File p_i45101_2_, IResourcePack p_i45101_3_, IMetadataSerializer p_i45101_4_, GameSettings p_i45101_5_)
    {
        this.dirResourcepacks = p_i45101_1_;
        this.dirServerResourcepacks = p_i45101_2_;
        this.rprDefaultResourcePack = p_i45101_3_;
        this.rprMetadataSerializer = p_i45101_4_;
        this.fixDirResourcepacks();
        this.updateRepositoryEntriesAll();
        Iterator iterator = p_i45101_5_.resourcePacks.iterator();

        while (iterator.hasNext())
        {
            String s = (String)iterator.next();
            Iterator iterator1 = this.repositoryEntriesAll.iterator();

            while (iterator1.hasNext())
            {
                ResourcePackRepository.Entry entry = (ResourcePackRepository.Entry)iterator1.next();

                if (entry.getResourcePackName().equals(s))
                {
                    this.repositoryEntries.add(entry);
                    break;
                }
            }
        }
    }

    private void fixDirResourcepacks()
    {
        if (!this.dirResourcepacks.isDirectory() && (!this.dirResourcepacks.delete() || !this.dirResourcepacks.mkdirs()))
        {
            logger.debug("Unable to create resourcepack folder: " + this.dirResourcepacks);
        }
    }

    private List getResourcePackFiles()
    {
        return this.dirResourcepacks.isDirectory() ? Arrays.asList(this.dirResourcepacks.listFiles(resourcePackFilter)) : Collections.emptyList();
    }

    public void updateRepositoryEntriesAll()
    {
        ArrayList arraylist = Lists.newArrayList();
        Iterator iterator = this.getResourcePackFiles().iterator();

        while (iterator.hasNext())
        {
            File file1 = (File)iterator.next();
            ResourcePackRepository.Entry entry = new ResourcePackRepository.Entry(file1, null);

            if (!this.repositoryEntriesAll.contains(entry))
            {
                try
                {
                    entry.updateResourcePack();
                    arraylist.add(entry);
                }
                catch (Exception exception)
                {
                    arraylist.remove(entry);
                }
            }
            else
            {
                int i = this.repositoryEntriesAll.indexOf(entry);

                if (i > -1 && i < this.repositoryEntriesAll.size())
                {
                    arraylist.add(this.repositoryEntriesAll.get(i));
                }
            }
        }

        this.repositoryEntriesAll.removeAll(arraylist);
        iterator = this.repositoryEntriesAll.iterator();

        while (iterator.hasNext())
        {
            ResourcePackRepository.Entry entry1 = (ResourcePackRepository.Entry)iterator.next();
            entry1.closeResourcePack();
        }

        this.repositoryEntriesAll = arraylist;
    }

    public List getRepositoryEntriesAll()
    {
        return ImmutableList.copyOf(this.repositoryEntriesAll);
    }

    public List getRepositoryEntries()
    {
        return ImmutableList.copyOf(this.repositoryEntries);
    }

    public void func_148527_a(List p_148527_1_)
    {
        this.repositoryEntries.clear();
        this.repositoryEntries.addAll(p_148527_1_);
    }

    public File getDirResourcepacks()
    {
        return this.dirResourcepacks;
    }

    public ListenableFuture func_180601_a(String p_180601_1_, String p_180601_2_)
    {
        String s2;

        if (p_180601_2_.matches("^[a-f0-9]{40}$"))
        {
            s2 = p_180601_2_;
        }
        else
        {
            s2 = p_180601_1_.substring(p_180601_1_.lastIndexOf("/") + 1);

            if (s2.contains("?"))
            {
                s2 = s2.substring(0, s2.indexOf("?"));
            }

            if (!s2.endsWith(".zip"))
            {
                return Futures.immediateFailedFuture(new IllegalArgumentException("Invalid filename; must end in .zip"));
            }

            s2 = "legacy_" + s2.replaceAll("\\W", "");
        }

        final File file1 = new File(this.dirServerResourcepacks, s2);
        this.field_177321_h.lock();
        ListenableFuture listenablefuture;

        try
        {
            this.func_148529_f();

            if (file1.exists() && p_180601_2_.length() == 40)
            {
                try
                {
                    String s3 = Hashing.sha1().hashBytes(Files.toByteArray(file1)).toString();

                    if (s3.equals(p_180601_2_))
                    {
                        ListenableFuture listenablefuture1 = this.func_177319_a(file1);
                        return listenablefuture1;
                    }

                    logger.warn("File " + file1 + " had wrong hash (expected " + p_180601_2_ + ", found " + s3 + "). Deleting it.");
                    FileUtils.deleteQuietly(file1);
                }
                catch (IOException ioexception)
                {
                    logger.warn("File " + file1 + " couldn\'t be hashed. Deleting it.", ioexception);
                    FileUtils.deleteQuietly(file1);
                }
            }

            final GuiScreenWorking guiscreenworking = new GuiScreenWorking();
            Map map = Minecraft.getSessionInfo();
            final Minecraft minecraft = Minecraft.getMinecraft();
            Futures.getUnchecked(minecraft.addScheduledTask(new Runnable()
            {
                private static final String __OBFID = "CL_00001089";
                public void run()
                {
                    minecraft.displayGuiScreen(guiscreenworking);
                }
            }));
            final SettableFuture settablefuture = SettableFuture.create();
            this.field_177322_i = HttpUtil.func_180192_a(file1, p_180601_1_, map, 52428800, guiscreenworking, minecraft.getProxy());
            Futures.addCallback(this.field_177322_i, new FutureCallback()
            {
                private static final String __OBFID = "CL_00002394";
                public void onSuccess(Object p_onSuccess_1_)
                {
                    ResourcePackRepository.this.func_177319_a(file1);
                    settablefuture.set((Object)null);
                }
                public void onFailure(Throwable p_onFailure_1_)
                {
                    settablefuture.setException(p_onFailure_1_);
                }
            });
            listenablefuture = this.field_177322_i;
        }
        finally
        {
            this.field_177321_h.unlock();
        }

        return listenablefuture;
    }

    public ListenableFuture func_177319_a(File p_177319_1_)
    {
        this.field_148532_f = new FileResourcePack(p_177319_1_);
        return Minecraft.getMinecraft().scheduleResourcesRefresh();
    }

    /**
     * Getter for the IResourcePack instance associated with this ResourcePackRepository
     */
    public IResourcePack getResourcePackInstance()
    {
        return this.field_148532_f;
    }

    public void func_148529_f()
    {
        this.field_177321_h.lock();

        try
        {
            if (this.field_177322_i != null)
            {
                this.field_177322_i.cancel(true);
            }

            this.field_177322_i = null;
            this.field_148532_f = null;
        }
        finally
        {
            this.field_177321_h.unlock();
        }
    }

    @SideOnly(Side.CLIENT)
    public class Entry
    {
        private final File resourcePackFile;
        private IResourcePack reResourcePack;
        private PackMetadataSection rePackMetadataSection;
        private BufferedImage texturePackIcon;
        private ResourceLocation locationTexturePackIcon;
        private static final String __OBFID = "CL_00001090";

        private Entry(File p_i1295_2_)
        {
            this.resourcePackFile = p_i1295_2_;
        }

        public void updateResourcePack() throws IOException
        {
            this.reResourcePack = (IResourcePack)(this.resourcePackFile.isDirectory() ? new FolderResourcePack(this.resourcePackFile) : new FileResourcePack(this.resourcePackFile));
            this.rePackMetadataSection = (PackMetadataSection)this.reResourcePack.getPackMetadata(ResourcePackRepository.this.rprMetadataSerializer, "pack");

            try
            {
                this.texturePackIcon = this.reResourcePack.getPackImage();
            }
            catch (IOException ioexception)
            {
                ;
            }

            if (this.texturePackIcon == null)
            {
                this.texturePackIcon = ResourcePackRepository.this.rprDefaultResourcePack.getPackImage();
            }

            this.closeResourcePack();
        }

        public void bindTexturePackIcon(TextureManager p_110518_1_)
        {
            if (this.locationTexturePackIcon == null)
            {
                this.locationTexturePackIcon = p_110518_1_.getDynamicTextureLocation("texturepackicon", new DynamicTexture(this.texturePackIcon));
            }

            p_110518_1_.bindTexture(this.locationTexturePackIcon);
        }

        public void closeResourcePack()
        {
            if (this.reResourcePack instanceof Closeable)
            {
                IOUtils.closeQuietly((Closeable)this.reResourcePack);
            }
        }

        public IResourcePack getResourcePack()
        {
            return this.reResourcePack;
        }

        public String getResourcePackName()
        {
            return this.reResourcePack.getPackName();
        }

        public String getTexturePackDescription()
        {
            return this.rePackMetadataSection == null ? EnumChatFormatting.RED + "Invalid pack.mcmeta (or missing \'pack\' section)" : this.rePackMetadataSection.getPackDescription().getFormattedText();
        }

        public boolean equals(Object p_equals_1_)
        {
            return this == p_equals_1_ ? true : (p_equals_1_ instanceof ResourcePackRepository.Entry ? this.toString().equals(p_equals_1_.toString()) : false);
        }

        public int hashCode()
        {
            return this.toString().hashCode();
        }

        public String toString()
        {
            return String.format("%s:%s:%d", new Object[] {this.resourcePackFile.getName(), this.resourcePackFile.isDirectory() ? "folder" : "zip", Long.valueOf(this.resourcePackFile.lastModified())});
        }

        Entry(File p_i1296_2_, Object p_i1296_3_)
        {
            this(p_i1296_2_);
        }
    }
}