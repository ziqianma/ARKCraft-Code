package com.arkcraft.module.core.common.proxy;

import com.arkcraft.module.core.client.gui.book.proxy.DCommon;
import com.arkcraft.module.core.common.network.ARKMessagePipeline;
import com.arkcraft.module.core.common.network.MsgBallistaShot;
import com.arkcraft.module.item.common.event.ItemsCommonEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.SidedProxy;

public class CommonProxy
{

    @SidedProxy(clientSide = "com.arkcraft.module.core.client.gui.book.proxy.DClient", serverSide = "com.arkcraft.module.core.client.gui.book.proxy.DCommon")
    public static DCommon dossierProxy;

    public CommonProxy() {}

    public void registerPreRenderers() {}

    public void registerRenderers() {}

    public void init() {}

    public void registerWeapons() {}

    public void registerEventHandlers()
    {
    }

    public void registerPackets(ARKMessagePipeline pipeline)
    {
        pipeline.registerPacket(MsgBallistaShot.class);
        //	pipeline.registerPacket(MsgExplosion.class);
    }

    public EntityPlayer getPlayer()
    {
        return null;
    }
}
