package fr.aiidor.uhc.tools;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private ItemStack item;
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material);
	}
	
	public ItemBuilder(Material material, String displayName) {
		item = new ItemStack(material);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		item.setItemMeta(meta);
	}
	
	public ItemBuilder(Material material, Integer amount) {
		item = new ItemStack(material, amount);
	}
	
	public ItemBuilder(Material material, byte data) {
		item = new ItemStack(material, 1, data);
	}
	
	public ItemBuilder(Material material, Integer amount, byte data) {
		item = new ItemStack(material, amount, data);
	}
	
	public ItemBuilder(Material material, byte data, String displayName) {
		item = new ItemStack(material, 1, data);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		item.setItemMeta(meta);
	}
	
	public ItemBuilder(Material material, Integer amount, String displayName) {
		item = new ItemStack(material, amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		item.setItemMeta(meta);
	}
	
	public ItemBuilder(Material material, Integer amount, byte data, String displayName) {
		item = new ItemStack(material, amount, data);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		item.setItemMeta(meta);
	}

	public ItemBuilder setDisplayName(String displayName) {
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setAmount(Integer amount) {
		item.setAmount(amount);
		return this;
	}
	
	public ItemBuilder setType(Material material) {
		item.setType(material);
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment enchant, Integer level) {
		ItemMeta meta = item.getItemMeta();
		
		meta.addEnchant(enchant, level, true);
		
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setGliding() {
		ItemMeta meta = item.getItemMeta();
		
		meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		item.setItemMeta(meta);
		return this;
	}
	
	
	public ItemBuilder removeEnchant(Enchantment enchant) {
		ItemMeta meta = item.getItemMeta();
		
		meta.removeEnchant(enchant);
		
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder addFlag(ItemFlag flag) {
		
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(flag);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder removeFlag(ItemFlag flag) {
		
		ItemMeta meta = item.getItemMeta();
		meta.removeItemFlags(flag);
		item.setItemMeta(meta);
		return this;
	}
	
	
	public ItemStack getItem() {
		return item;
	}
}
