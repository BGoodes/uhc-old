package fr.aiidor.dwuhc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.tools.ItemBuilder;

public class MounteBank extends DWRole {
	
	public MounteBank(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.MOUNTEBANK;
	}
	
	@Override
	public List<PotionEffect> getPotionEffects() {
		return Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 2*20, 0, false, false));
	}
	
	@Override
	public ItemStack[] getStartItems() {
		return new ItemStack[] {new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchant(Enchantment.PROTECTION_FALL, 4).getItem(), new ItemBuilder(Material.ENDER_PEARL, 3).getItem()};
	}
}
