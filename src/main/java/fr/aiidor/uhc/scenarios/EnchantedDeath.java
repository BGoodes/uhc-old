package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class EnchantedDeath extends Scenario {
	
	public EnchantedDeath(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "enchanted-death";
	}

	@Override
	public Material getIcon() {
		return Material.ENCHANTMENT_TABLE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.ENCHANTMENT);
	}
}
