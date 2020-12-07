package fr.aiidor.uhc.listeners;

import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class ProjectileListener implements Listener {
	
	
	@EventHandler
	public void projectileLaunchEvent(ProjectileLaunchEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (ScenariosManager.AIIDOR_HARDCORE.isActivated()) {
			ScenariosManager.AIIDOR_HARDCORE.projectileLaunchEvent(e);
		}
	}
	
	@EventHandler
	public void projectileHitEvent(ProjectileHitEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getEntity().getWorld()))
			return;

		if (e.getEntity().getType() == EntityType.EGG) {
			Egg egg = (Egg) e.getEntity();

			if (game.getSettings().IsActivated(ScenariosManager.EGGS)) {
				ScenariosManager.EGGS.spawnRandomEntity(egg.getLocation());
			}
		}

		if (ScenariosManager.AIIDOR_HARDCORE.isActivated()) {
			ScenariosManager.AIIDOR_HARDCORE.projectileHitEvent(e);
		}
	}
    
	@EventHandler
	public void potionSplashEvent(PotionSplashEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (ScenariosManager.AIIDOR_HARDCORE.isActivated()) {
			ScenariosManager.AIIDOR_HARDCORE.potionSplashEvent(e);
		}
	}
}
