package fr.aiidor.uhc.tools;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;

public class Teleportation {

	private final UHCPlayer player;
	private World world;
	private Location tp;
	
	public Teleportation(UHCPlayer player) {
		this(player, null);
	}
	
	public Teleportation(UHCPlayer player, Location tp) {
		this.player = player;
		this.tp = tp;
		
		GameManager gm = UHC.getInstance().getGameManager();
		
		if (gm.hasGame()) {
			this.world = gm.getGame().getMainWorld().getMainWorld();
		}
	}
	
	public UHCPlayer getPlayer() {
		return player;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}

	public Teleportation randomValue(Integer size, Integer top) {
        this.tp = getRandomLocation(world, size, top);
        return this;
	}
	
	public Teleportation randomValue(Integer size) {
        this.tp = getRandomLocation(world, size);
        return this;
	}
	
	public Teleportation revive(Integer size) {
		
		if (size >= world.getWorldBorder().getSize() / 2) size = ((int) world.getWorldBorder().getSize() / 2) - 10;
        this.tp = getRandomLocation(world, size);
        return this;
	}
	
	public Boolean teleport() {
		
		if (!player.isConnected()) return false;
		if (tp == null) return false;
		
		Player p = player.getPlayer();
		p.teleport(tp);
		
		return true;
	}
	
	public static Location getRandomLocation(World world, Integer size) {
        Random random = new Random();
        
        int x = 0;
        int z = 0;
        
        if (size > 0) {
    	    int rangeMax = (int) size;
    	    int rangeMin = (int) -size;
    	   
    	    x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
    	    z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        }
	    
	    Chunk c = world.getChunkAt(x, z);
	    if (c.isLoaded()) c.load(true);
	    
        int y = world.getHighestBlockYAt(x, z);
        
        Location location = new Location(world, x, y, z);
        
    	if (location.getY() <= 40) {
    		location.setY(149);
    	}
        
        return location.add(0, 10, 0);
	}
	
	public static Location getRandomLocation(World world, Integer size, Integer top) {
        Random random = new Random();
        
        int x = 0;
        int z = 0;
        
        if (size > 0) {
    	    int rangeMax = (int) size;
    	    int rangeMin = (int) -size;
    	   
    	    x = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
    	    z = random.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        }
        
        int y = top;
        
        return new Location(world, x, y, z);
	}
}
