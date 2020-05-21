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

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.events.EventsManager;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.scoreboard.ScoreboardManager;
import fr.aiidor.uhc.tools.Cage;
import fr.aiidor.uhc.world.WorldManager;

public class UHC extends JavaPlugin {
	
	private static UHC instance;
	private GameManager gameManager;
	private ScenariosManager scenariosManager;
	private GuiManager guiManager;
	
	private Settings settings;
	private GameSettings game_settings;
	
	//SCOREBOARD
    private ScoreboardManager scoreboardManager;
    private ScheduledExecutorService executorMonoThread;
    private ScheduledExecutorService scheduledExecutorService;
    
    private Cage cage;
	
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
				//ERROR WORLD
			} else {
				throw new NullPointerException("The world \"" + worldname + "\" cannot be found");
			}
		}
		
		settings = new Settings();

		//GAME CREATION
		if (UHCFile.CONFIG.getYamlConfig().getBoolean("ServerManager.create-game")) {	
			Game game = gameManager.createGame("§5§lUHC", null, Bukkit.getWorld(worldname));
			
			game_settings = new GameSettings(game);
		}
		
		if (UHCFile.CONFIG.getYamlConfig().getBoolean("Lobby.Cage.generate")) {	
			
			ConfigurationSection c = UHCFile.CONFIG.getYamlConfig().getConfigurationSection("Lobby.Cage");
			cage = new Cage(getSettings().getLobby().clone().subtract(0, 2, 0), c.getInt("size"), c.getInt("height"), Material.BARRIER, Material.BARRIER);
			cage.create();
			
			getLogger().info(Lang.CAGE_GENERATE.get());
		}
		
		//EVENTS
		new EventsManager().register();
		
		//COMMANDS
		CommandHost cmd_host = new CommandHost();
		PluginCommand pc_host = getCommand("host");
		
		pc_host.setExecutor(cmd_host);
		pc_host.setTabCompleter(cmd_host);

		if (pc_host.getPermission() != null)
			pc_host.setPermissionMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), pc_host.getPermission()));
		
		//SCOREBOARD
        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager();
	}
	
	@Override
	public void onDisable() {
		
		if (hasCage()) cage.destroy();
		scoreboardManager.onDisable();
		
		if (gameManager.hasGame()) {
			Game game = gameManager.getGame();
			
			if (UHCFile.CONFIG.getYamlConfig().getBoolean("ServerManager.remove-worlds")) {
				for (World world : game.getWorlds()) {
					new WorldManager(world).deleteWorld();
				}
			}
		}
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
	
	public GameSettings getGameSettings() {
		return game_settings;
	}
	
	public void setGameSettings(GameSettings game_settings) {
		this.game_settings = game_settings;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }
 
    public ScheduledExecutorService getExecutorMonoThread() {
        return executorMonoThread;
    }
 
    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }
    
    public Boolean hasCage() {
    	return cage != null;
    }
    public Cage getCage() {
    	return cage;
    }
}
