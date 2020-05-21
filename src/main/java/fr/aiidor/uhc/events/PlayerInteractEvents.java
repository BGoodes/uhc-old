package fr.aiidor.uhc.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class PlayerInteractEvents implements Listener {
	
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		ItemStack hand = e.getItem();
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (hand != null && hand.getItemMeta().hasDisplayName()) {
				
				String displayName = hand.getItemMeta().getDisplayName();
				
				if (hand.getType() == Material.ENDER_CHEST) {
					
					//CONFIG
					if (displayName.equalsIgnoreCase(Lang.INV_CONFIGURATION.get())) {
						
						e.setCancelled(true);
						
						if (!game.playerHasPermission(player, Permission.CONFIG)) {
							player.sendMessage(Lang.ST_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CONFIG.toString()));
							player.getInventory().setItemInHand(null);
							return;
						}
						
						player.openInventory(GuiManager.INV_CONFIG.getInventory());
						return;
					}
				}
				
				if (hand.getType() == Material.BANNER && !game.isStart()) {
					
					//TEAM CHOOSE
					if (displayName.equalsIgnoreCase(Lang.INV_TEAM_CHOOSE.get())) {
						
						e.setCancelled(true);
						player.openInventory(GuiManager.INV_TEAMS_CHOOSE.getInventory(p));
						return;
					}
				}
			}

		}
		
		if (hand != null) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				
				if (ScenariosManager.FIRELESS.isActivated(game) && player.getGameMode() != GameMode.CREATIVE) {
					if (!ScenariosManager.FIRELESS.flint_and_steel) {
						if (hand != null && hand.getType() == Material.FLINT_AND_STEEL) {
							e.setCancelled(true);
						}
					}
				}
				
				if (ScenariosManager.TNTFLY.isActivated(game) && ScenariosManager.TNTFLY.chain) {
					if (hand != null && hand.getType() == Material.FLINT_AND_STEEL) {
						Block Target = e.getClickedBlock();
						if (Target != null && Target.getType() == Material.TNT) {
							
								Target.setType(Material.AIR);
							e.setCancelled(true);
								
							Location location = Target.getLocation().add(0.5, 0.25, 0.5);
							TNTPrimed tnt = (TNTPrimed) Target.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
							tnt.setVelocity(new Vector(0, 0.25, 0));
								
							tnt.teleport(location);
							location.getWorld().playSound(location, Sound.FUSE, 0.7f, 1f);
						}
					}
				}
			}
		}

		
		if (ScenariosManager.BEDBOMB.isActivated(game) && e.getAction() == Action.RIGHT_CLICK_BLOCK && game.isStart()) {
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				if (e.getClickedBlock().getType() == Material.BED_BLOCK) {
					
					e.setCancelled(true);
					Block bed = e.getClickedBlock();
					
					bed.setType(Material.AIR);
					bed.getWorld().createExplosion(bed.getLocation(), 5, true);
				}
			}
		}
	}
}
