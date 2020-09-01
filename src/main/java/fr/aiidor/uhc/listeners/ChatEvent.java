package fr.aiidor.uhc.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.commands.CommandMessage;
import fr.aiidor.uhc.enums.ActionChat;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.inventories.Inv_Scenarios;
import fr.aiidor.uhc.listeners.events.ActionChatEvent;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.world.WorldPanel;

public class ChatEvent implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		Player player = e.getPlayer();
		
		if (!gm.hasGame()) {
			if (ActionChat.hasActionChat(player)) ActionChat.removeActionChat(player);
			return;
		}

		Game game = gm.getGame();
		
		if (!game.isHere(e.getPlayer().getUniqueId()))  {
			e.setCancelled(true);
			
			if (ActionChat.hasActionChat(player)) ActionChat.removeActionChat(player);
			return;
		}
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (ActionChat.hasActionChat(player)) {
			e.setCancelled(true);
			player.sendMessage(Lang.AC_CHAT.get().replace(LangTag.VALUE.toString(), e.getMessage()));
			
			Bukkit.getServer().getPluginManager().callEvent(new ActionChatEvent(p, ActionChat.getActionChat(player), e.getMessage(), game));
			return;
		}
			
		e.setCancelled(true);
			
		if (!p.hasPermission(Permission.CHAT)) {
			p.getPlayer().sendMessage(Lang.PM_ERROR_CHAT.get());
			return;
		}
			
		String prefix = p.getRank().getPrefix();
		
		if (game.hasTeam() && p.hasTeam() && game.getSettings().team_chat) {
				
		UHCTeam t = p.getTeam();
				
			if (e.getMessage().startsWith("@")) {
						
				game.broadcast(Lang.MSG_CHAT.get()
						.replace(LangTag.PLAYER_NAME.toString(), prefix + p.getDisplayName())
						.replace(LangTag.VALUE.toString(), "§b@§7" + e.getMessage().substring(1))
					);
						
			} else {
				p.getTeam().broadcast(Lang.MSG_CHAT_TEAM.get()
					.replace(LangTag.PLAYER_NAME.toString(), t.getColor().getChatcolor() + p.getName())
					.replace(LangTag.VALUE.toString(), e.getMessage())
				);	
			}
					
		} else {
				game.broadcast(Lang.MSG_CHAT.get()
				.replace(LangTag.PLAYER_NAME.toString(), prefix + p.getDisplayName())
				.replace(LangTag.VALUE.toString(), e.getMessage())
			);
		}

		if (ScenariosManager.GOOD_GAME.isActivated() && ScenariosManager.GOOD_GAME.death && p.isAlive() && e.getMessage().toLowerCase().contains("gg")) {
			ScenariosManager.GOOD_GAME.death = false;
			ScenariosManager.GOOD_GAME.giveItem(p);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPreprocess(PlayerCommandPreprocessEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		GameSettings s = game.getSettings();
		Player player =  e.getPlayer();
		
		String cmd = e.getMessage().substring(1);
		String label = cmd.split(" ")[0];
		 
		String[] args = new String[] {""};
		if (cmd.split(" ").length > 1) args = cmd.substring(label.length() + 1).split(" ");
		 
		if (label.equalsIgnoreCase("tell")) {
			e.setCancelled(true);
			 
			new CommandMessage().onCommand(player, null, label, args);
			return;
		 }
		 
		if (!game.isHere(player.getUniqueId())) return;
		
		 //CLEAR
		 if (label.equalsIgnoreCase("clear")) {
			 e.setCancelled(true);
			 
			 String command = "/clear [player] [itemID] [Meta]";
			
			 if (args.length == 0) {
			 	 
				 player.getInventory().clear();
				 player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
				 player.sendMessage(Lang.CMD_EXECUTE.get());
				 return;				
			 }
				
			 if (args.length > 3) {
			 	 player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				 return;
			 }
			 
			 if (args.length > 1) {
				 
				 System.out.println(args[1] + " " + Material.matchMaterial(args[1].replace("minecraft:", "")));
				 if (Material.matchMaterial(args[1]) == null) {
				 	 player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
					 return;
				 }
				 
				 if (args.length > 3) {
					 try {
						 
						 Byte.parseByte(args[2]);
						 
					 } catch (NumberFormatException ex) {
					 	 player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
						 return;
					 }
				 }
			 }
			 
			 if (!s.playerHasPermission(player, Permission.CLEAR) && !player.hasPermission(Permission.CLEAR.getSpigotPerm())) {
				 player.sendMessage(Lang.CMD_ERROR_PERM.get().replace(LangTag.PERM.toString(), Permission.CLEAR.toString()));
				 return;
			 }
			
			 if (args[0].equals("@e")) {
				 player.sendMessage(Lang.CMD_ERROR_SELECTOR.get().replace(LangTag.VALUE.toString(), "@e"));
				 return;
			 }
			 
			 List<Entity> targets = getTarget(player, game, args[0], Permission.CLEAR);
			 if (targets == null) return;
			
			 for (Entity en : targets) {
				 Player p = (Player) en;
				
				 if (args.length == 1) {
					 p.getInventory().clear();
					 p.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
					 
				 } else {
					 
					 for (ItemStack item : p.getInventory().getContents()) {
						 if (item.getType() == Material.matchMaterial(args[1])) {
							 if (args.length < 3 || item.getData().getData() == Byte.parseByte(args[2])) {
								 p.getInventory().remove(item);
							 }
						 }
					 }
					 
					 for (ItemStack item : p.getInventory().getArmorContents()) {
						 if (item.getType() == Material.matchMaterial(args[1])) {
							 if (args.length < 3 || item.getData().getData() == Byte.parseByte(args[2])) {
								 p.getInventory().remove(item);
							 }
						 }
					 }
				 }
			 }
			
			 player.sendMessage(Lang.CMD_EXECUTE.get());
			 return;
		 }
		 
		 //DIFFICULTY
		 /*if (label.equalsIgnoreCase("difficulty")) {
			 e.setCancelled(true);
			 String command = "/difficulty <difficulty>";
			
			 if (args.length == 0) {
			 	 player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				 return;				
			 }
			 
			 if (args.length != 1) {
			 	 player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				 return;	
			 }
			 
			 Difficulty d = null;
			 
			 if (d == null) {
			 	 player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				 return;
			 }
			 
			 for (World w : game.getWorlds()) {
				 w.setDifficulty(d);
			 }
			 
			 player.sendMessage(Lang.CMD_EXECUTE.get());
			 return;
		 }*/
		 
		 //GAMEMODE
		 /*if (label.equalsIgnoreCase("gamemode")) {
			 e.setCancelled(true);
			 String command = "/difficulty <difficulty>";
			
			 if (args.length == 0) {
			 	 player.sendMessage(Lang.CMD_HELP.get().replace(LangTag.COMMAND.toString(), command));
				 return;				
			 }
			 
			 if (args.length != 1) {
			 	 player.sendMessage(Lang.CMD_ERROR_INVALID_ARGUMENT.get().replace(LangTag.COMMAND.toString(), command));
				 return;	
			 }
			 
			 return;
		 }*/
		 
		 //TIME
		 if (label.equalsIgnoreCase("time")) {
			 if (game.getSettings().uhc_cycle && game.isUHCWorld(player.getWorld())) {
				 e.setCancelled(true);
				 player.sendMessage(Lang.CMD_ERROR_OFF.get());
				 return;
			 }
			 
			 return;
		 }
	 }
	
	private List<Entity> getTarget(Player player, Game game, String arg, Permission perm) {
		
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
	
	@EventHandler
	public void actionChatEvent(ActionChatEvent e) {
		
		Player player = e.getPlayer();
		String msg = e.getMessage();
		
		if (msg.equalsIgnoreCase(Lang.AC_CANCEL.get())) {
			ActionChat.removeActionChat(player);
			
			if (e.getActionChat() == ActionChat.SCENARIO_SEARCH) {
				player.openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
			}
			
			if (e.getActionChat() == ActionChat.WORLD_CREATION) {
				player.openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			}
			
			return;
		}
		
		if (e.getActionChat() == ActionChat.SCENARIO_SEARCH) {
			player.openInventory(new Inv_Scenarios().getInventory(0, null, msg));
			ActionChat.removeActionChat(player);
			return;
		}
		
		if (e.getActionChat() == ActionChat.WORLD_CREATION) {
			if (msg.length() > 30) {
				player.sendMessage(Lang.CMD_ERROR_NAME_SIZE.get().replace(LangTag.VALUE.toString(), ""+30));
				return;
			}
			
			if (Bukkit.getWorld(msg) != null) {
				player.sendMessage(Lang.ST_ERROR_WORLD_EXIST.get());
				return;
			}
			
			if (!e.getGame().hasWorldPanel()) {
				
				WorldPanel panel = new WorldPanel(msg);
				
				for (Environment en : Environment.values()) {
					if (Bukkit.getWorld(panel.getWorldName(en)) != null) {
						player.sendMessage(Lang.ST_ERROR_WORLD_EXIST.get());
						return;
					}
				}
				
				e.getGame().setWorldPanel(panel);
			}
			
			player.openInventory(GuiManager.INV_WORLD_CREATION.getInventory());
			ActionChat.removeActionChat(player);
			return;
		}
	}
}
