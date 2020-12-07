package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;
import fr.aiidor.uhc.world.UHCWorld;

public class GuiWorldSettings extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_W_SETTINGS.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{" ", " ", " ", " ", " ", "T", "R", "D", "X"}
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			
			dictionary.put("T", new ItemBuilder(Material.FEATHER, Lang.INV_W_TP.get()).getItem());
			dictionary.put("P", new ItemBuilder(Material.EMPTY_MAP, Lang.INV_W_PREGEN.get()).getItem());
			dictionary.put("R", new ItemBuilder(Material.BEACON, Lang.INV_W_REGEN.get()).getItem());
			dictionary.put("D", new ItemBuilder(Material.DARK_OAK_DOOR_ITEM, Lang.INV_W_DELETE.get()).getItem());
		}
		
		return dictionary;
	}
	
	private UHCWorld getUHCWorld(Inventory inv) {
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			ItemStack item = inv.getItem(0);
			
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				return game.getUHCWorld(item.getItemMeta().getDisplayName().substring(2));
			}
		}
		
		return null;
	}
	
	public Inventory getInventory(UHCWorld w) {
		Inventory inv = super.getInventory();
		
		inv.setItem(0, new ItemBuilder(Material.NAME_TAG, "§6" + w.getMainWorldName()).getItem());
		inv.setItem(1, getConfigItem(Lang.INV_W_AUTOREGEN.get(), w.canRegen()));
		
		inv.setItem(2, getConfigItem(Lang.INV_W_OVERWORLD.get(), w.overworld_state));
		inv.setItem(3, getConfigItem(Lang.INV_W_NETHER.get(), w.nether_state));
		inv.setItem(4, getConfigItem(Lang.INV_W_END.get(), w.end_state));
		
		return inv;
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		Player player = event.getPlayer();
		
		Game game = event.getGame();
		
		if (game.getState() != GameState.WAITING) {
			player.sendMessage(Lang.ST_ERROR_OPTION_START.get());
			
			playClickSound(player);
			player.closeInventory();
			return;
		}
		
		UHCWorld w = getUHCWorld(event.getInventory());
		
		if (w == null) {
			playClickSound(player);
			player.openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
		
		if (e.getSlot() == 1) {
			
			playClickSound(player);
			w.setCanRegen(!w.canRegen());
			update(e.getInventory(), player);
			return;
		}
		
		
		if (e.getSlot() == 2) {
			
			playClickSound(player);
			
			if (w.getOverWorldName().equals(w.getMainWorldName())) {
				player.closeInventory();
				player.sendMessage(Lang.ST_ERROR_DESACTIVATE_MAIN_WORLD.get());
				return;
			}
			
			w.overworld_state = !w.overworld_state;
			
			update(e.getInventory(), player);
			return;
		}
		
		if (e.getSlot() == 3) {
			playClickSound(player);
			
			if (w.getNetherName().equals(w.getMainWorldName())) {
				player.closeInventory();
				player.sendMessage(Lang.ST_ERROR_DESACTIVATE_MAIN_WORLD.get());
				return;
			}
			
			w.nether_state = !w.nether_state;
			

			update(e.getInventory(), player);
			return;
		}
		
		if (e.getSlot() == 4) {
			
			playClickSound(player);
			
			if (w.getEndName().equals(w.getMainWorldName())) {
				player.closeInventory();
				player.sendMessage(Lang.ST_ERROR_DESACTIVATE_MAIN_WORLD.get());
				return;
			}
			
			w.end_state = !w.end_state;
			
			update(e.getInventory(), player);
			return;
		}
		
		if (e.getSlot() == 5) {
				
			playClickSound(player);
			player.closeInventory();
				
			if (!w.getMainWorldState()) {
				event.getPlayer().sendMessage(Lang.ST_ERROR_WORLD_GENERATION.get().replace(LangTag.VALUE.toString(), w.getMainWorldName()));
				return;
			}
				
			Location loc = new Location(w.getMainWorld(), 0, 150, 0);
				
			player.teleport(loc);
			return;
		}

		if (e.getSlot() == 6) {
			playClickSound(player);
			player.closeInventory();
			
			w.regenerate();
			return;
		}
			
		if (e.getSlot() == 7) {
				
			playClickSound(player);
			player.closeInventory();
				
			if (w.getAll().contains(UHC.getInstance().getSettings().getLobbyWorld())) {
				player.sendMessage(Lang.ST_ERROR_LOBBY_WORLD.get());
				return;
			}
				
			if (event.getGame().getMainWorld().equals(w)) {
				player.sendMessage(Lang.ST_ERROR_MAIN_WORLD.get());
				return;
			}

			if (!w.delete()) player.sendMessage(Lang.ST_ERROR_UNLOAD_FAIL.get().replace(LangTag.VALUE.toString(), w.getMainWorldName()));
			
			if (w.getAll().isEmpty()) event.getGame().removeUHCWorld(w);
			else player.sendMessage(Lang.ST_ERROR_DELETING_FAIL.get().replace(LangTag.VALUE.toString(), w.getMainWorldName()));
				
			return;
		}
		
		if (e.getSlot() == 8) {
			playClickSound(player);
			player.openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
	}
	
	public void update(Inventory inv, Player player) {
		
		UHCWorld w = getUHCWorld(inv);
		
		if (w == null) {
			playClickSound(player);
			player.openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory() != null) {
				if (w.equals(getUHCWorld(p.getOpenInventory().getTopInventory()))) {
					p.openInventory(getInventory(w));
				}
			}
		}
	}
}
