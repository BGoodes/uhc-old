package fr.aiidor.uhc.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class ItemBuilder {
	
	private final ItemStack item;
	
	public ItemBuilder(ItemStack item) {
		this.item = item;
	}
	
	public ItemBuilder(Material material) {
		item = new ItemStack(material);
	}
	
	public ItemBuilder(Material material, String displayName) {
		item = new ItemStack(material);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		
		item.setItemMeta(meta);
	}
	
	public ItemBuilder(Material material, String displayName, byte data) {
		item = new ItemStack(material, 1, data);
		
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
	
	public ItemBuilder setBlinking() {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder addEnchant(Enchantment enchant, Integer level) {
		ItemMeta meta = item.getItemMeta();
		meta.addEnchant(enchant, level, true);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder addEnchants(HashMap<Enchantment, Integer> enchants) {
		ItemMeta meta = item.getItemMeta();
		for (Entry<Enchantment, Integer> e : enchants.entrySet()) meta.addEnchant(e.getKey(), e.getValue(), true);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder removeEnchant(Enchantment enchant) {
		ItemMeta meta = item.getItemMeta();
		meta.removeEnchant(enchant);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder addStoredEnchant(Enchantment enchant, Integer level) {
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.addStoredEnchant(enchant, level, true);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder addStoredEnchants(HashMap<Enchantment, Integer> enchants) {
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		for (Entry<Enchantment, Integer> e : enchants.entrySet()) meta.addStoredEnchant(e.getKey(), e.getValue(), true);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemBuilder removeStoredEnchant(Enchantment enchant) {
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
		meta.removeStoredEnchant(enchant);
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
	
	public ItemBuilder setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		
		meta.setLore(lore);
		
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setLore(String lore) {
		return setLore(Arrays.asList(lore));
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
	
	public ItemBuilder setPotionType(PotionType type, Boolean splash) {
		Potion pot = new Potion(1);
		
		pot.setType(type);
		pot.setSplash(splash);
		pot.apply(item);
		return this;
	}		   
	
	public ItemBuilder addPotionEffect(PotionEffect pot, Boolean arg1) {
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.addCustomEffect(pot, arg1);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder removePotionEffect(PotionEffectType type) {
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.removeCustomEffect(type);
		item.setItemMeta(meta);
		return this;
	}
	
	public ItemBuilder setHeadOwner(String name) {
		
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(name);
		item.setItemMeta(meta);
		
		return this;
	}
	
	public ItemStack getItem() {
		return item;
	}
}
