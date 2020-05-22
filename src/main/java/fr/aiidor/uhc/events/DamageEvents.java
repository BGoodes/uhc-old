package fr.aiidor.uhc.events;

import java.util.Arrays;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.scenarios.SuperHeroes.Power;

public class DamageEvents implements Listener {
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		//PLAYERS
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (!game.isHere(player.getUniqueId())) return;
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			if (!game.isStart()) {
				e.setCancelled(true);
				return;
			}
			
			//INVINCIBILITY
			if (game.getState() == GameState.RUNNING && game.isRunning() && game.getRunner().getTime() < game.getSettings().invincibility_time * 60) {
				e.setCancelled(true);
				return;
			}
			
			if (ScenariosManager.NOFALL.isActivated() && e.getCause() == DamageCause.FALL) {
				e.setCancelled(true);
				return;
			}
			
			if (ScenariosManager.SUPER_HEROES.isActivated() && e.getCause() == DamageCause.FALL) {
				if (ScenariosManager.SUPER_HEROES.hasPower(p) && ScenariosManager.SUPER_HEROES.getPower(p) == Power.JUMP_BOOST) {
					e.setCancelled(true);
				}
			}
			
			if (ScenariosManager.TNTFLY.isActivated() && e.getCause().name().contains("EXPLOSION")) {
				if (ScenariosManager.TNTFLY.damage_reduction != 0) e.setDamage(e.getDamage() / ScenariosManager.TNTFLY.damage_reduction);
				player.setVelocity(player.getVelocity().multiply(ScenariosManager.TNTFLY.boost));
			}
		}
		
		//ENTITIES
		if (ScenariosManager.FIRELESS.isActivated()) {
			if (!ScenariosManager.FIRELESS.canDamage(e.getCause(), e.getEntity())) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
				return;
			}
		}
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (!game.isHere(player.getUniqueId())) return;
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			if (e.getDamager() instanceof Player) {
				//PVP TIME
				if (!game.canPvp()) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (e.getDamager() instanceof Projectile) {
				Projectile pj = (Projectile) e.getDamager();
				
				if (pj.getShooter() instanceof Player) {
					
					Player damager = (Player) pj.getShooter();
					if (!game.canPvp()) {
						e.setCancelled(true);
						return;
					}
					
					if (pj.getType() == EntityType.ARROW) {
						
						if (game.getSettings().bow_display_life) {
							
							Double life = Math.round((player.getHealth() + ((CraftPlayer) player).getHandle().getAbsorptionHearts() - e.getFinalDamage()) * 100D * 5D) / 100D;
							
							String life_text = "§a" + life + "%";
							if (life < 80) life_text = "§e" + life + "%";
							if (life <= 50) life_text = "§6" + life + "%";
							if (life <= 20) life_text = "§c" + life + "%";
							if (life <= 5) life_text = "§4" + life + "%";
							
							damager.sendMessage(Lang.MSG_BOW_DAMAGE.get()
									.replace(LangTag.VALUE.toString(), life_text)
									.replace(LangTag.PLAYER_NAME.toString(), p.getName()));
						}
						
						if (ScenariosManager.CUPID.isActivated()) {
							ScenariosManager.CUPID.heal(damager, e.getFinalDamage());
						}
					}
				}
			}
		}
		
		if (e.getDamager() instanceof Player) {
			
			Player player = (Player) e.getDamager();
			
			if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) && game.getSettings().strength_nerf) { 
				for (PotionEffect effect : player.getActivePotionEffects()) {
		            
		            if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
		            	
		            	int level = effect.getAmplifier() + 1;
		            	double newDamage = e.getDamage(EntityDamageEvent.DamageModifier.BASE) / (level * 1.3D + 1.0D) + (game.getSettings().strength_nerf_damage * level);
		            	double damagePercent = newDamage / e.getDamage(EntityDamageEvent.DamageModifier.BASE);
		            	
		            	for (DamageModifier dm : Arrays.asList(DamageModifier.ARMOR, DamageModifier.RESISTANCE, DamageModifier.MAGIC, DamageModifier.BLOCKING)) {
		            		
		            		try {
		            			e.setDamage(dm, e.getDamage(dm) * damagePercent);
							} catch (Exception e2) {}	
		            	}
		            	
		            	e.setDamage(DamageModifier.BASE, newDamage);
		            	break;
		            } 
				}
			}
		}
		
		if (game.getSettings().IsActivated(ScenariosManager.RODLESS)) {
			if (!ScenariosManager.RODLESS.canDamage(e.getDamager(), e.getEntity())) {
				e.setCancelled(true);
				return;
			}
		}
		
		
		if (ScenariosManager.ASSAULT_AND_BATTERY.isActivated()) {
			ScenariosManager.ASSAULT_AND_BATTERY.entityDamageByEntityEvent(e, game);
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
