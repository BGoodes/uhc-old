package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class OpenHouse extends Scenario {
	
	public OpenHouse(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "open-house";
	}

	@Override
	public Material getIcon() {
		return Material.WOOD_DOOR;
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
