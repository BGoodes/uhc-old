package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class OnlyOneWinner extends Scenario {

	public OnlyOneWinner(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "only-one-winner";
	}

	@Override
	public Material getIcon() {
		return Material.BANNER;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
}
