package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class EnderReplacement extends Scenario {
	
	public EnderReplacement(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "ender-replacement";
	}

	@Override
	public Material getIcon() {
		return Material.ENDER_PEARL;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.FUN);
	}
}
