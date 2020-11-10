package fr.aiidor.uhc.listeners;

import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkPopulateEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GenerateWorldEvent;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.world.SweatWorldGenerator;

public class WorldEvents implements Listener {

	@EventHandler
	public void onGenerateWorldEvent(GenerateWorldEvent e) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;

		WorldCreator wc = e.getWorldCreator();

		if (ScenariosManager.SWEAT_WORLD.isActivated() && e.getWorldCreator().environment() == Environment.NORMAL) {
			wc.generator(new SweatWorldGenerator());
		}
	}

	@EventHandler
	public void onchunkPopulateEvent(ChunkPopulateEvent e) {
		if (!UHC.getInstance().getGameManager().hasGame())
			return;

		Game game = UHC.getInstance().getGameManager().getGame();

		if (game.isUHCWorld(e.getWorld())) {

			if (ScenariosManager.CHUNK_APOCALYPSE.isActivated() && e.getWorld().getEnvironment() != Environment.THE_END) {
				ScenariosManager.CHUNK_APOCALYPSE.apocalypse(e.getChunk());
			}
		}
	}
}
