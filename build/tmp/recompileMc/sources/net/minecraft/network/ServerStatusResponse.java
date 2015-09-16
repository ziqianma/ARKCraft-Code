package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class ServerStatusResponse
{
    private IChatComponent serverMotd;
    private ServerStatusResponse.PlayerCountData playerCount;
    private ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersion;
    private String favicon;
    private static final String __OBFID = "CL_00001385";

    public IChatComponent getServerDescription()
    {
        return this.serverMotd;
    }

    public void setServerDescription(IChatComponent motd)
    {
        this.serverMotd = motd;
        invalidateJson();
    }

    public ServerStatusResponse.PlayerCountData getPlayerCountData()
    {
        return this.playerCount;
    }

    public void setPlayerCountData(ServerStatusResponse.PlayerCountData countData)
    {
        this.playerCount = countData;
        invalidateJson();
    }

    public ServerStatusResponse.MinecraftProtocolVersionIdentifier getProtocolVersionInfo()
    {
        return this.protocolVersion;
    }

    public void setProtocolVersionInfo(ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersionData)
    {
        this.protocolVersion = protocolVersionData;
        invalidateJson();
    }

    public void setFavicon(String faviconBlob)
    {
        this.favicon = faviconBlob;
        invalidateJson();
    }

    public String getFavicon()
    {
        return this.favicon;
    }

    public static class MinecraftProtocolVersionIdentifier
        {
            private final String name;
            private final int protocol;
            private static final String __OBFID = "CL_00001389";

            public MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn)
            {
                this.name = nameIn;
                this.protocol = protocolIn;
            }

            public String getName()
            {
                return this.name;
            }

            public int getProtocol()
            {
                return this.protocol;
            }

            public static class Serializer implements JsonDeserializer, JsonSerializer
                {
                    private static final String __OBFID = "CL_00001390";

                    public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
                    {
                        JsonObject jsonobject = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "version");
                        return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getJsonObjectStringFieldValue(jsonobject, "name"), JsonUtils.getJsonObjectIntegerFieldValue(jsonobject, "protocol"));
                    }

                    public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
                    {
                        JsonObject jsonobject = new JsonObject();
                        jsonobject.addProperty("name", p_serialize_1_.getName());
                        jsonobject.addProperty("protocol", Integer.valueOf(p_serialize_1_.getProtocol()));
                        return jsonobject;
                    }

                    public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
                    {
                        return this.serialize((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_serialize_1_, p_serialize_2_, p_serialize_3_);
                    }
                }
        }

    public static class PlayerCountData
        {
            private final int maxPlayers;
            private final int onlinePlayerCount;
            private GameProfile[] players;
            private static final String __OBFID = "CL_00001386";

            public PlayerCountData(int maxOnlinePlayers, int onlinePlayers)
            {
                this.maxPlayers = maxOnlinePlayers;
                this.onlinePlayerCount = onlinePlayers;
            }

            public int getMaxPlayers()
            {
                return this.maxPlayers;
            }

            public int getOnlinePlayerCount()
            {
                return this.onlinePlayerCount;
            }

            public GameProfile[] getPlayers()
            {
                return this.players;
            }

            public void setPlayers(GameProfile[] playersIn)
            {
                this.players = playersIn;
            }

            public static class Serializer implements JsonDeserializer, JsonSerializer
                {
                    private static final String __OBFID = "CL_00001387";

                    public ServerStatusResponse.PlayerCountData deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
                    {
                        JsonObject jsonobject = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "players");
                        ServerStatusResponse.PlayerCountData playercountdata = new ServerStatusResponse.PlayerCountData(JsonUtils.getJsonObjectIntegerFieldValue(jsonobject, "max"), JsonUtils.getJsonObjectIntegerFieldValue(jsonobject, "online"));

                        if (JsonUtils.jsonObjectFieldTypeIsArray(jsonobject, "sample"))
                        {
                            JsonArray jsonarray = JsonUtils.getJsonObjectJsonArrayField(jsonobject, "sample");

                            if (jsonarray.size() > 0)
                            {
                                GameProfile[] agameprofile = new GameProfile[jsonarray.size()];

                                for (int i = 0; i < agameprofile.length; ++i)
                                {
                                    JsonObject jsonobject1 = JsonUtils.getElementAsJsonObject(jsonarray.get(i), "player[" + i + "]");
                                    String s = JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "id");
                                    agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getJsonObjectStringFieldValue(jsonobject1, "name"));
                                }

                                playercountdata.setPlayers(agameprofile);
                            }
                        }

                        return playercountdata;
                    }

                    public JsonElement serialize(ServerStatusResponse.PlayerCountData p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
                    {
                        JsonObject jsonobject = new JsonObject();
                        jsonobject.addProperty("max", Integer.valueOf(p_serialize_1_.getMaxPlayers()));
                        jsonobject.addProperty("online", Integer.valueOf(p_serialize_1_.getOnlinePlayerCount()));

                        if (p_serialize_1_.getPlayers() != null && p_serialize_1_.getPlayers().length > 0)
                        {
                            JsonArray jsonarray = new JsonArray();

                            for (int i = 0; i < p_serialize_1_.getPlayers().length; ++i)
                            {
                                JsonObject jsonobject1 = new JsonObject();
                                UUID uuid = p_serialize_1_.getPlayers()[i].getId();
                                jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
                                jsonobject1.addProperty("name", p_serialize_1_.getPlayers()[i].getName());
                                jsonarray.add(jsonobject1);
                            }

                            jsonobject.add("sample", jsonarray);
                        }

                        return jsonobject;
                    }

                    public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
                    {
                        return this.serialize((ServerStatusResponse.PlayerCountData)p_serialize_1_, p_serialize_2_, p_serialize_3_);
                    }
                }
        }

    public static class Serializer implements JsonDeserializer, JsonSerializer
        {
            private static final String __OBFID = "CL_00001388";

            public ServerStatusResponse deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                JsonObject jsonobject = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "status");
                ServerStatusResponse serverstatusresponse = new ServerStatusResponse();

                if (jsonobject.has("description"))
                {
                    serverstatusresponse.setServerDescription((IChatComponent)p_deserialize_3_.deserialize(jsonobject.get("description"), IChatComponent.class));
                }

                if (jsonobject.has("players"))
                {
                    serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData)p_deserialize_3_.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));
                }

                if (jsonobject.has("version"))
                {
                    serverstatusresponse.setProtocolVersionInfo((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_deserialize_3_.deserialize(jsonobject.get("version"), ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
                }

                if (jsonobject.has("favicon"))
                {
                    serverstatusresponse.setFavicon(JsonUtils.getJsonObjectStringFieldValue(jsonobject, "favicon"));
                }

                net.minecraftforge.fml.client.FMLClientHandler.instance().captureAdditionalData(serverstatusresponse, jsonobject);
                return serverstatusresponse;
            }

            public JsonElement serialize(ServerStatusResponse p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                JsonObject jsonobject = new JsonObject();

                if (p_serialize_1_.getServerDescription() != null)
                {
                    jsonobject.add("description", p_serialize_3_.serialize(p_serialize_1_.getServerDescription()));
                }

                if (p_serialize_1_.getPlayerCountData() != null)
                {
                    jsonobject.add("players", p_serialize_3_.serialize(p_serialize_1_.getPlayerCountData()));
                }

                if (p_serialize_1_.getProtocolVersionInfo() != null)
                {
                    jsonobject.add("version", p_serialize_3_.serialize(p_serialize_1_.getProtocolVersionInfo()));
                }

                if (p_serialize_1_.getFavicon() != null)
                {
                    jsonobject.addProperty("favicon", p_serialize_1_.getFavicon());
                }

                net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.enhanceStatusQuery(jsonobject);
                return jsonobject;
            }

            public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                return this.serialize((ServerStatusResponse)p_serialize_1_, p_serialize_2_, p_serialize_3_);
            }
        }

    private java.util.concurrent.Semaphore mutex = new java.util.concurrent.Semaphore(1);
    private String json = null;
    /**
     * Returns this object as a Json string.
     * Converting to JSON if a cached version is not available.
     *
     * Also to prevent potentially large memory allocations on the server
     * this is moved from the S00PacketServerInfo writePacket function
     *
     * As this method is called from the network threads thread safety is important!
     *
     * @return
     */
    public String getJson()
    {
        String ret = this.json;
        if (ret == null)
        {
            mutex.acquireUninterruptibly();
            ret = this.json;
            if (ret == null)
            {
                ret = net.minecraft.network.status.server.S00PacketServerInfo.GSON.toJson(this);
                this.json = ret;
            }
            mutex.release();
        }
        return ret;
    }

    /**
     * Invalidates the cached json, causing the next call to getJson to rebuild it.
     * This is needed externally because PlayerCountData.setPlayer's is public.
     */
    public void invalidateJson()
    {
        this.json = null;
    }
}