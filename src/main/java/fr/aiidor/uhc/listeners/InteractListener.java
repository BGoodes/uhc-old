package fr.aiidor.uhc.listeners;

import java.util.Arrays;
import java.util.List;

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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class InteractListener implements Listener {
	
	private List<EntityType> monsterEgg = Arrays.asList(EntityType.IRON_GOLEM, EntityType.WITHER, EntityType.ENDER_DRAGON);
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerInteractEvent(PlayerInteractEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		ItemStack hand = e.getItem();
		
		if (p.getState() == PlayerState.DYING) {
			e.setCancelled(true);
			return;
		}
		
		game.getSettings().enchantLimiter(hand);
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (hand != null && hand.getItemMeta().hasDisplayName()) {
				
				String displayName = hand.getItemMeta().getDisplayName();
				
				if (hand.getType() == Material.ENDER_CHEST) {
					
					//CONFIG
					if (displayName.equalsIgnoreCase(Lang.INV_CONFIGURATION.get())) {
						
						e.setCancelled(true);
						
						if (!game.getSettings().playerHasPermission(player, Permission.CONFIG)) {
							player.sendMessage(Lang.ST_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CONFIG.toString()));
							player.getInventory().setItemInHand(null);
							return;
						}
						
						player.openInventory(GuiManager.INV_CONFIG.getInventory());
						return;
					}
				}
				
				if (hand.getType() == Material.BANNER && !game.isStart() && !p.isSpec() && game.hasTeam()) {
					
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
			
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK && game.isStart()) {
				Block target = e.getClickedBlock();
				
				if (target != null && hand.getType() == Material.MONSTER_EGG) {
					for (EntityType t : monsterEgg) {
						if (hand.getDurability() == t.getTypeId()) {
							
							if (hand.getAmount() - 1 <= 0) hand.setType(Material.AIR);
							else hand.setAmount(hand.getAmount() - 1);
							
							e.setCancelled(true);
							target.getLocation().getWorld().spawnEntity(target.getLocation(), t);
						}
					}
				}
				
				//FIRELESS
				if (ScenariosManager.FIRELESS.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
					if (!ScenariosManager.FIRELESS.flint_and_steel) {
						if (hand.getType() == Material.FLINT_AND_STEEL) {
							e.setCancelled(true);
						}
					}
				}
				
				//TNT_FLY
				if (ScenariosManager.TNTFLY.isActivated() && ScenariosManager.TNTFLY.chain) {
					if (hand.getType() == Material.FLINT_AND_STEEL) {
						if (target != null && target.getType() == Material.TNT) {
							
							target.setType(Material.AIR);
							e.setCancelled(true);
								
							Location location = target.getLocation().add(0.5, 0.25, 0.5);
							TNTPrimed tnt = (TNTPrimed) target.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
							tnt.setVelocity(new Vector(0, 0.25, 0));
							
							tnt.teleport(location);
							location.getWorld().playSound(location, Sound.FUSE, 0.7f, 1f);
						}
					}
				}
			}
			
			
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				
				if (ScenariosManager.SOUP.isActivated()) {
					if (hand.getType() == Material.MUSHROOM_SOUP) {
						if (player.getHealth() != player.getMaxHealth()) {
							
							ScenariosManager.SOUP.healPlayer(p);
							player.getInventory().getItemInHand().setType(Material.BOWL);
						}
						
					}
				}
				
				if (ScenariosManager.TRACKER.isActivated()) {
					if (hand.getType() == Material.COMPASS) {
						ScenariosManager.TRACKER.track(p);
					}
				}
			}
		}

		
		if (ScenariosManager.BEDBOMB.isActivated() && e.getAction() == Action.RIGHT_CLICK_BLOCK && game.isStart()) {
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
	
	@EventHandler
	public void playerInteractAtEntityEvent(PlayerInteractAtEntityEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (!game.isStart()) return;
		
		if (e.getRightClicked() instanceof Player) {
			Player clicked = (Player) e.getRightClicked();
			
			if (game.isHere(clicked.getUniqueId()) && p.isAlive()) {
				UHCPlayer c = game.getUHCPlayer(clicked.getUniqueId());
				
				if (ScenariosManager.OPEN_HOUSE.isActivated() && player.isSneaking()) {
					if (c.isAlive()) {
						player.openInventory(clicked.getInventory());
					}
				}
			}
		}
	}
}
