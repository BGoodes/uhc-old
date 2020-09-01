package fr.aiidor.dwuhc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GreedyDemon extends DWRole {
	
	public GreedyDemon(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.GREEDY_DEMON;
	}
	
	@Override
	public List<PotionEffect> getNightPotionEffects() {
		return Arrays.asList(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2*20, 0, false, false));
	}
	
	@Override
	public Boolean hasDemonList() {
		return true;
	}
}
