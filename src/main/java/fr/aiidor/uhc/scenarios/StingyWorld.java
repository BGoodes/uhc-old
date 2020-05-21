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
import fr.aiidor.uhc.inventories.GuiClickEvent;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.ItemBuilder;

public class StingyWorld extends Scenario {
	
	private Gui gui;
	
	public Boolean stingyBlocks;
	public Boolean stingyMobs;
	public Boolean stingyTrees;
	public Boolean stingyLakes;
	public Boolean stingyBombs;

	
	public StingyWorld(ScenariosManager manager) {
		super(manager);
		
		stingyBlocks = true;
		stingyMobs = true;
		stingyTrees = true;
		stingyLakes = false;
		stingyBombs = false;
		
		gui = new GuiBuilder() {
			
			@Override
			public Boolean titleIsDynamic() {
				return false;
			}
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				ItemStack clicked = event.getItemClicked();
				
				e.setCancelled(true);
				
				if (e.getSlot() == 0 && clicked.getType() == Material.BRICK) {
					stingyBlocks = !stingyBlocks;
					update();
					return;
				}
				
				if (e.getSlot() == 1 && clicked.getType() == Material.ROTTEN_FLESH) {
					stingyMobs = !stingyMobs;
					update();
					return;
				}
				
				if (e.getSlot() == 2 && clicked.getType() == Material.LOG) {
					stingyTrees = !stingyTrees;
					update();
					return;
				}
				
				if (e.getSlot() == 3 && clicked.getType() == Material.WATER_BUCKET) {
					stingyLakes = !stingyLakes;
					update();
					return;
				}
				
				if (e.getSlot() == 4 && clicked.getType() == Material.TNT) {
					stingyBombs = !stingyBombs;
					update();
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
						{"B", "M", "L", "W", "T", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("B", getItem(Material.BRICK, Lang.STINGY_BLOCKS.get(), stingyBlocks));
				dictionnary.put("M", getItem(Material.ROTTEN_FLESH, Lang.STINGY_MOBS.get(), stingyMobs));
				dictionnary.put("L", getItem(Material.LOG, Lang.STINGY_TREES.get(), stingyTrees));
				dictionnary.put("W", getItem(Material.WATER_BUCKET, Lang.STINGY_LAKES.get(), stingyLakes));
				dictionnary.put("T", getItem(Material.TNT, Lang.STINGY_BOMBS.get(), stingyBombs));
				
				return dictionnary;
			}
		};
	}
	
	private ItemStack getItem(Material mat, String displayName, Boolean on) {
		ItemBuilder builder = new ItemBuilder(mat, displayName);
		List<String> lore = new ArrayList<String>();
		
		if (on) lore.add(Lang.INV_ON.get());
		else lore.add(Lang.INV_OFF.get());
		
		builder.setLore(lore);
		
		return builder.getItem();
	}
	
	@Override
	public String getID() {
		return "stingy-world";
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		
		if (stingyBlocks) lore.add("§7" + Lang.removeColor(Lang.STINGY_BLOCKS.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.STINGY_BLOCKS.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (stingyMobs) lore.add("§7" + Lang.removeColor(Lang.STINGY_MOBS.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.STINGY_MOBS.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (stingyTrees) lore.add("§7" + Lang.removeColor(Lang.STINGY_TREES.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.STINGY_TREES.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (stingyLakes) lore.add("§7" + Lang.removeColor(Lang.STINGY_LAKES.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.STINGY_LAKES.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (stingyBombs) lore.add("§7" + Lang.removeColor(Lang.STINGY_BOMBS.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.STINGY_BOMBS.get()) + " §8» " + Lang.INV_OFF.get());
		
		return lore;
	}

	@Override
	public Material getIcon() {
		return Material.DIRT;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
}
