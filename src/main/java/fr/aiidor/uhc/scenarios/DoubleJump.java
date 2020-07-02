package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class DoubleJump extends Scenario {
	
	public DoubleJump(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "double-jump";
	}

	@Override
	public Material getIcon() {
		return Material.RABBIT_FOOT;
	}

	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.BELIEVE_FLY)) return false;
		return true;
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
