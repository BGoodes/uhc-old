package fr.aiidor.uhc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.UHCItem;

public class CommandHost implements CommandExecutor, TabCompleter {

	//COMMANDES :
	
	//HOST (BAN / KICK / MUTE / ADVERT)
	//HOST BROADCAST / ALERT / TITLE
	//HOST CONFIG
	
	//PERMS : uhc.host, whitelist.bypass
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHC uhc = UHC.getInstance();
		
		if (!uhc.getGameManager().hasGame()) {
			sender.sendMessage(Lang.ST_ERROR_NO_GAME.get());
			return true;
		}
		
		Game game = uhc.getGameManager().getGame();
		
		if (args.length == 0) {
			if (!game.hasHost()) {
				
				//BECOME HOST
				if (sender instanceof Player) {
					
					Player player = (Player) sender;
					String perm = "uhc.host";
					
					if (!player.hasPermission(perm)) {
						player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), perm));
						return true;
					}
					
					if (game.isHere(player.getUniqueId())) {
						game.getUHCPlayer(player.getUniqueId()).setRank(Rank.HOST);
						
						if (!game.isStart()) player.getInventory().setItem(4, UHCItem.getConfigChest());
						player.sendMessage(Lang.ST_BECOME_HOST.get());
						player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.7f, 1);
						return true; 
					}
					
					if (!game.getSettings().playerHasPermission(player, Permission.CONFIG)) {
						player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CONFIG.toString()));
						return true;
					}
				}
			}
			
			//TOUTES LES COMMANDES
			
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ERROR_COMMAND_PLAYER.get());
			return true;
		}
		
		Player player = (Player) sender;
		
		if (!game.getSettings().playerHasPermission(player, Permission.CONFIG)) {
			player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CONFIG.toString()));
			return true;
		}
		
		if (args[0].equalsIgnoreCase("test")) {
			player.sendMessage(game.getRandomPlayer().getName());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("tp")) {
			player.teleport(new Location(Bukkit.getWorld("salut"), 0, 150, 0));
			return true;
		}
		
		if (args[0].equalsIgnoreCase("config")) {
			
			if (args.length != 1) {
				player.sendMessage(Lang.CMD_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), 
						"/host config"));
				return true;
			}
			
			player.openInventory(GuiManager.INV_CONFIG.getInventory());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("invsee")) {
			
			String command = "/host invsee <player>";
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Lang.CMD_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return true;
			}
			
			String targetName = args[1];
			if (!isConnected(targetName, game)) {
				player.sendMessage(Lang.CMD_PLAYER_OFFLINE.get().replace(LangTag.PLAYER_NAME.toString(), targetName));
				return true;
			}
			
			player.openInventory(getPlayer(targetName, game).getInventory());
			return true;
		}
		
		player.sendMessage(Lang.CMD_INVALID_COMMAND.get());
		return false;
	}
	
	
	public Boolean isConnected(String name, Game game) {
		return getPlayer(name, game) != null;
	}
	
	public Player getPlayer(String name, Game game) {
		for (UHCPlayer p : game.getAllPlayers()) {
			if (p.isConnected() && p.getPlayer().getName().equals(name)) {
				return p.getPlayer();
			}
		}
		
		return null;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
			 
		List<String> completion = new ArrayList<String>();
			
		if (args.length == 1) {
				
			for (String string : Arrays.asList("config", "invsee")) {
						
				if (args[0].equals("") || string.toLowerCase().startsWith(args[0].toLowerCase())) {
					completion.add(string);
				}
			}
				
			Collections.sort(completion);
		}
			
			
		if (!completion.isEmpty()) return completion;
		
		return null;
	}
}
