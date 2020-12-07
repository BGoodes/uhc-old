package fr.aiidor.uhc.comparators;

import java.util.Comparator;

import org.bukkit.block.Biome;

import fr.aiidor.uhc.enums.Lang;

public class BiomeComparator implements Comparator<Biome> {
	
	@Override
	public int compare(Biome arg0, Biome arg1) {
		return Lang.removeColor(arg0.name()).compareTo(Lang.removeColor(arg1.name()));
	}

}
