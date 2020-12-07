package fr.aiidor.uhc.world.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import fr.aiidor.uhc.utils.MCMath;

public class SugarCanePopulator extends BlockPopulator {
	
	private final int percentage;

	public SugarCanePopulator(int percentage) {
		this.percentage = percentage;
	}
	
	public void populate(World world, Random random, Chunk chunk) {
		for (int x = 1; x < 15; x++) {
			for (int z = 1; z < 15; z++) {
				
				Block block = world.getHighestBlockAt(chunk.getBlock(x, 0, z).getLocation());
				Block below = block.getRelative(BlockFace.DOWN);
				
				
				if (this.percentage > random.nextInt(100) && (below.getType() == Material.SAND || below.getType() == Material.GRASS)) {

					for (BlockFace f : MCMath.getBlockFaces()) {
						
						if (below.getRelative(f).getType() == Material.STATIONARY_WATER) {
							if (block.getType() == Material.AIR) {
								int height = random.nextInt(3) + 1;
								Location location = block.getLocation();
								while (height > 0) {
									world.getBlockAt(location).setType(Material.SUGAR_CANE_BLOCK);
									location = location.add(0.0D, 1.0D, 0.0D);
									height--;
								}
							}
						}
					}
				}
			}
		}
	}
}