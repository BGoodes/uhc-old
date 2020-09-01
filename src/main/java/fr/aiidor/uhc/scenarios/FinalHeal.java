package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class FinalHeal extends Scenario {
	
	public FinalHeal(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "final-heal";
	}

	@Override
	public Material getIcon() {
		return Material.POTION;
	}

	@Override
	public ItemStack getScenarioIcon(Boolean icon, Boolean category, Boolean config) {
		
		ItemStack item = super.getScenarioIcon(icon, category, config);
		
		if (icon) {
			Potion pot = new Potion(1);
			pot.setType(PotionType.INSTANT_HEAL);
			pot.apply(item);
		}

		return item;
	}
	
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	public void heal(Game game) {
		
		game.playSound(Sound.LEVEL_UP);
		
		for (UHCPlayer p : game.getPlayingPlayers()) {
			p.getPlayer().setHealth(p.getPlayer().getMaxHealth());
		}
		
		game.broadcast(Lang.BC_FINAL_HEAL.get());
	}
}
