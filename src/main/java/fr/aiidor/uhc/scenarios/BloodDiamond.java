package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiClickEvent;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.ItemBuilder;

public class BloodDiamond extends Scenario {
	
	public int life = 1;
	private GuiBuilder gui;
	
	public BloodDiamond(ScenariosManager manager) {
		super(manager);
		
		gui = new GuiBuilder() {
			
			@Override
			public Boolean titleIsDynamic() {
				return false;
			}
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 3) {
					
					life--;
					if (life < 1) life = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					life++;
					if (life > 20) life = 20;
					
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
						{" ", " ", " ", "-", "F", "+", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				String name = "§7" + life/2f + " §c❤";
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.GOLDEN_APPLE, name).getItem());
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-0.5 §d❤").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+0.5 §d❤").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public String getID() {
		return "blood-diamond";
	}

	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add("§7" + life/2f + " §c❤");
		
		return lore;
	}
	
	@Override
	public Material getIcon() {
		return Material.DIAMOND_ORE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.SURVIVAL);
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
}
