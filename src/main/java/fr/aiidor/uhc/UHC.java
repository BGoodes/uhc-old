package fr.aiidor.uhc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aiidor.uhc.commands.CommandDW;
import fr.aiidor.uhc.commands.CommandHost;
import fr.aiidor.uhc.commands.CommandMessage;
import fr.aiidor.uhc.commands.CommandScenario;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.EventsManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.scoreboard.ScoreboardManager;
import fr.aiidor.uhc.scoreboard.TablistManager;
import fr.aiidor.uhc.tools.Cage;
import fr.aiidor.uhc.world.WorldManager;

public class UHC extends JavaPlugin {
	
	private static UHC instance;
	private GameManager gameManager;
	private ScenariosManager scenariosManager;
	private GuiManager guiManager;
	
	private Settings settings;
	
	//SCOREBOARD
    private ScoreboardManager scoreboardManager;
    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;
	
    private TablistManager tablistManager;
    
    //PERMISSIONS
    //whitelist.bypass
    //uhc.host
    
	@Override
	public void onEnable() {
		instance = this;
		
		//FILES
		for (UHCFile file : UHCFile.values()) {
			file.create(getLogger());
		}
		
		//MANAGERS
		gameManager = new GameManager();
		scenariosManager = new ScenariosManager();
		guiManager = new GuiManager();
		
		//WORLDS
		FileConfiguration config = UHCFile.CONFIG.getYamlConfig();
		String worldname = config.getString("World.world-name");
		
		if (Bukkit.getWorld(worldname) == null) {
			//WORLD CREATION
			if (config.getBoolean("ServerManager.generate-worlds")) {
				ConfigurationSection section = config.getConfigurationSection("World");
				
				Long seed = section.getLong("seed");
				Environment env = Environment.valueOf(section.getString("dimension").toUpperCase());
				WorldType type = WorldType.valueOf(section.getString("type").toUpperCase());
				Boolean generateStructures = section.getBoolean("generate-structures");
				
				new WorldManager(worldname).create(seed, env, type, generateStructures);
				
				if (env == Environment.NORMAL) {
					if (Bukkit.getWorld(worldname + "_nether") == null) new WorldManager(worldname + "_nether").create(seed, Environment.NETHER, type, generateStructures);
					if (Bukkit.getWorld(worldname + "_the_end") == null) new WorldManager(worldname + "_the_end").create(seed, Environment.THE_END, type, generateStructures);
				}
				
				//ERROR WORLD
			} else {
				throw new NullPointerException("The world \"" + worldname + "\" cannot be found");
			}
		}
		
		settings = new Settings();
		
		//GAME CREATION
		if (UHCFile.CONFIG.getYamlConfig().getBoolean("Game.create-default-game")) {	
			
			UHCType gm = UHCType.valueOf(UHCFile.CONFIG.getYamlConfig().getString("Game.default-game-mode"));
			
			gameManager.createGame(gm.getTabName(), gm.getNew(), null, null, null, Bukkit.getWorld(worldname));
		}
		
		if (UHCFile.CONFIG.getYamlConfig().getBoolean("Lobby.cage.generate")) {	
			
			ConfigurationSection c = UHCFile.CONFIG.getYamlConfig().getConfigurationSection("Lobby.cage");
			settings.cage = new Cage(getSettings().lobby.clone().subtract(0, 2, 0), c.getInt("size"), c.getInt("height"), Material.BARRIER, Material.STAINED_GLASS_PANE);
			settings.cage.create();
			
			getLogger().info(Lang.CAGE_GENERATE.get());
		}
		
		//EVENTS
		new EventsManager().register();
		
		//COMMANDS
		CommandHost cmd_host = new CommandHost();
		PluginCommand pc_host = getCommand("host");
		
		pc_host.setExecutor(cmd_host);
		pc_host.setTabCompleter(cmd_host);
		pc_host.setPermissionMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), pc_host.getPermission()));
		
		
		CommandDW cmd_dw = new CommandDW();
		PluginCommand pc_dw = getCommand("dw");
		
		pc_dw.setExecutor(cmd_dw);
		pc_dw.setTabCompleter(cmd_dw);
		
		getCommand("msg").setExecutor(new CommandMessage());
		getCommand("scenarios").setExecutor(new CommandScenario());
		
		//SCOREBOARD
        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager();

        tablistManager = new TablistManager();
        
        Bukkit.getConsoleSender().sendMessage("§e========================================");
        Bukkit.getConsoleSender().sendMessage("   §fPlugin UHC HOST - Version 1.1");
        Bukkit.getConsoleSender().sendMessage("         §fAuthor : B. Goodes");
        Bukkit.getConsoleSender().sendMessage("§e========================================");
	}
	
	@Override
	public void onDisable() {
		
		if (settings.hasCage()) settings.cage.destroy();
		
		if (gameManager.hasGame()) {
			Game game = gameManager.getGame();
			
			if (UHCFile.CONFIG.getYamlConfig().getBoolean("ServerManager.delete-worlds")) {
				for (World w : game.getWorlds()) {
					new WorldManager(w.getName()).deleteWorld();
				}
			}
		}
		
		scoreboardManager.onDisable();
	}
	
	public static UHC getInstance() {
		return instance;
	}
	
	public GameManager getGameManager() {
		return gameManager;
	}
	
	public ScenariosManager getScenarioManager() {
		return scenariosManager;
	}
	
	public GuiManager getGuiManager() {
		return guiManager;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
 
    public TablistManager getTablistManager() {
        return tablistManager;
    }
    
    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }
 
    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
}
