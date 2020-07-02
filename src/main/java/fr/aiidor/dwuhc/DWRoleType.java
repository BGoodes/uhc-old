package fr.aiidor.dwuhc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.aiidor.uhc.enums.UHCFile;

public enum DWRoleType {
		
	VILLAGER(DWCamp.VILLAGERS, true), GUARDIAN(DWCamp.VILLAGERS, true), HUNTER(DWCamp.VILLAGERS, true), MOUNTEBANK(DWCamp.VILLAGERS, true), PROPHET(DWCamp.VILLAGERS, true), INQUISITOR(DWCamp.VILLAGERS, false),
	HERMIT(DWCamp.VILLAGERS, false), CAT_LADY(DWCamp.VILLAGERS, true),
	
	GURU(DWCamp.SECTARIANS, false),
	
	
	DEMON(DWCamp.DEMONS, false),
	
	
	PROWLER(DWCamp.NEUTRALS, false);
		
	private final String name;
	private final String lore;
	private final DWCamp baseCamp;
	
	private Boolean canSectarian;
	
	private DWRoleType(DWCamp baseCamp, Boolean canSectarian) {
		this.name = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/roles/" + name()).get("name");
		this.lore = (String) UHCFile.LANG.getJSONObject("gamemodes/devil-watches/roles/" + name()).get("lore");
		this.baseCamp = baseCamp;
		this.canSectarian = canSectarian;
	}
	
	public DWRole create(DWplayer player, DWCamp camp) {
			
		switch (this) {
			case VILLAGER: return new Villager(player, camp);
			case HUNTER: return new Hunter(player, camp);
			case GUARDIAN: return new Guardian(player, camp);
			case GURU: return new Guru(player, camp);
			case PROPHET: return new Prophet(player, camp);
			case MOUNTEBANK: return new MounteBank(player, camp);
			case PROWLER: return new Prowler(player, camp);
			case HERMIT: return new Hermit(player, camp);
			case INQUISITOR: return new Inquisitor(player, camp);
			case DEMON: return new Demon(player, camp);
			case CAT_LADY: return new CatLady(player, camp);
			default: return new Villager(player, camp);
		}
	}
	
	public String getName() {
		return name;
	}
	
	public String getLore() {
		return lore;
	}
	
	public DWCamp getBaseCamp() {
		return baseCamp;
	}
	
	public Boolean canSectarian() {
		return canSectarian;
	}
	
	public static List<DWRoleType> getRoleTypeList(DWCamp camp) {
		List<DWRoleType> list = new ArrayList<DWRoleType>();
		
		for (DWRoleType role : DWRoleType.values()) {
			if (role.getBaseCamp() == camp) list.add(role);
		}
		
		return list;
	}
	
	public static List<DWRoleType> getRoles() {
		List<DWRoleType> list = new ArrayList<DWRoleType>();
		
		for (DWCamp camp : DWCamp.values()) {
			
			List<DWRoleType> roles = getRoleTypeList(camp);
			roles.sort(new RoleComparator());
			
			for (DWRoleType role : roles) {
				if (role != DWRoleType.VILLAGER) list.add(role);
			}
		}
		
		return list;
	}
	
	
	private static class RoleComparator implements Comparator<DWRoleType> {

		@Override
		public int compare(DWRoleType arg0, DWRoleType arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	}
}
