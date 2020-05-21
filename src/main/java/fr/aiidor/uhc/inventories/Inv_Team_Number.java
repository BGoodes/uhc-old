package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Team_Number extends GuiConfigBuilder {
	
	@Override
	public String getTitle() {
		return Lang.INV_TEAM_NUMBER_CONFIG.get();
	}
	
	@Override
	public Gui getBackInventory() {
		return GuiManager.INV_CONFIG_TEAMS;
	}

	@Override
	public Integer[] getBannersValues() {
		setCenter(new ItemBuilder(Material.ANVIL, Lang.INV_TEAM_NUMBER.get()).getItem());
		return new Integer[] {-10, -5, -1, 0, +1, +5, +10};
	}

	@Override
	public void addValue(Integer value) {
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			GameSettings s = game.getSettings();
			
			Integer team_number = game.getSettings().getTeamNumber();
			
			if (team_number + value <= 2) s.setTeamNumber(2);
			else if (team_number + value >= 231) s.setTeamNumber(231);
			else s.setTeamNumber(team_number + value);
		}
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList(Lang.INV_TEAM_NUMBER.get());
	}
}
