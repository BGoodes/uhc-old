package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Bombers extends ItemScenario {
	
	public Bombers(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "bombers";
	}

	@Override
	public Material getIcon() {
		return Material.TNT;
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
		return Arrays.asList(new ItemBuilder(Material.FLINT_AND_STEEL).addEnchant(Enchantment.DURABILITY, 10).getItem());
	}
	
	public void addDrops(Player killer, List<ItemStack> drops) {
		
		ItemStack hand = null;
		if (killer != null) hand = killer.getItemInHand();
		
		Integer m = 2;
		
		if (hand != null && hand.hasItemMeta()) {
			m += hand.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
		}
		
		Integer n = new Random().nextInt(m + 1);
		if (n > 0) drops.add(new ItemStack(Material.TNT, n));
	}
}
