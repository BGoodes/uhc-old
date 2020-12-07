package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiLoots extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_LOOTS.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"/", "T", "S", "P", " ", " ", " ", " ", " "},
				{"/", "/", "/", "/", "/", "/", "/", "/", "/"},
				{"/", "T1", "S1", "P1", " ", " ", " ", " ", "X"}
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
			
			dictionary.put("/", getGlass((byte) 5));
			
			List<String> lore = new ArrayList<String>();
			
			lore.add("");
			lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1%"));
			lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1%"));
			
			lore.add(Lang.INV_LEFT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5%"));
			lore.add(Lang.INV_RIGHT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5%"));
			
			dictionary.put("T", new ItemBuilder(Material.SAPLING, Lang.INV_TREES.get()).getItem());
			dictionary.put("T1", getConfigItem(null, Lang.INV_UHC_DROP.get(), game.getSettings().uhc_trees));
			
			dictionary.put("S", new ItemBuilder(Material.FLINT, Lang.INV_SILEX.get().replace(LangTag.VALUE.toString(), game.getSettings().silex_drop.toString()))
					.setLore(lore).getItem());
			
			dictionary.put("S1", getConfigItem(null, Lang.INV_UHC_DROP.get(), game.getSettings().uhc_silex));
			
			dictionary.put("P", new ItemBuilder(Material.ENDER_PEARL, Lang.INV_PEARLS.get().replace(LangTag.VALUE.toString(), game.getSettings().pearls_drop.toString()))					
					.setLore(lore).getItem());
			
			dictionary.put("P1", getConfigItem(null, Lang.INV_UHC_DROP.get(), game.getSettings().uhc_pearls));
			
		}
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		ItemStack clicked = event.getItemClicked();
		Game game = event.getGame();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 1) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_TREES.getInventory());
			return;
		}
		
		if (e.getSlot() == 2) {
			if (e.getClick() == ClickType.LEFT) {
				game.getSettings().silex_drop++;
			}
			
			if (e.getClick() == ClickType.SHIFT_LEFT) {
				game.getSettings().silex_drop+=5;
			}
			
			if (e.getClick() == ClickType.RIGHT) {
				game.getSettings().silex_drop--;
			}
			
			if (e.getClick() == ClickType.SHIFT_RIGHT) {
				game.getSettings().silex_drop-=5;
			}
			
			if (game.getSettings().silex_drop <= 0) game.getSettings().silex_drop = 0;
			if (game.getSettings().silex_drop >= 100) game.getSettings().silex_drop = 100;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 3) {
			if (e.getClick() == ClickType.LEFT) {
				game.getSettings().pearls_drop++;
			}
			
			if (e.getClick() == ClickType.SHIFT_LEFT) {
				game.getSettings().pearls_drop+=5;
			}
			
			if (e.getClick() == ClickType.RIGHT) {
				game.getSettings().pearls_drop--;
			}
			
			if (e.getClick() == ClickType.SHIFT_RIGHT) {
				game.getSettings().pearls_drop-=5;
			}
			
			if (game.getSettings().pearls_drop <= 0) game.getSettings().pearls_drop = 0;
			if (game.getSettings().pearls_drop >= 100) game.getSettings().pearls_drop = 100;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 19 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().uhc_trees = !game.getSettings().uhc_trees;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 20 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().uhc_silex = !game.getSettings().uhc_silex;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 21 && clicked.getType() == Material.INK_SACK) {
			game.getSettings().uhc_pearls = !game.getSettings().uhc_pearls;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 26) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
	}
}
