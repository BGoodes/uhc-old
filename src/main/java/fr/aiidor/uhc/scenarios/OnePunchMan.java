package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.UHCPlayer;

public class OnePunchMan extends Scenario {
	
	private Boolean XP_mode;
	
	public OnePunchMan(ScenariosManager manager) {
		super(manager);
		
		XP_mode = true;
	}
	
	public Boolean isXPMmode() {
		return XP_mode;
	}
	
	@Override
	public String getID() {
		return "one-punch-man";
	}

	@Override
	public Material getIcon() {
		return Material.GOLD_BOOTS;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.ANIME, Category.FUN);
	}
	
	public Integer getStrength(UHCPlayer p) {
		if (p.isConnected()) {
			if (XP_mode) {
				
			}
		}
		
		return 0;
	}
}
