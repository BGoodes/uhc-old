package fr.aiidor.dwuhc;

import java.util.HashMap;
import java.util.Map;

public class DWSettings {
	
	public Map<DWRoleType, Integer> compo;
	public Integer sectarians = 1;
	
	public DWSettings() {
		compo = new HashMap<DWRoleType, Integer>();
		
		for (DWRoleType role : DWRoleType.values()) {
			if (role != DWRoleType.VILLAGER) compo.put(role, 0);
		}

	}
	
	public Integer getRoleNumber() {
		int sum = 0;
		
		for (int i : compo.values()) {
			sum +=i;
		}
		
		return sum;
	}
}
