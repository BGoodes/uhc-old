package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class WebCage extends Scenario {
	
	public WebCage(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "web-cage";
	}

	@Override
	public Material getIcon() {
		return Material.WEB;
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
