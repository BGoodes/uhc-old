package fr.aiidor.dwuhc;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.game.UHCPlayer;

public class DWplayer {

	private UHCPlayer player;
	private DWRole role;
	
	public Guardian guardian = null;
	
	public DWplayer(UHCPlayer player) {
		this.player = player;
	}
	
	public UHCPlayer getUHCPlayer() {
		return player;
	}
	
	public Boolean hasRole() {
		return role != null;
	}
	
	public DWRole getRole() {
		return role;
	}

	public DWCamp getCamp() {
		return role.getCamp();
	}
	
	public void setRole(DWRole role) {
		this.role = role;
	}
	
	public Boolean isCorrupted() {
		return getCamp() == DWCamp.SECTARIANS || getCamp() == DWCamp.DEMONS;
	}
	
	public Boolean isAlive() {
		return getUHCPlayer().isAlive();
	}
	
	public Boolean isProtected() {
		return guardian != null;
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
		if (isProtected()) list.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 2*20, 0, false, false));
			
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
	
	public void setRole(DWRoleType roleType, DWCamp camp) {
		DWRole role = roleType.create(this, camp);
		setRole(role);
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
