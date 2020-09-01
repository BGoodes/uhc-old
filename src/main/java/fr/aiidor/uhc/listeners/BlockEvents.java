package fr.aiidor.uhc.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.GameSettings;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

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
		
		Block b = e.getBlock();
		
		if (ScenariosManager.ENCHANTING_CENTER.isActivated() && ScenariosManager.ENCHANTING_CENTER.table_loc != null && player.getGameMode() != GameMode.CREATIVE) {
			if (b.getLocation().distance(ScenariosManager.ENCHANTING_CENTER.table_loc) <= 10) {
				e.setCancelled(true);
				playCancelEffect(player, b);
				player.sendMessage(Lang.MSG_ERROR_PLACE_BLOCK.get());
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
		Block b = e.getBlock();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		if (!game.isStart()) {
			if (player.getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
				return;
			}
		}
		
		if (ScenariosManager.ENCHANTING_CENTER.isActivated() && ScenariosManager.ENCHANTING_CENTER.table_loc != null && player.getGameMode() != GameMode.CREATIVE) {
			if (b.getLocation().distance(ScenariosManager.ENCHANTING_CENTER.table_loc) <= 10) {
				e.setCancelled(true);
				playCancelEffect(player, b);
				player.sendMessage(Lang.MSG_ERROR_BREAK_BLOCK.get());
				return;
			}
		}
		
		if (ScenariosManager.FLOWER_POWER.isActivated() && player.getGameMode() != GameMode.CREATIVE) {
			if (ScenariosManager.FLOWER_POWER.dropItem(b)) {
				e.setCancelled(true);
				b.setType(Material.AIR);
				return;
			}
		}
		
		if (ScenariosManager.STINGY_WORLD.isActivated() && ScenariosManager.STINGY_WORLD.stingyBlocks) {
			e.setCancelled(true);
			
			if (b.getType() == Material.SUGAR_CANE_BLOCK) {
				
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
			
			b.setType(Material.AIR);
			return;
		}
		
		if ((b.getType() == Material.LEAVES || b.getType() == Material.LEAVES_2) && game.getSettings().uhc_trees) {
			
			if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.SHEARS) {
				if (game.getSettings().trees_shears) {
						
					ItemStack shear = player.getItemInHand();
					shear.setDurability((short) (shear.getDurability() -1));
						
					e.setCancelled(true);
					leavesDrop(game.getSettings(), b);
					return;
				}
					
			} else {
				e.setCancelled(true);
				leavesDrop(game.getSettings(), b);
				return;
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
	
	private void playCancelEffect(Player p, Block b) {
		
		Location l = b.getLocation().clone();
		b.getWorld().playEffect(l, Effect.STEP_SOUND, b.getType());
		
		l.add(new Vector(0.5, 0, 0.5));
		
		for (int i = 0; i != 15; i ++) {
			l.add(new Vector(0, 0.05, 0));
			PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, true, (float) l.getX(), (float) l.getY(), (float) l.getZ(), 0, 0, 0, 0, 0, 0);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(particles);
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
		
		if (game.getSettings().uhc_trees) {
			e.setCancelled(true);
			leavesDrop(game.getSettings(), e.getBlock());
		}
	}
	
	@SuppressWarnings("deprecation")
	private void leavesDrop(GameSettings settings, Block l) {
		Collection<ItemStack> drops = l.getDrops();
		
		for (ItemStack i : drops) {
			if (i.getType() == Material.SAPLING && settings.trees_sapling) {
				l.getWorld().dropItemNaturally(l.getLocation().add(0.5, 0, 0.5), i);
			}
		}
		
		if (settings.all_trees_drop || (l.getData()%4==0 || l.getData() == 9 || l.getData() == 13)) {
			if (settings.trees_apple > 0) {
				
				if (settings.trees_apple * 10 >= new Random().nextInt(1000)) {
					if (!settings.trees_gapple) l.getWorld().dropItemNaturally(l.getLocation().add(0.5, 0, 0.5), new ItemStack(Material.APPLE));
					else l.getWorld().dropItemNaturally(l.getLocation().add(0.5, 0, 0.5), new ItemStack(Material.GOLDEN_APPLE));
				}
			}
		}
		
		l.setType(Material.AIR);
	}
}
