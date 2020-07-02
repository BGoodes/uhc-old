package fr.aiidor.uhc.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.GameManager;

public class ServerListEvent implements Listener {
	
	@EventHandler
	public void onServerListPing(ServerListPingEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		
		if (gm.hasGame()) {
			String gamestate = gm.getGame().getState().name();
			
			String line1 = Lang.valueOf("MOTD_" + gamestate + "_LINE_1").get();
			String line2 = Lang.valueOf("MOTD_" + gamestate + "_LINE_2").get();
			
			e.setMotd(line1 + "\n" + line2);
		}
	}
}
