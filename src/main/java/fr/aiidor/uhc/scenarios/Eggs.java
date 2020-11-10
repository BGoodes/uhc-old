package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class Eggs extends MobScenario {
	
	public Eggs(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "eggs";
	}

	@Override
	public Material getIcon() {
		return Material.EGG;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
}