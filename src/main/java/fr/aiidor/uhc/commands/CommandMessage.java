package fr.aiidor.uhc.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;

public class CommandMessage extends UHCCommand {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHC uhc = UHC.getInstance();
		
		if (!uhc.getGameManager().hasGame()) {
			sender.sendMessage(Lang.ST_ERROR_NO_GAME.get());
			return true;
		}
		
		//Game game = uhc.getGameManager().getGame();
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ERROR_COMMAND_PLAYER.get());
			return true;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), "/" + label.toLowerCase() + " <pseudo> <message>"));
			return true;
		}
		
		if (args.length < 2) {
			player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/" + label.toLowerCase() + " <pseudo> <message>"));
			return true;
		}
		
		String targetname = args[0];
		if (!isConnected(targetname)) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFLINE.get());
			return true;
		}
		
		Player target = Bukkit.getPlayer(targetname);
		
		if (player.getName().equals(targetname)) {
			player.sendMessage(Lang.CMD_ERROR_SELF_MSG.get());
			return true;
		}
		
		StringBuilder msg = new StringBuilder();
		
		for (int i = 1; i != args.length; i++) {
			if (i + 1 < args.length) msg.append(args[i] + " ");
			else msg.append(args[i]);
		}
		
		player.sendMessage(Lang.MSG_PRIVATE_SEND.get()
				.replace(LangTag.PLAYER_NAME.toString(), target.getName())
				.replace(LangTag.VALUE.toString(), msg)
			);
		
		target.sendMessage(Lang.MSG_PRIVATE_RECEIVE.get()
				.replace(LangTag.PLAYER_NAME.toString(), player.getName())
				.replace(LangTag.VALUE.toString(), msg)
			);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		return null;
	}
}
