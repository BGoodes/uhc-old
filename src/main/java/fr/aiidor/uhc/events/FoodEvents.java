package fr.aiidor.uhc.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;

public class FoodEvents implements Listener {
	
	@EventHandler
	public void foodChangeLevel(FoodLevelChangeEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.isHere(e.getEntity().getUniqueId())) return;
		if (!game.isStart()) {
			e.setCancelled(true);
		}
	}
}
