package net.minecraft.client.main;

import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import java.net.Proxy;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GameConfiguration
{
    public final GameConfiguration.UserInformation userInfo;
    public final GameConfiguration.DisplayInformation displayInfo;
    public final GameConfiguration.FolderInformation folderInfo;
    public final GameConfiguration.GameInformation gameInfo;
    public final GameConfiguration.ServerInformation serverInfo;
    private static final String __OBFID = "CL_00001918";

    public GameConfiguration(GameConfiguration.UserInformation p_i45491_1_, GameConfiguration.DisplayInformation p_i45491_2_, GameConfiguration.FolderInformation p_i45491_3_, GameConfiguration.GameInformation p_i45491_4_, GameConfiguration.ServerInformation p_i45491_5_)
    {
        this.userInfo = p_i45491_1_;
        this.displayInfo = p_i45491_2_;
        this.folderInfo = p_i45491_3_;
        this.gameInfo = p_i45491_4_;
        this.serverInfo = p_i45491_5_;
    }

    @SideOnly(Side.CLIENT)
    public static class DisplayInformation
        {
            public final int width;
            public final int height;
            public final boolean fullscreen;
            public final boolean checkGlErrors;
            private static final String __OBFID = "CL_00001917";

            public DisplayInformation(int p_i45490_1_, int p_i45490_2_, boolean p_i45490_3_, boolean p_i45490_4_)
            {
                this.width = p_i45490_1_;
                this.height = p_i45490_2_;
                this.fullscreen = p_i45490_3_;
                this.checkGlErrors = p_i45490_4_;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class FolderInformation
        {
            public final File mcDataDir;
            public final File resourcePacksDir;
            public final File assetsDir;
            public final String assetIndex;
            private static final String __OBFID = "CL_00001916";

            public FolderInformation(File p_i45489_1_, File p_i45489_2_, File p_i45489_3_, String p_i45489_4_)
            {
                this.mcDataDir = p_i45489_1_;
                this.resourcePacksDir = p_i45489_2_;
                this.assetsDir = p_i45489_3_;
                this.assetIndex = p_i45489_4_;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class GameInformation
        {
            public final boolean isDemo;
            public final String version;
            private static final String __OBFID = "CL_00001915";

            public GameInformation(boolean p_i45488_1_, String p_i45488_2_)
            {
                this.isDemo = p_i45488_1_;
                this.version = p_i45488_2_;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class ServerInformation
        {
            public final String serverName;
            public final int serverPort;
            private static final String __OBFID = "CL_00001914";

            public ServerInformation(String p_i45487_1_, int p_i45487_2_)
            {
                this.serverName = p_i45487_1_;
                this.serverPort = p_i45487_2_;
            }
        }

    @SideOnly(Side.CLIENT)
    public static class UserInformation
        {
            public final Session session;
            public final PropertyMap userProperties;
            public final Proxy proxy;
            private static final String __OBFID = "CL_00001913";

            public UserInformation(Session p_i45486_1_, PropertyMap p_i45486_2_, Proxy p_i45486_3_)
            {
                this.session = p_i45486_1_;
                this.userProperties = p_i45486_2_;
                this.proxy = p_i45486_3_;
            }
        }
}