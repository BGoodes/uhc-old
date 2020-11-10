package fr.aiidor.uhc.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.commands.CommandMessage;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.tools.ItemBuilder;
import fr.aiidor.uhc.tools.UHCItem;

public class CommandEvent implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPreprocess(PlayerCommandPreprocessEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		GameSettings s = game.getSettings();
		Player player = e.getPlayer();

		String cmd = e.getMessage().substring(1);
		String label = cmd.split(" ")[0];

		String[] args = new String[] {};
		if (cmd.split(" ").length > 1)
			args = cmd.substring(label.length() + 1).split(" ");

		if (label.equalsIgnoreCase("tell")) {
			e.setCancelled(true);

			new CommandMessage().onCommand(player, null, label, args);
			return;
		}

		if (!game.isHere(player.getUniqueId())) return;

		// CLEAR ----------------------------------------------------------
		if (label.equalsIgnoreCase("clear")) {
			e.setCancelled(true);

			String command = "/clear [player] [itemID] [metadata]";

			if (!s.playerHasPermission(player, Permission.CLEAR) && !player.hasPermission(Permission.CLEAR.getSpigotPerm())) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CLEAR.toString()));
				return;
			}

			if (args.length == 0) {

				player.getInventory().clear();
				player.getInventory().setArmorContents(new ItemStack[] { null, null, null, null });
				player.sendMessage(Lang.CMD_EXECUTE.get());
				return;
			}

			if (args.length > 3) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			Material mat = null;
			Byte data = null;

			// SELECTOR
			if (args[0].equals("@e")) {
				player.sendMessage(Lang.CMD_ERROR_SELECTOR.get().replace(LangTag.VALUE.toString(), "@e"));
				return;
			}

			List<Entity> targets = getTarget(player, game, args[0]);
			if (targets == null) return;

			// ARGS
			if (args.length > 1) {

				mat = UHCItem.getFromID(args[1]);

				if (mat == null) {
					player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
					return;
				}

				if (args.length == 3) {
					try {

						data = Byte.parseByte(args[2]);

					} catch (NumberFormatException ex) {
						
						player.sendMessage( Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
						return;
					}
				}
			}

			for (Entity en : targets) {
				Player p = (Player) en;

				for (ItemStack item : p.getInventory().getContents()) {
					if (item != null && (mat == null || item.getType() == mat)) {
						if (data == null || item.getData().getData() == data) {
							p.getInventory().remove(item);
						}
					}
				}

				for (ItemStack item : p.getInventory().getArmorContents()) {
					if (item != null && (mat == null || item.getType() == mat)) {
						if (data == null || item.getData().getData() == data) {
							p.getInventory().remove(item);
						}
					}
				}
			}

			player.sendMessage(Lang.CMD_EXECUTE.get());
			return;
		}

		// DIFFICULTY ----------------------------------------------------------
		if (label.equalsIgnoreCase("difficulty")) {
			e.setCancelled(true);

			String command = "/difficulty <difficulty>";
			
			if (!s.playerHasPermission(player, Permission.DIFFICULTY) && !player.hasPermission(Permission.DIFFICULTY.getSpigotPerm())) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.DIFFICULTY.toString()));
				return;
			}

			if (args.length == 0) {
				player.sendMessage(Lang.ST_DIFFICULTY.get().replace(LangTag.VALUE.toString(), game.getMainWorld().getMainWorld().getDifficulty().name().toLowerCase()));
				return;
			}

			if (args.length != 1) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			Difficulty d = null;
			String a = args[0];

			if (a.equals("0") || a.equalsIgnoreCase("peacefull")) d = Difficulty.PEACEFUL;
			else if (a.equals("1") || a.equalsIgnoreCase("easy")) d = Difficulty.EASY;
			else if (a.equals("2") || a.equalsIgnoreCase("normal")) d = Difficulty.NORMAL;
			else if (a.equals("3") || a.equalsIgnoreCase("hard")) d = Difficulty.HARD;

			if (d == null) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			for (World w : game.getWorlds()) {
				w.setDifficulty(d);
			}

			player.sendMessage(Lang.CMD_EXECUTE.get());
			return;
		}

		// GAMEMODE ----------------------------------------------------------
		if (label.equalsIgnoreCase("gamemode")) {
			e.setCancelled(true);

			String command = "/gamemode <gamemode> [player]";

			if (!s.playerHasPermission(player, Permission.GAMEMODE) && !player.hasPermission(Permission.GAMEMODE.getSpigotPerm())) {
				player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.GAMEMODE.toString()));
				return;
			}

			if (args.length == 0) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			if (args.length > 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			GameMode gameMode = null;
			String a = args[0];

			if (a.equals("0") || a.equalsIgnoreCase("survival")) gameMode = GameMode.SURVIVAL;
			else if (a.equals("1") || a.equalsIgnoreCase("creative")) gameMode = GameMode.CREATIVE;
			else if (a.equals("2") || a.equalsIgnoreCase("adventure")) gameMode = GameMode.ADVENTURE;
			else if (a.equals("3") || a.equalsIgnoreCase("spectator")) gameMode = GameMode.SPECTATOR;

			if (gameMode == null) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			if (args.length == 2) {

				if (args[1].equals("@e")) {
					player.sendMessage(Lang.CMD_ERROR_SELECTOR.get().replace(LangTag.VALUE.toString(), "@e"));
					return;
				}

				List<Entity> targets = getTarget(player, game, args[1]);
				if (targets == null)
					return;

				for (Entity en : targets) {
					((Player) en).setGameMode(gameMode);
				}

			} else {
				player.setGameMode(gameMode);
			}

			player.sendMessage(Lang.CMD_EXECUTE.get());
			return;
		}

		// GIVE ----------------------------------------------------------
		if (label.equalsIgnoreCase("give")) {
			e.setCancelled(true);
			
			String command = "/give <player> <itemID> [amount] [metadata]";
			
			if (args.length == 0) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}

			if (args.length < 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}
			
			if (args.length > 4) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}
			
			Material mat = null;
			Byte data = null;
			Integer amount = null;

			// SELECTOR
			if (args[0].equals("@e")) {
				player.sendMessage(Lang.CMD_ERROR_SELECTOR.get().replace(LangTag.VALUE.toString(), "@e"));
				return;
			}

			List<Entity> targets = getTarget(player, game, args[0]);
			if (targets == null) return;
			
			//MATERIAL
			mat = UHCItem.getFromID(args[1]);
			
			if (mat == null) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}
			
			if (args.length >= 3) {
				
				//AMOUNT
				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException ex) {
					player.sendMessage( Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
					return;
				}
				
				
				if (args.length == 4) {
					
					//DATA
					try {
						data = Byte.parseByte(args[3]);
					} catch (NumberFormatException ex) {
						player.sendMessage( Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
						return;
					}
				}
			}
			
			ItemBuilder item = new ItemBuilder(mat);
			if (data != null) item = new ItemBuilder(mat, amount, data);
			else if (amount != null) item.setAmount(amount);
			
			for (Entity en : targets) {
				UHCPlayer p = game.getUHCPlayer(((Player) en).getUniqueId());

				if (p != null) {
					p.giveItem(item.getItem());
				}
			}
			
			player.sendMessage(Lang.CMD_EXECUTE.get());
			return;
		}

		// GAMERULE ----------------------------------------------------------
		/*if (label.equalsIgnoreCase("gamerule")) {
			e.setCancelled(true);
			
			String command = "/gamerule <rule name> [value]";
					
			if (args.length == 0) {
				player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}
			
			if (args.length > 2) {
				player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				return;
			}
			
		}*/

		// TIME ----------------------------------------------------------
		if (label.equalsIgnoreCase("time")) {
			if (game.getSettings().uhc_cycle && game.isUHCWorld(player.getWorld())) {
				e.setCancelled(true);
				player.sendMessage(Lang.CMD_ERROR_OFF.get());
				return;
			}

			return;
		}
	}

	private List<Entity> getTarget(Player player, Game game, String arg) {

		if (arg.equals("@p") || arg.equals("@s")) {
			return Arrays.asList(player);
		}

		if (arg.equals("@a")) {
			List<Entity> players = new ArrayList<Entity>();

			for (UHCPlayer p : game.getConnectedPlayers()) {
				players.add(p.getPlayer());
			}

			return players;
		}

		if (arg.equals("@e")) {
			List<Entity> entities = new ArrayList<Entity>();
			for (World w : game.getWorlds()) {
				for (Entity e : w.getEntities()) {
					if (e instanceof Player) {
						Player p = (Player) e;
						if (!game.isHere(p.getUniqueId())) {
							continue;
						}
					}
					entities.add(e);
				}
			}

			return entities;
		}

		Player target = Bukkit.getPlayer(arg);

		if (target == null) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFLINE.get().replace(LangTag.PLAYER_NAME.toString(), arg));
			return null;
		}

		if (!game.isHere(target.getUniqueId()) && !player.hasPermission(Permission.CLEAR.getSpigotPerm())) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFGAME.get().replace(LangTag.PLAYER_NAME.toString(), arg));
			return null;
		}

		return Arrays.asList(target);
	}
}
