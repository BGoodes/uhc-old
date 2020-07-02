package fr.aiidor.uhc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.dwuhc.CatLady;
import fr.aiidor.dwuhc.DWRoleType;
import fr.aiidor.dwuhc.DWplayer;
import fr.aiidor.dwuhc.Guardian;
import fr.aiidor.dwuhc.Guru;
import fr.aiidor.dwuhc.Prophet;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.gamemodes.DevilWatches;

public class CommandDW extends UHCCommand {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHC uhc = UHC.getInstance();
		
		if (!uhc.getGameManager().hasGame()) {
			sender.sendMessage(Lang.ST_ERROR_NO_GAME.get());
			return true;
		}
		
		Game game = uhc.getGameManager().getGame();
		
		if (game.getUHCMode().getUHCType() != UHCType.DEVIL_WATCHES) {
			sender.sendMessage(Lang.CMD_ERROR_GAMEMODE.get().replace(LangTag.VALUE.toString(), UHCType.DEVIL_WATCHES.getName()));
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ERROR_COMMAND_PLAYER.get());
			return true;
		}
		
		DevilWatches dw = (DevilWatches) game.getUHCMode();
		Player player = (Player) sender;
		
		if (args.length == 0) {
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("role")) {
			
			if (args.length != 1) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/dw role"));
				return true;
			}
			
			if (!dw.hasRole(player.getUniqueId())) {
				player.sendMessage(Lang.CMD_ERROR_NO_ROLE.get());
				return true;
			}
			
			player.sendMessage(dw.getDWPlayer(player.getUniqueId()).getRole().getCompleteLore());
			return true;
		}
			
		if (args[0].equalsIgnoreCase("protect")) {
			
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), "/dw protect <player>"));
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/dw protect <player>"));
				return true;
			}
			
			if (!dw.hasRole(player.getUniqueId())) {
				player.sendMessage(Lang.CMD_ERROR_NO_ROLE.get());
				return true;
			}
			
			if (dw.getDWPlayer(player.getUniqueId()).getRole().getRoleType() != DWRoleType.GUARDIAN) {
				player.sendMessage(Lang.CMD_ERROR_ROLE.get().replace(LangTag.ROLE_NAME.toString(), DWRoleType.GUARDIAN.getName()));
				return true;
			}
			
			DWplayer p = dw.getDWPlayer(player.getUniqueId());
			((Guardian) p.getRole()).protect(args[1]);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("see")) {
			
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), "/dw see <player>"));
				return true;
			}
			
			if (args.length != 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/dw see <player>"));
				return true;
			}
			
			if (!dw.hasRole(player.getUniqueId())) {
				player.sendMessage(Lang.CMD_ERROR_NO_ROLE.get());
				return true;
			}
			
			if (dw.getDWPlayer(player.getUniqueId()).getRole().getRoleType() != DWRoleType.PROPHET) {
				player.sendMessage(Lang.CMD_ERROR_ROLE.get().replace(LangTag.ROLE_NAME.toString(), DWRoleType.PROPHET.getName()));
				return true;
			}
			
			DWplayer p = dw.getDWPlayer(player.getUniqueId());
			((Prophet) p.getRole()).see(args[1]);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("announce")) {
			
			if (args.length == 1) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), "/dw announce <message>"));
				return true;
			}
			
			if (args.length < 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/dw announce <message>"));
				return true;
			}
			
			if (!dw.hasRole(player.getUniqueId())) {
				player.sendMessage(Lang.CMD_ERROR_NO_ROLE.get());
				return true;
			}
			
			if (dw.getDWPlayer(player.getUniqueId()).getRole().getRoleType() != DWRoleType.GURU) {
				player.sendMessage(Lang.CMD_ERROR_ROLE.get().replace(LangTag.ROLE_NAME.toString(), DWRoleType.GURU.getName()));
				return true;
			}
			
			DWplayer p = dw.getDWPlayer(player.getUniqueId());
			
			StringBuilder msg = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				msg.append(" " + args[i]);
			}
			
			if (!msg.toString().isEmpty()) ((Guru) p.getRole()).broadcast(msg.toString().substring(1));
			return true;
		}
		
		if (args[0].equalsIgnoreCase("cat")) {
			
			if (args.length != 1) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/dw cat"));
				return true;
			}
			
			if (!dw.hasRole(player.getUniqueId())) {
				player.sendMessage(Lang.CMD_ERROR_NO_ROLE.get());
				return true;
			}
			
			if (dw.getDWPlayer(player.getUniqueId()).getRole().getRoleType() != DWRoleType.CAT_LADY) {
				player.sendMessage(Lang.CMD_ERROR_ROLE.get().replace(LangTag.ROLE_NAME.toString(), DWRoleType.CAT_LADY.getName()));
				return true;
			}
			
			((CatLady) dw.getDWPlayer(player.getUniqueId()).getRole()).summonCat();
			return true;
		}
		
		player.sendMessage(Lang.CMD_ERROR_INVALID_COMMAND.get());
		return false;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
			 
		List<String> completion = new ArrayList<String>();
			
		if (args.length == 1) {
				
			for (String string : Arrays.asList("role", "compo", "protect", "see", "announce")) {
						
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
