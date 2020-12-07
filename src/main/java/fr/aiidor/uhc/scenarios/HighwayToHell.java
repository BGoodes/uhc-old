package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;

public class HighwayToHell extends ItemScenario {
	
	private List<ItemStack> items;
	
	public HighwayToHell(ScenariosManager manager) {
		super(manager);
		
		items = Arrays.asList(
				new ItemStack(Material.IRON_PICKAXE, 1), 
				new ItemStack(Material.LAVA_BUCKET, 1), 
				new ItemStack(Material.FLINT_AND_STEEL, 1), 
				new ItemStack(Material.OBSIDIAN, 14), 
				new ItemStack(Material.COOKED_BEEF, 64)
			);
	}
	
	@Override
	public String getID() {
		return "highway-to-hell";
	}

	@Override
	public Material getIcon() {
		return Material.OBSIDIAN;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	

	@Override
	public GiveTime giveTime() {
		return GiveTime.LOADING;
	}

	@Override
	public List<ItemStack> getItems() {
		return items;
	}
	
	
}
