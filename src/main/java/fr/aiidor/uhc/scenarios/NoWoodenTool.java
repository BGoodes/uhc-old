package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;

public class NoWoodenTool extends Scenario {
	
	private Gui gui;
	
	private String[] tools = new String[] {"SWORD", "PICKAXE", "AXE", "SPADE", "HOE"};
	private String[] materials = new String[] {"WOOD", "GOLD", "STONE", "IRON", "DIAMOND"};
	
	private Integer[] indexes = new Integer[] {2, 2, 2, 2, 2};
	
	public NoWoodenTool(ScenariosManager manager) {
		super(manager);
		
		gui = new Gui() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);

				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() < indexes.length) {
					Integer i = indexes[e.getSlot()];
					i++;
					
					if (i >= indexes.length) i = 0;
					indexes[e.getSlot()] = i;
					
					update();
					playClickSound(event.getPlayer());
					return;
				}
			}
			
			@Override
			public Inventory getInventory() {
				Inventory inv = Bukkit.createInventory(null, 9, getName());
				inv.setItem(8, getBackIcon());
				
				for (Integer i = 0; i < tools.length; i++) {
					Material mat = Material.valueOf(materials[indexes[i]] + "_" + tools[i]);
					inv.setItem(i, new ItemStack(mat));
				}
				
				return inv;
			}
		};
	}
	
	@Override
	public String getID() {
		return "no-wooden-tool";
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}

	@Override
	public Material getIcon() {
		return Material.WOOD_SPADE;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	
	public ItemStack changeItem(ItemStack item) {
		Material mat = item.getType();

		for (Integer i = 0; i < tools.length; i++) {
			String tool = tools[i];
			
			if (mat.name().endsWith("_" + tool)) {
				
				for (Integer i2 = 0; i2 < materials.length; i2++) {
					String material = materials[i2];
					
					if (mat.name().startsWith(material + "_")) {
						if (i2 < indexes[i]) {
							
							item.setType(Material.valueOf(materials[indexes[i]] + "_" + tool));
						}
					}
				}
			}
		}
		
		return item;
	}
}
