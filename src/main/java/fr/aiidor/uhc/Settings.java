package fr.aiidor.uhc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;

import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.tools.Cage;

public class Settings {
	
	public Boolean auto_host;
	
	public Boolean remove_op;
	public Boolean op_to_host;
	public Boolean log_game_bc; 
	public Boolean server_restart;
	
	public String default_world;
	public WorldType default_world_type;
	public Boolean default_generate_structure;
	public Long default_seed;
	
	public Location lobby;
	public Boolean lobby_player_damage;
	
	public Cage cage;
	
	public Settings() {
		FileConfiguration config = UHCFile.CONFIG.getYamlConfig();
		
		auto_host = config.getBoolean("Game.auto-host");
		
		remove_op = config.getBoolean("Permissions.remove-op");
		op_to_host = config.getBoolean("Permissions.set-op-to-host");
		log_game_bc = config.getBoolean("ServerManager.log-game-broadcast");
		server_restart = config.getBoolean("ServerManager.server-restart");
		
		default_world = config.getString("World.default-world");
		
		default_seed = config.getLong("World.seed");
		default_world_type = WorldType.valueOf(config.getString("World.type").toUpperCase());
		default_generate_structure = config.getBoolean("World.generate-structures");
		
		//GET LOBBY
		String worldname = config.getString("World.world-name");
		String lobby_name = config.getString("Lobby.location.world-name");

		if (Bukkit.getWorld(lobby_name) != null) {
				
			Integer x = config.getInt("Lobby.location.x");
			Integer y = config.getInt("Lobby.location.y");
			Integer z = config.getInt("Lobby.location.z");
				
			lobby = new Location(Bukkit.getWorld(lobby_name), x, y, z);
			
		} else {
			lobby = new Location(Bukkit.getWorld(worldname), 0, 150, 0);
		}
		
		lobby_player_damage = config.getBoolean("Lobby.player-damage");
	}
	
	public World getLobbyWorld() {
		if (lobby == null) return null;
		return lobby.getWorld();
	}
	
    public Boolean hasCage() {
    	return cage != null;
    }
}
