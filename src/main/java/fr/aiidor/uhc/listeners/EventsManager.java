package fr.aiidor.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.utils.UHCItem;

public class EventsManager implements Listener {
	
	public void register() {
		
		PluginManager pm = Bukkit.getPluginManager();
		UHC uhc = UHC.getInstance();
		
		pm.registerEvents(this, uhc);
		pm.registerEvents(new DamageListener(), uhc);
		pm.registerEvents(new EntityListener(), uhc);
		pm.registerEvents(new ExplosionListener(), uhc);
		pm.registerEvents(new InteractListener(), uhc);
		pm.registerEvents(new BlockListener(), uhc);
		pm.registerEvents(new ServerListListener(), uhc);
		pm.registerEvents(new ChatListener(), uhc);
		pm.registerEvents(new ConnexionListener(), uhc);
		pm.registerEvents(new FoodListener(), uhc);
		pm.registerEvents(new DeathListener(), uhc);
		pm.registerEvents(new CraftListener(), uhc);
		pm.registerEvents(new PortalListener(), uhc);
		pm.registerEvents(new WorldListener(), uhc);
		pm.registerEvents(new InventoryListener(), uhc);
		pm.registerEvents(new AchievementsListener(), uhc);
		pm.registerEvents(new CommandListener(), uhc);
		pm.registerEvents(new ProjectileListener(), uhc);
	}
	
	@EventHandler
	public void changeGamemodeEvent(PlayerGameModeChangeEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (ScenariosManager.BELIEVE_FLY.isActivated()) {
			player.setAllowFlight(true);
		}
	}
	
	@EventHandler
	public void furnaceBurnEvent(FurnaceBurnEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getBlock().getWorld())) return;
		
		if (ScenariosManager.FASTSMELING.isActivated()) {
			if (e.getBlock().getState() instanceof Furnace) {
				ScenariosManager.FASTSMELING.increaseFurnace((Furnace)e.getBlock().getState());
			}
		}

	}
	
	@EventHandler
	public void itemDespawnEvent(ItemDespawnEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.getWorlds().contains(e.getLocation().getWorld())) return;
		
		if (ScenariosManager.ETERNAL_ITEMS.isActivated()) {
			e.setCancelled(true);
			e.getEntity().setTicksLived(1);
		}
	}
	
	@EventHandler
	public void playerPickUpItem(PlayerPickupItemEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (p.getState() == PlayerState.DYING) {
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		ItemStack item = e.getItemDrop().getItemStack();
		
		if (item.isSimilar(UHCItem.config_chest)) {
			e.getItemDrop().remove();
		}
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (p.getState() == PlayerState.DYING) {
			e.setCancelled(true);
			return;
		}
		
		if (!game.isStart() && player.getGameMode() != GameMode.CREATIVE) {
			if (item.isSimilar(UHCItem.getTeamSelecter()) || (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && 
				item.getItemMeta().getDisplayName().equals(UHCItem.getTeamSelecter().getItemMeta().getDisplayName()))) {
				
				e.setCancelled(true);
			}	
		}
	}
	
	@EventHandler
	public void playerSneakEvent(PlayerToggleSneakEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		Player player = e.getPlayer();

		if (!game.isHere(player.getUniqueId())) return;
		
		
	}
	
	@EventHandler
	public void playerFlyEvent(PlayerToggleFlightEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (ScenariosManager.DOUBLE_JUMP.isActivated() && game.isStart()) {
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				
				e.setCancelled(true);
				player.setAllowFlight(false);
				player.setFlying(false);

				Location l = player.getLocation();
				Vector v = l.getDirection().multiply(1.2f).setY(1.2f);
				player.setVelocity(v);
				l.getWorld().playSound(l, Sound.WITHER_SHOOT, 0.5f, 1f);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void playerMoveEvent(PlayerMoveEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (ScenariosManager.DOUBLE_JUMP.isActivated() && game.isStart()) {
			if (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) {
				if (player.isOnGround() && !player.getAllowFlight()) {
					player.setAllowFlight(true);
				}
			}
		}
	}
	
	
	@EventHandler
	public void playerEmptyBucketEvent(PlayerBucketEmptyEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (e.getBucket() == Material.LAVA_BUCKET) {
			
			if (ScenariosManager.NO_BUCKET.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
				if (!ScenariosManager.NO_BUCKET.lava) {
					e.setCancelled(true);
					return;
				}
			}
			
			if (ScenariosManager.FIRELESS.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
				if (!ScenariosManager.FIRELESS.lava_bucket) {
					e.setCancelled(true);
					return;
				}
			}
		}
		
		if (e.getBucket() == Material.WATER_BUCKET) {
			
			if (ScenariosManager.NO_BUCKET.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
				if (!ScenariosManager.NO_BUCKET.water) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void playerFillBucketEvent(PlayerBucketFillEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (ScenariosManager.NO_BUCKET.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
			if (e.getBucket() == Material.LAVA_BUCKET && !ScenariosManager.NO_BUCKET.lava) {
				e.setCancelled(true);
				return;
			}
			
			if (e.getBucket() == Material.WATER && !ScenariosManager.NO_BUCKET.water) {
				e.setCancelled(true);
				return;
			}
		}
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyLakes) {
			if (e.getItemStack().getType() != Material.MILK_BUCKET) {
				e.setCancelled(true);
				player.updateInventory();
				return;
			}
		}
	}
}