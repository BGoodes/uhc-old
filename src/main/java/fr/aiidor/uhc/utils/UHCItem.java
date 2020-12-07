package fr.aiidor.uhc.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.team.UHCTeam;

public class UHCItem {
	
	public static ItemStack config_chest = new ItemBuilder(Material.ENDER_CHEST, Lang.INV_CONFIGURATION.get()).getItem();
	private static ItemStack team_selecter = new ItemBuilder(Material.BANNER, (byte) 15, Lang.INV_TEAM_CHOOSE.get()).getItem();
	public static ItemStack golden_head = new ItemBuilder(Material.GOLDEN_APPLE, Lang.GOLDEN_HEAD_NAME.get()).getItem();
	
	public static ItemStack getTeamSelecter(UHCTeam t) {
		return new ItemBuilder(Material.BANNER, t.getColor().getBannerColor(), Lang.INV_TEAM_CHOOSE.get()).getItem();
	}
	
	public static ItemStack getTeamSelecter() {
		return team_selecter;
	}
	
	public static Material getFromID(String id) {
		id = id.toLowerCase();
		
		if (!id.contains("apple") && !id.contains("carrot")) id = id.replaceAll("golden", "gold");
		return Material.matchMaterial(id.replace("minecraft:", "").replaceAll("wooden", "wood").replaceAll("shovel", "spade"));
	}
}
