package fr.aiidor.uhc.events;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class ExplosionEvents implements Listener {


	@EventHandler
	public void explosionPrimeEvent(ExplosionPrimeEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		if (ScenariosManager.TNTFLY.isActivated() && ScenariosManager.TNTFLY.chain) {
			if (e.getEntity() instanceof TNTPrimed) {
				e.getEntity().setVelocity(new Vector(0, 0.25, 0));
			}
		}
	}
	
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getEntity().getWorld())) return;
		
		onExplosion(game, e.blockList());
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyBombs) {
			e.setYield(0);
		}
	}
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;
		
		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getBlock().getWorld())) return;
		
		onExplosion(game, e.blockList());
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyBombs) {
			e.setYield(0);
		}
	}
	
	private void onExplosion(Game game, List<Block> blocks) {
		
		Iterator<Block> b = blocks.iterator();
		
		if (ScenariosManager.TNTFLY.isActivated() && ScenariosManager.TNTFLY.chain) {
			while(b.hasNext()) {
				Block bs = b.next();
				
				if (bs.getType() == Material.TNT) {
					
					b.remove();
					bs.setType(Material.AIR);
					
					Location location = bs.getLocation().add(0.5, 0.25, 0.5);
					TNTPrimed tnt = (TNTPrimed) bs.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
					tnt.setVelocity(new Vector(0, 0.25, 0));
					
					tnt.teleport(location);
					location.getWorld().playSound(location, Sound.FUSE, 0.7f, 1f);
				}
			}
		}
	}
}
