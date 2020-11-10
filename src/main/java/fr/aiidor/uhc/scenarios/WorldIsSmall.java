package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

public class WorldIsSmall extends Scenario {

	public WorldIsSmall(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "world-is-small";
	}
	
	@Override
	public Material getIcon() {
		return Material.GRASS;
	}
	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		
		super.changeStateEvent(e);
			
		if (!e.getGame().isWaiting()) {
			e.setCancelled(true);
			
			if (e.getPlayer() != null) {
				e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
				e.getPlayer().closeInventory();
			}
		}
	}
}
