package fr.aiidor.uhc.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DurabilityManager {
	
	private ItemStack item;
	
	public DurabilityManager(ItemStack item) {
		this.item = item;
	}
	
	public Boolean canDamage() {
		return item != null && item.getType() != Material.AIR && item.hasItemMeta();
	}
	
	public void damage(short damage) {
		item.setDurability((short) (item.getDurability() - damage));
	}
	
	public ItemStack getItem() {
		return item;
	}
}
