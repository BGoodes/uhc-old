package fr.aiidor.uhc.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.PluginManager;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

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
		
		if (ScenariosManager.FASTSMELING.isActivated(game)) {
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
		
		if (ScenariosManager.ETERNAL_ITEMS.isActivated(game)) {
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
		
		if (!game.isStart() && player.getGameMode() != GameMode.CREATIVE) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerSneakEvent(PlayerToggleSneakEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (!game.isStart()) {
			
			if (player.isSneaking()) {
				if (ScenariosManager.ONE_PUNCH_MAN.isActivated(game)) {
					
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
			if (ScenariosManager.FIRELESS.isActivated(game) && player.getGameMode() != GameMode.CREATIVE) {
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
		
		if (ScenariosManager.STINGY_WORLD.isActivated(game) && ScenariosManager.STINGY_WORLD.stingyLakes) {
			if (e.getItemStack().getType() != Material.MILK_BUCKET) {
				e.setCancelled(true);
				player.updateInventory();
				return;
			}
		}
	}
}