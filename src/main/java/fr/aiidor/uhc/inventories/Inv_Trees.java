package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Trees extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_TREES.get();
	}

	@Override
	public Boolean titleIsDynamic() {
		return false;
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"A", "D", "T", "S", "G", " ", " ", " ", " "},
				{"GL", "GL", "GL", "GL", "GL", "GL", "GL", "GL", "GL"},
				{"A1", "D1", "T1", "S1" , "G1", " ", " ", " ", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			dictionary.put("GL", getGlass((byte) 13));
			
			dictionary.put("A", new ItemBuilder(Material.APPLE, Lang.INV_TREES_DROP_APPLE.get()).getItem());
			dictionary.put("A1", new ItemBuilder(Material.WOOD_BUTTON, Lang.INV_TREES_RATE.get().replace(LangTag.VALUE.toString(), "+" + game.getSettings().trees_apple + "%")).setLore(Arrays.asList(
					Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "0.1%"), 
					Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "0.1%"),	
					" ",
					Lang.INV_LEFT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "2%"), 
					Lang.INV_RIGHT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "2%")
				)).getItem());
			
			dictionary.put("D", new ItemBuilder(Material.SAPLING, Lang.INV_TREES_DROP_SAPLING.get()).getItem());
			dictionary.put("D1", getConfigItem(null, Lang.INV_STATE.get(), game.getSettings().trees_sapling));
			
			dictionary.put("T", new ItemBuilder(Material.SAPLING, (byte) 2, Lang.INV_TREES_ALL_TREES.get()).getItem());
			dictionary.put("T1", getConfigItem(null, Lang.INV_STATE.get(), game.getSettings().all_trees_drop));
			
			dictionary.put("S", new ItemBuilder(Material.SHEARS, Lang.INV_TREES_SHEARS.get()).getItem());
			dictionary.put("S1", getConfigItem(null, Lang.INV_STATE.get(), game.getSettings().trees_shears));
			
			dictionary.put("G", new ItemBuilder(Material.GOLDEN_APPLE, Lang.INV_TREES_GOLDEN_APPLE.get()).getItem());
			dictionary.put("G1", getConfigItem(null, Lang.INV_STATE.get(), game.getSettings().trees_gapple));
		}

		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		ItemStack clicked = event.getItemClicked();
		Game game = event.getGame();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 18 && clicked.getType() == Material.WOOD_BUTTON) {
			if (e.getClick() == ClickType.LEFT) {
				game.getSettings().trees_apple = game.getSettings().trees_apple + 0.1;
			}
			
			if (e.getClick() == ClickType.RIGHT) {
				game.getSettings().trees_apple = game.getSettings().trees_apple - 0.1;
			}
			
			if (e.getClick() == ClickType.SHIFT_LEFT) {
				game.getSettings().trees_apple = game.getSettings().trees_apple + 2;
			}
			
			if (e.getClick() == ClickType.SHIFT_RIGHT) {
				game.getSettings().trees_apple = game.getSettings().trees_apple - 2;
			}
			
			game.getSettings().trees_apple = Math.round(game.getSettings().trees_apple * 10D) / 10D;
			
			if (game.getSettings().trees_apple < 0) game.getSettings().trees_apple = 0.0;
			if (game.getSettings().trees_apple > 50) game.getSettings().trees_apple = 50.0;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		
		if (e.getSlot() == 19 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().trees_sapling = !game.getSettings().trees_sapling;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 20 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().all_trees_drop = !game.getSettings().all_trees_drop;
			playClickSound(event.getPlayer());
			update();
			return;
		}

		if (e.getSlot() == 21 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().trees_shears = !game.getSettings().trees_shears;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 22 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().trees_gapple = !game.getSettings().trees_gapple;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 26) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_LOOTS.getInventory());
			return;
		}
	}
}
