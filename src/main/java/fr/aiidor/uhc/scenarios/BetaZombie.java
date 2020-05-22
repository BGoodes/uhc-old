package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class BetaZombie extends Scenario {
	
	public BetaZombie(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "beta-zombie";
	}

	@Override
	public Material getIcon() {
		return Material.ROTTEN_FLESH;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
}
