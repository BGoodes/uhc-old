package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

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
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.DOUBLE_JUMP)) return false;
		return true;
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
		super.changeStateEvent(e);
		
		if (e.getState()) {
			for (UHCPlayer p : e.getGame().getAllConnectedPlayers()) {
				p.getPlayer().setAllowFlight(true);
			}
		}
		else {
			for (UHCPlayer p : e.getGame().getPlayingPlayers()) {
				if (p.getPlayer().getGameMode() == GameMode.SURVIVAL || p.getPlayer().getGameMode() == GameMode.ADVENTURE) {
					p.getPlayer().setAllowFlight(false);
				}	
			}
		}
	}
}
