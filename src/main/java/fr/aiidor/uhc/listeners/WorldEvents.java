package fr.aiidor.uhc.listeners;

import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import fr.aiidor.uhc.listeners.events.GenerateWorldEvent;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.world.Wg_Flat;

public class WorldEvents implements Listener {
	
	@EventHandler
	public void onGenerateWorldEvent(GenerateWorldEvent e) {
		
		WorldCreator wc = e.getWorldCreator();
		
		if (ScenariosManager.SWEAT_WORLD.isActivated() && e.getWorldCreator().environment() == Environment.NORMAL) {
			wc.generator(new Wg_Flat());
		}
	}
	
	@EventHandler
	public void onchunkPopulateEvent(ChunkPopulateEvent e) {
		if (ScenariosManager.CHUNK_APOCALYPSE.isActivated()) {
			ScenariosManager.CHUNK_APOCALYPSE.apocalypse(e.getChunk());
		}
	}
}
