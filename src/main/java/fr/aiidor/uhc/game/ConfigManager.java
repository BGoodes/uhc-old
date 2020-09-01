package fr.aiidor.uhc.game;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;

public class ConfigManager {
	
	//private Game game;
	private File file;
	
	public ConfigManager(Game game, File file) {
		//this.game = game;
		this.file = file;
	}
	
	public static void create(String name) {
		create(name, null);
	}
	
	public static Boolean exist(String name) {
		return exist(name, null);
	}
	
	public static Boolean exist(String name, String folder_name) {
		File folder = new File(UHC.getInstance().getDataFolder().getPath() + File.separator + "configs");
		if (folder_name != null) folder = new File(UHC.getInstance().getDataFolder().getPath() + File.separator + "configs" + File.separator + folder_name);
		
		if (!folder.exists()) return false;
		File file = new File(folder, name + ".yml");
		
		if (file.exists()) return false;
		
		return true;
	}
	
	public static void create(String name, String folder_name) {
		
		Logger logger = UHC.getInstance().getLogger();
		
		File folder = new File(UHC.getInstance().getDataFolder().getPath() + File.separator + "configs");
		if (folder_name != null) folder = new File(UHC.getInstance().getDataFolder().getPath() + File.separator + "configs" + File.separator + folder_name);
		
		if (!folder.exists()) folder.mkdirs();
		
		File file = new File(folder, name + ".yml");
		
		if (file.exists()) return;
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		logger.info(Lang.FILE_CREATED.get().replace(LangTag.VALUE.toString(), name + ".yml"));
	}
	
	public void save() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		
		
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void load() {
		//FileConfiguration config = YamlConfiguration.loadConfiguration(file);
	}
	
	
}
