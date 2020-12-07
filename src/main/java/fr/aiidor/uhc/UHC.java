package fr.aiidor.uhc;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import fr.aiidor.uhc.commands.CommandHost;
import fr.aiidor.uhc.commands.CommandMessage;
import fr.aiidor.uhc.commands.CommandScenario;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.EventsManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.scoreboard.ScoreboardManager;
import fr.aiidor.uhc.scoreboard.TablistManager;
import fr.aiidor.uhc.utils.Cage;
import fr.aiidor.uhc.utils.UHCItem;
import fr.aiidor.uhc.world.UHCWorld;
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
    /*
     * uhc.host
     * uhc.staff
     * 
     * uhc.command.restart
     * 
     * uhc.bypass.slot
     * uhc.bypass.whitelist
     */
    
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
				WorldType type = WorldType.valueOf(section.getString("type").toUpperCase());
				Boolean generateStructures = section.getBoolean("generate-structures");
				
				new WorldManager(worldname).create(seed, Environment.NORMAL, type, generateStructures);
				
				if (Bukkit.getWorld(worldname + "_nether") == null) new WorldManager(worldname + "_nether").create(seed, Environment.NETHER, type, generateStructures);
				if (Bukkit.getWorld(worldname + "_the_end") == null) new WorldManager(worldname + "_the_end").create(seed, Environment.THE_END, type, generateStructures);
				
				//ERROR WORLD
			} else {
				throw new NullPointerException("The world \"" + worldname + "\" cannot be found");
			}
		}
		
		settings = new Settings();
		
		//GAME CREATION
		if (UHCFile.CONFIG.getYamlConfig().getBoolean("Game.create-default-game")) {	
			
			UHCType gm = UHCType.CLASSIC;
			
			Environment env = Environment.valueOf(config.getString("World.dimension").toUpperCase());
			String mainworld = worldname;
			
			if (env == Environment.NETHER) mainworld = worldname + "_nether";
			if (env == Environment.THE_END) mainworld = worldname + "_the_end";
				
			gameManager.createGame(gm.getTabName(), gm.getNew(), null, null, null, new UHCWorld(worldname, mainworld), null);
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
		
		pc_host = getCommand("h");
		
		pc_host.setExecutor(cmd_host);
		pc_host.setTabCompleter(cmd_host);
		
		getCommand("msg").setExecutor(new CommandMessage());
		getCommand("scenarios").setExecutor(new CommandScenario());
		
		//SCOREBOARD
        scheduledExecutorService = Executors.newScheduledThreadPool(16);
        executorMonoThread = Executors.newScheduledThreadPool(1);
        scoreboardManager = new ScoreboardManager();

        tablistManager = new TablistManager();
        
        //CRAFTS
		ShapedRecipe recipe = new ShapedRecipe(UHCItem.golden_head);
		recipe.shape(new String[] { "GGG", "GHG", "GGG" });
	    recipe.setIngredient('G', Material.GOLD_INGOT);
	    recipe.setIngredient('H', new ItemStack(Material.SKULL_ITEM, 1, (short) 3).getData());
		 
		getServer().addRecipe(recipe);
		
		//AFFICHAGE FINAL
        Bukkit.getConsoleSender().sendMessage("§e---------------------------------");
        Bukkit.getConsoleSender().sendMessage("§fPlugin UHC HOST - Version 1.4");     
        Bukkit.getConsoleSender().sendMessage("§6Author : B. Goodes");
        Bukkit.getConsoleSender().sendMessage("§e---------------------------------");
	}
	
	@Override
	public void onDisable() {
		
		if (settings.hasCage()) settings.cage.destroy();
		
		if (gameManager.hasGame()) {
			Game game = gameManager.getGame();
			
			if (UHCFile.CONFIG.getYamlConfig().getBoolean("ServerManager.delete-worlds")) {
				for (UHCWorld w : game.getUHCWorlds()) {
					w.delete();
				}
			}
			
			gameManager.reload();
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
