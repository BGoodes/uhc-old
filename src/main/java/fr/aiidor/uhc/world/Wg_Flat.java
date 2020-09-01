package fr.aiidor.uhc.world;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

public class Wg_Flat extends ChunkGenerator {
	
    int currentHeight = 50;

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
    	SimplexOctaveGenerator generator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        ChunkData chunk = createChunkData(world);
        generator.setScale(0.005D);

        for (int X = 0; X < 16; X++)
            for (int Z = 0; Z < 16; Z++) {
            	currentHeight = (int) (generator.noise(chunkX * 16 + X, chunkZ * 16 + Z, 0.5D, 0.5D) * 15D + 50D);
                chunk.setBlock(X, currentHeight, Z, Material.GRASS);
                chunk.setBlock(X, currentHeight-1, Z, Material.DIRT);
                for (int i = currentHeight-2; i > 0; i--)
                    chunk.setBlock(X, i, Z, Material.STONE);
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        
        return chunk;
    }
    
    private class TreePopulator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {

			if (random.nextBoolean()) {
			    int amount = random.nextInt(4)+1;  //Nombre d'arbre
			    for (int i = 1; i < amount; i++) {
			        int X = random.nextInt(15);
			        int Z = random.nextInt(15);
			        int Y;
			        
			        for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); //TROUVER PLUS HAUT
			        
			        switch (chunk.getBlock(X, Y, Z).getBiome()) {
			        
					case TAIGA:
					case TAIGA_HILLS: 
					case TAIGA_MOUNTAINS: 
					case COLD_TAIGA: 
					case COLD_TAIGA_HILLS: 
					case COLD_TAIGA_MOUNTAINS: 
						if (random.nextInt(4) == 0)
							world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.MEGA_REDWOOD); 
						else world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.REDWOOD);
						break;
						
					case SAVANNA:
					case SAVANNA_MOUNTAINS:
					case SAVANNA_PLATEAU:
					case SAVANNA_PLATEAU_MOUNTAINS:
						world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.ACACIA); break;
						
					case ROOFED_FOREST:
					case ROOFED_FOREST_MOUNTAINS:
						if (random.nextInt(4) == 0)
							world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TREE); 
						else world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.DARK_OAK);
						break;
						
					case DESERT:
					case DESERT_HILLS:
					case DESERT_MOUNTAINS:
						break;
						
					default:  world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TREE); 
						break;
					}
			       
			    }
			}
		}
    	
    }
    
   /* private class OreGenerator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {

			Material[] ores = {Material.COAL_ORE, Material.IRON_ORE, Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE};
			
			int X, Y, Z;
			boolean isStone;
			for (int i = 1; i < 15; i++) {  //Nombre de tries
				if (random.nextInt(100) < 5) {  
					X = random.nextInt(15);
					Z = random.nextInt(15);
					Y = random.nextInt(40)+20;  //RANDOM CO
						if (world.getBlockAt(X, Y, Z).getType() == Material.STONE) {
							isStone = true;
							while (isStone) {
								world.getBlockAt(X, Y, Z).setType(ores[random.nextInt(ores.length)]);
								if (random.nextInt(100) < 40)  {   // Probabilité de continuer la veine
									switch (random.nextInt(6)) {  // Choix de la direction
										case 0: X++; break;
										case 1: Y++; break;
										case 2: Z++; break;
										case 3: X--; break;
										case 4: Y--; break;
										case 5: Z--; break;
									}
									
									isStone = world.getBlockAt(X, Y, Z).getType() == Material.STONE;
								} else isStone = false;
							}
					}
			    }
			}
		}
    	
    }*/
    
   /* private class LakePopulator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {
			
			if (random.nextInt(100) < 10) {
	            int chunkX = chunk.getX();
	            int chunkZ = chunk.getZ();

	            int randomX = chunkX * 16 + random.nextInt(16);
	            int randomZ = chunkZ * 16 + random.nextInt(16);
	            int y = 0;

	            for (y = world.getMaxHeight() - 1; world.getBlockAt(randomX, y, randomZ).getType() == Material.AIR; y--) ;
	            y -= 7;

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
	                            int X1 = randomX + j;
	                            int Y1 = y + j1 - 1;
	                            int Z1 = randomZ + k1;

	                            if (world.getBlockAt(X1, Y1, Z1).getType() == Material.DIRT) {
	                                world.getBlockAt(X1, Y1, Z1).setType(Material.GRASS);
	                            }
	                        }
	                    }
	                }
	            }
	        }
		}
    	
    }*/
    
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
    	return Arrays.asList(new TreePopulator());
    }
}
