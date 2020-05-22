package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.uhc.enums.Category;

public class HasteyBabies extends Scenario {

	public HasteyBabies(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "hastey-babies";
	}
	
	@Override
	public Material getIcon() {
		return Material.GOLD_PICKAXE;
	}
	
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	
	public ItemStack setEnchant(ItemStack tool) {
		ItemMeta toolM = tool.getItemMeta();
		toolM.addEnchant(Enchantment.DIG_SPEED, 1, true);
		tool.setItemMeta(toolM);
		return tool;
	}
}
