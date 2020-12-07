package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class Urban extends Scenario {
	
	public Urban(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "urban";
	}

	@Override
	public Material getIcon() {
		return Material.ANVIL;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.GENERATION, Category.FUN);
	}
	
	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.SWEAT_WORLD)) return false;
		return true;
	}
	
	@Override
	public Boolean needWorldGeneration() {
		return true;
	}
}
