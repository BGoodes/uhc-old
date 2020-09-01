package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
import fr.aiidor.uhc.tools.ItemBuilder;
import fr.aiidor.uhc.world.UHCWorld;
import fr.aiidor.uhc.world.WorldManager;

public class Inv_World_Settings extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_W_SETTINGS.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"W", "N", "O", "E", " ", " ", "T", "D", "X"}
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
			dictionary.put("D", new ItemBuilder(Material.DARK_OAK_DOOR_ITEM, Lang.INV_W_DELETE.get()).getItem());
		}
		
		return dictionary;
	}
	
	private UHCWorld getUHCWorld(Inventory inv) {
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			ItemStack item = inv.getItem(0);
			
			if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
				World w = Bukkit.getWorld(item.getItemMeta().getDisplayName().substring(2));
				
				if (w == null) return null;
				return game.getUHCWorld(w);
			}
		}
		
		return null;
	}
	
	public Inventory getInventory(UHCWorld w) {
		Inventory inv = super.getInventory();
		
		inv.setItem(0, new ItemBuilder(Material.NAME_TAG, "§6" + w.getMainWorldName()).getItem());
		return inv;
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		UHCWorld w = getUHCWorld(event.getInventory());
		Player player = event.getPlayer();
		
		Game game = event.getGame();
		
		if (game.getState() != GameState.WAITING) {
			player.sendMessage(Lang.ST_ERROR_OPTION_START.get());
			
			playClickSound(player);
			player.closeInventory();
			return;
		}
		
		if (w == null) {
			playClickSound(player);
			player.openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
		
		if (e.getSlot() == 6) {
				
			playClickSound(player);
			player.closeInventory();
				
			if (w.getMainWorld() == null) {
				player.sendMessage(Lang.ST_ERROR_WORLD_GENERATION.get());
				return;
			}
				
			Location loc = UHC.getInstance().getSettings().lobby.clone();
			loc.setWorld(w.getMainWorld());
				
			player.teleport(loc);
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

			for (World wo : w.getAll()) {
				WorldManager wm = new WorldManager(wo);
					
				if (wm.unload())
					wm.deleteWorld();
				else player.sendMessage(Lang.ST_ERROR_UNLOAD_FAIL.get().replace(LangTag.VALUE.toString(), wo.getName()));
			}
				
			if (w.getAll().isEmpty()) {
					event.getGame().removeUHCWorld(w);
			}
				
			return;
		}
		
		if (e.getSlot() == 8) {
			playClickSound(player);
			player.openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
	}
}
