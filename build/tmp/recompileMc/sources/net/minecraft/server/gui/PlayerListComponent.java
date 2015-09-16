package net.minecraft.server.gui;

import java.util.Vector;
import javax.swing.JList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class PlayerListComponent extends JList implements IUpdatePlayerListBox
{
    private MinecraftServer server;
    private int ticks;
    private static final String __OBFID = "CL_00001795";

    public PlayerListComponent(MinecraftServer server)
    {
        this.server = server;
        server.registerTickable(this);
    }

    /**
     * Updates the JList with a new model.
     */
    public void update()
    {
        if (this.ticks++ % 20 == 0)
        {
            Vector vector = new Vector();

            for (int i = 0; i < this.server.getConfigurationManager().playerEntityList.size(); ++i)
            {
                vector.add(((EntityPlayerMP)this.server.getConfigurationManager().playerEntityList.get(i)).getName());
            }

            this.setListData(vector);
        }
    }
}