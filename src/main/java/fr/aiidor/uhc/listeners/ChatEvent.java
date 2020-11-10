package fr.aiidor.uhc.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.ActionChat;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
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

		if (p.isSpec()) {
			game.broadcast(Lang.MSG_CHAT.get().replace(LangTag.PLAYER_NAME.toString(), Rank.SPECTATOR.getPrefix() + p.getName())
					.replace(LangTag.VALUE.toString(), e.getMessage()));
			return;
		}

		if (game.hasTeam() && p.hasTeam() && game.getSettings().team_chat) {

			UHCTeam t = p.getTeam();

			if (e.getMessage().startsWith("@")) {

				game.broadcast(Lang.MSG_CHAT.get().replace(LangTag.PLAYER_NAME.toString(), prefix + p.getDisplayName())
						.replace(LangTag.VALUE.toString(), "§b@§7" + e.getMessage().substring(1)));

			} else {
				p.getTeam()
						.broadcast(Lang.MSG_CHAT_TEAM.get()
								.replace(LangTag.PLAYER_NAME.toString(), t.getColor().getChatcolor() + p.getName())
								.replace(LangTag.VALUE.toString(), e.getMessage()));
			}

		} else {
			game.broadcast(Lang.MSG_CHAT.get().replace(LangTag.PLAYER_NAME.toString(), prefix + p.getDisplayName())
					.replace(LangTag.VALUE.toString(), e.getMessage()));
		}

		if (ScenariosManager.GOOD_GAME.isActivated() && ScenariosManager.GOOD_GAME.death && p.isAlive() && e.getMessage().toLowerCase().contains("gg")) {
			ScenariosManager.GOOD_GAME.death = false;
			ScenariosManager.GOOD_GAME.giveItem(p);
		}
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
