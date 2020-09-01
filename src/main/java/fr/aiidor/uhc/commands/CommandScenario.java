package fr.aiidor.uhc.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.GuiManager;

public class CommandScenario extends UHCCommand {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		UHC uhc = UHC.getInstance();
		
		if (!uhc.getGameManager().hasGame()) {
			sender.sendMessage(Lang.ST_ERROR_NO_GAME.get());
			return true;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage(Lang.ERROR_COMMAND_PLAYER.get());
			return true;
		}
		
		Player player = (Player) sender;
		
		if (!uhc.getGameManager().getGame().getSettings().scenarios_list) {
			player.sendMessage(Lang.MSG_ERROR_SCENARIO_LIST_OFF.get());
			return true;
		}
		
		if (args.length != 0) {
			player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/scenarios"));
			return true;
		}
		
		 player.openInventory(GuiManager.INV_SCENARIO_LIST.getInventory());
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
		return null;
	}
}
