package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.utils.Cage;

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
	
	public void generate(Location loc) {
		new Cage(loc.subtract(0, 1, 0), 3, 6, 2,  Material.WEB, Material.WEB, Material.WEB, false).create();
	}
}
