package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;

public class VillagerMadness extends ItemScenario {
	
	private List<ItemStack> items;
	
	@SuppressWarnings("deprecation")
	public VillagerMadness(ScenariosManager manager) {
		super(manager);
		
		items = Arrays.asList(
				new ItemStack(Material.MONSTER_EGG, 64, EntityType.VILLAGER.getTypeId()), 
				new ItemStack(Material.EMERALD_BLOCK, 64) 
			);
	}
	
	@Override
	public String getID() {
		return "villager-madness";
	}

	@Override
	public Material getIcon() {
		return Material.EMERALD;
	}
	
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
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
