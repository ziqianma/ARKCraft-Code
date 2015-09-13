//package com.arkcraft.mod.core.handler;
//
//import com.arkcraft.mod.core.GlobalAdditions;
//import com.arkcraft.mod.core.Main;
//
//import net.minecraftforge.client.event.sound.SoundLoadEvent;
//
//public class SoundHandler {
//
//	@ForgeSubscribe
//	   public void onSound(SoundLoadEvent event)
//	   {
//	       try
//	       {
//	       		String [] soundFiles = {
//	       				"lasershot.ogg",
//	       				"miningshot.wav",
//	       				"miningcharge.wav",
//	       				"miningLoaded1.wav",
//	       				"plasmaboil1.wav",
//	       				"plasmaboil2.wav",
//	       				"plasmaCharged.wav",
//	       				"plasmashot2.wav",
//	       				"plasmablast1.wav",
//	       				"blastershot1.wav",
//	       				"blastershot2.wav"
//	       				};
//	       		for (int i = 0; i < soundFiles.length; i++){
//	       			event.manager.soundPoolSounds.addSound(soundFiles[i], Main.class.getResource("/mods/SenitielsSpaceMarineMod/sounds/" + soundFiles[i]));
//	       		}
//	      
//	       }
//	       catch (Exception e)
//	       {
//	           System.err.println("Space Marine Mod: Failed to register one or more sounds.");
//	       }
//	   }
//}