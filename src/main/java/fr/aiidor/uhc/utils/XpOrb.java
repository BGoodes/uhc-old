package fr.aiidor.uhc.utils;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

public class XpOrb {
	
	private ExperienceOrb orb;
	
	public XpOrb(Location location, double experience) {
		
		if (experience < 1) {
			if (new Random().nextInt(100) <= experience * 100) {
				experience = 1;
			} else {
				experience = 0;
			}
		}
		
		if (experience != 0) {
			orb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);
			orb.setExperience((int) experience);
		}

	}
	
	public ExperienceOrb getOrb() {
		return orb;
	}
}
