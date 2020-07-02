package fr.aiidor.uhc.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;

public class CraftEvents implements Listener {
	
	@EventHandler
	public void ChangeCraft(PrepareItemCraftEvent e) {
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.isHere(e.getView().getPlayer().getUniqueId())) return;
		
		if (e.getInventory() instanceof CraftingInventory) {
			CraftingInventory inv = e.getInventory();
			
			if (inv.getResult() == null) return;
			Material result = inv.getResult().getType();
			
			if (result == Material.ENCHANTMENT_TABLE) {
				
				if (ScenariosManager.ENCHANTED_DEATH.isActivated()) {
					inv.setResult(new ItemStack(Material.AIR));
					return;
				}
			}
			
			if (result.name().endsWith("_SPADE") || result.name().endsWith("_PICKAXE") || result.name().endsWith("_AXE")) {
				
				if (ScenariosManager.HASTEY_BABIES.isActivated()) {
					inv.setResult(ScenariosManager.HASTEY_BABIES.setEnchant(inv.getResult()));
				}
				
				if (ScenariosManager.HASTEY_BOYS.isActivated()) {
					inv.setResult(ScenariosManager.HASTEY_BOYS.setEnchants(inv.getResult()));
				}
			}
		}
	}
	
	@EventHandler
	public void ChangeCraft(CraftItemEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.isHere(e.getView().getPlayer().getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(e.getView().getPlayer().getUniqueId());
		
		if (ScenariosManager.INVENTORS.isActivated() && !game.isStart()) {
			ScenariosManager.INVENTORS.hasInvented(p, e.getCurrentItem(), game);
		}
	}
}