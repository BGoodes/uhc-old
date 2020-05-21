package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fr.aiidor.uhc.enums.Category;

public class Timber extends Scenario {
	
	public Timber(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "timber";
	}

	@Override
	public Material getIcon() {
		return Material.LOG;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	
	private BlockFace[] faces = {BlockFace.UP, BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST};
	
	public void destroy(Block base) {
		if (base.getType() == Material.LOG || base.getType() == Material.LOG_2) {
			breakBlock(base, 0);
		}
	}
	
	private void breakBlock(Block b, Integer limit) {
		Material mat = b.getType();
		
		if (limit != 0) {
			b.getWorld().playSound(b.getLocation(), Sound.DIG_WOOD, 0.5f, 1f);
			b.breakNaturally();
		}

		if (limit == 30) return;
		
		for (BlockFace face : faces) {
			if (b.getRelative(face).getType() == mat) {
				breakBlock(b.getRelative(face), limit + 1);
			}
		}
	}
	
		
}
