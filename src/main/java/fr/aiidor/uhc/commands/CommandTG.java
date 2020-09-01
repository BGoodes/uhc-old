package fr.aiidor.uhc.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.tg.TGMole;
import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.gamemodes.TaupeGun;

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
		
		TaupeGun tg = (TaupeGun) game.getUHCMode();
		Player player = (Player) sender;
		
		if (args.length == 0) {
			
			return true;
		}
		
		if (args[0].equalsIgnoreCase("reveal")) {
			if (args.length != 1) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), "/" + label.toLowerCase() + " reveal"));
				return true;
			}
			
			if (!tg.isMole(player.getUniqueId())) {
				player.sendMessage(Lang.TG_CMD_ERROR_MOLE.get());
				return true;
			}
			
			TGMole m = tg.getMole(player.getUniqueId());
			UHCPlayer p = m.getUHCPlayer();
			
			if (m.mole_team.equals(p.getTeam())) {
				game.broadcast(Lang.TG_SUPER_MOLE_REVEAL.get().replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName()));
				
				if (p.hasTeam()) p.leaveTeam(false);
				tg.removeMole(m);

				p.giveItem(new ItemStack(Material.GOLDEN_APPLE, 1));
				return true;
			}
			
			if (p.hasTeam()) p.leaveTeam(false);
			m.mole_team.join(p, false);
				
			p.giveItem(new ItemStack(Material.GOLDEN_APPLE, 1));
			game.broadcast(Lang.TG_MOLE_REVEAL.get().replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName()));
				
			if (!m.isSuperMole()) tg.removeMole(m);
			
			return true;
		}
		
		player.sendMessage(Lang.CMD_ERROR_INVALID_COMMAND.get());
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label,  String[] args) {
			 
		List<String> completion = new ArrayList<String>();
			
		if (args.length == 1) {
				
			for (String string : Arrays.asList("chat", "claim", "reveal")) {
						
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
