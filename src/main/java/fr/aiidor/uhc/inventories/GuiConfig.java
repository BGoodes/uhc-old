package fr.aiidor.uhc.inventories;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiConfig extends GuiBuilder {
	
	@Override
	public String getTitle() {
		return Lang.INV_CONFIGURATION.get();
	}
	
	@Override
	public InventoryHolder getHolder() {
		return null;
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"B", "L", "L", "L", "L", "L", "L", "L", "B"},
				{"L", "T", "J", "Y", "O", "W", "D", "/", "L"},
				{"L", "G", "A", "R", "!", " ", " ", " ", "L"},
				{"B", "L", "L", "L", "S", "C", "L", "L", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getExitIcon());
		dictionary.put("S", new ItemBuilder(Material.SLIME_BALL, Lang.INV_START_GAME.get()).setGliding().getItem());
		
		dictionary.put("T", new ItemBuilder(Material.BANNER, (byte) 14, Lang.INV_TEAMS.get()).getItem());
		dictionary.put("J", new ItemBuilder(Material.SKULL_ITEM, Lang.INV_PLAYERS.get(), (byte) 3).setHeadOwner("B_Goodes").getItem());
		dictionary.put("Y", new ItemBuilder(Material.CHEST, Lang.INV_STUFF.get()).getItem());
		dictionary.put("O", new ItemBuilder(Material.WATCH, Lang.INV_TIME.get()).getItem());
		dictionary.put("W", new ItemBuilder(Material.BEACON, Lang.INV_WORLD_BORDER.get()).getItem());
		dictionary.put("D", new ItemBuilder(Material.ENDER_PORTAL_FRAME, Lang.INV_WORLDS.get()).getItem());
		dictionary.put("/", new ItemBuilder(Material.DIAMOND_SWORD, Lang.INV_SCENARIOS.get()).setGliding().getItem());
		
		dictionary.put("G", new ItemBuilder(Material.GOLDEN_APPLE, (byte) 1, Lang.INV_LIFE.get()).getItem());
		dictionary.put("A", new ItemBuilder(Material.APPLE, Lang.INV_LOOTS.get()).getItem());
		dictionary.put("R", new ItemBuilder(Material.BOOK, Lang.INV_RULES.get()).getItem());
		
		//GLASS
		dictionary.put("L", getGlass((byte) 3));
		dictionary.put("B", getGlass((byte) 11));
		
		dictionary.put("!", new ItemBuilder(Material.AIR).getItem());
		
		//CANCEL BUTTON
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			if (game.getState() == GameState.STARTING) {
				dictionary.put("C", new ItemBuilder(Material.REDSTONE_BLOCK, Lang.INV_CANCEL_GAME.get()).setGliding().getItem());
			}
		}
		
		if (!dictionary.containsKey("C")) dictionary.put("C", getGlass((byte) 3));
		
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 35) {
			playClickSound(event.getPlayer());
			event.getPlayer().closeInventory();
			return;
		}
		
		//GAME START & CANCEL
		if (e.getSlot() == 31) {
			
			event.getPlayer().closeInventory();
			
			if (!event.getGame().start()) {
				event.getPlayer().sendMessage(Lang.ST_ERROR_GAME_START.get());
			}
			
			return;
		}
		
		if (e.getSlot() == 32 && event.getItemClicked().getType() == Material.REDSTONE_BLOCK) {
			
			event.getPlayer().closeInventory();
			
			if (event.getGame().getState() == GameState.STARTING) {
				
				if (event.getGame().isRunning()) {
					if (event.getGame().getRunner().getTime() > 0) {
						
						event.getGame().getRunner().stop();
						return;
					}
					
				}
			}
			
			event.getPlayer().sendMessage(Lang.ST_ERROR_GAME_CANCEL.get());
			
			return;
		}
		
		//OPTIONS
		
		if (e.getSlot() == 10) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_TEAMS.getInventory());
			return;
		}
		
		if (e.getSlot() == 11) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_PLAYERS.getInventory());
			return;
		}
		
		if (e.getSlot() == 12) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_STUFF.getInventory());
			return;
		}
		
		if (e.getSlot() == 13) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_TIME.getInventory());
			return;
		}
		
		if (e.getSlot() == 14) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WB.getInventory());
			return;
		}
		
		if (e.getSlot() == 15) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
		
		if (e.getSlot() == 16) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
			return;
		}
		
		if (e.getSlot() == 19) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_LIFE.getInventory());
			return;
		}
		
		if (e.getSlot() == 20) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_LOOTS.getInventory());
			return;
		}
	}
}
