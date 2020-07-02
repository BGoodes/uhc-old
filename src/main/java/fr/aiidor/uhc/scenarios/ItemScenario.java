package fr.aiidor.uhc.scenarios;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

public abstract class ItemScenario extends Scenario {

	public ItemScenario(ScenariosManager manager) {
		super(manager);
	}
	
	public enum GiveTime {
		LOADING, FIRST_EPISODE, EPISODE;
	}
	
	public abstract GiveTime giveTime();
	public abstract List<ItemStack> getItems();
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (e.getGame().isStart() && giveTime() == GiveTime.LOADING) {
			
			e.getPlayer().closeInventory();
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
			e.setCancelled(true);
		}
	}
	
	public void GiveItems(UHCPlayer player) {
		for (ItemStack item : getItems()) {
			player.giveItem(item);
		}
	}
}
