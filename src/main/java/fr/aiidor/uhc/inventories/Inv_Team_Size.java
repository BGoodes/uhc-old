package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Team_Size extends GuiConfigBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_TEAM_SIZE_CONFIG.get();
	}
	
	@Override
	public boolean startProtection() {
		return true;
	}
	
	@Override
	public Gui getBackInventory() {
		return GuiManager.INV_CONFIG_TEAMS;
	}

	@Override
	public Integer[] getBannersValues() {
		setCenter(new ItemBuilder(Material.ANVIL, Lang.INV_TEAM_SIZE.get()).getItem());
		return new Integer[] {-10, -5, -1, 0, +1, +5, +10};
	}

	@Override
	public void addValue(Integer value) {
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			GameSettings s = game.getSettings();
			
			Integer team_size = game.getSettings().getTeamSize();
			
			if (team_size + value <= 1) s.setTeamSize(1);
			else if (team_size + value >= 200) s.setTeamSize(200);
			else s.setTeamSize(team_size + value);
		}
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList(Lang.INV_TEAM_SIZE.get());
	}
}
