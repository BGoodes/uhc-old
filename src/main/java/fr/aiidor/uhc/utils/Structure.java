package fr.aiidor.uhc.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Structure {
	
    @SuppressWarnings("deprecation")
	public static void wallArrounndRegion(Location l1, Location l2, Material mat, Byte data, Boolean replace) {
    	
        int minX = Math.min(l1.getBlockX(), l2.getBlockX());
        int maxX = Math.max(l1.getBlockX(), l2.getBlockX());
        int minY = Math.min(l1.getBlockY(), l2.getBlockY());
        int maxY = Math.max(l1.getBlockY(), l2.getBlockY());
        int minZ = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int maxZ = Math.max(l1.getBlockZ(), l2.getBlockZ());
             
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), x, y, minZ).getBlock();
                
				if (replace || b.getType() == Material.AIR) {
					b.setType(mat);
					if (data != null) b.setData(data);
				}
            }
        }
     
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), x, y, maxZ).getBlock();
                
				if (replace || b.getType() == Material.AIR) {
					b.setType(mat);
					if (data != null) b.setData(data);
				}
            }
        }
     
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), minX, y, z).getBlock();
                
				if (replace || b.getType() == Material.AIR) {
					b.setType(mat);
					if (data != null) b.setData(data);
				}
            }
        }
     
        for (int z = minZ; z <= maxZ; z++) {
            for (int y = minY; y <= maxY; y++) {
                Block b = new Location(l1.getWorld(), maxX, y, z).getBlock();
                
				if (replace || b.getType() == Material.AIR) {
					b.setType(mat);
					if (data != null) b.setData(data);
				}
            }
        }
    }
}
