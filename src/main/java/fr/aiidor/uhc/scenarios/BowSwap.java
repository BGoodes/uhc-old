package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class BowSwap extends Scenario {
	
	private Integer probability = 20;
	private Gui gui;
	
	public BowSwap(ScenariosManager manager) {
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
					
					probability -= 5;
					if (probability < 1) probability = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 3) {
					
					probability--;
					if (probability < 1) probability = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					probability++;
					if (probability > 100) probability = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				
				if (e.getSlot() == 6) {
					
					probability += 5;
					if (probability > 100) probability = 100;
					
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
				
				String name = Lang.INV_PROBABILITY_OPTION.get().replace(LangTag.VALUE.toString(), probability.toString() + "%");
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.ENDER_PEARL, name).getItem());
				
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
		lore.add(Lang.INV_PROBABILITY_OPTION.get().replace(LangTag.VALUE.toString(), probability.toString() + "%"));
		
		return lore;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public String getID() {
		return "bow-swap";
	}

	@Override
	public Material getIcon() {
		return Material.ENDER_PEARL;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
	
	public void tp(Player damaged, Player damager) {
		if (new Random().nextInt(100) <= probability) {
			Location loc1 = damaged.getLocation();
			Location loc2 = damager.getLocation();
			
			damaged.teleport(loc2, TeleportCause.PLUGIN);
			damager.teleport(loc1, TeleportCause.PLUGIN);
		}
	}
}
