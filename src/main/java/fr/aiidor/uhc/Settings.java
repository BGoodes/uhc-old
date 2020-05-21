package fr.aiidor.uhc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import fr.aiidor.uhc.enums.UHCFile;

public class Settings {
	
	private Boolean remove_op;
	private Boolean op_to_staff;
	private Location lobby;
	
	public Settings() {
		FileConfiguration config = UHCFile.CONFIG.getYamlConfig();
		
		remove_op = config.getBoolean("Permissions.remove-op");
		op_to_staff = config.getBoolean("Permissions.set-op-to-staff");
		
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

	public Boolean OpIsStaff() {
		return op_to_staff;
	}

	public Boolean getRemoveOp() {
		return remove_op;
	}
	
	public Location getLobby() {
		return lobby;
	}
	
	public World getLobbyWorld() {
		if (lobby == null) return null;
		return lobby.getWorld();
	}
}
