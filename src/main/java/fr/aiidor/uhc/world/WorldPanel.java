package fr.aiidor.uhc.world;

import org.bukkit.World.Environment;
import org.bukkit.WorldType;

import fr.aiidor.uhc.Settings;
import fr.aiidor.uhc.UHC;

public class WorldPanel {
	
	public String overworld_name;
	
	public Long overworld_seed;
	public Long nether_seed;
	public Long end_seed;
	
	public WorldType overworld_type;
	public WorldType nether_type;
	public WorldType end_type;
	
	public Boolean overworld_structure;
	public Boolean nether_structure;
	public Boolean end_structure;
	
	public Environment main_dimension;
	
	public WorldPanel(String overworld_name) {
		this.overworld_name = overworld_name;
		
		Settings s = UHC.getInstance().getSettings();
			
		overworld_seed = s.default_seed;
		nether_seed = s.default_seed;
		end_seed = s.default_seed;
		
		overworld_type = s.default_world_type;
		nether_type = s.default_world_type;
		end_type = s.default_world_type;
		
		overworld_structure = s.default_generate_structure;
		nether_structure = s.default_generate_structure;
		end_structure = s.default_generate_structure;
		
		main_dimension = Environment.NORMAL;
	}
	
	public String getWorldName(Environment env) {
		if (env == Environment.NETHER) return getNetherName();
		if (env == Environment.THE_END) return getEndName();
		return overworld_name;	
	}
	
	public String getNetherName() {
		return overworld_name + "_nether";
	}
	
	public String getEndName() {
		return overworld_name + "_the_end";
	}
}
