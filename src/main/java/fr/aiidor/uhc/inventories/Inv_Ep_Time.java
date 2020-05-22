package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Ep_Time extends GuiConfigBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_EP_TIME_CONFIG.get();
	}
	
	@Override
	public boolean startProtection() {
		return false;
	}
	
	@Override
	public Gui getBackInventory() {
		return GuiManager.INV_CONFIG_TIME;
	}

	@Override
	public Integer[] getBannersValues() {
		setCenter(new ItemBuilder(Material.WATCH, Lang.INV_EP_TIME.get()).getItem());
		return new Integer[] {-10, -5, -1, 0, +1, +5, +10};
	}

	@Override
	public void addValue(Integer value) {
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			GameSettings s = game.getSettings();
			
			s.ep_time = s.ep_time + value;
			
			if (s.ep_time <= 5) s.ep_time = 5;
			if (s.ep_time >= 120) s.ep_time = 120;
		}
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList(Lang.INV_EP_TIME.get());
	}
}
