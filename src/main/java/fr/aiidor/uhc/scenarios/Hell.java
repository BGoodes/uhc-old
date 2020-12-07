package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

import fr.aiidor.uhc.enums.Category;

public class Hell extends Scenario {
	
	public Hell(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "hell";
	}

	@Override
	public Material getIcon() {
		return Material.NETHERRACK;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.GENERATION, Category.SURVIVAL);
	}
	
	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.BIOME_APOCALYPSE)) return false;
		if (scenario.equals(ScenariosManager.BIOME_CENTER)) return false;
		return true;
	}
	
	public static class HellPopulator extends BlockPopulator {
		
		@SuppressWarnings("deprecation")
		public void populate(World world, Random random, Chunk chunk) {
			
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y <= 131; y++) {
					for (int z = 0; z < 16; z++) {
						
						Block b = chunk.getBlock(x, y, z);
						Material type = b.getType();
						
						if (type == Material.SNOW_BLOCK) {
							b.setType(Material.SOUL_SAND);
						}
						
						if (type.name().contains("WATER")) {
							b.setType(Material.STATIONARY_LAVA);
						}
						
						if (type == Material.GRASS || type == Material.DIRT) {
							
							Integer c = random.nextInt(10);
							
							if (c < 2) {
								b.setType(Material.DIRT);
								b.setData((byte) 1);
							}
							
							else if (c == 2) b.setType(Material.COBBLESTONE);
							else if (c == 3) b.setType(Material.MOSSY_COBBLESTONE);
						
							if (random.nextInt(200) == 0) b.setType(Material.NETHERRACK);
							
							if (type == Material.GRASS) {
								if (random.nextInt(30) == 0) b.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
							}
						}
						
						if (type == Material.SAND) {
							if (random.nextInt(10) == 0) b.setType(Material.SANDSTONE);
						}
						
						
						else if (type == Material.STONE) {
							Integer c = random.nextInt(15);
							if (c < 2) {
								b.setType(Material.COBBLESTONE);
							}

							if (c == 2) {
								b.setType(Material.MOSSY_COBBLESTONE);
							}
							
							if (c == 3) {
								b.setType(Material.NETHERRACK);
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public Boolean needWorldGeneration() {
		return true;
	}
}
