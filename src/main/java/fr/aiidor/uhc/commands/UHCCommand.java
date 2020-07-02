package fr.aiidor.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

public abstract class UHCCommand implements CommandExecutor, TabCompleter {
	
	public Boolean isConnected(String name) {
		return Bukkit.getPlayerExact(name) != null;
	}
}
