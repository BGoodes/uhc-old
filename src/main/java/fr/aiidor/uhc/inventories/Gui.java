package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public abstract class Gui {
	
	public abstract void onClick(GuiClickEvent event);
	
	public String getTitle() {
		return getInventory().getTitle();
	}
	
	public abstract Inventory getInventory();
	
	public void playClickSound(Player player) {
		player.playSound(player.getLocation(), Sound.CLICK, 0.7f, 1f);
	}
	
	public Boolean isSameInventory(Inventory inv) {
		return inv.getName().equals(getTitle());
	}
	
	public ItemStack getBackIcon() {
		return new ItemBuilder(Material.BARRIER, Lang.INV_BACK.get()).setGliding().getItem();
	}
	
	public ItemStack getExitIcon() {
		return new ItemBuilder(Material.BARRIER, Lang.INV_EXIT.get()).setGliding().getItem();
	}
	
	public ItemStack getGlass(Byte data) {
		return new ItemBuilder(Material.STAINED_GLASS_PANE, data, " ").getItem();
	}
	
	public ItemStack getConfigItem(String display_name, Boolean state) {
		return getConfigItem(null, null, display_name, state);
	}
	
	public ItemStack getConfigItem(String display_name, Integer i) {
		return getConfigItem(null, null, display_name, i != 0);
	}
	
	public ItemStack getConfigItem(Material material, String display_name, Integer i) {
		return getConfigItem(material, null, display_name, i != 0);
	}
	
	public ItemStack getConfigItem(Material material, String display_name, Boolean state) {
		return getConfigItem(material, null, display_name, state);
	}
	
	public ItemStack getConfigItem(Material material, Byte data, String display_name, Integer i) {
		return getConfigItem(material, data, display_name, i != 0);
	}
	
	public ItemStack getConfigItem(Material material, Byte data, String display_name, Boolean state) {
		ItemBuilder builder;
		
		if (display_name == null) display_name = (Lang.INV_STATE.get());
			
		if (material == null) {
			if (state) builder = new ItemBuilder(Material.INK_SACK, display_name, (byte) 10);
			else builder = new ItemBuilder(Material.INK_SACK, display_name, (byte) 8);
			
		} else {
			if (data != null) builder = new ItemBuilder(material, data, display_name);
			else builder = new ItemBuilder(material, display_name);
		}
		
		
		List<String> lore = new ArrayList<String>();
		
		if (state) lore.add(Lang.INV_ON.get());
		else lore.add(Lang.INV_OFF.get());

		
		builder.setLore(lore);
		
		return builder.getItem();
	}
	
	public void update() {
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory() != null) {
				if (isSameInventory(p.getOpenInventory().getTopInventory())) {
					p.openInventory(getInventory());
				}
			}
		}
	}
	
	public Inventory getInventory(UHCPlayer player) {
		return getInventory();
	}
}
