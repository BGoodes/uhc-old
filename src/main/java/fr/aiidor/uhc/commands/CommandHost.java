package fr.aiidor.uhc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.UHCItem;

public class CommandHost extends UHCCommand {

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
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ERROR_COMMAND_PLAYER.get());
			return true;
		}
		
		if (args.length == 0) {
			if (!game.hasHost()) {
				
				//BECOME HOST
				Player player = (Player) sender;
				String perm = "uhc.host";
				
				if (!player.hasPermission(perm)) {
					player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), perm));
					return true;
				}
				
				if (game.isHere(player.getUniqueId())) {
					game.getUHCPlayer(player.getUniqueId()).setRank(Rank.HOST);
					
					if (!game.isStart()) player.getInventory().setItem(4, UHCItem.config_chest);
					
					player.sendMessage(Lang.ST_BECOME_HOST.get());
					player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.7f, 1);
					return true; 	

				}
				
			} else {
				//TOUTES LES COMMANDES
				sender.sendMessage("§eLes commandes : "
						+ "\n §6- " + "/" + label.toLowerCase() + " config"
						+ "\n §6- " + "/" + label.toLowerCase() + " broadcast §f<message>"
						+ "\n §6- " + "/" + label.toLowerCase() + " invsee §f<player>"
						+ "\n §6- " + "/" + label.toLowerCase() + " revive §f<player>"
						+ "\n §6- " + "/" + label.toLowerCase() + " restart"

						);
			}
			
			return true;
		}
		
		Player player = (Player) sender;
				
		if (args[0].equalsIgnoreCase("config")) {
			
			if (args.length != 1) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), 
						"/" + label.toLowerCase() + " config"));
				return true;
			}
			
			if (!game.getSettings().playerHasPermission(player, Permission.CONFIG)) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CONFIG.toString()));
				return true;
			}
			
			player.openInventory(GuiManager.INV_CONFIG.getInventory());
			return true;
		}
		
		//RESTART ------------------------------------
		if (args[0].equalsIgnoreCase("restart")) {
			
			if (args.length != 1) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), 
						"/" + label.toLowerCase() + " restart"));
				return true;
			}
			
			if (!player.hasPermission("uhc.command.restart")) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), "uhc.command.restart"));
				return true;
			}
			
			if (game.getState() == GameState.ENDING && UHC.getInstance().getSettings().server_restart) {
				player.sendMessage(Lang.ST_ERROR_SERVER_RESTART.get());
				return true;
			}
			
			UHC.getInstance().getGameManager().restartGame();
			return true;
		}
		
		//BROADCAST -------------------------------------
		if (args[0].equalsIgnoreCase("broadcast")) {
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), "/" + label.toLowerCase() + " broadcast <message>"));
				return true;
			}
			
			if (args.length < 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/" + label.toLowerCase() + " broadcast <message>"));
				return true;
			}
			
			if (!game.getSettings().playerHasPermission(player, Permission.ALERT)) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.ALERT.toString()));
				return true;
			}
			
			StringBuilder msg = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				msg.append(" " + args[i]);
			}
			
			if (!msg.toString().isEmpty()) {
				game.broadcast(Lang.BC_BROADCAST.get().replace(LangTag.VALUE.toString(), msg.toString().replace("&!", "§")));
				game.playSound(Sound.CHICKEN_EGG_POP);
			}
			return true;
		}
		
		if (args[0].equalsIgnoreCase("invsee")) {
			
			String command = "/" + label.toLowerCase() + " invsee <player>";
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return true;
			}
			
			if (!game.getSettings().playerHasPermission(player, Permission.INVSEE)) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.INVSEE.toString()));
				return true;
			}
			
			String targetName = args[1];
			if (!isConnected(targetName)) {
				player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFLINE.get().replace(LangTag.PLAYER_NAME.toString(), targetName));
				return true;
			}
			
			player.openInventory(Bukkit.getPlayer(targetName).getInventory());
			return true;
		}
		
		if (args[0].equalsIgnoreCase("revive")) {
			
			String command = "/" + label.toLowerCase() + " revive <player>";
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return true;
			}
			
			if (!game.getSettings().playerHasPermission(player, Permission.REVIVE)) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.REVIVE.toString()));
				return true;
			}
			
			String targetName = args[1];
			
			if (!isConnected(targetName)) {
				player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFLINE.get().replace(LangTag.PLAYER_NAME.toString(), targetName));
				return true;
			}
			
			Player target = Bukkit.getPlayer(targetName);
			
			if (!game.isHere(target.getUniqueId())) {
				player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFGAME.get().replace(LangTag.PLAYER_NAME.toString(), targetName));
				return true;
			}
			
			UHCPlayer t = game.getUHCPlayer(target.getUniqueId());
			
			if (!t.isDead()) {
				player.sendMessage(Lang.CMD_ERROR_PLAYER_NOT_DEAD.get());
				return true;
			}
			
			t.revive();
			
			game.broadcast(Lang.BC_REVIVE.get().replace(LangTag.PLAYER_NAME.toString(), t.getDisplayName()));
			return true;
		}
		
		player.sendMessage(Lang.CMD_ERROR_INVALID_COMMAND.get());
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
			 
		List<String> completion = new ArrayList<String>();
			
		if (args.length == 1) {
				
			for (String string : Arrays.asList("broadcast", "config", "invsee", "revive", "restart")) {
						
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
