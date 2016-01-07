package com.arkcraft.module.core.common.handlers;

import com.arkcraft.lib.LogHelper;
import com.arkcraft.module.core.common.config.CoreBalance;
import com.arkcraft.module.core.common.gen.ore.WrappedOreGenerator;
import com.arkcraft.module.core.common.gen.ore.WrappedOreGenerator.Instruction;
import net.minecraft.block.Block;

import java.util.ArrayList;

//TODO add structure generation
public class GenerationHandler
{

    public static ArrayList<Instruction> oresToGenerate = new ArrayList<Instruction>();
    public static WrappedOreGenerator generator;

    public GenerationHandler() {}

    /**
     * The only
     */
    public static void addOreToGen(Block block, int height)
    {
        /* This is for standard ore generation. */
        addOreToGen(block, height, CoreBalance.GEN.MAX_DEFAULT_ORE_BLOCKS_SPAWN_PER_VEIN, CoreBalance.GEN.MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK);

    }

    public static void addOreToGen(Block block, int height, int maxBlocksInVain)
    {
        addOreToGen(block, height, maxBlocksInVain, CoreBalance.GEN.MAX_DEFAULT_ORE_VEIN_SPAWN_PER_CHUNK);
    }

    /**
     * Full Override method - Doesn't follow @BALANCE.GEN.class
     */
    public static void addOreToGen(Block block, int height, int maxBlocksInVain, int maxVeinsInChunk)
    {
		/* Gen ID is where to generate it. -1 is in nether, 0 is overworld, 1 is end. */
        Instruction instruction = new Instruction(block, height, maxBlocksInVain, maxVeinsInChunk);
        generator = new WrappedOreGenerator(0, instruction);
    }

    public WrappedOreGenerator getOreGenerator()
    {
        if (generator != null)
        {
            return generator;
        }
        else
        {
            LogHelper.error("Generator is null!");
        }
        return null;
    }

}
