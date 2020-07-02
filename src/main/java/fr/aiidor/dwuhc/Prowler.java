package fr.aiidor.dwuhc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;

public class Prowler extends DWRole {
	
	public Integer time = 0;
	
	public Prowler(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.PROWLER;
	}
	
	@Override
	public List<PotionEffect> getPotionEffects() {
		return Arrays.asList(new PotionEffect(PotionEffectType.SPEED, 2*20, 0, false, false));
	}
	
	public void kill(DWCamp camp) {
		if (camp == DWCamp.VILLAGERS || camp == DWCamp.SECTARIANS) {
			
			if (getCamp() == DWCamp.NEUTRALS || camp == getCamp()) {
				
				DWCamp newCamp = camp == DWCamp.VILLAGERS ? DWCamp.SECTARIANS : DWCamp.VILLAGERS;
				
				setCamp(newCamp);
				
				if (p.getUHCPlayer().isConnected()) {
					p.getUHCPlayer().getPlayer().sendMessage(Lang.DW_PROWLER_JOIN.get()
						.replace(LangTag.CAMP_NAME.toString(), Lang.removeColor(newCamp.getName()))
						.replace(LangTag.VALUE_1.toString(), camp.getMemberName())
						.replace(LangTag.VALUE_2.toString(), Lang.removeColor(camp.getName())));
				}
			}
		}
	}
}
