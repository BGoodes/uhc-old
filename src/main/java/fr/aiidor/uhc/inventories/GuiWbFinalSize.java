package fr.aiidor.uhc.inventories;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiWbFinalSize extends GuiConfigBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_WB_FINAL_SIZE_CONFIG.get();
	}
	
	@Override
	public boolean startProtection() {
		return true;
	}
	
	@Override
	public Gui getBackInventory() {
		return GuiManager.INV_CONFIG_WB;
	}

	@Override
	public Integer[] getBannersValues() {
		setCenter(new ItemBuilder(Material.ANVIL, Lang.INV_WB_FINAL_SIZE.get()).getItem());
		return new Integer[] {-100, -10, -1, 0, +1, +10, +100};
	}

	@Override
	public void addValue(Integer value) {
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			
			GameSettings s = game.getSettings();
			s.wb_size_min = s.wb_size_min + value;
			
			if (s.wb_size_min <= 20) s.wb_size_min = 20;
			if (s.wb_size_min >= 10000) s.wb_size_min = 10000;
		}
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList(Lang.INV_WB_FINAL_SIZE.get());
	}
}
