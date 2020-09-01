package fr.aiidor.dwuhc;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.gamemodes.DevilWatches;

public abstract class DWRole {
	
	private DWCamp camp;
	private Boolean power;
	
	protected DWplayer p;
	
	public DWRole(DWplayer player, DWCamp camp) {
		this.camp = camp;
		this.p = player;
		this.power = true;
	}
	
	public String getName() {
		return getRoleType().getName();
	}
	
	public abstract DWRoleType getRoleType();
	
	public DWCamp getCamp() {
		return camp;
	}
	
	public void setCamp(DWCamp camp) {
		
		if (this.camp != camp) {
			//SECTARIANS MSG

			if (DevilWatches.isDevilWatches()) {
				DevilWatches dw = DevilWatches.getDevilWatches();
				if (camp == DWCamp.SECTARIANS) {
					
					for (DWplayer p : dw.getPlayingPlayers()) {
						if (p.hasRole() && p.getRole().hasSectariansList()) 
							p.getUHCPlayer().getPlayer().sendMessage(Lang.DW_NEW_SECTARIAN.get().replace(LangTag.PLAYER_NAME.toString(), p.getUHCPlayer().getName()));
					}
				}
			}
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
	
	public Boolean isSectaireMember() {
		return camp == DWCamp.SECTARIANS;
	}
	
	public Boolean hasSectariansList() {
		return false;
	}
	
	public Boolean isDemonMember() {
		return camp == DWCamp.DEMONS;
	}
	
	public Boolean hasDemonList() {
		return false;
	}
	
	public String getLore() {
		
		String message = Lang.DW_ANNOUNCE_ROLE.get();
		
		message = message.replace(LangTag.ROLE_CAMP_PREFIX.toString(), getRoleType().getBaseCamp().getPrefix() + "§o");
		message = message.replace(LangTag.ROLE_NAME.toString(), getName());
		message = message.replace(LangTag.DW_SECTARIAN.toString(), camp == DWCamp.SECTARIANS ? Lang.DW_SECTARIAN.get() : "");
		message = message.replace(LangTag.GOAL.toString(), camp.getGoal());
		message = message.replace(LangTag.POWER.toString(), getRoleType().getLore());
		
		return message;
	}
	
	public String getCompleteLore() {
		String lore = getLore();
		
		if (camp == DWCamp.SECTARIANS) lore = lore + "\n" + Lang.DW_SEAL.get();
		
		if (DevilWatches.isDevilWatches()) {
			DevilWatches dw = DevilWatches.getDevilWatches();
			
			if (hasDemonList()) {
				StringBuilder names = new StringBuilder();
				
				for (DWplayer p : dw.getAliveDemons()) {
					names.append("§4, §c" + p.getUHCPlayer().getName());
				}
				
				if (!names.toString().isEmpty()) lore = lore + "\n" + Lang.DW_DEMONS_LIST.get().replace(LangTag.VALUE.toString(), names.toString().substring(4));
			}
			
			if (hasSectariansList()) {
				StringBuilder names = new StringBuilder();
				
				for (DWplayer p : dw.getAliveSectarians()) {
					names.append("§5, §d" + p.getUHCPlayer().getName());
				}
				
				if (!names.toString().isEmpty()) lore = lore + "\n" + Lang.DW_SECTARIANS_LIST.get().replace(LangTag.VALUE.toString(), names.toString().substring(4));
			}
		}
		
		return lore;
	}
	
	public void stop() {}
}
