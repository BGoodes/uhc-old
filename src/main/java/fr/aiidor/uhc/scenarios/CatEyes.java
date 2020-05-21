package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;

public class CatEyes extends Scenario {

	public PotionEffect nightVision = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false);
	
	public CatEyes(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "cat-eyes";
	}
	
	@Override
	public Material getIcon() {
		return Material.EYE_OF_ENDER;
	}
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN, Category.SURVIVAL);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (e.getGame().isStart() && e.getState() == false) {
			for (UHCPlayer player : e.getGame().getAlivePlayers()) {
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
		}
	}
	
}
