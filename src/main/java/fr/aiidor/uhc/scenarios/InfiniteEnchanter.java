package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class InfiniteEnchanter extends Scenario {
	
	public InfiniteEnchanter(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "infinite-enchanter";
	}

	@Override
	public Material getIcon() {
		return Material.EXP_BOTTLE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.ENCHANTMENT);
	}
}
