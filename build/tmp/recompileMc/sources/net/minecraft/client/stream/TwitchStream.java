package net.minecraft.client.stream;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.properties.Property;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Util;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.opengl.GL11;
import tv.twitch.AuthToken;
import tv.twitch.ErrorCode;
import tv.twitch.broadcast.EncodingCpuUsage;
import tv.twitch.broadcast.FrameBuffer;
import tv.twitch.broadcast.GameInfo;
import tv.twitch.broadcast.IngestList;
import tv.twitch.broadcast.IngestServer;
import tv.twitch.broadcast.StreamInfo;
import tv.twitch.broadcast.VideoParams;
import tv.twitch.chat.ChatRawMessage;
import tv.twitch.chat.ChatTokenizedMessage;
import tv.twitch.chat.ChatUserInfo;
import tv.twitch.chat.ChatUserMode;
import tv.twitch.chat.ChatUserSubscription;

@SideOnly(Side.CLIENT)
public class TwitchStream implements BroadcastController.BroadcastListener, ChatController.ChatListener, IngestServerTester.IngestTestListener, IStream
{
    private static final Logger field_152950_b = LogManager.getLogger();
    public static final Marker field_152949_a = MarkerManager.getMarker("STREAM");
    private final BroadcastController broadcastController;
    private final ChatController chatController;
    private String field_176029_e;
    private final Minecraft field_152953_e;
    private final IChatComponent field_152954_f = new ChatComponentText("Twitch");
    private final Map field_152955_g = Maps.newHashMap();
    private Framebuffer field_152956_h;
    private boolean field_152957_i;
    private int field_152958_j = 30;
    private long field_152959_k = 0L;
    private boolean field_152960_l = false;
    private boolean loggedIn;
    private boolean field_152962_n;
    private boolean field_152963_o;
    private IStream.AuthFailureReason field_152964_p;
    private static boolean field_152965_q;
    private static final String __OBFID = "CL_00001812";

    public TwitchStream(Minecraft mcIn, final Property p_i46057_2_)
    {
        this.field_152964_p = IStream.AuthFailureReason.ERROR;
        this.field_152953_e = mcIn;
        this.broadcastController = new BroadcastController();
        this.chatController = new ChatController();
        this.broadcastController.func_152841_a(this);
        this.chatController.func_152990_a(this);
        this.broadcastController.func_152842_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.chatController.func_152984_a("nmt37qblda36pvonovdkbopzfzw3wlq");
        this.field_152954_f.getChatStyle().setColor(EnumChatFormatting.DARK_PURPLE);

        if (p_i46057_2_ != null && !Strings.isNullOrEmpty(p_i46057_2_.getValue()) && OpenGlHelper.framebufferSupported)
        {
            Thread thread = new Thread("Twitch authenticator")
            {
                private static final String __OBFID = "CL_00001811";
                public void run()
                {
                    try
                    {
                        URL url = new URL("https://api.twitch.tv/kraken?oauth_token=" + URLEncoder.encode(p_i46057_2_.getValue(), "UTF-8"));
                        String s = HttpUtil.get(url);
                        JsonObject jsonobject = JsonUtils.getElementAsJsonObject((new JsonParser()).parse(s), "Response");
                        JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonobject, "token");

                        if (JsonUtils.getJsonObjectBooleanFieldValue(jsonobject1, "valid"))
                        {
                            String s1 = JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "user_name");
                            TwitchStream.field_152950_b.debug(TwitchStream.field_152949_a, "Authenticated with twitch; username is {}", new Object[] {s1});
                            AuthToken authtoken = new AuthToken();
                            authtoken.data = p_i46057_2_.getValue();
                            TwitchStream.this.broadcastController.func_152818_a(s1, authtoken);
                            TwitchStream.this.chatController.func_152998_c(s1);
                            TwitchStream.this.chatController.func_152994_a(authtoken);
                            Runtime.getRuntime().addShutdownHook(new Thread("Twitch shutdown hook")
                            {
                                private static final String __OBFID = "CL_00001810";
                                public void run()
                                {
                                    TwitchStream.this.shutdownStream();
                                }
                            });
                            TwitchStream.this.broadcastController.func_152817_A();
                            TwitchStream.this.chatController.func_175984_n();
                        }
                        else
                        {
                            TwitchStream.this.field_152964_p = IStream.AuthFailureReason.INVALID_TOKEN;
                            TwitchStream.field_152950_b.error(TwitchStream.field_152949_a, "Given twitch access token is invalid");
                        }
                    }
                    catch (IOException ioexception)
                    {
                        TwitchStream.this.field_152964_p = IStream.AuthFailureReason.ERROR;
                        TwitchStream.field_152950_b.error(TwitchStream.field_152949_a, "Could not authenticate with twitch", ioexception);
                    }
                }
            };
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Shuts down a steam
     */
    public void shutdownStream()
    {
        field_152950_b.debug(field_152949_a, "Shutdown streaming");
        this.broadcastController.statCallback();
        this.chatController.func_175988_p();
    }

