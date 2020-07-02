package fr.aiidor.dwuhc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Inquisitor extends DWRole {
	
	public Inquisitor(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.INQUISITOR;
	}
	
	@Override
	public List<PotionEffect> getDayPotionEffects() {
		return Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2*20, 0, false, false));
	}
}
