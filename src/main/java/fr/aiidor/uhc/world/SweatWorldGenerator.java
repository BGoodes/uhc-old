package fr.aiidor.uhc.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class SweatWorldGenerator extends ChunkGenerator {

	public Integer currentHeight = 50;
	public Boolean lakes = true;

	@Override
	public ChunkData generateChunkData(World world, Random random, int chunkx, int chunkz, BiomeGrid biome) {
		
		SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
		ChunkData chunk = createChunkData(world);
		
		generator.setScale(0.005D);

		for (int x = 0; x < 16; x++)
			for (int z = 0; z < 16; z++) {
				
				currentHeight = (int) (generator.noise(chunkx * 16 + x, chunkz * 16 + z, 0.5D, 0.5D) * 15D + 50D);
				chunk.setBlock(x, currentHeight, z, Material.GRASS);
				chunk.setBlock(x, currentHeight - 1, z, Material.DIRT);
				
				for (int i = currentHeight - 2; i > 0; i--) chunk.setBlock(x, i, z, Material.STONE);
				chunk.setBlock(x, 0, z, Material.BEDROCK);
			}

		return chunk;
	}

	private class TreePopulator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {

			if (random.nextBoolean()) {
				
				int amount = random.nextInt(4) + 1; // Nombre d'arbre
				
				for (int i = 1; i < amount; i++) {
					int x = random.nextInt(15);
					int z = random.nextInt(15);
					int y;

					for (y = world.getMaxHeight() - 1; chunk.getBlock(x, y, z).getType() == Material.AIR; y--); // TROUVER PLUS HAUT
					
					Biome biome = chunk.getBlock(x, y, z).getBiome();
					Integer r = 0;
					
					switch (biome) {
					
					case BIRCH_FOREST:
					case BIRCH_FOREST_HILLS:
					case BIRCH_FOREST_HILLS_MOUNTAINS:
					case BIRCH_FOREST_MOUNTAINS:
						if (random.nextBoolean())world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.BIRCH);
						else world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.TALL_BIRCH);
						break;
						
					case TAIGA:
					case TAIGA_HILLS:
					case TAIGA_MOUNTAINS:
					case COLD_TAIGA:
					case COLD_TAIGA_HILLS:
					case COLD_TAIGA_MOUNTAINS:
						r = random.nextInt(4);
						if (r == 0) world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.MEGA_REDWOOD);
						else world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.REDWOOD);
						
						break;

					case SAVANNA:
					case SAVANNA_MOUNTAINS:
					case SAVANNA_PLATEAU:
					case SAVANNA_PLATEAU_MOUNTAINS:
						world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.ACACIA);
						break;

					case ROOFED_FOREST:
					case ROOFED_FOREST_MOUNTAINS:
						if (random.nextInt(4) == 0)
							world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.TREE);
						else
							world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.DARK_OAK);
						break;

					case DESERT:
					case DESERT_HILLS:
					case DESERT_MOUNTAINS:
						break;
						
					case SWAMPLAND:
					case SWAMPLAND_MOUNTAINS:
						world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.SWAMP); 
						break;
						
					case JUNGLE:
					case JUNGLE_EDGE:
					case JUNGLE_EDGE_MOUNTAINS:
					case JUNGLE_HILLS:
					case JUNGLE_MOUNTAINS:
						r = random.nextInt(4);
						switch (r) {
							case 1: case 2: world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.JUNGLE_BUSH); break;
							case 3: world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.JUNGLE); break; 
							default: world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.SMALL_JUNGLE); break;
						}
						
						break;

					default:
						world.generateTree(chunk.getBlock(x, y, z).getLocation(), TreeType.TREE);
						break;
					}

				}
			}
		}

	}
	
	private class LakePopulator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {
			
			 if (random.nextInt(100) == 0) {
	            int chunkX = chunk.getX();
	            int chunkZ = chunk.getZ();

	            int randomX = chunkX * 16 + random.nextInt(16);
	            int randomZ = chunkZ * 16 + random.nextInt(16);
	            int y = 0;

	            for (y = world.getMaxHeight() - 1; world.getBlockAt(randomX, y, randomZ).getType() == Material.AIR; y--);
	            y -= 9;

	            Block block = world.getBlockAt(randomX + 8, y, randomZ + 8);

	            if (random.nextInt(100) < 90) {
	                block.setType(Material.WATER);
	            } else {
	                block.setType(Material.LAVA);
	            }

	            boolean[] booleans = new boolean[2048];

	            int i = random.nextInt(4) + 4;

	            int j, j1, k1;

	            for (j = 0; j < i; ++j) {
	                double d0 = random.nextDouble() * 6.0D + 3.0D;
	                double d1 = random.nextDouble() * 4.0D + 2.0D;
	                double d2 = random.nextDouble() * 6.0D + 3.0D;
	                double d3 = random.nextDouble() * (16.0D - d0 - 2.0D) + 1.0D + d0 / 2.0D;
	                double d4 = random.nextDouble() * (8.0D - d1 - 4.0D) + 2.0D + d1 / 2.0D;
	                double d5 = random.nextDouble() * (16.0D - d2 - 2.0D) + 1.0D + d2 / 2.0D;

	                for (int k = 1; k < 15; ++k) {
	                    for (int l = 1; l < 15; ++l) {
	                        for (int i1 = 0; i1 < 7; ++i1) {
	                            double d6 = (k - d3) / (d0 / 2.0D);
	                            double d7 = (i1 - d4) / (d1 / 2.0D);
	                            double d8 = (l - d5) / (d2 / 2.0D);
	                            double d9 = d6 * d6 + d7 * d7 + d8 * d8;

	                            if (d9 < 1.0D) {
	                                booleans[(k * 16 + l) * 8 + i1] = true;
	                            }
	                        }
	                    }
	                }
	            }

	            for (j = 0; j < 16; ++j) {
	                for (k1 = 0; k1 < 16; ++k1) {
	                    for (j1 = 0; j1 < 8; ++j1) {
	                        if (booleans[(j * 16 + k1) * 8 + j1]) {
	                            world.getBlockAt(randomX + j, y + j1, randomZ + k1).setType(j1 > 4 ? Material.AIR : block.getType());
	                        }
	                    }
	                }
	            }

	            for (j = 0; j < 16; ++j) {
	                for (k1 = 0; k1 < 16; ++k1) {
	                    for (j1 = 4; j1 < 8; ++j1) {
	                        if (booleans[(j * 16 + k1) * 8 + j1]) {
	                            int x1 = randomX + j;
	                            int y1 = y + j1 - 1;
	                            int z1 = randomZ + k1;

	                            if (world.getBlockAt(x1, y1, z1).getType() == Material.DIRT) {
	                                world.getBlockAt(x1, y1, z1).setType(Material.GRASS);
	                            }
	                        }
	                    }
	                }
	            }
	        }
		}
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world) {
		List<BlockPopulator> list = new ArrayList<>();
		
		list.add(new TreePopulator());
		if (lakes) list.add(new LakePopulator());
		
		return list;
	}
}
