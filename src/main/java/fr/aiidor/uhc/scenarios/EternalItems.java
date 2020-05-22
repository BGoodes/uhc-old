package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class EternalItems extends Scenario {

	public EternalItems(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "eternal-items";
	}
	
	@Override
	public Material getIcon() {
		return Material.NETHER_STAR;
	}
	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
}
