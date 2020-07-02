package fr.aiidor.uhc.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;

public class TablistManager {
	
    public final Map<UUID, PersonalTablist> tablists;
    
	public TablistManager() {
		tablists = new HashMap<UUID, PersonalTablist>();
		
		UHC.getInstance().getScheduledExecutorService().scheduleAtFixedRate(() ->  {
			
	        for (PersonalTablist tablist : tablists.values()) {
	        	UHC.getInstance().getExecutorMonoThread().execute(() -> tablist.update());
	        }       
		}, 80, 80, TimeUnit.MILLISECONDS);
	}

    public void onLogin(Player player) {
        if (tablists.containsKey(player.getUniqueId())) return;
        tablists.put(player.getUniqueId(), new PersonalTablist(player));
    }

    public void onLogout(Player player) {
        if (tablists.containsKey(player.getUniqueId())) {
        	tablists.remove(player.getUniqueId());
        }
    }
}
