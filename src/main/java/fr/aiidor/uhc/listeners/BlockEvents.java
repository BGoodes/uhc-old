package fr.aiidor.uhc.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class BlockEvents implements Listener {
	
	
	@EventHandler
	public void playerPlaceBlockEvent(BlockPlaceEvent e) {
		
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
	}
	
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
		
		if (ScenariosManager.FLOWER_POWER.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
			if (ScenariosManager.FLOWER_POWER.dropItem(e.getBlock())) {
				e.setCancelled(true);
				e.getBlock().setType(Material.AIR);
				return;
			}
		}
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyBlocks) {
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
		
		if ((e.getBlock().getType() == Material.LEAVES || e.getBlock().getType() == Material.LEAVES_2) && game.getSettings().uhc_trees) {
			if (player.getItemInHand() == null ||  player.getItemInHand().getType() != Material.SHEARS  || game.getSettings().trees_shears) {
				leavesDrop(game.getSettings(), e.getBlock());
			}
		}
		
		if (ScenariosManager.CUTCLEAN.isActivated()) {
			if (player.getGameMode() != GameMode.CREATIVE)
				ScenariosManager.CUTCLEAN.heat(e);
		}
		
		if (ScenariosManager.TIMBER.isActivated()) {
			if (player.getGameMode() != GameMode.CREATIVE)
				ScenariosManager.TIMBER.destroy(e.getBlock());
		}
		
		if (ScenariosManager.BLOOD_DIAMOND.isActivated()) {
			if (player.getGameMode() != GameMode.CREATIVE && e.getBlock().getType() == Material.DIAMOND_ORE) {
				player.damage(ScenariosManager.BLOOD_DIAMOND.life);
			}	
		}
	}
	
	@EventHandler
	public void leaveDecayEvent(LeavesDecayEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		if (!game.getWorlds().contains(e.getBlock().getWorld())) return;
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyTrees) {
			
			e.setCancelled(true);
			e.getBlock().setType(Material.AIR);
			return;
		}
		
		if (game.getSettings().uhc_trees) leavesDrop(game.getSettings(), e.getBlock());
	}
	
	@SuppressWarnings("deprecation")
	private void leavesDrop(GameSettings settings, Block l) {
		Collection<ItemStack> drops = l.getDrops();
		
		for (ItemStack i : drops) {
				
			if (i.getType() == Material.SAPLING && settings.trees_sapling) {
				l.getWorld().dropItemNaturally(l.getLocation().add(0.5, 0, 0.5), i);
			}
		}
		
		if (l.getData()%4==0 || l.getData() == 9 || l.getData() == 13 || settings.all_trees_drop) {
			if (settings.trees_apple != 0) {
				
				if (settings.trees_apple * 10 >= new Random().nextInt(1000)) {
					if (!settings.trees_gapple) l.getWorld().dropItemNaturally(l.getLocation().add(0.5, 0, 0.5), new ItemStack(Material.APPLE));
					else l.getWorld().dropItemNaturally(l.getLocation().add(0.5, 0, 0.5), new ItemStack(Material.GOLDEN_APPLE));
					
					l.setType(Material.AIR);
				}
			}
		}
		
		if (!drops.isEmpty() && settings.trees_sapling) l.setType(Material.AIR);
	}
}
