package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class SweatWorld extends Scenario {
	
	public SweatWorld(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "sweat-world";
	}

	@Override
	public Material getIcon() {
		return Material.DIAMOND_SWORD;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.GENERATION, Category.PVP);
	}
}
