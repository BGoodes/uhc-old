package fr.aiidor.uhc.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

public class PortalEvents implements Listener {
	
	@EventHandler
	public void playerPortalEvent(PlayerPortalEvent e) {
		System.out.println(e.getTo());
	}
}
