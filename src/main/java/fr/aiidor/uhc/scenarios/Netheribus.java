package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class Netheribus extends Scenario {
	
	public Netheribus(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "netheribus";
	}

	@Override
	public Material getIcon() {
		return Material.NETHER_BRICK;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.SKYHIGH)) return false;
		return true;
	}
	
	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.SURVIVAL, Category.FUN);
	}
}
