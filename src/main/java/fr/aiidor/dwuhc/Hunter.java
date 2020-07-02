package fr.aiidor.dwuhc;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.tools.ItemBuilder;

public class Hunter extends DWRole {
	
	public Hunter(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.HUNTER;
	}
	
	@Override
	public ItemStack[] getStartItems() {
		return new ItemStack[] {new ItemBuilder(Material.ENCHANTED_BOOK).addStoredEnchant(Enchantment.ARROW_DAMAGE, 4).getItem()};
	}
}
