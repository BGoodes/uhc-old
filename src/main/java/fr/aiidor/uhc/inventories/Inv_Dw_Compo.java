package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.dwuhc.DWCamp;
import fr.aiidor.dwuhc.DWRoleType;
import fr.aiidor.dwuhc.DWSettings;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Dw_Compo extends GuiBuilder {

	private final Integer[] role_slots = {12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
	
	@Override
	public String getTitle() {
		return Lang.INV_DW_COMPO.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"G", "G", "L", "L", "L", "L", "L", "G", "G"},
				{"G", "V", "S", " ", " ", " ", " ", " ", "G"}, //10, 11, 12, 13, 14, 15, 16
				{"L", " ", " ", " ", " ", " ", " ", " ", "L"}, //19, 20, 21, 22, 23, 24, 25
				{"L", " ", " ", " ", " ", " ", " ", " ", "L"}, //28, 29, 30, 31, 32, 33, 34
				{"G", " ", " ", " ", " ", " ", " ", " ", "G"}, //37, 38, 39, 40, 41, 42, 43
				{"G", "G", "L", "L", "L", "L", "L", "G", "X"},        // size : 28
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		dictionary.put("L", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 14, " ").getItem());
		dictionary.put("G", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 2, " ").getItem());
		
		dictionary.put("V", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("S", new ItemBuilder(Material.AIR).getItem());
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
				
				DevilWatches dw = (DevilWatches) game.getUHCMode();
				Integer number = game.getAlivePlayers().size() - dw.getSettings().getRoleNumber();
				
				dictionary.put("V", new ItemBuilder(Material.EXP_BOTTLE, "§6" + DWRoleType.VILLAGER.getName() + " : §e" + number).setAmount(number).getItem());
				dictionary.put("S", new ItemBuilder(Material.RED_ROSE, "§4" + Lang.removeColor(DWCamp.SECTARIANS.getName()) + " : §c" + dw.getSettings().sectarians).setAmount(dw.getSettings().sectarians)
						.setLore(Arrays.asList(
								Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1"),
								Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1"),
								"",
								Lang.INV_LEFT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5"),
								Lang.INV_RIGHT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5")))
						.getItem());
			}
		}
		return dictionary;
	}
	
	@Override
	public Inventory getInventory() {
		Inventory inv = super.getInventory();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
				
				DevilWatches dw = (DevilWatches) game.getUHCMode();
				
				Integer i = 0;
				
				
				for (DWRoleType r : DWRoleType.getRoles()) {
					
					if (dw.getSettings().compo.containsKey(r)) {
						
						Integer slot = role_slots[i];
						Integer number = dw.getSettings().compo.get(r);
						
						inv.setItem(slot, new ItemBuilder(getConfigItem(r.getBaseCamp().getPrefix() + r.getName(), number)).setAmount(number)
								.setLore(Arrays.asList(
									Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1"),
									Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1"),
									"",
									Lang.INV_LEFT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5"),
									Lang.INV_RIGHT_SHIFT_CLICK.get().replace(LangTag.VALUE.toString(), "5")))
								.getItem());
						i++;
					}
				}
			}
		}
		
		return inv;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		ItemStack clicked = event.getItemClicked();
		
		e.setCancelled(true);
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			if (game.getUHCMode().getUHCType() == UHCType.DEVIL_WATCHES) {
				
				
				DWSettings s = ((DevilWatches) game.getUHCMode()).getSettings();
				
				if (e.getSlot() == 11) {
					
					if (e.getClick() == ClickType.LEFT) {
						s.sectarians++;
					}
					
					if (e.getClick() == ClickType.SHIFT_LEFT) {
						s.sectarians+=5;
					}
					
					if (e.getClick() == ClickType.RIGHT) {
						s.sectarians--;
					}
					
					if (e.getClick() == ClickType.SHIFT_RIGHT) {
						s.sectarians-=5;
					}
					
					if (s.sectarians < 0) s.sectarians = 0;
					if (s.sectarians >= 50) s.sectarians = 50;
					
					playClickSound(event.getPlayer());
					update();
				}
				
				if (clicked.getType() == Material.INK_SACK && clicked.getItemMeta().hasDisplayName()) {
					String displayName = clicked.getItemMeta().getDisplayName();
					
					for (DWRoleType r : DWRoleType.values()) {
						if (s.compo.keySet().contains(r)) {
							if (r.getName().equals(displayName.substring(2))) {
								
								Integer value = s.compo.get(r);
								
								if (e.getClick() == ClickType.RIGHT) {
									value--;
								}
								
								if (e.getClick() == ClickType.SHIFT_RIGHT) {
									value-=5;
								}
								
								
								if (e.getClick() == ClickType.LEFT) {
									value++;
								}
								
								if (e.getClick() == ClickType.SHIFT_LEFT) {
									value+=5;
								}
								
								if (value > 20) value = 20;
								if (value < 0) value = 0;
								
								s.compo.put(r, value);
								
								playClickSound(event.getPlayer());
								update();
								return;
							}
						}
					}
				}
			}
		}

		
		if (e.getSlot() == 53) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_DW.getInventory());
			return;
		}
	}
}
