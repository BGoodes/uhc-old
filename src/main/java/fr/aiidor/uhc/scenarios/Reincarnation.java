package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class Reincarnation extends MobScenario {
	
	public Reincarnation(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "reincarnation";
	}

	@Override
	public Material getIcon() {
		return Material.BONE;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
}