package fr.aiidor.uhc.tools;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;

public class UHCItem {
	
	private static ItemStack config_chest = new ItemBuilder(Material.ENDER_CHEST, Lang.INV_CONFIGURATION.get()).getItem();
	private static ItemStack team_selecter = new ItemBuilder(Material.BANNER, (byte) 15, Lang.INV_TEAM_CHOOSE.get()).getItem();
	
	public static ItemStack getConfigChest() {
		return config_chest;
	}
	
	public static ItemStack getTeamSelecter() {
		return team_selecter;
	}
}
