package com.arkcraft.mod.core.handler;

import java.util.ArrayList;

import com.arkcraft.mod.core.gen.ore.WrappedOreGenerator;
import com.arkcraft.mod.core.gen.ore.WrappedOreGenerator.Instruction;
import com.arkcraft.mod.core.lib.LogHelper;

import net.minecraft.block.Block;

//TODO add structure generation
public class GenerationHandler {

	public static ArrayList<Instruction> oresToGenerate = new ArrayList<Instruction>();
	public static WrappedOreGenerator generator;
	
	public GenerationHandler() {}
	
	public static void addOreToGen(Block block, int height) {
		/* This is for standard ore generation. */
		addOreToGen(block, height, 5, 5);
		
	}
	
	public static void addOreToGen(Block block, int height, int maxBlocksInVain) {
		addOreToGen(block, height, maxBlocksInVain, 5);
	}
	
	public static void addOreToGen(Block block, int height, int maxBlocksInVain, int maxVeinsInChunk) {
		/* Gen ID is where to generate it. -1 is in nether, 0 is overworld, 1 is end. */
		Instruction instruction = new Instruction(block, height, maxBlocksInVain, maxVeinsInChunk);
		generator = new WrappedOreGenerator(0, instruction);
	}
	
	public WrappedOreGenerator getOreGenerator() { if(generator != null) return generator; else LogHelper.error("Generator is null!"); return null; }
	
}
