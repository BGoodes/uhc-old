package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiPvpTime extends GuiConfigBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_PVP_TIME_CONFIG.get();
	}
	
	@Override
	public boolean startProtection() {
		return true;
	}
	
	@Override
	public Gui getBackInventory() {
		return GuiManager.INV_CONFIG_TIME;
	}

	@Override
	public Integer[] getBannersValues() {
		setCenter(new ItemBuilder(Material.WATCH, Lang.INV_PVP_TIME.get()).getItem());
		return new Integer[] {-10, -5, -1, 0, +1, +5, +10};
	}

	@Override
	public void addValue(Integer value) {
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			GameSettings s = game.getSettings();
			
			s.pvp_time = s.pvp_time + value;
			
			if (s.pvp_time <= 0) s.pvp_time = 0;
			if (s.pvp_time >= 120) s.pvp_time = 120;
		}
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList(Lang.INV_PVP_TIME.get());
	}
}