    public void func_152935_j()
    {
        int i = this.field_152953_e.gameSettings.streamChatEnabled;
        boolean flag = this.field_176029_e != null && this.chatController.func_175990_d(this.field_176029_e);
        boolean flag1 = this.chatController.func_153000_j() == ChatController.ChatState.Initialized && (this.field_176029_e == null || this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected);

        if (i == 2)
        {
            if (flag)
            {
                field_152950_b.debug(field_152949_a, "Disconnecting from twitch chat per user options");
                this.chatController.func_175991_l(this.field_176029_e);
            }
        }
        else if (i == 1)
        {
            if (flag1 && this.broadcastController.func_152849_q())
            {
                field_152950_b.debug(field_152949_a, "Connecting to twitch chat per user options");
                this.func_152942_I();
            }
        }
        else if (i == 0)
        {
            if (flag && !this.func_152934_n())
            {
                field_152950_b.debug(field_152949_a, "Disconnecting from twitch chat as user is no longer streaming");
                this.chatController.func_175991_l(this.field_176029_e);
            }
            else if (flag1 && this.func_152934_n())
            {
                field_152950_b.debug(field_152949_a, "Connecting to twitch chat as user is streaming");
                this.func_152942_I();
            }
        }

        this.broadcastController.func_152821_H();
        this.chatController.func_152997_n();
    }

    protected void func_152942_I()
    {
        ChatController.ChatState chatstate = this.chatController.func_153000_j();
        String s = this.broadcastController.getChannelInfo().name;
        this.field_176029_e = s;

        if (chatstate != ChatController.ChatState.Initialized)
        {
            field_152950_b.warn("Invalid twitch chat state {}", new Object[] {chatstate});
        }
        else if (this.chatController.func_175989_e(this.field_176029_e) == ChatController.EnumChannelState.Disconnected)
        {
            this.chatController.func_152986_d(s);
        }
        else
        {
            field_152950_b.warn("Invalid twitch chat state {}", new Object[] {chatstate});
        }
    }

