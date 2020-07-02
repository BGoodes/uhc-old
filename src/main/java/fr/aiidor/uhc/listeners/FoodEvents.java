package fr.aiidor.uhc.listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.game.UHCPlayer;

public class FoodEvents implements Listener {
	
	@EventHandler
	public void foodChangeLevel(FoodLevelChangeEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		if (!game.isHere(e.getEntity().getUniqueId())) return;
		if (!game.isStart()) {
			e.setCancelled(true);
		}
		
		
	}
	
	@EventHandler
	public void itemConsumeEvent(PlayerItemConsumeEvent e) {
		
		GameManager gm = UHC.getInstance().getGameManager();
		if (!gm.hasGame()) return;

		Game game = gm.getGame();
		
		Player player = e.getPlayer();
		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
				
		if (!game.isHere(player.getUniqueId())) return;
		
		ItemStack hand = player.getItemInHand();
		
		if (e.getItem().getType() == Material.GOLDEN_APPLE && game.getSettings().uhc_apple) {
			
			e.setCancelled(true);
			
			if (hand.getAmount() - 1 <= 0) hand.setType(Material.AIR);
			else hand.setAmount(hand.getAmount() - 1);
			
			player.setItemInHand(hand);
			
			player.setSaturation((float) (player.getSaturation() + game.getSettings().gapple_saturation));
			player.setFoodLevel(player.getFoodLevel() + game.getSettings().gapple_food_level);
			
			player.getWorld().playSound(player.getLocation(), Sound.BURP, 0.4f, 1f);
			
			if (e.getItem().getDurability() == 1) {
				
				//NAPPLE
				if (!game.getSettings().notch_apple) {
					//MESSAGE
					return;
				}
				
				for (PotionEffect pe : game.getSettings().napple_effects) {
					p.addPotionEffect(pe);
				}
				
				p.setAbso(game.getSettings().napple_abso, 2400);
				
			} else {
				
				//GAPPLE
				if (!game.getSettings().golden_apple) {
					return;
				}
				
				for (PotionEffect pe : game.getSettings().gapple_effects) {
					p.addPotionEffect(pe);
				}
				
				//ABSO
				p.setAbso(game.getSettings().gapple_abso, 2400);
			}
		}
	}
}
