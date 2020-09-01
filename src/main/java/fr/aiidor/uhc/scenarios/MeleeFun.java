package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

public class MeleeFun extends Scenario {
	
	public MeleeFun(ScenariosManager manager) {
		super(manager);
	}
	
	
	@Override
	public String getID() {
		return "melee-fun";
	}
	
	@Override
	public Material getIcon() {
		return Material.RAW_FISH;
	}
	
	@Override
	public Boolean isOriginal() {
		return false;
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		super.changeStateEvent(e);
		
		if (e.getState()) {
			for (UHCPlayer p : e.getGame().getAllConnectedPlayers()) {
				resetMeleeFun(p.getPlayer());
			}
		}
		else {
			for (UHCPlayer p : e.getGame().getAllConnectedPlayers()) {
				setMeleeFun(p.getPlayer());
			}
		}
	}
	
	public void resetMeleeFun(Player player) {
		player.setNoDamageTicks(0);
	}
	
	public void setMeleeFun(Player player) {
		player.setNoDamageTicks(20);
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.FUN);
	}
}
