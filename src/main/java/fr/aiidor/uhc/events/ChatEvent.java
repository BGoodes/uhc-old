package fr.aiidor.uhc.events;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.GuiManager;

public class ChatEvent implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (game.isHere(e.getPlayer().getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(e.getPlayer().getUniqueId());
			
			e.setCancelled(true);
			game.broadcast("§f" + p.getDisplayName() + " §8»§7 " + e.getMessage());
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
