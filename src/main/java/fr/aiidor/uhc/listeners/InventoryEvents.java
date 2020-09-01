package fr.aiidor.uhc.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.ArmorEquipEvent;

public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onPlayerClickEvent(InventoryClickEvent e) {
		
		if (!UHC.getInstance().getGameManager().hasGame()) return;
		
		if (!(e.getWhoClicked() instanceof Player)) return;
		if (e.getCurrentItem() == null) return;

		Player player = (Player) e.getWhoClicked();	
		ItemStack clicked = e.getCurrentItem();
		Game game = UHC.getInstance().getGameManager().getGame();
		Inventory inv = e.getInventory();
		
		if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
			
			String display_name = clicked.getItemMeta().getDisplayName();
				
			if (display_name.equals(Lang.INV_LOCKED.get()) && player.getGameMode() != GameMode.CREATIVE) {
				e.setCancelled(true);
				return;
			}
		}
		
		if (UHC.getInstance().getGuiManager().onPlayerClickEvent(inv, player, clicked, game, e)) return;
		game.getSettings().enchantLimiter(clicked);
	}
	
	public void onArmorEquip(ArmorEquipEvent e) {
		ItemStack item = e.getItem();
		
		if (item.hasItemMeta()) {
			e.getGame().getSettings().enchantLimiter(item);
		}

	}
}
