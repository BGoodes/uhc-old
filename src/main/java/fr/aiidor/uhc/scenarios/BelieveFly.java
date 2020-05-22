package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;

public class BelieveFly extends Scenario {
	
	public BelieveFly(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "believe-fly";
	}

	@Override
	public Material getIcon() {
		return Material.FEATHER;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (!e.getGame().isWaiting()) {
			
			e.getPlayer().closeInventory();
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
			e.setCancelled(true);
		}
	}
}
