package fr.aiidor.uhc.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.potion.PotionType;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.commands.CommandMessage;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.tools.ItemBuilder;

public class ChatEvent implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (e.getMessage().contains("TEST")) {
			
			player.getInventory().addItem(new ItemBuilder(Material.POTION, Lang.DW_ITEM_HOLY_WATER.get()).setPotionType(PotionType.WATER_BREATHING, true).addFlag(ItemFlag.HIDE_POTION_EFFECTS).getItem());
			
			e.setCancelled(true);
			return;
		}
		
		if (game.isHere(e.getPlayer().getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			
			e.setCancelled(true);
			
			if (!p.hasPermission(Permission.CHAT)) {
				p.getPlayer().sendMessage(Lang.PM_ERROR_CHAT.get());
				return;
			}
			
			if (game.hasTeam() && p.hasTeam()) {
				
				UHCTeam t = p.getTeam();
				if (game.getSettings().team_chat) {
					
					
					if (e.getMessage().startsWith("@")) {
						
						game.broadcast(Lang.MSG_CHAT.get()
								.replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName())
								.replace(LangTag.VALUE.toString(), "§b@§7" + e.getMessage().substring(1))
							);
						
					} else {
						
						//CHAT DE TEAM
						if (ScenariosManager.MYSTERY_TEAMS.isActivated()) {
							p.getTeam().broadcast(Lang.MSG_CHAT_TEAM.get()
									.replace(LangTag.PLAYER_NAME.toString(), t.getColor().getChatcolor() + "§k" + p.getName() + "§r")
									.replace(LangTag.VALUE.toString(), e.getMessage())
								);
						}
						else {
							p.getTeam().broadcast(Lang.MSG_CHAT_TEAM.get()
									.replace(LangTag.PLAYER_NAME.toString(), t.getColor().getChatcolor() + p.getName())
									.replace(LangTag.VALUE.toString(), e.getMessage())
								);
						}
								
					}
					
					
				} else {
					game.broadcast(Lang.MSG_CHAT.get()
							.replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName())
							.replace(LangTag.VALUE.toString(), e.getMessage())
						);
				}
				
			} else {
				game.broadcast(Lang.MSG_CHAT.get()
						.replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName())
						.replace(LangTag.VALUE.toString(), e.getMessage())
					);
			}

			if (ScenariosManager.GOOD_GAME.isActivated() && ScenariosManager.GOOD_GAME.death && p.isAlive() && e.getMessage().toLowerCase().contains("gg")) {
				ScenariosManager.GOOD_GAME.death = false;
				ScenariosManager.GOOD_GAME.giveItem(p);
			}
			
		} else {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPreprocess(PlayerCommandPreprocessEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player =  e.getPlayer();
		
		 String cmd = e.getMessage().substring(1);
		 String label = cmd.split(" ")[0];
		 
		 String[] args = new String[] {""};
		 if (cmd.split(" ").length > 1) args = cmd.substring(label.length() + 1).split(" ");
		 
		 if (e.getMessage().toLowerCase().contains("/time")) {
			 if (game.getSettings().uhc_cycle) e.setCancelled(true);
		 }
		 
		 if (e.getMessage().toLowerCase().contains("/tell")) {
			 e.setCancelled(true);
		
			 System.out.println(args[0]);
			 System.out.println("---------------------");
			 
			 new CommandMessage().onCommand(player, null, label, args);
			 return;
		 }
	 }
}
