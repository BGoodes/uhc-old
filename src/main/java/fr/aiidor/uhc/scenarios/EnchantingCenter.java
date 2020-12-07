package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

public class EnchantingCenter extends Scenario {
	
	public Location table_loc;
	public EnchantingCenter(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "enchanting-center";
	}

	@Override
	public Material getIcon() {
		return Material.ENCHANTMENT_TABLE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.ENCHANTMENT);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		super.changeStateEvent(e);
		
		if (!e.getGame().isWaiting()) {
			e.setCancelled(true);
			
			if (e.getPlayer() != null) {
				e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
				e.getPlayer().closeInventory();
			}
		}
	}

	
	public void spawnEnchantTable(Game game, World world) {
		Vector center = game.getSettings().map_center;
		
		table_loc = new Location(world, center.getX(), world.getMaxHeight(), center.getZ());
		
		while (table_loc.getY() > 0) {
			Block b = table_loc.getBlock();
			if (b.getType() == Material.AIR && b.getRelative(BlockFace.DOWN).getType() != Material.AIR) {
				b.setType(Material.ENCHANTMENT_TABLE);
				return;
			}
			else table_loc.subtract(0, 1, 0);
		}
			
		game.log(Lang.ENCHANT_TABLE_GENERATION_ERROR.get());
		game.getSettings().setActivated(this, false);
	}
}
