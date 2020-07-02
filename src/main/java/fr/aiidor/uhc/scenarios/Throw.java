package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class Throw extends Scenario {
	
	public Throw(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "throw";
	}

	@Override
	public Material getIcon() {
		return Material.FIREBALL;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
}
