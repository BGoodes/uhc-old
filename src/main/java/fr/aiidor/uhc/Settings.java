package fr.aiidor.uhc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.tools.Cage;

public class Settings {
	
	public Boolean remove_op;
	public Boolean op_to_staff;
	public Boolean log_game_bc; 
	
	public Location lobby;
	public Cage cage;
	
	public Settings() {
		FileConfiguration config = UHCFile.CONFIG.getYamlConfig();
		
		remove_op = config.getBoolean("Permissions.remove-op");
		op_to_staff = config.getBoolean("Permissions.set-op-to-staff");
		log_game_bc = config.getBoolean("ServerManager.log-game-broadcast");
		
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
		
		
	}
	
	public World getLobbyWorld() {
		if (lobby == null) return null;
		return lobby.getWorld();
	}
	
    public Boolean hasCage() {
    	return cage != null;
    }
}
