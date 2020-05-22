package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import fr.aiidor.uhc.enums.Category;

public class Bookception extends Scenario {
	
	public Bookception(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "bookception";
	}

	@Override
	public Material getIcon() {
		return Material.ENCHANTED_BOOK;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.ENCHANTMENT);
	}
	
	public ItemStack getRandomBook(String playerName) {
		
		ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
		Random r = new Random();
			
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		
		for (Integer i = 0; i <= r.nextInt(3); i++) {
			Enchantment enchant = Enchantment.values()[r.nextInt(Enchantment.values().length)];
			
			Integer level = r.nextInt(enchant.getMaxLevel()) + 1;
			meta.addStoredEnchant(enchant, level, true);
		}
		
		meta.setDisplayName("§d§o" + playerName);
		item.setItemMeta(meta);
		
		return item;
	}
}
