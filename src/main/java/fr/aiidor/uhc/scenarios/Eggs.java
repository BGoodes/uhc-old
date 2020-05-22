package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;

import fr.aiidor.uhc.enums.Category;

public class Eggs extends Scenario {
	
	 private List<EntityType> entities = new ArrayList<>();
	 private List<EntityType> blacklist = Arrays.asList(EntityType.PAINTING, EntityType.ITEM_FRAME, EntityType.LEASH_HITCH, EntityType.ENDER_PEARL);
	  
	public Eggs(ScenariosManager manager) {
		super(manager);
		
        for (EntityType ent : EntityType.values()) {
        	if (ent.isSpawnable() && !blacklist.contains(ent)) {
        		entities.add(ent);
        	}
        }
	}
	
	@Override
	public String getID() {
		return "eggs";
	}

	@Override
	public Material getIcon() {
		return Material.EGG;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
	
	public void spawnRandomEntity(Location loc) {
		
        EntityType choose = entities.get(new Random().nextInt(entities.size()));
        if (choose == null) return;
        
        Entity spawn = loc.getWorld().spawnEntity(loc, choose);
        
        if (choose == EntityType.SKELETON) {
        	Skeleton skel = (Skeleton) spawn;
        	if (new Random().nextBoolean()) {
        		skel.setSkeletonType(SkeletonType.WITHER);
        	}
        }
	}
}
