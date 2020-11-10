package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;

import fr.aiidor.uhc.enums.Category;

public class FenceHead extends Scenario {
	
	public FenceHead(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "fence-head";
	}

	@Override
	public Material getIcon() {
		return Material.SKULL_ITEM;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	@SuppressWarnings("deprecation")
	public void generate(Location loc, String name, Boolean up) {
		
		if (up) loc.add(0, 1, 0);
		
		Block fence = loc.getBlock();
		Block head =  fence.getRelative(BlockFace.UP);
		
		if (fence.getType() == Material.AIR && head.getType() == Material.AIR) {
			
			fence.setType(Material.FENCE);
			head.setType(Material.SKULL);
			
			BlockFace face = BlockFace.values()[new Random().nextInt(BlockFace.values().length)];
			while (face == BlockFace.UP || face == BlockFace.DOWN || face == BlockFace.SELF) {
				face = BlockFace.values()[new Random().nextInt(BlockFace.values().length)];
			}
			
			head.setData((byte) 1);
			
			if (head.getState() instanceof Skull) {
				Skull skull = (Skull) head.getState();
				skull.setSkullType(SkullType.PLAYER);
				skull.setOwner(name);
				
				skull.setRotation(face);
				
				skull.update();
			}
		}
	}
}
