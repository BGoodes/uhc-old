package fr.aiidor.uhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class EntityListener implements Listener {
	
	@EventHandler
	public void entitySpawnEvent(EntitySpawnEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getLocation().getWorld())) return;
		
		if (ScenariosManager.AIIDOR_HARDCORE.isActivated()) {
			ScenariosManager.AIIDOR_HARDCORE.entitySpawnEvent(e);
		}
	}
}
