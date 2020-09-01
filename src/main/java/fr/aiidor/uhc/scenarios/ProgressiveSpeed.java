package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

public class ProgressiveSpeed extends Scenario {
	
	public ProgressiveSpeed(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "progressive-speed";
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
			pot.setType(PotionType.SPEED);
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
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		super.changeStateEvent(e);
		
		if (!e.getState() && !e.isCancelled()) {
			for (UHCPlayer p : e.getGame().getAlivePlayers()) {
				p.removePotionEffect(PotionEffectType.SPEED);
			}
		}
	}
}
