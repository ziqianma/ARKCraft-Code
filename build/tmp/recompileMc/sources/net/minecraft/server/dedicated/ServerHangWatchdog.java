package net.minecraft.server.dedicated;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.SERVER)
public class ServerHangWatchdog implements Runnable
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final DedicatedServer server;
    private final long maxTickTime;
    private static final String __OBFID = "CL_00002634";

    public ServerHangWatchdog(DedicatedServer server)
    {
        this.server = server;
        this.maxTickTime = server.getMaxTickTime();
    }

    public void run()
    {
        while (this.server.isServerRunning())
        {
            long i = this.server.getCurrentTime();
            long j = MinecraftServer.getCurrentTimeMillis();
            long k = j - i;

            if (k > this.maxTickTime)
            {
                LOGGER.fatal("A single server tick took " + String.format("%.2f", new Object[] {Float.valueOf((float)k / 1000.0F)}) + " seconds (should be max " + String.format("%.2f", new Object[] {Float.valueOf(0.05F)}) + ")");
                LOGGER.fatal("Considering it to be crashed, server will forcibly shutdown.");
                ThreadMXBean threadmxbean = ManagementFactory.getThreadMXBean();
                ThreadInfo[] athreadinfo = threadmxbean.dumpAllThreads(true, true);
                StringBuilder stringbuilder = new StringBuilder();
                Error error = new Error();
                ThreadInfo[] athreadinfo1 = athreadinfo;
                int l = athreadinfo.length;

                for (int i1 = 0; i1 < l; ++i1)
                {
                    ThreadInfo threadinfo = athreadinfo1[i1];

                    if (threadinfo.getThreadId() == this.server.getServerThread().getId())
                    {
                        error.setStackTrace(threadinfo.getStackTrace());
                    }

                    stringbuilder.append(threadinfo);
                    stringbuilder.append("\n");
                }

                CrashReport crashreport = new CrashReport("Watching Server", error);
                this.server.addServerInfoToCrashReport(crashreport);
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Thread Dump");
                crashreportcategory.addCrashSection("Threads", stringbuilder);
                File file1 = new File(new File(this.server.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

                if (crashreport.saveToFile(file1))
                {
                    LOGGER.error("This crash report has been saved to: " + file1.getAbsolutePath());
                }
                else
                {
                    LOGGER.error("We were unable to save this crash report to disk.");
                }

                this.func_180248_a();
            }

            try
            {
                Thread.sleep(i + this.maxTickTime - j);
            }
            catch (InterruptedException interruptedexception)
            {
                ;
            }
        }
    }

    private void func_180248_a()
    {
        try
        {
            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                private static final String __OBFID = "CL_00002633";
                public void run()
                {
                    Runtime.getRuntime().halt(1);
                }
            }, 10000L);
            System.exit(1);
        }
        catch (Throwable throwable)
        {
            Runtime.getRuntime().halt(1);
        }
    }
}