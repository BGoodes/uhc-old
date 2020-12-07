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
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiWorldborder extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_WORLD_BORDER.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"I", "F", "S", " ", " ", " ", " ", " ", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			
			dictionary.put("I", new ItemBuilder(Material.STAINED_GLASS, (byte) 5, Lang.INV_WB_SIZE.get()).getItem());
			dictionary.put("F", new ItemBuilder(Material.STAINED_GLASS, (byte) 14, Lang.INV_WB_FINAL_SIZE.get()).getItem());
			dictionary.put("S", new ItemBuilder(Material.QUARTZ, Lang.INV_WB_SPEED.get())
					.setLore(Arrays.asList(
							Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "0.1 " + Lang.INV_BLOCK_PER_SECOND.get()),
							Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "0.1 " + Lang.INV_BLOCK_PER_SECOND.get()))
						)
					.getItem());
		}
		
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		Game game = event.getGame();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 0) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WB_SIZE.getInventory());
			return;
		}
		
		if (e.getSlot() == 1) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WB_FINAL_SIZE.getInventory());
			return;
		}
		
		if (e.getSlot() == 2) {
			if (e.getClick() == ClickType.LEFT) {
				game.getSettings().wb_speed = game.getSettings().wb_speed + 0.1;
			}
			
			if (e.getClick() == ClickType.RIGHT) {
				game.getSettings().wb_speed = game.getSettings().wb_speed - 0.1;
			}
			
			game.getSettings().wb_speed = Math.round(game.getSettings().wb_speed * 10D) / 10D;
			
			if (game.getSettings().wb_speed <= 0.1) game.getSettings().wb_speed = 0.1;
			if (game.getSettings().wb_speed >= 5) game.getSettings().wb_speed = 5.0;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
	}
}