    public void func_152922_k()
    {
        if (this.broadcastController.isBroadcasting() && !this.broadcastController.isBroadcastPaused())
        {
            long i = System.nanoTime();
            long j = (long)(1000000000 / this.field_152958_j);
            long k = i - this.field_152959_k;
            boolean flag = k >= j;

            if (flag)
            {
                FrameBuffer framebuffer = this.broadcastController.func_152822_N();
                Framebuffer framebuffer1 = this.field_152953_e.getFramebuffer();
                this.field_152956_h.bindFramebuffer(true);
                GlStateManager.matrixMode(5889);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.ortho(0.0D, (double)this.field_152956_h.framebufferWidth, (double)this.field_152956_h.framebufferHeight, 0.0D, 1000.0D, 3000.0D);
                GlStateManager.matrixMode(5888);
                GlStateManager.pushMatrix();
                GlStateManager.loadIdentity();
                GlStateManager.translate(0.0F, 0.0F, -2000.0F);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.viewport(0, 0, this.field_152956_h.framebufferWidth, this.field_152956_h.framebufferHeight);
                GlStateManager.enableTexture2D();
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                float f = (float)this.field_152956_h.framebufferWidth;
                float f1 = (float)this.field_152956_h.framebufferHeight;
                float f2 = (float)framebuffer1.framebufferWidth / (float)framebuffer1.framebufferTextureWidth;
                float f3 = (float)framebuffer1.framebufferHeight / (float)framebuffer1.framebufferTextureHeight;
                framebuffer1.bindFramebufferTexture();
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 9729.0F);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 9729.0F);
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.startDrawingQuads();
                worldrenderer.addVertexWithUV(0.0D, (double)f1, 0.0D, 0.0D, (double)f3);
                worldrenderer.addVertexWithUV((double)f, (double)f1, 0.0D, (double)f2, (double)f3);
                worldrenderer.addVertexWithUV((double)f, 0.0D, 0.0D, (double)f2, 0.0D);
                worldrenderer.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
                tessellator.draw();
                framebuffer1.unbindFramebufferTexture();
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5889);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
                this.broadcastController.func_152846_a(framebuffer);
                this.field_152956_h.unbindFramebuffer();
                this.broadcastController.func_152859_b(framebuffer);
                this.field_152959_k = i;
            }
        }
    }

    public boolean func_152936_l()
    {
        return this.broadcastController.func_152849_q();
    }

    public boolean func_152924_m()
    {
        return this.broadcastController.isReadyToBroadcast();
    }

    public boolean func_152934_n()
    {
        return this.broadcastController.isBroadcasting();
    }

    public void func_152911_a(Metadata p_152911_1_, long p_152911_2_)
    {
        if (this.func_152934_n() && this.field_152957_i)
        {
            long j = this.broadcastController.func_152844_x();

            if (!this.broadcastController.func_152840_a(p_152911_1_.func_152810_c(), j + p_152911_2_, p_152911_1_.func_152809_a(), p_152911_1_.func_152806_b()))
            {
                field_152950_b.warn(field_152949_a, "Couldn\'t send stream metadata action at {}: {}", new Object[] {Long.valueOf(j + p_152911_2_), p_152911_1_});
            }
            else
            {
                field_152950_b.debug(field_152949_a, "Sent stream metadata action at {}: {}", new Object[] {Long.valueOf(j + p_152911_2_), p_152911_1_});
            }
        }
    }

    public void func_176026_a(Metadata p_176026_1_, long p_176026_2_, long p_176026_4_)
    {
        if (this.func_152934_n() && this.field_152957_i)
        {
            long k = this.broadcastController.func_152844_x();
            String s = p_176026_1_.func_152809_a();
            String s1 = p_176026_1_.func_152806_b();
            long l = this.broadcastController.func_177946_b(p_176026_1_.func_152810_c(), k + p_176026_2_, s, s1);

            if (l < 0L)
            {
                field_152950_b.warn(field_152949_a, "Could not send stream metadata sequence from {} to {}: {}", new Object[] {Long.valueOf(k + p_176026_2_), Long.valueOf(k + p_176026_4_), p_176026_1_});
            }
            else if (this.broadcastController.func_177947_a(p_176026_1_.func_152810_c(), k + p_176026_4_, l, s, s1))
            {
                field_152950_b.debug(field_152949_a, "Sent stream metadata sequence from {} to {}: {}", new Object[] {Long.valueOf(k + p_176026_2_), Long.valueOf(k + p_176026_4_), p_176026_1_});
            }
            else
            {
                field_152950_b.warn(field_152949_a, "Half-sent stream metadata sequence from {} to {}: {}", new Object[] {Long.valueOf(k + p_176026_2_), Long.valueOf(k + p_176026_4_), p_176026_1_});
            }
        }
    }

    public boolean isPaused()
    {
        return this.broadcastController.isBroadcastPaused();
    }

    public void func_152931_p()
    {
        if (this.broadcastController.func_152830_D())
        {
            field_152950_b.debug(field_152949_a, "Requested commercial from Twitch");
        }
        else
        {
            field_152950_b.warn(field_152949_a, "Could not request commercial from Twitch");
        }
    }

    public void func_152916_q()
    {
        this.broadcastController.func_152847_F();
        this.field_152962_n = true;
        this.updateStreamVolume();
    }

    public void func_152933_r()
    {
        this.broadcastController.func_152854_G();
        this.field_152962_n = false;
        this.updateStreamVolume();
    }

    public void updateStreamVolume()
    {
        if (this.func_152934_n())
        {
            float f = this.field_152953_e.gameSettings.streamGameVolume;
            boolean flag = this.field_152962_n || f <= 0.0F;
            this.broadcastController.setPlaybackDeviceVolume(flag ? 0.0F : f);
            this.broadcastController.setRecordingDeviceVolume(this.func_152929_G() ? 0.0F : this.field_152953_e.gameSettings.streamMicVolume);
        }
    }

    public void func_152930_t()
    {
        GameSettings gamesettings = this.field_152953_e.gameSettings;
        VideoParams videoparams = this.broadcastController.func_152834_a(formatStreamKbps(gamesettings.streamKbps), formatStreamFps(gamesettings.streamFps), formatStreamBps(gamesettings.streamBytesPerPixel), (float)this.field_152953_e.displayWidth / (float)this.field_152953_e.displayHeight);

        switch (gamesettings.streamCompression)
        {
            case 0:
                videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_LOW;
                break;
            case 1:
                videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_MEDIUM;
                break;
            case 2:
                videoparams.encodingCpuUsage = EncodingCpuUsage.TTV_ECU_HIGH;
        }

        if (this.field_152956_h == null)
        {
            this.field_152956_h = new Framebuffer(videoparams.outputWidth, videoparams.outputHeight, false);
        }
        else
        {
            this.field_152956_h.createBindFramebuffer(videoparams.outputWidth, videoparams.outputHeight);
        }

        if (gamesettings.streamPreferredServer != null && gamesettings.streamPreferredServer.length() > 0)
        {
            IngestServer[] aingestserver = this.func_152925_v();
            int i = aingestserver.length;

            for (int j = 0; j < i; ++j)
            {
                IngestServer ingestserver = aingestserver[j];

                if (ingestserver.serverUrl.equals(gamesettings.streamPreferredServer))
                {
                    this.broadcastController.func_152824_a(ingestserver);
                    break;
                }
            }
        }

        this.field_152958_j = videoparams.targetFps;
        this.field_152957_i = gamesettings.streamSendMetadata;
        this.broadcastController.func_152836_a(videoparams);
        field_152950_b.info(field_152949_a, "Streaming at {}/{} at {} kbps to {}", new Object[] {Integer.valueOf(videoparams.outputWidth), Integer.valueOf(videoparams.outputHeight), Integer.valueOf(videoparams.maxKbps), this.broadcastController.func_152833_s().serverUrl});
        this.broadcastController.func_152828_a((String)null, "Minecraft", (String)null);
    }

    public void func_152914_u()
    {
        if (this.broadcastController.func_152819_E())
        {
            field_152950_b.info(field_152949_a, "Stopped streaming to Twitch");
        }
        else
        {
            field_152950_b.warn(field_152949_a, "Could not stop streaming to Twitch");
        }
    }

    public void func_152900_a(ErrorCode p_152900_1_, AuthToken p_152900_2_) {}

    public void func_152897_a(ErrorCode p_152897_1_)
    {
        if (ErrorCode.succeeded(p_152897_1_))
        {
            field_152950_b.debug(field_152949_a, "Login attempt successful");
            this.loggedIn = true;
        }
        else
        {
            field_152950_b.warn(field_152949_a, "Login attempt unsuccessful: {} (error code {})", new Object[] {ErrorCode.getString(p_152897_1_), Integer.valueOf(p_152897_1_.getValue())});
            this.loggedIn = false;
        }
    }

    public void func_152898_a(ErrorCode p_152898_1_, GameInfo[] p_152898_2_) {}

    public void func_152891_a(BroadcastController.BroadcastState p_152891_1_)
    {
        field_152950_b.debug(field_152949_a, "Broadcast state changed to {}", new Object[] {p_152891_1_});

        if (p_152891_1_ == BroadcastController.BroadcastState.Initialized)
        {
            this.broadcastController.func_152827_a(BroadcastController.BroadcastState.Authenticated);
        }
    }

    public void func_152895_a()
    {
        field_152950_b.info(field_152949_a, "Logged out of twitch");
    }

    public void func_152894_a(StreamInfo p_152894_1_)
    {
        field_152950_b.debug(field_152949_a, "Stream info updated; {} viewers on stream ID {}", new Object[] {Integer.valueOf(p_152894_1_.viewers), Long.valueOf(p_152894_1_.streamId)});
    }

    public void func_152896_a(IngestList p_152896_1_) {}

    public void func_152893_b(ErrorCode p_152893_1_)
    {
        field_152950_b.warn(field_152949_a, "Issue submitting frame: {} (Error code {})", new Object[] {ErrorCode.getString(p_152893_1_), Integer.valueOf(p_152893_1_.getValue())});
        this.field_152953_e.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText("Issue streaming frame: " + p_152893_1_ + " (" + ErrorCode.getString(p_152893_1_) + ")"), 2);
    }

    public void func_152899_b()
    {
        this.updateStreamVolume();
        field_152950_b.info(field_152949_a, "Broadcast to Twitch has started");
    }

    public void func_152901_c()
    {
        field_152950_b.info(field_152949_a, "Broadcast to Twitch has stopped");
    }

    public void func_152892_c(ErrorCode p_152892_1_)
    {
        ChatComponentTranslation chatcomponenttranslation;

        if (p_152892_1_ == ErrorCode.TTV_EC_SOUNDFLOWER_NOT_INSTALLED)
        {
            chatcomponenttranslation = new ChatComponentTranslation("stream.unavailable.soundflower.chat.link", new Object[0]);
            chatcomponenttranslation.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://help.mojang.com/customer/portal/articles/1374877-configuring-soundflower-for-streaming-on-apple-computers"));
            chatcomponenttranslation.getChatStyle().setUnderlined(Boolean.valueOf(true));
            ChatComponentTranslation chatcomponenttranslation1 = new ChatComponentTranslation("stream.unavailable.soundflower.chat", new Object[] {chatcomponenttranslation});
            chatcomponenttranslation1.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.field_152953_e.ingameGUI.getChatGUI().printChatMessage(chatcomponenttranslation1);
        }
        else
        {
            chatcomponenttranslation = new ChatComponentTranslation("stream.unavailable.unknown.chat", new Object[] {ErrorCode.getString(p_152892_1_)});
            chatcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_RED);
            this.field_152953_e.ingameGUI.getChatGUI().printChatMessage(chatcomponenttranslation);
        }
    }

    public void func_152907_a(IngestServerTester p_152907_1_, IngestServerTester.IngestTestState p_152907_2_)
    {
        field_152950_b.debug(field_152949_a, "Ingest test state changed to {}", new Object[] {p_152907_2_});

        if (p_152907_2_ == IngestServerTester.IngestTestState.Finished)
        {
            this.field_152960_l = true;
        }
    }

    public static int formatStreamFps(float p_152948_0_)
    {
        return MathHelper.floor_float(10.0F + p_152948_0_ * 50.0F);
    }

    public static int formatStreamKbps(float p_152946_0_)
    {
        return MathHelper.floor_float(230.0F + p_152946_0_ * 3270.0F);
    }

    public static float formatStreamBps(float p_152947_0_)
    {
        return 0.1F + p_152947_0_ * 0.1F;
    }

    public IngestServer[] func_152925_v()
    {
        return this.broadcastController.func_152855_t().getServers();
    }

    public void func_152909_x()
    {
        IngestServerTester ingestservertester = this.broadcastController.func_152838_J();

        if (ingestservertester != null)
        {
            ingestservertester.func_153042_a(this);
        }
    }

    public IngestServerTester func_152932_y()
    {
        return this.broadcastController.isReady();
    }

    public boolean func_152908_z()
    {
        return this.broadcastController.isIngestTesting();
    }

    public int func_152920_A()
    {
        return this.func_152934_n() ? this.broadcastController.getStreamInfo().viewers : 0;
    }

    public void func_176023_d(ErrorCode p_176023_1_)
    {
        if (ErrorCode.failed(p_176023_1_))
        {
            field_152950_b.error(field_152949_a, "Chat failed to initialize");
        }
    }

    public void func_176022_e(ErrorCode p_176022_1_)
    {
        if (ErrorCode.failed(p_176022_1_))
        {
            field_152950_b.error(field_152949_a, "Chat failed to shutdown");
        }
    }

    public void func_176017_a(ChatController.ChatState p_176017_1_) {}

    public void func_180605_a(String p_180605_1_, ChatRawMessage[] p_180605_2_)
    {
        ChatRawMessage[] achatrawmessage1 = p_180605_2_;
        int i = p_180605_2_.length;

        for (int j = 0; j < i; ++j)
        {
            ChatRawMessage chatrawmessage = achatrawmessage1[j];
            this.func_176027_a(chatrawmessage.userName, chatrawmessage);

            if (this.func_176028_a(chatrawmessage.modes, chatrawmessage.subscriptions, this.field_152953_e.gameSettings.streamChatUserFilter))
            {
                ChatComponentText chatcomponenttext = new ChatComponentText(chatrawmessage.userName);
                ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("chat.stream." + (chatrawmessage.action ? "emote" : "text"), new Object[] {this.field_152954_f, chatcomponenttext, EnumChatFormatting.getTextWithoutFormattingCodes(chatrawmessage.message)});

                if (chatrawmessage.action)
                {
                    chatcomponenttranslation.getChatStyle().setItalic(Boolean.valueOf(true));
                }

                ChatComponentText chatcomponenttext1 = new ChatComponentText("");
                chatcomponenttext1.appendSibling(new ChatComponentTranslation("stream.userinfo.chatTooltip", new Object[0]));
                Iterator iterator = GuiTwitchUserMode.func_152328_a(chatrawmessage.modes, chatrawmessage.subscriptions, (IStream)null).iterator();

                while (iterator.hasNext())
                {
                    IChatComponent ichatcomponent = (IChatComponent)iterator.next();
                    chatcomponenttext1.appendText("\n");
                    chatcomponenttext1.appendSibling(ichatcomponent);
                }

                chatcomponenttext.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, chatcomponenttext1));
                chatcomponenttext.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.TWITCH_USER_INFO, chatrawmessage.userName));
                this.field_152953_e.ingameGUI.getChatGUI().printChatMessage(chatcomponenttranslation);
            }
        }
    }

    public void func_176025_a(String p_176025_1_, ChatTokenizedMessage[] p_176025_2_) {}

    private void func_176027_a(String p_176027_1_, ChatRawMessage p_176027_2_)
    {
        ChatUserInfo chatuserinfo = (ChatUserInfo)this.field_152955_g.get(p_176027_1_);

        if (chatuserinfo == null)
        {
            chatuserinfo = new ChatUserInfo();
            chatuserinfo.displayName = p_176027_1_;
            this.field_152955_g.put(p_176027_1_, chatuserinfo);
        }

        chatuserinfo.subscriptions = p_176027_2_.subscriptions;
        chatuserinfo.modes = p_176027_2_.modes;
        chatuserinfo.nameColorARGB = p_176027_2_.nameColorARGB;
    }

    private boolean func_176028_a(Set p_176028_1_, Set p_176028_2_, int p_176028_3_)
    {
        return p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_BANNED) ? false : (p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_ADMINSTRATOR) ? true : (p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_MODERATOR) ? true : (p_176028_1_.contains(ChatUserMode.TTV_CHAT_USERMODE_STAFF) ? true : (p_176028_3_ == 0 ? true : (p_176028_3_ == 1 ? p_176028_2_.contains(ChatUserSubscription.TTV_CHAT_USERSUB_SUBSCRIBER) : false)))));
    }

    public void func_176018_a(String p_176018_1_, ChatUserInfo[] p_176018_2_, ChatUserInfo[] p_176018_3_, ChatUserInfo[] p_176018_4_)
    {
        ChatUserInfo[] achatuserinfo3 = p_176018_3_;
        int i = p_176018_3_.length;
        int j;
        ChatUserInfo chatuserinfo;

        for (j = 0; j < i; ++j)
        {
            chatuserinfo = achatuserinfo3[j];
            this.field_152955_g.remove(chatuserinfo.displayName);
        }

        achatuserinfo3 = p_176018_4_;
        i = p_176018_4_.length;

        for (j = 0; j < i; ++j)
        {
            chatuserinfo = achatuserinfo3[j];
            this.field_152955_g.put(chatuserinfo.displayName, chatuserinfo);
        }

        achatuserinfo3 = p_176018_2_;
        i = p_176018_2_.length;

        for (j = 0; j < i; ++j)
        {
            chatuserinfo = achatuserinfo3[j];
            this.field_152955_g.put(chatuserinfo.displayName, chatuserinfo);
        }
    }

    public void func_180606_a(String p_180606_1_)
    {
        field_152950_b.debug(field_152949_a, "Chat connected");
    }

    public void func_180607_b(String p_180607_1_)
    {
        field_152950_b.debug(field_152949_a, "Chat disconnected");
        this.field_152955_g.clear();
    }

    public void func_176019_a(String p_176019_1_, String p_176019_2_) {}

    public void func_176021_d() {}

    public void func_176024_e() {}

    public void func_176016_c(String p_176016_1_) {}

    public void func_176020_d(String p_176020_1_) {}

    public boolean func_152927_B()
    {
        return this.field_176029_e != null && this.field_176029_e.equals(this.broadcastController.getChannelInfo().name);
    }

    public String func_152921_C()
    {
        return this.field_176029_e;
    }

    public ChatUserInfo func_152926_a(String p_152926_1_)
    {
        return (ChatUserInfo)this.field_152955_g.get(p_152926_1_);
    }

    public void func_152917_b(String p_152917_1_)
    {
        this.chatController.func_175986_a(this.field_176029_e, p_152917_1_);
    }

    public boolean func_152928_D()
    {
        return field_152965_q && this.broadcastController.func_152858_b();
    }

    public ErrorCode func_152912_E()
    {
        return !field_152965_q ? ErrorCode.TTV_EC_OS_TOO_OLD : this.broadcastController.getErrorCode();
    }

    public boolean func_152913_F()
    {
        return this.loggedIn;
    }

    public void func_152910_a(boolean p_152910_1_)
    {
        this.field_152963_o = p_152910_1_;
        this.updateStreamVolume();
    }

    public boolean func_152929_G()
    {
        boolean flag = this.field_152953_e.gameSettings.streamMicToggleBehavior == 1;
        return this.field_152962_n || this.field_152953_e.gameSettings.streamMicVolume <= 0.0F || flag != this.field_152963_o;
    }

    public IStream.AuthFailureReason func_152918_H()
    {
        return this.field_152964_p;
    }

    static
    {
        try
        {
            if (Util.getOSType() == Util.EnumOS.WINDOWS)
            {
                System.loadLibrary("avutil-ttv-51");
                System.loadLibrary("swresample-ttv-0");
                System.loadLibrary("libmp3lame-ttv");

                if (System.getProperty("os.arch").contains("64"))
                {
                    System.loadLibrary("libmfxsw64");
                }
                else
                {
                    System.loadLibrary("libmfxsw32");
                }
            }

            field_152965_q = true;
        }
        catch (Throwable var1)
        {
            field_152965_q = false;
        }
    }
}