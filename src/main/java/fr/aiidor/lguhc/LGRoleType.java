package fr.aiidor.lguhc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.aiidor.uhc.enums.UHCFile;

public enum LGRoleType {
		
	VILLAGER(LGCamp.VILLAGERS), WEREWOLF(LGCamp.VILLAGERS);
		
	private final String name;
	private final String lore;
	private final LGCamp baseCamp;
	
	private LGRoleType(LGCamp baseCamp) {
		this.name = (String) UHCFile.LANG.getJSONObject("gamemodes/werewolf/roles/" + name()).get("name");
		this.lore = (String) UHCFile.LANG.getJSONObject("gamemodes/werewolf/roles/" + name()).get("lore");
		this.baseCamp = baseCamp;
	}
	
	public LGRole create(LGPlayer player) {
			
		switch (this) {
			default: return new Villager(player);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getLore() {
		return lore;
	}
	
	public LGCamp getBaseCamp() {
		return baseCamp;
	}
	
	public static List<LGRoleType> getRoleTypeList(LGCamp camp) {
		List<LGRoleType> list = new ArrayList<LGRoleType>();
		
		for (LGRoleType role : LGRoleType.values()) {
			if (role.getBaseCamp() == camp) list.add(role);
		}
		
		return list;
	}
	
	public static List<LGRoleType> getRoles() {
		List<LGRoleType> list = new ArrayList<LGRoleType>();
		
		for (LGCamp camp : LGCamp.values()) {
			
			List<LGRoleType> roles = getRoleTypeList(camp);
			roles.sort(new RoleComparator());
			
			for (LGRoleType role : roles) {
				if (role != LGRoleType.VILLAGER) list.add(role);
			}
		}
		
		return list;
	}
	
	
	private static class RoleComparator implements Comparator<LGRoleType> {

		@Override
		public int compare(LGRoleType arg0, LGRoleType arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	}
}
