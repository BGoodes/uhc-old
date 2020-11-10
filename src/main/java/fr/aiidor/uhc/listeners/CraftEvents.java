package fr.aiidor.uhc.listeners;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
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
import fr.aiidor.uhc.tools.UHCItem;

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
			
			if (inv.getResult().equals(UHCItem.golden_head) && (!game.getSettings().golden_head || !game.getSettings().uhc_apple)) {
				inv.setResult(new ItemStack(Material.AIR));
				return;
			}
			
			Material result = inv.getResult().getType();
			
			if (result == Material.ENCHANTMENT_TABLE) {
				
				if (ScenariosManager.ENCHANTED_DEATH.isActivated() || ScenariosManager.ENCHANTING_CENTER.isActivated()) {
					inv.setResult(new ItemStack(Material.AIR));
					return;
				}
			}
			
			
			if (ScenariosManager.NO_WOODEN_TOOL.isActivated()) {
				inv.setResult(ScenariosManager.NO_WOODEN_TOOL.changeItem(inv.getResult()));
			}
			
			if (result.name().endsWith("_SPADE") || result.name().endsWith("_PICKAXE") || result.name().endsWith("_AXE")) {
				
				if (ScenariosManager.HASTEY_BABIES.isActivated()) {
					inv.setResult(ScenariosManager.HASTEY_BABIES.setEnchant(inv.getResult()));
				}
				
				if (ScenariosManager.HASTEY_BOYS.isActivated()) {
					inv.setResult(ScenariosManager.HASTEY_BOYS.setEnchants(inv.getResult()));
				}
			}
			
			if (ScenariosManager.INVENTORS.isActivated() && ScenariosManager.INVENTORS.isInvented(inv.getResult().getType())) {
				inv.setResult(ScenariosManager.INVENTORS.property(inv.getResult()));
			}
		}
	}
	
	@EventHandler
	public void Craft(CraftItemEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		HumanEntity player = e.getView().getPlayer();
		
		if (!game.isHere(player.getUniqueId())) return;
		
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
		
		if (ScenariosManager.INVENTORS.isActivated() && game.isStart()) {
			if (!ScenariosManager.INVENTORS.craft(p, e.getCurrentItem(), game)) {
				e.setCancelled(true);
				return;
			}
			
			//if (p.isConnected()) p.getPlayer().updateInventory();
		}
	}
}
