package fr.aiidor.dwuhc;

import java.util.List;
import java.util.Random;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Villager extends DWRole {

	private VillagerJob type;
	
	public Villager(DWplayer player, DWCamp camp) {
		super(player, camp);
		type = VillagerJob.values()[new Random().nextInt(VillagerJob.values().length)];
	}

	@Override
	public DWRoleType getRoleType() {
		return DWRoleType.VILLAGER;
	}
	
	@Override
	public ItemStack[] getStartItems() {
		return type.getStartItems();
	}
	
	@Override
	public List<PotionEffect> getPotionEffects() {
		return type.getEffects();
	}
	
	@Override
	public String getLore() {
		return super.getLore() + " " + type.getLore();
	}
}
