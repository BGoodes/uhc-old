package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class NoFall extends Scenario {

	public NoFall(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "no-fall";
	}
	
	@Override
	public Material getIcon() {
		return Material.FEATHER;
	}
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.SURVIVAL);
	}
}
