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
		
		INV_CONFIG = createInventory(new GuiConfig());
		INV_CONFIG_SCENARIOS = createInventory(new GuiScenarios());
		INV_CONFIG_SCENARIOS_SETTINGS = createInventory(new GuiScenariosSettings());
		INV_CONFIG_STUFF = createInventory(new GuiStuff());
		INV_CONFIG_ENCHANTS = createInventory(new GuiEnchants());
		INV_CONFIG_WORLDS = createInventory(new GuiWorlds());
		INV_WORLD_CREATION = createInventory(new GuiWorldCreation());
		INV_WORLD_SETTINGS = createInventory(new GuiWorldSettings());
		
		INV_CONFIG_START_STUFF = createInventory(new GuiStartStuff());
		INV_CONFIG_DEATH_STUFF = createInventory(new GuiDeathStuff());
		INV_CONFIG_PLAYERS = createInventory(new GuiPlayers());
		
		INV_TEAMS_CHOOSE = createInventory(new GuiTeamChoose());
		INV_CONFIG_TEAMS = createInventory(new GuiTeams());
		
		
		INV_TEAM_NUMBER = createInventory(new GuiTeamNumber());
		INV_TEAM_SIZE = createInventory(new GuiTeamSize());
		
		INV_SCENARIO_LIST = createInventory(new GuiScenariosList());
		
		INV_CONFIG_LOOTS = createInventory(new GuiLoots());
		INV_CONFIG_TREES = createInventory(new GuiTrees());
		
		INV_CONFIG_WB = createInventory(new GuiWorldborder());
		INV_CONFIG_WB_SIZE = createInventory(new GuiWbSize());
		INV_CONFIG_WB_FINAL_SIZE = createInventory(new GuiWbFinalSize());
		
		INV_CONFIG_TIME = createInventory(new GuiTime());
		INV_CONFIG_INVINCIBILITY_TIME = createInventory(new GuiInvincibityTime());
		INV_CONFIG_PVP_TIME = createInventory(new GuiPvpTime());
		INV_CONFIG_WB_TIME = createInventory(new GuiWbTime());
		INV_CONFIG_EP1_TIME = createInventory(new GuiEpisode1Time());
		INV_CONFIG_EP_TIME = createInventory(new GuiEpisodeTime());
		
		INV_CONFIG_LIFE = createInventory(new GuiHealth());
		INV_CONFIG_LIFE_APPLE = createInventory(new GuiHealthApples());
		
		INV_CONFIG_GAPPLES = createInventory(new GuiHealthGapple());
		INV_CONFIG_NAPPLES = createInventory(new GuiHealthNapples());
		INV_CONFIG_HAPPLES = createInventory(new GuiHealthHapples());
		
		INV_CONFIG_DISPLAY_LIFE = createInventory(new GuiDisplayHealth());
		
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
