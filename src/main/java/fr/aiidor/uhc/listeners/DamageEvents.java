package fr.aiidor.uhc.listeners;

import java.util.Arrays;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.dwuhc.DWRole;
import fr.aiidor.dwuhc.DWRoleType;
import fr.aiidor.uhc.Settings;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings.StrenghtNerf;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.scenarios.SuperHeroes.Power;

public class DamageEvents implements Listener {
	
	@EventHandler
	public void entityDamageEvent(EntityDamageEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		Settings settings = UHC.getInstance().getSettings();
		
		if (!settings.lobby_player_damage && !game.isStart() && e.getEntity().getWorld().equals(settings.getLobbyWorld())) {
			if (e.getEntity() instanceof Player) {
				if (game.isHere(((Player) e.getEntity()).getUniqueId())) e.setCancelled(true);
				return;
			}
		}
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (ScenariosManager.FIRELESS.isActivated()) {
			if (!ScenariosManager.FIRELESS.canDamage(e.getCause(), e.getEntity())) {
				e.setCancelled(true);
				e.getEntity().setFireTicks(0);
				return;
			}
		}
		
		//PLAYERS
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (!game.isHere(player.getUniqueId())) return;
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			if (!game.isStart()) {
				e.setCancelled(true);
				return;
			}

			if (p.getState() == PlayerState.DYING) {
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
			
			if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
				DevilWatches dw = (DevilWatches) game.getUHCMode();
				
				if (dw.hasRole(player.getUniqueId())) {
					DWRole r = dw.getDWPlayer(player.getUniqueId()).getRole();
					
					if (r.getRoleType() == DWRoleType.CAT_LADY && e.getCause() == DamageCause.FALL) {
						e.setCancelled(true);
						return;
					}
				}
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
			
			if (ScenariosManager.GAPZAP.isActivated()) {
				player.removePotionEffect(PotionEffectType.REGENERATION);
			}
			
			if (ScenariosManager.DROPPING_COINS.isActivated()) {
				ScenariosManager.DROPPING_COINS.drop(player);
			}
		}
	}
	
	@EventHandler
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		
		//CANCEL DAMAGE
		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			
			if (game.isHere(damager.getUniqueId())) {
				
				UHCPlayer d = game.getUHCPlayer(damager.getUniqueId());
				
				
				if (e.getEntity() instanceof Player) {
					if (!game.canPvp()) {
						e.setCancelled(true);
						return;
					}
				}
				
				if (d.getState() == PlayerState.DYING) {
					e.setCancelled(true);
					return;
				}
			}
		}
		
		if (e.getDamager() instanceof Projectile) {
			if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
				
				if (game.isHere(((Player)((Projectile) e.getDamager()).getShooter()).getUniqueId())) {
					if (!game.canPvp()) {
						e.setCancelled(true);
						return;
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
		
		//-------------------------------------------------
		
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			if (game.isHere(player.getUniqueId())) {
				
				UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
				
				if (e.getDamager() instanceof Projectile) {
					Projectile pj = (Projectile) e.getDamager();
					
					if (pj.getShooter() instanceof Player) {
						
						Player damager = (Player) pj.getShooter();
						
						if (game.isHere(damager.getUniqueId())) {
							if (pj.getType() == EntityType.ARROW) {
								
								if (game.getSettings().getDisplayBowLife() && game.getSettings().getDisplayLife()) {
									
									Double life = Math.round((player.getHealth() + ((CraftPlayer) player).getHandle().getAbsorptionHearts() - e.getFinalDamage()) * 100D * 5D) / 100D;
									
									String life_text = "§a" + life + "%";
									if (life < 80) life_text = "§e" + life + "%";
									if (life <= 50) life_text = "§6" + life + "%";
									if (life <= 20) life_text = "§c" + life + "%";
									if (life <= 5) life_text = "§4" + life + "%";
									
									damager.sendMessage(Lang.MSG_BOW_DAMAGE.get()
											.replace(LangTag.VALUE.toString(), life_text)
											.replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName()));
								}
								
								if (ScenariosManager.CUPID.isActivated()) {
									ScenariosManager.CUPID.heal(damager, e.getFinalDamage());
								}
								
								if (ScenariosManager.BOW_SWAP.isActivated()) {
									ScenariosManager.BOW_SWAP.tp(player, damager);
								}
							}
						}
					}
				}
			}
			
		}
		
		
		//DAMAGE GENERAUX
		if (e.getDamager() instanceof Player) {
			
			Player player = (Player) e.getDamager();
			ItemStack hand = player.getItemInHand();
			
			if (player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) && game.getSettings().strength_nerf != StrenghtNerf.OFF) { 
				for (PotionEffect effect : player.getActivePotionEffects()) {
					
					if (effect.getType().equals(PotionEffectType.INCREASE_DAMAGE)) {
						
						int level = effect.getAmplifier() + 1;
						
						Double magic_damage = 0D;
						
						if (hand != null && hand.hasItemMeta()) {
							magic_damage = hand.getItemMeta().getEnchantLevel(Enchantment.DAMAGE_ALL) * 1.25D;
						}
						
						double newDamage;
						double damage = (e.getDamage(DamageModifier.BASE) - magic_damage) / (level * 1.3D + 1D);
						
						if (game.getSettings().strength_nerf == StrenghtNerf.PERCENT) newDamage = damage * (game.getSettings().strength_nerf_percent * level + 1.0D);
						else newDamage = damage + (game.getSettings().strength_nerf_damage * level);
						
						newDamage += magic_damage;
						
						double damagePercent = newDamage / e.getDamage(DamageModifier.BASE);
						
						for (DamageModifier dm : Arrays.asList(DamageModifier.ARMOR, DamageModifier.RESISTANCE, DamageModifier.MAGIC, DamageModifier.BLOCKING)) {
							if (e.isApplicable(dm)) {
								e.setDamage(dm, e.getDamage(dm) * damagePercent);
							}
						}
						
						e.setDamage(DamageModifier.BASE, newDamage);
					}
				}
			}
		}
			
		if (e.getDamager() instanceof Projectile) {
			Projectile pj = (Projectile) e.getDamager();
			
			if (pj.getShooter() instanceof Player) {
				
				Player damager = (Player) pj.getShooter();
				if (game.isHere(damager.getUniqueId())) {
					
					UHCPlayer d = game.getUHCPlayer(damager.getUniqueId());
					
					if (pj.getType() == EntityType.ARROW) {
						
						if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
							
							DevilWatches dw = (DevilWatches) game.getUHCMode();
							
							if (dw.hasRole(d.getUUID())) {
								DWRole role = dw.getDWPlayer(d.getUUID()).getRole();
								
								if (role.getRoleType() == DWRoleType.HUNTER && role.hasPower()) {
									
									Double distance = damager.getLocation().distance(e.getEntity().getLocation());
									Double damage = e.getDamage() * (1D + 0.1D * (int) (distance/15));
									e.setDamage(damage);
								}
							}
						}
					}
				}

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
