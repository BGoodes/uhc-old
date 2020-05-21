package fr.aiidor.uhc.tools;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullBuilder {
	
	private ItemStack skull;
	
	public SkullBuilder() {
		skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
	}
	
	public SkullBuilder(String displayName) {
		skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		setDisplayName(displayName);
	}
	
	public SkullBuilder(String displayName, String playerName) {
		skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
		setOwner(playerName);
		setDisplayName(displayName);
	}
	
	public SkullBuilder setDisplayName(String displayName) {
		
		ItemMeta meta = skull.getItemMeta();
		meta.setDisplayName(displayName);
		
		skull.setItemMeta(meta);
		return this;
	}
	
	public SkullBuilder setAmount(Integer amount) {
		skull.setAmount(amount);
		return this;
	}
	
	public SkullBuilder setOwner(String playerName) {
	    SkullMeta meta = (SkullMeta) skull.getItemMeta();
	     
		meta.setOwner(playerName);
	    skull.setItemMeta(meta);
	    return this;
	}
	
	public SkullBuilder setTrophy(String playerName) {
	    SkullMeta meta = (SkullMeta) skull.getItemMeta();
	     
		meta.setOwner(playerName);
		meta.setDisplayName("§6Tête de §c" + playerName);

	    meta.setLore(Arrays.asList(new String[] { "§bCette tête a été récupéré ", "§bsur un joueur §cmort." }));
	    skull.setItemMeta(meta);
	    return this;
	}
	
	public SkullBuilder addEnchant(Enchantment enchant, Integer level) {
		ItemMeta meta = skull.getItemMeta();
		
		meta.addEnchant(enchant, level, true);
		
		skull.setItemMeta(meta);
		return this;
	}
	
	public SkullBuilder setGliding() {
		ItemMeta meta = skull.getItemMeta();
		
		meta.addEnchant(Enchantment.ARROW_INFINITE, 0, true);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		skull.setItemMeta(meta);
		return this;
	}
	
	
	public SkullBuilder removeEnchant(Enchantment enchant) {
		ItemMeta meta = skull.getItemMeta();
		
		meta.removeEnchant(enchant);
		
		skull.setItemMeta(meta);
		return this;
	}
	
	public SkullBuilder setLore(List<String> lore) {
		ItemMeta meta = skull.getItemMeta();
		
		meta.setLore(lore);
		
		skull.setItemMeta(meta);
		return this;
	}
	
	public SkullBuilder addFlag(ItemFlag flag) {
		
		ItemMeta meta = skull.getItemMeta();
		meta.addItemFlags(flag);
		skull.setItemMeta(meta);
		return this;
	}
	
	public SkullBuilder removeFlag(ItemFlag flag) {
		
		ItemMeta meta = skull.getItemMeta();
		meta.removeItemFlags(flag);
		skull.setItemMeta(meta);
		return this;
	}
	
	public ItemStack getSkull() {
		return skull;
	}
}
