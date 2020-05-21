package fr.aiidor.uhc.scenarios.comparators;

import java.util.Comparator;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.scenarios.Scenario;

public class NameComparator implements Comparator<Scenario> {

	private Boolean croissant;
	
	public NameComparator(Boolean croissant) {
		this.croissant = croissant;
	}
	
	@Override
	public int compare(Scenario arg0, Scenario arg1) {
		if (croissant) return Lang.removeColor(arg0.getName()).compareTo(Lang.removeColor(arg1.getName()));
		else return -Lang.removeColor(arg0.getName()).compareTo(Lang.removeColor(arg1.getName()));
	}

}
