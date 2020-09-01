package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class GapZap extends Scenario {
	
	public GapZap(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "gapzap";
	}

	@Override
	public Material getIcon() {
		return Material.GOLDEN_APPLE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
}
