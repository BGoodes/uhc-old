package fr.aiidor.lguhc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.uhc.game.UHCPlayer;

public class LGPlayer {

	private UHCPlayer player;
	private LGRole role;
	
	public LGPlayer(UHCPlayer player) {
		this.player = player;
	}
	
	public UHCPlayer getUHCPlayer() {
		return player;
	}
	
	public Boolean hasRole() {
		return role != null;
	}
	
	public LGRole getRole() {
		return role;
	}

	public LGCamp getCamp() {
		return role.getCamp();
	}
	
	public void setRole(LGRole role) {
		this.role = role;
	}
	
	public Boolean isAlive() {
		return getUHCPlayer().isAlive();
	}
	
	public Boolean hasPotionEffects() {
		return !getPotionEffects().isEmpty();
	}
	
	public Boolean hasDayPotionEffects() {
		return !getDayPotionEffects().isEmpty();
	}
	
	public Boolean hasNightPotionEffects() {
		return !getNightPotionEffects().isEmpty();
	}
	
	public List<PotionEffect> getPotionEffects() {
		List<PotionEffect> list = new ArrayList<PotionEffect>();
		
		if (role.hasPotionEffects()) list.addAll(role.getPotionEffects());
			
		return list;
	}
	
	public List<PotionEffect> getDayPotionEffects() {
		List<PotionEffect> list = new ArrayList<PotionEffect>();
		
		if (role.hasDayPotionEffects()) list.addAll(role.getDayPotionEffects());
		
		return list;
	}
	
	public List<PotionEffect> getNightPotionEffects() {
		List<PotionEffect> list = new ArrayList<PotionEffect>();
		
		if (role.hasNightPotionEffects()) list.addAll(role.getNightPotionEffects());
		
		return list;
	}
	
	public void announceRole() {
		
		UHCPlayer p = getUHCPlayer();
		
		if (p.isConnected()) {
			p.getPlayer().sendMessage(role.getCompleteLore());
			p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.LEVEL_UP, 0.5f, 1f);
		}
		
		if (role.hasStartItems()) {
			for (ItemStack i : role.getStartItems()) {
				getUHCPlayer().giveItem(i);
			}	
		}
	}
}
