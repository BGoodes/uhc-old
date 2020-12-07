package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import fr.aiidor.uhc.enums.Category;

public class BiomeApocalypse extends Scenario {
	
	public BiomeApocalypse(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "biome-apocalypse";
	}

	@Override
	public Material getIcon() {
		return Material.SNOW;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.GENERATION);
	}
	
	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.HELL)) return false;
		return true;
	}
	
	public void apocalypse(Block block) {
		if (block.getType() != Material.AIR) block.setBiome(Biome.values()[new Random().nextInt(Biome.values().length)]);
	}
}
