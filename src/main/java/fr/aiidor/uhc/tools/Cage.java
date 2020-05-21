package fr.aiidor.uhc.tools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Cage {
	
	private Location loc;
	private Integer size;
	private Integer Height;
	private Material ground;
	private Material wall;
	
	public Cage(Location loc, Integer size, Integer Height, Material ground, Material wall) {
		this.loc = loc;
		this.size = size;
		this.Height = Height;
		this.ground = ground;
		this.wall = wall;
	}
	
	public void create() {
		generate(loc, size, Height, ground, wall);
	}
	
	public void destroy() {
		generate(loc, size, Height, Material.AIR, Material.AIR);
	}
	
	@SuppressWarnings("deprecation")
	public void setGround(Material mat, Byte block_data) {
		for (int x = loc.getBlockX() - size; x <= loc.getBlockX() + size; x++) {
			for (int z = loc.getBlockZ() - size; z <= loc.getBlockZ() + size; z++) {
				
				Block b = loc.getWorld().getBlockAt(x, loc.getBlockY() , z);
				b.setType(mat);
				if (block_data != null) b.setData(block_data);
			}
		}
	}
	
	public void setWall(Material mat, Byte block_data) {
		Location l1 = new Location(loc.getWorld(), loc.getX() - size, loc.getY() + Height, loc.getZ() + size);
		Location l2 = new Location(loc.getWorld(), loc.getX() + size, loc.getY(), loc.getZ() - size);
		
		Structure.wallArrounndRegion(l1, l2, mat, block_data);
	}
	
	private void generate(Location loc, Integer size, Integer Height, Material ground, Material wall) {
		setGround(ground, null);
		setWall(wall, null);
	}
}
