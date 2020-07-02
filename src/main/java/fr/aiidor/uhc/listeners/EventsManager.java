package fr.aiidor.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import fr.aiidor.dwuhc.DWRole;
import fr.aiidor.dwuhc.DWRoleType;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.tools.UHCItem;

public class EventsManager implements Listener {
	
	public void register() {
		
		PluginManager pm = Bukkit.getPluginManager();
		UHC uhc = UHC.getInstance();
		
		pm.registerEvents(this, uhc);
		pm.registerEvents(new DamageEvents(), uhc);
		pm.registerEvents(new EntitySpawnEvents(), uhc);
		pm.registerEvents(new ExplosionEvents(), uhc);
		pm.registerEvents(new PlayerInteractEvents(), uhc);
		pm.registerEvents(new BlockEvents(), uhc);
		pm.registerEvents(new ServerListEvent(), uhc);
		pm.registerEvents(new ChatEvent(), uhc);
		pm.registerEvents(new PlayerConnexionEvents(), uhc);
		pm.registerEvents(new FoodEvents(), uhc);
		pm.registerEvents(new DeathEvents(), uhc);
		pm.registerEvents(new CraftEvents(), uhc);
		pm.registerEvents(new PortalEvents(), uhc);
		pm.registerEvents(uhc.getGuiManager(), uhc);
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
		
	}
	
	@EventHandler
	public void playerDropItem(PlayerDropItemEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		ItemStack item = e.getItemDrop().getItemStack();
		
		if (item.isSimilar(UHCItem.getConfigChest())) {
			e.getItemDrop().remove();
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
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (e.isSneaking()) {
			if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
				DevilWatches dw = (DevilWatches) game.getUHCMode();
					
				if (dw.hasRole(p.getUUID())) {
					DWRole role = dw.getDWPlayer(player.getUniqueId()).getRole();
						
					if (role.getRoleType() == DWRoleType.MOUNTEBANK && role.hasPower()) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 10, 3, false, false));
					}
				}
			}
		}
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
			if (ScenariosManager.FIRELESS.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
				if (!ScenariosManager.FIRELESS.lava_bucket) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}
	
	@EventHandler
	public void playerEmptyBucketEvent(PlayerBucketFillEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyLakes) {
			if (e.getItemStack().getType() != Material.MILK_BUCKET) {
				e.setCancelled(true);
				player.updateInventory();
				return;
			}
		}
	}
	
	@EventHandler
	public void holyWater(ProjectileLaunchEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		if (e.getEntity() instanceof ThrownPotion) {
			ThrownPotion potion = (ThrownPotion) e.getEntity();
			
			if (potion.getItem().hasItemMeta() && potion.getItem().getItemMeta().hasDisplayName() &&
				potion.getItem().getItemMeta().hasDisplayName() &&
				potion.getItem().getItemMeta().getDisplayName().contentEquals(Lang.DW_ITEM_HOLY_WATER.get())) {
				
				potion.setCustomName("holy_water");
			}
		}
	}
	
	@EventHandler
	public void holyWater(PotionSplashEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
			DevilWatches dw = (DevilWatches) game.getUHCMode();
			
			if (e.getEntity().getCustomName().equals("holy_water")) {
				
				e.setCancelled(true);
				for (LivingEntity ent : e.getAffectedEntities()) {
					if (ent instanceof Player) {
						Player player = (Player) ent;
						
						if (dw.hasRole(player.getUniqueId())) {
							if (dw.getDWPlayer(player.getUniqueId()).isCorrupted()) {
								player.damage(2);
							}
						}
					}
				}
			}
		}

	}
	
}