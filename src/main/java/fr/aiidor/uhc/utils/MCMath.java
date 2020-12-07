package fr.aiidor.uhc.utils;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class MCMath {
	
	private static BlockFace faces[] = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
	
	public static BlockFace[] getBlockFaces() {
		return faces;
	}
			
	public static double distance2D(Location l1, Location l2) {
		return distance2D(l1.toVector(), l2.toVector());
	}
	
	public static double distance2D(Vector v1, Vector v2) {
		return Math.sqrt(Math.pow(v2.getX() - v1.getX(), 2) + Math.pow(v2.getZ() - v1.getZ(), 2));
	}
	
    public static Vector getDirection(Location source, Location target) {
        double dX = source.getX() - target.getX();
        double dY = source.getY() - target.getY();
        double dZ = source.getZ() - target.getZ();
        
        double yaw = Math.atan2(dZ, dX);
        double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
        
        double x = Math.sin(pitch) * Math.cos(yaw);
        double y = Math.sin(pitch) * Math.sin(yaw);
        double z = Math.cos(pitch);
        
        return new Vector(x, z, y);
    }
}
