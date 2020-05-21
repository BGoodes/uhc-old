package fr.aiidor.uhc.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class BlockEvents implements Listener {
	
	
	@EventHandler
	public void playerBreakBlockEvent(BlockBreakEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Player player = e.getPlayer();
		Game game = gm.getGame();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (!game.isStart()) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
				return;
			}
			
		}
		
		if (ScenariosManager.FLOWER_POWER.isActivated(game) && player.getGameMode() != GameMode.CREATIVE) {
			if (ScenariosManager.FLOWER_POWER.dropItem(e.getBlock())) {
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				return;
			}
		}
		
		if (ScenariosManager.STINGY_WORLD.isActivated(game) && ScenariosManager.STINGY_WORLD.stingyBlocks) {
			e.setCancelled(true);
			
			if (e.getBlock().getType() == Material.SUGAR_CANE_BLOCK) {
				Block b = e.getBlock();
				
				List<Block> blocks = new ArrayList<Block>();
				
				while (b.getRelative(BlockFace.UP).getType() == Material.SUGAR_CANE_BLOCK) {
					b = b.getRelative(BlockFace.UP);
					blocks.add(b);
				}
				
				Collections.reverse(blocks);
				
				for (Block cane : blocks) {
					cane.setType(Material.AIR);
					cane.getWorld().playEffect(cane.getLocation(), Effect.STEP_SOUND, Material.SUGAR_CANE_BLOCK);
				}
			}
			
			e.getBlock().setType(Material.AIR);
			return;
		}
		
		
		if (ScenariosManager.CUTCLEAN.isActivated(game)) {
			if (player.getGameMode() != GameMode.CREATIVE)
				ScenariosManager.CUTCLEAN.heat(e);
		}
		
		if (ScenariosManager.TIMBER.isActivated(game)) {
			if (player.getGameMode() != GameMode.CREATIVE)
				ScenariosManager.TIMBER.destroy(e.getBlock());
		}
	}
	
	@EventHandler
	public void leaveDecayEvent(LeavesDecayEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getBlock().getWorld())) return;
		
		if (ScenariosManager.STINGY_WORLD.isActivated(game) && ScenariosManager.STINGY_WORLD.stingyTrees) {
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			return;
		}
	}
}
