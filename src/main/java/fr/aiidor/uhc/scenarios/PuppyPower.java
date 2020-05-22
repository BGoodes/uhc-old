package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;

public class PuppyPower extends Scenario {
	
	private List<ItemStack> items;
	
	@SuppressWarnings("deprecation")
	public PuppyPower(ScenariosManager manager) {
		super(manager);
		
		items = new ArrayList<ItemStack>();
		
		items.add(new ItemStack(Material.MONSTER_EGG, 64, EntityType.WOLF.getTypeId()));
		items.add(new ItemStack(Material.BONE, 64));
		items.add(new ItemStack(Material.ROTTEN_FLESH, 64));
	}
	
	@Override
	public String getID() {
		return "puppy-power";
	}

	@Override
	public Material getIcon() {
		return Material.BONE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.FUN);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (e.getGame().isStart()) {
			
			e.getPlayer().closeInventory();
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
			e.setCancelled(true);
		}
	}
	
	public void GiveItems(UHCPlayer player) {
		for (ItemStack item : items) {
			player.giveItem(item);
		}
	}
}
