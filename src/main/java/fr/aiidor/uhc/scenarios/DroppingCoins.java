package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class DroppingCoins extends Scenario {
	
	private Integer percent;
	
	private Gui gui;	
	
	public DroppingCoins(ScenariosManager manager) {
		super(manager);
		
		percent = 5;
		
		gui = new GuiBuilder() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 2) {
					
					percent -= 5;
					if (percent < 1) percent = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 3) {
					
					percent--;
					if (percent < 1) percent = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					percent++;
					if (percent > 90) percent = 90;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 6) {
					
					percent += 5;
					if (percent > 100) percent = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
			}
			
			@Override
			public String getTitle() {
				return getName();
			}
			
			@Override
			public String[][] getMatrix() {

				String[][] items = {
						{" ", " ", "--", "-", "F", "+", "++", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				String name = Lang.INV_PROBABILITY_OPTION.get().replace(LangTag.VALUE.toString(), percent.toString() + "%");
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.GOLD_NUGGET, name).getItem());
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-1%").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+1%").setLore(Arrays.asList(name)).getItem());
				
				dictionnary.put("--",  new ItemBuilder(Material.STONE_BUTTON, "§c-5%").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("++",  new ItemBuilder(Material.STONE_BUTTON, "§a+5%").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.INV_PROBABILITY_OPTION.get().replace(LangTag.VALUE.toString(), percent.toString() + "%"));
		
		return lore;
	}
	@Override
	public String getID() {
		return "dropping-coins";
	}

	@Override
	public Material getIcon() {
		return Material.GOLD_NUGGET;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
	
	public void drop(Player player) {
		
		if (new Random().nextInt(100) <= percent) {
			ItemStack[] inv = player.getInventory().getContents();
			ItemStack item = inv[new Random().nextInt(inv.length)];
			
			if (item != null && item.getType() != Material.AIR) {
				player.getInventory().remove(item);
				player.getWorld().dropItemNaturally(player.getLocation(), item);
			}
		}

	}
}
