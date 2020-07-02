package fr.aiidor.dwuhc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.tools.ItemBuilder;

public enum VillagerJob {
	
	SMITH, MINOR, BOOKSELLER, GUARD;
	
	private final String name;
	private final String lore;
	
	private ItemStack[] items = null;
	private List<PotionEffect> effects = null;
	
	private VillagerJob() {
		
		this.name = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/roles/villager-type/" + name()).get("name");
		this.lore = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/roles/villager-type/" + name()).get("lore");
		
		if (name().equalsIgnoreCase("SMITH")) {
			
			items = new ItemStack[] {
					new ItemBuilder(Material.ANVIL).getItem(),
					new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).getItem(),
					new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchant(Enchantment.DAMAGE_ALL, 2).getItem()
				};
		}
		
		if (name().equalsIgnoreCase("MINOR")) {
			items = new ItemStack[] {new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 2).getItem()};
			effects = Arrays.asList(new PotionEffect(PotionEffectType.FAST_DIGGING, 2*20, 0, false, false));
		}
		
		if (name().equalsIgnoreCase("BOOKSELLER")) {
			items = new ItemStack[] {new ItemBuilder(Material.BOOK, 8).getItem(), new ItemBuilder(Material.EXP_BOTTLE, 12).getItem()};
		}
		
		if (name().equalsIgnoreCase("GUARD")) {
			items = new ItemStack[] {new ItemBuilder(Material.DIAMOND_HELMET, 1).getItem()};
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getLore() {
		return lore;
	}
	
	public ItemStack[] getStartItems() {
		return items;
	}
	
	public List<PotionEffect> getEffects() {
		return effects;
	}
}
