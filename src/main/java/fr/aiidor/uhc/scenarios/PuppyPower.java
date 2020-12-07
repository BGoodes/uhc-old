package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;

public class PuppyPower extends ItemScenario {
	
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
		return Arrays.asList(Category.FUN);
	}

	@Override
	public GiveTime giveTime() {
		return GiveTime.LOADING;
	}

	@Override
	public List<ItemStack> getItems() {
		return items;
	}
	
	
}
