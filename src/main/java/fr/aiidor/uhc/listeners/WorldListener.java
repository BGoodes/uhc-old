package fr.aiidor.uhc.listeners;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldInitEvent;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GenerateWorldEvent;
import fr.aiidor.uhc.scenarios.Hell.HellPopulator;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.world.generator.SweatWorldGenerator;

public class WorldListener implements Listener {

	@EventHandler
	public void generateWorldEvent(GenerateWorldEvent e) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;

		WorldCreator wc = e.getWorldCreator();
		
		if (ScenariosManager.SWEAT_WORLD.isActivated() && e.getWorldCreator().environment() == Environment.NORMAL) {
			wc.generator(new SweatWorldGenerator());
			return;
		}
		
		if (ScenariosManager.URBAN.isActivated() && e.getWorldCreator().environment() == Environment.NORMAL) {
			wc.type(WorldType.FLAT);
			wc.generatorSettings("3;7,59*1,3*3,2;1;village(size=50 distance=9),decoration");
			return;
		}
	}
	
	@EventHandler
	public void worldInitEvent(WorldInitEvent e) {
	    World world = e.getWorld(); 
	    
	    if (ScenariosManager.HELL.isActivated() && world.getEnvironment() == Environment.NORMAL) {
	    	world.getPopulators().add(new HellPopulator());
	    }
	}

	@EventHandler
	public void chunkPopulateEvent(ChunkPopulateEvent e) {
		if (!UHC.getInstance().getGameManager().hasGame())
			return;

		Game game = UHC.getInstance().getGameManager().getGame();
		Chunk chunk = e.getChunk();	
		
		World world = chunk.getWorld();
		
		Boolean chunk_apocalypse = ScenariosManager.CHUNK_APOCALYPSE.isActivated() && new Random().nextInt(100) <= ScenariosManager.CHUNK_APOCALYPSE.percent;
		
		if (game.isUHCWorld(e.getWorld())) {
			for (int x = 0; x < 16; x++) {
				for (int y = 0; y <= world.getMaxHeight(); y++) {
					for (int z = 0; z < 16; z++) {
						
	                    Block block = chunk.getBlock(x, y, z);
	                    
	        			if (chunk_apocalypse && world.getEnvironment() != Environment.THE_END) {
	        				if (block.getType() != Material.AIR) block.setType(Material.AIR, false);
	        				continue;
	        			}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void chunkLoadEvent(ChunkLoadEvent e) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;
		
		Game game = UHC.getInstance().getGameManager().getGame();
		Chunk chunk = e.getChunk();	
		
		World world = chunk.getWorld();
		
		if (!game.getWorlds().contains(world)) return;
		
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y <= world.getMaxHeight(); y++) {
				for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                   
                    /*
                     * MEGA_SPRUCE_TAIGA / MEGA_SPRUCE_TAIGA_HILLS   pour le devil watches ?
                     * 
                     * 
                     */
                    
					if (ScenariosManager.HELL.isActivated() && world.getEnvironment() != Environment.NETHER) {
						block.setBiome(Biome.HELL);
					}
					
					if (ScenariosManager.BIOME_APOCALYPSE.isActivated() && world.getEnvironment() == Environment.NORMAL) {
						ScenariosManager.BIOME_APOCALYPSE.apocalypse(block);
					}
					
					if (ScenariosManager.BIOME_CENTER.isActivated() && world.getEnvironment() == Environment.NORMAL) {
						ScenariosManager.BIOME_CENTER.setBiome(game, block);
					}
				}
			}
		}
	}
}
