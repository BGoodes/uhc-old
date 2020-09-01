package fr.aiidor.lguhc;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public abstract class LGRole {
	
	private LGCamp camp;
	private Boolean power;
	
	protected LGPlayer p;
	
	public LGRole(LGPlayer player) {
		this.camp = getRoleType().getBaseCamp();
		this.p = player;
		this.power = true;
	}
	
	public String getName() {
		return getRoleType().getName();
	}
	
	public abstract LGRoleType getRoleType();
	
	public LGCamp getCamp() {
		return camp;
	}
	
	public void setCamp(LGCamp camp) {
		
		if (this.camp != camp) {
			//LGS MSG
			
		}
		
		this.camp = camp;
	}
	
	public ItemStack[] getStartItems() {
		return null;
	}
	
	public Boolean hasStartItems() {
		return getStartItems() != null;
	}
	
	public String getStartItemsLore() {
		return null;
	}
	
	public List<PotionEffect> getPotionEffects() {
		return null;
	}
	
	public Boolean hasPotionEffects() {
		return getPotionEffects() != null;
	}
	
	public List<PotionEffect> getNightPotionEffects() {
		return null;
	}
	
	public Boolean hasNightPotionEffects() {
		return getNightPotionEffects() != null;
	}
	
	public List<PotionEffect> getDayPotionEffects() {
		return null;
	}
	
	public Boolean hasDayPotionEffects() {
		return getDayPotionEffects() != null;
	}
	
	public void setPower(Boolean power) {
		this.power = power;
	}
	
	public Boolean hasPower() {
		return power;
	}

	public String getLore() {
		
		String message = "";
		
		return message;
	}
	
	public String getCompleteLore() {
		String lore = getLore();
		
		return lore;
	}
	
	public void stop() {}
}
