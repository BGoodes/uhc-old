package fr.aiidor.uhc.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.tools.ItemBuilder;

public abstract class Gui {
	
	public abstract void onClick(GuiClickEvent event);
	public abstract Boolean titleIsDynamic();
	
	public String getTitle() {
		return getInventory().getTitle();
	}
	
	public abstract Inventory getInventory();
	
	public void playClickSound(Player player) {
		player.playSound(player.getLocation(), Sound.CLICK, 0.7f, 1f);
	}
		
	public Boolean isSameInventory(Inventory inv) {
		
		if ((!titleIsDynamic() && inv.getName().equals(getTitle())) || 
		   (titleIsDynamic() && inv.getName().contains(getTitle()))) return true;
		
		else return false;
		
	}
	
	public ItemStack getBackIcon() {
		return new ItemBuilder(Material.BARRIER, Lang.INV_BACK.get()).setGliding().getItem();
	}
	
	public ItemStack getExitIcon() {
		return new ItemBuilder(Material.BARRIER, Lang.INV_EXIT.get()).setGliding().getItem();
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
