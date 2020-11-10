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
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Scenarios_Settings extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_SCENARIOS.get() + " : §3" + Lang.SETTINGS.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"H", "R", " ", " ", " ", " ", " ", "S", "X"}
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
			
			dictionary.put("H", getConfigItem(Lang.INV_SCENARIO_HIDE.get(), game.getSettings().scenarios_list));
			
			Integer s = game.getSettings().random_scenarios;
			
			ItemBuilder item = new ItemBuilder(Material.COMPASS, s, Lang.INV_SCENARIO_RANDOM.get().replace(LangTag.VALUE.toString(), s > 0 ? s + "" : s == 0 ? Lang.INV_OFF.get() : Lang.INV_RANDOM.get()));
			
			List<String> lore = new ArrayList<String>();
			
			lore.add("");
			lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1"));
			lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1"));
			
			lore.add(Lang.INV_LEFT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5"));
			lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "5"));
			
			item.setLore(lore);
			
			dictionary.put("R", item.getItem());
		}
		
		return dictionary;
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		GameSettings s = event.getGame().getSettings();
		
		if (e.getSlot() == 0) {
			
			s.scenarios_list = !s.scenarios_list;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 1) {
			
			if (!event.getGame().isWaiting()) {
				event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_START.get());
				event.getPlayer().closeInventory();
				return;
			}
			
			if (e.getClick() == ClickType.LEFT) s.random_scenarios++;
			if (e.getClick() == ClickType.SHIFT_LEFT) s.random_scenarios += 5;
			if (e.getClick() == ClickType.RIGHT) s.random_scenarios--;
			if (e.getClick() == ClickType.SHIFT_RIGHT) s.random_scenarios -= 5;
			
			Integer n = UHC.getInstance().getScenarioManager().getScenarios().size();
			if (s.random_scenarios < -1) s.random_scenarios = -1;
			if (s.random_scenarios >= n) s.random_scenarios = n;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
			return;
		}
	}
}
