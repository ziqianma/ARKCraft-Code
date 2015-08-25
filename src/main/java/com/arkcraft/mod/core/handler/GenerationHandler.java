package com.arkcraft.mod.core.handler;

import java.util.ArrayList;

import com.arkcraft.mod.core.gen.ore.WrappedOreGenerator.Instruction;

//TODO add structure generation
public class GenerationHandler {

	public static ArrayList<Instruction> oresToGenerate = new ArrayList<Instruction>();
	
	public GenerationHandler() {}
	
	public static void addOreToGen(Instruction... instructions) {
		for(Instruction i : instructions) oresToGenerate.add(i);
	}

}
