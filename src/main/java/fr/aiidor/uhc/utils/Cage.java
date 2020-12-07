package fr.aiidor.uhc.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Cage {
	
	private Location loc;
	private Integer size;
	private Integer Height;
	private Material floor;
	private Material roof;
	private Material wall;
	private Integer thickness;
	
	private Boolean replace;
	
	public Cage(Location loc, Integer size, Integer Height, Material floor, Material wall) {
		this(loc, size, Height, 1, floor, null, wall, true);
	}
	
	public Cage(Location loc, Integer size, Integer Height, Integer thickness, Material floor, Material roof, Material wall, Boolean replace) {
		this.loc = loc;
		this.size = size;
		this.Height = Height;
		this.thickness = thickness;
		this.floor = floor;
		this.roof = roof;
		this.wall = wall;
		this.replace = replace;
	}
	
	public void create() {
		for (int i = 0; i < thickness; i++) {
			generate(loc.clone().subtract(0, i, 0), size + i, Height + 2*i, floor, roof, wall, replace);
		}
	}
	
	public void destroy() {
		if (roof != null) {
			for (int i = 0; i < thickness; i++) {
				generate(loc.clone().subtract(0, i, 0), size + i, Height + 2*i, Material.AIR, roof, Material.AIR, true);
			}
		} else {
			for (int i = 0; i < thickness; i++) {
				generate(loc.clone().subtract(0, i, 0), size + i, Height + 2*i, Material.AIR, null, Material.AIR, true);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setFloor(Material mat, Location loc, Integer size, Byte block_data, Boolean replace) {
		
		for (int x = loc.getBlockX() - size; x <= loc.getBlockX() + size; x++) {
			for (int z = loc.getBlockZ() - size; z <= loc.getBlockZ() + size; z++) {
					
				Block b = loc.getWorld().getBlockAt(x, loc.getBlockY(), z);
					
				if (replace || b.getType() == Material.AIR) {
					b.setType(mat);
					if (block_data != null) b.setData(block_data);
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void setRoof(Material mat, Location loc, Integer size, Integer Height, Byte block_data, Boolean replace) {
		
		for (int x = loc.getBlockX() - size; x <= loc.getBlockX() + size; x++) {
			
			for (int z = loc.getBlockZ() - size; z <= loc.getBlockZ() + size; z++) {
					
				Block b = loc.getWorld().getBlockAt(x, loc.getBlockY() + Height, z);
						
				if (replace || b.getType() == Material.AIR) {
					b.setType(mat);
					if (block_data != null) b.setData(block_data);
				}
			}
		}
	}
	
	public void setWall(Material mat, Location loc, Integer size, Integer Height, Byte block_data, Boolean replace) {
		Location l1 = new Location(loc.getWorld(), loc.getX() - size, loc.getY() + Height, loc.getZ() + size);
		Location l2 = new Location(loc.getWorld(), loc.getX() + size, loc.getY() + 1, loc.getZ() - size);
			
		Structure.wallArrounndRegion(l1, l2, mat, block_data, replace);
	}
	
	private void generate(Location loc, Integer size, Integer Height, Material floor, Material roof, Material wall, Boolean replace) {
		setFloor(floor, loc, size, null, replace);
		setWall(wall, loc, size, Height, null, replace);
		if (roof != null) setRoof(roof, loc, size, Height, null, replace);
	}
}
