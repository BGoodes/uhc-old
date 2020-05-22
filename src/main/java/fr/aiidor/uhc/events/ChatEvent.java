package fr.aiidor.uhc.events;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class ChatEvent implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (game.isHere(e.getPlayer().getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(e.getPlayer().getUniqueId());
			
			e.setCancelled(true);
			
			if (!p.hasPermission(Permission.CHAT)) {
				p.getPlayer().sendMessage(Lang.PM_ERROR_CHAT.get());
				return;
			}
			
			game.broadcast("§f" + p.getName()+ " §8»§7 " + e.getMessage());
			
			if (ScenariosManager.GOOD_GAME.isActivated() && ScenariosManager.GOOD_GAME.death && p.isAlive() && e.getMessage().toLowerCase().contains("gg")) {
				ScenariosManager.GOOD_GAME.death = false;
				ScenariosManager.GOOD_GAME.giveItem(p);
			}
			
		} else {
			e.setCancelled(true);
		}
	}
	
	private List<String> scenario_cmd = Arrays.asList("/scenario", "/scénario", "/scenarios", "/scénarios");
	
	@EventHandler
	public void onPreprocess(PlayerCommandPreprocessEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Player player =  e.getPlayer();
		 if (scenario_cmd.contains(e.getMessage().toLowerCase())) {
			 e.setCancelled(true);
			 player.openInventory(GuiManager.INV_SCENARIO_LIST.getInventory());
			 return;
		 }
	 }
}
