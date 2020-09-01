package fr.aiidor.uhc.comparators;

import java.util.Comparator;

import fr.aiidor.uhc.game.UHCPlayer;

public class KillComparator implements Comparator<UHCPlayer> {
		
	@SuppressWarnings("removal")
	@Override
	public int compare(UHCPlayer arg0, UHCPlayer arg1) {
		if (arg0.getKills() == arg1.getKills()) return arg0.getName().compareTo(arg1.getName());
		return new Integer(arg0.getKills()).compareTo(arg1.getKills());
	}
}
