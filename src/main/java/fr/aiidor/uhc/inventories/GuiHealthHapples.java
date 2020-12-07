package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiHealthHapples extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_LIFE_HAPPLE.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"S", "P", "A", " ", " ", " ", " ", " ", "X"}
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
			
			dictionary.put("S", getConfigItem(null, game.getSettings().golden_head));
			dictionary.put("P", new ItemBuilder(Material.POTION, Lang.INV_LIFE_EFFECTS.get()).setPotionType(PotionType.REGEN, true).addFlag(ItemFlag.HIDE_POTION_EFFECTS).getItem());
			dictionary.put("A", new ItemBuilder(Material.GOLD_CHESTPLATE, Lang.INV_LIFE_ABSO.get().replace(LangTag.VALUE.toString(), (float) game.getSettings().happle_abso/2f + " §c❤")).setLore(Arrays.asList(
						Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "§f0.5 §c❤"),
						Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "§f0.5 §c❤")
					)).getItem());
			
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
			game.getSettings().golden_head = !event.getGame().getSettings().golden_head;
			update();
		}
		
		if (e.getSlot() == 2) {
			if (e.getClick() == ClickType.LEFT) {
				game.getSettings().happle_abso = game.getSettings().happle_abso + 1;
			}
			
			if (e.getClick() == ClickType.RIGHT) {
				game.getSettings().happle_abso = game.getSettings().happle_abso - 1;
			}
			
			if (game.getSettings().happle_abso <= 0) game.getSettings().happle_abso = 0;
			if (game.getSettings().happle_abso >= 20) game.getSettings().happle_abso = 20;
			
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_LIFE_APPLE.getInventory());
			return;
		}
	}
}
