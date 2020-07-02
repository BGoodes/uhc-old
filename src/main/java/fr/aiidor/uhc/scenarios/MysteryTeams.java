package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;
import fr.aiidor.uhc.team.UHCTeam;

public class MysteryTeams extends Scenario {
	
	public MysteryTeams(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "mystery-teams";
	}
	
	@Override
	public Material getIcon() {
		return Material.NAME_TAG;
	}
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
	
	@Override
	public Boolean compatibleWith(UHCType type) {
		if (type == UHCType.DEVIL_WATCHES) return false;
		return true;
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		Game game = e.getGame();
		
		if (e.getState()) {
			if (game.hasTeam()) {
				for (UHCTeam t : game.getTeams()) {
					setMystery(t);
				}
			}
		} else {
			if (game.hasTeam()) {
				for (UHCTeam t : game.getTeams()) {
					removeMystery(t);
				}
			}
		}
		
		super.changeStateEvent(e);
	}
	
	
	public void setMystery(UHCTeam t) {
		String prefix = t.getPrefix() + "§k";
		String suffix = "";
		
		for (int i = new Random().nextInt(3); i != 0; i--) {
			suffix = suffix + "a";
		}
		
		t.getTeam().setPrefix(prefix);
		t.getTeam().setSuffix(suffix + "§r");
	}
	
	public void removeMystery(UHCTeam t) {
		if (t.getTeam().getPrefix().endsWith("§k")) {
			t.getTeam().setPrefix(t.getPrefix().replace("§k", ""));
		}
		
		t.getTeam().setSuffix("");
	}
}
