package fr.aiidor.uhc.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class UHCWorld {
	
	private World main_world;
	
	private World overworld;
	private World nether;
	private World end;
	
	public UHCWorld(World overworld, Boolean generate) {
		
		World end = null;
		World nether = null;
		
		if (nether == null && Bukkit.getWorld(overworld.getName() + "_nether") != null) {
			nether = Bukkit.getWorld(overworld.getName() + "_nether");
		}
		
		if (end == null && Bukkit.getWorld(overworld.getName() + "_the_end") != null) {
			end = Bukkit.getWorld(overworld.getName() + "_the_end");
		}
		
		this.main_world = overworld;
		this.overworld = overworld;
		this.nether = nether;
		this.end = end;
	}
	
	public UHCWorld(World main_world, World overworld, World nether, World end) {
		
		this.main_world = main_world;
		this.overworld = overworld;
		this.nether = nether;
		this.end = end;
	}
	
	public Boolean hasOverWorld() {
		return overworld != null;
	}
	
	public Boolean hasNether() {
		return nether != null;
	}
	
	public Boolean hasEnd() {
		return end != null;
	}
	
	public World getOverWorld() {
		return overworld;
	}
	
	public World getNether() {
		return nether;
	}
	
	public World getEnd() {
		return end;
	}
	
	public List<World> getAll() {
		List<World> wl = new ArrayList<World>();
		if (hasOverWorld()) {
			wl.add(getOverWorld());
		}
		
		if (hasNether()) {
			wl.add(getNether());
		}
		
		if (hasEnd()) {
			wl.add(getEnd());
		}
		return wl;
	}
	
	public World getMainWorld() {
		return main_world;
	}
	
	public void setMainWorld(World world) {
		this.main_world = world;
	}
}
