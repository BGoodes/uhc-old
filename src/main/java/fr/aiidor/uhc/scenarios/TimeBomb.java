package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;

public class TimeBomb extends Scenario {
	
	public Integer boom_time;
	public Boolean boom;
	
	public TimeBomb(ScenariosManager manager) {
		super(manager);
		
		boom_time = 30;
		boom = true;
	}
	
	@Override
	public String getID() {
		return "timebomb";
	}

	@Override
	public Material getIcon() {
		return Material.CHEST;
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
