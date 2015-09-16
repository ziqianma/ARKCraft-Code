package net.minecraft.client.main;

import com.google.gson.GsonBuilder;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.properties.PropertyMap.Serializer;
import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Main
{
    private static final String __OBFID = "CL_00001461";

    public static void main(String[] p_main_0_)
    {
        System.setProperty("java.net.preferIPv4Stack", "true");
        OptionParser optionparser = new OptionParser();
        optionparser.allowsUnrecognizedOptions();
        optionparser.accepts("demo");
        optionparser.accepts("fullscreen");
        optionparser.accepts("checkGlErrors");
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec = optionparser.accepts("server").withRequiredArg();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec1 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(25565), new Integer[0]);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec2 = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec3 = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec4 = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec5 = optionparser.accepts("proxyHost").withRequiredArg();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec6 = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec7 = optionparser.accepts("proxyUser").withRequiredArg();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec8 = optionparser.accepts("proxyPass").withRequiredArg();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec9 = optionparser.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec10 = optionparser.accepts("uuid").withRequiredArg();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec11 = optionparser.accepts("accessToken").withRequiredArg().required();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec12 = optionparser.accepts("version").withRequiredArg().required();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec13 = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(854), new Integer[0]);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec14 = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(480), new Integer[0]);
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec15 = optionparser.accepts("userProperties").withRequiredArg().required();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec16 = optionparser.accepts("assetIndex").withRequiredArg();
        ArgumentAcceptingOptionSpec argumentacceptingoptionspec17 = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy", new String[0]);
        NonOptionArgumentSpec nonoptionargumentspec = optionparser.nonOptions();
        OptionSet optionset = optionparser.parse(p_main_0_);
        List list = optionset.valuesOf(nonoptionargumentspec);

        if (!list.isEmpty())
        {
            System.out.println("Completely ignored arguments: " + list);
        }

        String s = (String)optionset.valueOf(argumentacceptingoptionspec5);
        Proxy proxy = Proxy.NO_PROXY;

        if (s != null)
        {
            try
            {
                proxy = new Proxy(Type.SOCKS, new InetSocketAddress(s, ((Integer)optionset.valueOf(argumentacceptingoptionspec6)).intValue()));
            }
            catch (Exception exception)
            {
                ;
            }
        }

        final String s1 = (String)optionset.valueOf(argumentacceptingoptionspec7);
        final String s2 = (String)optionset.valueOf(argumentacceptingoptionspec8);

        if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(s1) && isNullOrEmpty(s2))
        {
            Authenticator.setDefault(new Authenticator()
            {
                private static final String __OBFID = "CL_00000828";
                protected PasswordAuthentication getPasswordAuthentication()
                {
                    return new PasswordAuthentication(s1, s2.toCharArray());
                }
            });
        }

        int i = ((Integer)optionset.valueOf(argumentacceptingoptionspec13)).intValue();
        int j = ((Integer)optionset.valueOf(argumentacceptingoptionspec14)).intValue();
        boolean flag = optionset.has("fullscreen");
        boolean flag1 = optionset.has("checkGlErrors");
        boolean flag2 = optionset.has("demo");
        String s3 = (String)optionset.valueOf(argumentacceptingoptionspec12);
        PropertyMap propertymap = (PropertyMap)(new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new Serializer()).create().fromJson((String)optionset.valueOf(argumentacceptingoptionspec15), PropertyMap.class);
        File file1 = (File)optionset.valueOf(argumentacceptingoptionspec2);
        File file2 = optionset.has(argumentacceptingoptionspec3) ? (File)optionset.valueOf(argumentacceptingoptionspec3) : new File(file1, "assets/");
        File file3 = optionset.has(argumentacceptingoptionspec4) ? (File)optionset.valueOf(argumentacceptingoptionspec4) : new File(file1, "resourcepacks/");
        String s4 = optionset.has(argumentacceptingoptionspec10) ? (String)argumentacceptingoptionspec10.value(optionset) : (String)argumentacceptingoptionspec9.value(optionset);
        String s5 = optionset.has(argumentacceptingoptionspec16) ? (String)argumentacceptingoptionspec16.value(optionset) : null;
        String s6 = (String)optionset.valueOf(argumentacceptingoptionspec);
        Integer integer = (Integer)optionset.valueOf(argumentacceptingoptionspec1);
        Session session = new Session((String)argumentacceptingoptionspec9.value(optionset), s4, (String)argumentacceptingoptionspec11.value(optionset), (String)argumentacceptingoptionspec17.value(optionset));
        GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag1), new GameConfiguration.FolderInformation(file1, file3, file2, s5), new GameConfiguration.GameInformation(flag2, s3), new GameConfiguration.ServerInformation(s6, integer.intValue()));
        Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread")
        {
            private static final String __OBFID = "CL_00000829";
            public void run()
            {
                Minecraft.stopIntegratedServer();
            }
        });
        Thread.currentThread().setName("Client thread");
        (new Minecraft(gameconfiguration)).run();
    }

    private static boolean isNullOrEmpty(String str)
    {
        return str != null && !str.isEmpty();
    }
}