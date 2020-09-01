package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Cupid extends Scenario {
	
	private Integer percent = 1;
	private Gui gui;
	
	public Cupid(ScenariosManager manager) {
		super(manager);
		
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
					if (percent > 100) percent = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				
				if (e.getSlot() == 6) {
					
					percent += 5;
					if (percent < 1) percent = 1;
					
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
				
				String name = Lang.CUPID_HEAL.get().replace(LangTag.VALUE.toString(), percent.toString());
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.POTION, name).setPotionType(PotionType.INSTANT_HEAL, false).getItem());
				
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
		lore.add(Lang.CUPID_HEAL.get().replace(LangTag.VALUE.toString(), percent.toString()));
		
		return lore;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public String getID() {
		return "cupid";
	}

	@Override
	public Material getIcon() {
		return Material.ARROW;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	public void heal(Player damager, Double damage) {
		Double h = (damager.getMaxHealth() / 100.0) * percent;
		if (h >= damage) h = damage;
		
		if (damager.getHealth() + h >= damager.getMaxHealth()) damager.setHealth(damager.getMaxHealth());
		else damager.setHealth(damager.getHealth() + h);
	}
}
