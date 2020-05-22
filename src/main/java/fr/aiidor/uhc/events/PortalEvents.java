package fr.aiidor.uhc.events;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.world.UHCWorld;

public class PortalEvents implements Listener {
	
	@EventHandler
    public void onPortal(PlayerPortalEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		Player player = e.getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		if (!game.isUHCWorld(player.getWorld())) return;
		
		UHCWorld w = game.getUHCWorld(player.getWorld());
		
		if (w.hasOverWorld()) {
					
			if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL && w.hasNether() && !w.getNether().getName().endsWith("_nether")) {
				e.useTravelAgent(true);
				e.getPortalTravelAgent().setCanCreatePortal(true);
				
				Location location = null;
				
				if (player.getWorld() == w.getOverWorld()) {
					location = new Location(w.getNether(), e.getFrom().getBlockX() / 8, e.getFrom().getBlockY(), e.getFrom().getBlockZ() / 8);
				
				} else {
					location = new Location(w.getOverWorld(), e.getFrom().getBlockX() * 8, e.getFrom().getBlockY(), e.getFrom().getBlockZ() * 8);
				}
				
				e.setTo(e.getPortalTravelAgent().findOrCreate(location));
			}
			
			if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL && w.hasEnd() && !w.getNether().getName().endsWith("_the_end")) {
				if (player.getWorld() == w.getOverWorld()) {
					
					Location loc = new Location(w.getEnd(), 100, 50, 0);
					e.setTo(loc);
					Block block = loc.getBlock();
					
					for (int x = block.getX() - 2; x <= block.getX() + 2; x++) {
						for (int z = block.getZ() - 2; z <= block.getZ() + 2; z++) {
							Block platformBlock = loc.getWorld().getBlockAt(x, block.getY() - 1, z);
							
							if (platformBlock.getType() != Material.OBSIDIAN) {
								platformBlock.setType(Material.OBSIDIAN);
							}
							
							for (int yMod = 1; yMod <= 3; yMod++) {
								Block b = platformBlock.getRelative(BlockFace.UP, yMod);
								if (b.getType() != Material.AIR) {
									b.setType(Material.AIR);
								}
							}
						}
					}
					
				} else if (player.getWorld() == w.getEnd()) {
					e.setTo(w.getOverWorld().getSpawnLocation());
				}
			}
		}
    }
}
