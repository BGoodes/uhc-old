package fr.aiidor.uhc.comparators;

import java.util.Comparator;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.scenarios.Scenario;

public class NameComparator implements Comparator<Scenario> {

	private Boolean asd;
	
	public NameComparator(Boolean asd) {
		this.asd = asd;
	}
	
	@Override
	public int compare(Scenario arg0, Scenario arg1) {
		if (asd) return Lang.removeColor(arg0.getName()).compareTo(Lang.removeColor(arg1.getName()));
		else return -Lang.removeColor(arg0.getName()).compareTo(Lang.removeColor(arg1.getName()));
	}

}
