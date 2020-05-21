package fr.aiidor.uhc.events;

import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class DamageEvents implements Listener {
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (!game.isHere(player.getUniqueId())) return;
			
			if (!game.isStart()) {
				e.setCancelled(true);
				return;
			}
			
			//INVINCIBILITY
			if (game.getState() == GameState.GAME && game.isRunning() && game.getRunner().getTime() < game.getSettings().invincibility_time) {
				e.setCancelled(true);
				return;
			}
		}
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		//FIRELESS
		if (ScenariosManager.FIRELESS.isActivated(game)) {
			if (!ScenariosManager.FIRELESS.canDamage(e.getCause(), e.getEntity())) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
				return;
			}
		}
		
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			
			if (ScenariosManager.NOFALL.isActivated(game) && e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				return;
			}
			
			if (ScenariosManager.TNTFLY.isActivated(game) && e.getCause().name().contains("EXPLOSION")) {
				if (ScenariosManager.TNTFLY.damage_reduction != 0) e.setDamage(e.getDamage() / ScenariosManager.TNTFLY.damage_reduction);
				player.setVelocity(player.getVelocity().multiply(ScenariosManager.TNTFLY.boost));
			}
		}
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			
			//PVP TIME
			if (game.getState() == GameState.GAME && game.isRunning() && game.getRunner().getTime() < game.getSettings().pvp_time * 60) {
				e.setCancelled(true);
				return;
			}
		}
		
		if (game.getSettings().IsActivated(ScenariosManager.RODLESS)) {
			if (!ScenariosManager.RODLESS.canDamage(e.getDamager(), e.getEntity())) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (!game.isStart()) return;
        if (e.getEntity().getType() == EntityType.EGG) {
            Egg egg = (Egg) e.getEntity();
            
            if (game.getSettings().IsActivated(ScenariosManager.EGGS)) {
            	ScenariosManager.EGGS.spawnRandomEntity(egg.getLocation());
            }
        }
    }
}
