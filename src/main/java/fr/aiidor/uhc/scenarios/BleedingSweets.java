package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;

public class BleedingSweets extends Scenario {
	
	public List<ItemStack> items;
	
	public BleedingSweets(ScenariosManager manager) {
		super(manager);
		
		items = new ArrayList<ItemStack>();
		
		items.add(new ItemStack(Material.DIAMOND, 1));
		items.add(new ItemStack(Material.GOLD_INGOT, 5));
		items.add(new ItemStack(Material.ARROW, 16));
		items.add(new ItemStack(Material.STRING, 1));
	}
	
	@Override
	public String getID() {
		return "bleeding-sweets";
	}

	@Override
	public Material getIcon() {
		return Material.DIAMOND;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN, Category.PVP);
	}
}
