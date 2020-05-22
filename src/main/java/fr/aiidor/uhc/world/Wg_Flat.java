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
                currentHeight = (int) (generator.noise(chunkX*16+X, chunkZ*16+Z, 0.5D, 0.5D)*15D+50D);
                chunk.setBlock(X, currentHeight, Z, Material.GRASS);
                chunk.setBlock(X, currentHeight-1, Z, Material.DIRT);
                for (int i = currentHeight-2; i > 0; i--)
                    chunk.setBlock(X, i, Z, Material.STONE);
                chunk.setBlock(X, 0, Z, Material.BEDROCK);
            }
        return chunk;
    }
    
    public class TreePopulator extends BlockPopulator {

		@Override
		public void populate(World world, Random random, Chunk chunk) {

			if (random.nextBoolean()) {
			    int amount = random.nextInt(4)+1;  //Nombre d'arbre
			    for (int i = 1; i < amount; i++) {
			        int X = random.nextInt(15);
			        int Z = random.nextInt(15);
			        int Y;
			        for (Y = world.getMaxHeight()-1; chunk.getBlock(X, Y, Z).getType() == Material.AIR; Y--); // Find the highest block of the (X,Z) coordinate chosen.
			        world.generateTree(chunk.getBlock(X, Y, Z).getLocation(), TreeType.TREE); 
			    }
			}
		}
    	
    }
    
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
    	return Arrays.asList(new TreePopulator());
    }
}
