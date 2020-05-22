package fr.aiidor.uhc.world;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;

public class WorldManager {
	
	private String worldname;
	
	public WorldManager(World world) {
		this.worldname = world.getName();
	}
	
	public WorldManager(String worldname) {
		this.worldname = worldname;
	}
	
	public Boolean load() {
		
		Logger logger = UHC.getInstance().getLogger();
		
		if (Bukkit.getWorld(worldname) == null) {
			logger.info(Lang.ERROR_WORLD_NOT_FOUND.get().replace(LangTag.WORLD_NAME.toString(), worldname));
			return false;
		}
		
		World world = Bukkit.getWorld(worldname);
		
		world.setGameRuleValue("naturalRegeneration", "false");
		world.setPVP(true);
		world.setDifficulty(Difficulty.HARD);
		
		WorldBorder wb = world.getWorldBorder();
		wb.setCenter(0, 0);
		wb.setSize(10000);
		
		return true;
	}
	
	public Boolean unload() {
		
		Logger logger = UHC.getInstance().getLogger();
		
		if (Bukkit.getWorld(worldname) == null) {
			logger.info(Lang.ERROR_WORLD_NOT_FOUND.get().replace(LangTag.WORLD_NAME.toString(), worldname));
			return false;
		}
		
		return true;
	}
	
	public void create(Long seed, Environment environment, WorldType type, Boolean generateStructures) {
		
		Logger logger = UHC.getInstance().getLogger();
		
		if (Bukkit.getWorld(worldname) != null) {
			logger.info(Lang.ERROR_WORLD_EXIST.get().replace(LangTag.WORLD_NAME.toString(), worldname));
			return;
		}
		
		logger.info(Lang.WORLD_GENERATION.get().replace(LangTag.WORLD_NAME.toString(), worldname));
		
		WorldCreator c = new WorldCreator(worldname);
		
		if (seed != 0) c.seed(seed);
		c.environment(environment);
		c.type(type);
		c.generateStructures(generateStructures);
		c.createWorld();
	}
	
	public void deleteWorld() {
		
		Logger logger = UHC.getInstance().getLogger();
		
		logger.info(Lang.WORLD_DELETING.get().replace(LangTag.WORLD_NAME.toString(), worldname));
		
		if (Bukkit.getWorld(worldname) == null) {
			logger.info(Lang.ERROR_WORLD_NOT_FOUND.get().replace(LangTag.WORLD_NAME.toString(), worldname));
			return;
		}
		
		World world = Bukkit.getWorld(worldname);
		File path = world.getWorldFolder();
		
		world.getPlayers().forEach(p-> p.kickPlayer(Lang.CAUSE_WORLD_DELETING.get()));
		
		for(Chunk c : world.getLoadedChunks())	{
			c.unload(false, false);
		}
		
		Bukkit.getServer().unloadWorld(world, false);
		
		if (deleteFile(path)) logger.info(Lang.WORLD_DELETING_DONE.get().replace(LangTag.WORLD_NAME.toString(), worldname));
		else logger.info(Lang.ERROR_WORLD_DELETING_FAIL.get().replace(LangTag.WORLD_NAME.toString(), worldname));
	}
	
	private Boolean deleteFile(File path) {
		
		if(path.exists()) {
			
			File files[] = path.listFiles();
			
			for(int i=0; i<files.length; i++) {
				
				if(files[i].isDirectory()) {
					
					deleteFile(files[i]);
				} else {
					
					files[i].delete();
				}
			}
		}
		
		return path.delete();
	}

	public void copyWorld(File target) {
		Logger logger = UHC.getInstance().getLogger();
		
		//INFO
		
		if (Bukkit.getWorld(worldname) == null) {
			logger.info(Lang.ERROR_WORLD_NOT_FOUND.get().replace(LangTag.WORLD_NAME.toString(), worldname));
			return;
		}
		
		copyFile(Bukkit.getWorld(worldname).getWorldFolder(), target);
	}
	
	private void copyFile(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyFile(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
}
