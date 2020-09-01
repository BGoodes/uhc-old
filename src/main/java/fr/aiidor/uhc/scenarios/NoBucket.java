package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;

public class NoBucket extends Scenario {

	
	private GuiBuilder gui;
	
	public Boolean lava = false;
	public Boolean water = false;
	
	public NoBucket(ScenariosManager manager) {
		super(manager);
		
		gui = new GuiBuilder() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				
				e.setCancelled(true);
				
				if (e.getSlot() == 0) {
					water = !water;
					update();
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 1) {
					lava = !lava;
					update();
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
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
						{"W", "L", " ", " ", " ", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("W", getConfigItem(Material.WATER_BUCKET, Lang.WATER_BUCKET.get(), water));
				dictionnary.put("L", getConfigItem(Material.LAVA_BUCKET, Lang.LAVA_BUCKET.get(), lava));
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		
		if (water) lore.add("§7" + Lang.removeColor(Lang.WATER_BUCKET.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.WATER_BUCKET.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (lava) lore.add("§7" + Lang.removeColor(Lang.LAVA_BUCKET.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.LAVA_BUCKET.get()) + " §8» " + Lang.INV_OFF.get());
		
		return lore;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public String getID() {
		return "no-bucket";
	}
	
	@Override
	public Material getIcon() {
		return Material.BUCKET;
	}
	
	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
}
