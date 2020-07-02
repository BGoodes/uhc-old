package fr.aiidor.uhc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;

public class CommandTG extends UHCCommand {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHC uhc = UHC.getInstance();
		
		if (!uhc.getGameManager().hasGame()) {
			sender.sendMessage(Lang.ST_ERROR_NO_GAME.get());
			return true;
		}
		
		Game game = uhc.getGameManager().getGame();
		
		if (game.getUHCMode().getUHCType() != UHCType.TAUPE_GUN) {
			sender.sendMessage(Lang.CMD_ERROR_GAMEMODE.get().replace(LangTag.VALUE.toString(), UHCType.TAUPE_GUN.getName()));
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ERROR_COMMAND_PLAYER.get());
			return true;
		}
		
		//TaupeGun tg = (TaupeGun) game.getUHCMode();
		Player player = (Player) sender;
		
		if (args.length == 0) {
			
			return true;
		}
		
		player.sendMessage(Lang.CMD_ERROR_INVALID_COMMAND.get());
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
			 
		List<String> completion = new ArrayList<String>();
			
		if (args.length == 1) {
				
			for (String string : Arrays.asList("chat", "reveal", "claim")) {
						
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
