package fr.aiidor.uhc.inventories;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.scenarios.Scenario;

public class GuiManager {
	
	private Set<Gui> inventories;
	
	public GuiManager() {
		inventories = new HashSet<Gui>();
		
		INV_CONFIG = createInventory(new Inv_Config());
		INV_CONFIG_SCENARIOS = createInventory(new Inv_Scenarios());
		INV_CONFIG_SCENARIOS_SETTINGS = createInventory(new Inv_Scenarios_Settings());
		INV_CONFIG_STUFF = createInventory(new Inv_Stuff());
		INV_CONFIG_ENCHANTS = createInventory(new Inv_Enchants());
		INV_CONFIG_WORLDS = createInventory(new Inv_Worlds());
		INV_WORLD_CREATION = createInventory(new Inv_World_Creation());
		INV_WORLD_SETTINGS = createInventory(new Inv_World_Settings());
		
		INV_CONFIG_START_STUFF = createInventory(new Inv_Start_Stuff());
		INV_CONFIG_DEATH_STUFF = createInventory(new Inv_Death_Stuff());
		INV_CONFIG_PLAYERS = createInventory(new Inv_Players());
		
		INV_TEAMS_CHOOSE = createInventory(new Inv_Teams_Choose());
		INV_CONFIG_TEAMS = createInventory(new Inv_Teams());
		
		
		INV_TEAM_NUMBER = createInventory(new Inv_Team_Number());
		INV_TEAM_SIZE = createInventory(new Inv_Team_Size());
		
		INV_SCENARIO_LIST = createInventory(new Inv_Scenarios_List());
		
		INV_CONFIG_LOOTS = createInventory(new Inv_Loots());
		INV_CONFIG_TREES = createInventory(new Inv_Trees());
		
		INV_CONFIG_WB = createInventory(new Inv_Worldborder());
		INV_CONFIG_WB_SIZE = createInventory(new Inv_Wb_Size());
		INV_CONFIG_WB_FINAL_SIZE = createInventory(new Inv_Wb_Final_Size());
		
		INV_CONFIG_TIME = createInventory(new Inv_Time());
		INV_CONFIG_INVINCIBILITY_TIME = createInventory(new Inv_Invincibility_Time());
		INV_CONFIG_PVP_TIME = createInventory(new Inv_Pvp_Time());
		INV_CONFIG_WB_TIME = createInventory(new Inv_Wb_Time());
		INV_CONFIG_EP1_TIME = createInventory(new Inv_Ep1_Time());
		INV_CONFIG_EP_TIME = createInventory(new Inv_Ep_Time());
		
		INV_CONFIG_LIFE = createInventory(new Inv_Life());
		INV_CONFIG_LIFE_APPLE = createInventory(new Inv_Life_Apples());
		
		INV_CONFIG_GAPPLES = createInventory(new Inv_Life_Gapples());
		INV_CONFIG_NAPPLES = createInventory(new Inv_Life_Napples());
		INV_CONFIG_HAPPLES = createInventory(new Inv_Life_Happles());
		
		INV_CONFIG_DISPLAY_LIFE = createInventory(new Inv_Display_Life());
		
		
		//DW
		INV_CONFIG_DW = createInventory(new Inv_Dw());
		INV_CONFIG_DW_COMPO = createInventory(new Inv_Dw_Compo());
		
		for (Scenario scenario : UHC.getInstance().getScenarioManager().getScenarios()) {
			if (scenario.hasSettings()) {
				createInventory(scenario.getSettings());
			}
		}
	}
	
	public static Gui INV_CONFIG;
	public static Gui INV_CONFIG_SCENARIOS;
	public static Gui INV_CONFIG_SCENARIOS_SETTINGS;
	public static Gui INV_CONFIG_STUFF;
	public static Gui INV_CONFIG_ENCHANTS;
	public static Gui INV_CONFIG_WORLDS;
	public static Gui INV_WORLD_CREATION;
	public static Gui INV_WORLD_SETTINGS;
	public static Gui INV_CONFIG_START_STUFF;
	public static Gui INV_CONFIG_DEATH_STUFF;
	public static Gui INV_CONFIG_PLAYERS;
	public static Gui INV_TEAMS_CHOOSE;
	public static Gui INV_CONFIG_TEAMS;
	public static Gui INV_SCENARIO_LIST;
	
	public static Gui INV_CONFIG_LOOTS;
	public static Gui INV_CONFIG_TREES;
	public static Gui INV_CONFIG_WB;
	public static Gui INV_CONFIG_TIME;
	
	//CONFIG VALUES
	public static Gui INV_TEAM_NUMBER;
	public static Gui INV_TEAM_SIZE;

	public static Gui INV_CONFIG_WB_SIZE;
	public static Gui INV_CONFIG_WB_FINAL_SIZE;
	
	public static Gui INV_CONFIG_PVP_TIME;
	public static Gui INV_CONFIG_INVINCIBILITY_TIME;
	public static Gui INV_CONFIG_WB_TIME;
	public static Gui INV_CONFIG_EP1_TIME;
	public static Gui INV_CONFIG_EP_TIME;
	public static Gui INV_CONFIG_LIFE;
	public static Gui INV_CONFIG_LIFE_APPLE;
	public static Gui INV_CONFIG_GAPPLES;
	public static Gui INV_CONFIG_NAPPLES;
	public static Gui INV_CONFIG_HAPPLES;

	public static Gui INV_CONFIG_DISPLAY_LIFE;
	
	//DW
	public static Gui INV_CONFIG_DW;
	public static Gui INV_CONFIG_DW_COMPO;
	
	
	public Gui createInventory(Gui inv) {
		inventories.add(inv);
		return inv;
	}
	
	public Boolean onPlayerClickEvent(Inventory inv, Player player, ItemStack clicked, Game game, InventoryClickEvent e) {
		
		for (Gui gui : inventories) {

			if (gui.isSameInventory(inv)) {
					
				gui.onClick(new GuiClickEvent(e, inv, player, clicked, game));
				return true;
			}
		}
		
		
		return false;
	}
}
