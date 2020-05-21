package fr.aiidor.uhc.inventories;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.scenarios.Scenario;

public class GuiManager implements Listener {
	
	private Set<Gui> inventories;
	
	public GuiManager() {
		inventories = new HashSet<Gui>();
		
		INV_CONFIG = createInventory(new Inv_Config());
		INV_CONFIG_SCENARIOS = createInventory(new Inv_Scenarios());
		INV_CONFIG_STUFF = createInventory(new Inv_Stuff());
		
		INV_CONFIG_START_STUFF = createInventory(new Inv_Start_Stuff());
		INV_CONFIG_DEATH_STUFF = createInventory(new Inv_Death_Stuff());
		INV_CONFIG_PLAYERS = createInventory(new Inv_Players());
		
		INV_TEAMS_CHOOSE = createInventory(new Inv_Teams_Choose());
		INV_CONFIG_TEAMS = createInventory(new Inv_Teams());
		
		
		INV_TEAM_NUMBER = createInventory(new Inv_Team_Number());
		INV_TEAM_SIZE = createInventory(new Inv_Team_Size());
		
		INV_SCENARIO_LIST = createInventory(new Inv_Scenarios_List());
		
		for (Scenario scenario : UHC.getInstance().getScenarioManager().getScenarios()) {
			if (scenario.hasSettings()) {
				createInventory(scenario.getSettings());
			}
		}
	}
	
	public static Gui INV_CONFIG;
	public static Gui INV_CONFIG_SCENARIOS;
	public static Gui INV_CONFIG_STUFF;
	public static Gui INV_CONFIG_START_STUFF;
	public static Gui INV_CONFIG_DEATH_STUFF;
	public static Gui INV_CONFIG_PLAYERS;
	public static Gui INV_TEAMS_CHOOSE;
	public static Gui INV_CONFIG_TEAMS;
	public static Gui INV_SCENARIO_LIST;
	
	//CONFIG VALUES
	public static Gui INV_TEAM_NUMBER;
	public static Gui INV_TEAM_SIZE;
	
	public Gui createInventory(Gui inv) {
		inventories.add(inv);
		return inv;
	}
	
	@EventHandler
	public void onPlayerClickEvent(InventoryClickEvent e) {
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			if (!(e.getWhoClicked() instanceof Player)) return;
			if (e.getCurrentItem() == null) return;
			
			Inventory inv = e.getInventory();
			Player player = (Player) e.getWhoClicked();	
			ItemStack clicked = e.getCurrentItem();
			Game game = UHC.getInstance().getGameManager().getGame();
			
			if (e.getClickedInventory().equals(player.getInventory()) && !game.isStart() && player.getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
				return;
			}
			
			for (Gui gui : inventories) {

				if (gui.isSameInventory(inv)) {
					
					gui.onClick(new GuiClickEvent(e, inv, player, clicked, game));
					return;
				}
			}
		}

	}
}
