package fr.aiidor.uhc.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;

public class UHCWorld {
	
	private String main_world;
	
	private String overworld;
	private String nether;
	private String end;
	
	public Boolean overworld_state = true;
	public Boolean nether_state = true;
	public Boolean end_state = false;
	
	private Boolean regen = false;
	
	private WorldPanel panel;
	
	public UHCWorld(String main_world, String overworld) {
		
		this.main_world = main_world;
		
		this.overworld = overworld;
		this.nether = overworld + "_nether";
		this.end = overworld + "_the_end";
	}
	
	public Boolean isGenerate() {
		
		if (overworld_state && !isGenerate(overworld)) return false;
		if (nether_state && !isGenerate(nether)) return false;
		if (end_state && !isGenerate(end)) return false;
					
		return true;
	}
	
	public void generate(WorldPanel panel) {
		Game game = UHC.getInstance().getGameManager().getGame();
		this.panel = panel;
		
		if (panel == null) panel = new WorldPanel(main_world);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.closeInventory();
		}
		
		game.title(Lang.BC_WORLD_GENERATION.get());
		
		WorldManager wOverworld = new WorldManager(overworld);
		WorldManager wNether = new WorldManager(nether);
		WorldManager wEnd = new WorldManager(end);
			
		if (!isGenerate(overworld) && overworld_state) wOverworld.create(panel.overworld_seed, Environment.NORMAL, panel.overworld_type, panel.overworld_structure);
		if (!isGenerate(nether) && nether_state) wNether.create(panel.nether_seed, Environment.NETHER, panel.nether_type, panel.nether_structure);
		if (!isGenerate(end) && end_state) wEnd.create(panel.end_seed, Environment.THE_END, panel.end_type, panel.end_structure);
			
		if (overworld_state) wOverworld.load();
		if (nether_state) wNether.load();
		if (end_state) wEnd.load();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.closeInventory();
			player.teleport(UHC.getInstance().getSettings().lobby);
		}
		
		game.title(Lang.BC_WORLD_GENERATION_END.get());
	}
	
	public Boolean delete() {
		for (World w : getAll()) {
			WorldManager wm = new WorldManager(w);
				
			if (wm.unload()) wm.deleteWorld();
			else return false;
		}
		
		return true;
	}
	
	public void regenerate() {
		delete();
		generate(panel);
	}
	
	public Boolean hasOverWorld() {
		return getOverWorld() != null;
	}
	
	public Boolean hasNether() {
		return getNether() != null;
	}
	
	public Boolean hasEnd() {
		return getEnd() != null;
	}
	
	public World getOverWorld() {
		if (overworld == null) return null;
		return Bukkit.getWorld(overworld);
	}
	
	public World getNether() {
		if (nether == null) return null;
		return Bukkit.getWorld(nether);
	}
	
	public World getEnd() {
		if (end == null) return null;
		return Bukkit.getWorld(end);
	}

	public Boolean getMainWorldState() {
		if (main_world.equals(overworld)) return getOverworldState();
		if (main_world.equals(nether)) return getOverworldState();
		if (main_world.equals(end)) return getOverworldState();
		return false;
	}
	
	public Boolean getOverworldState() {
		return overworld != null && isGenerate(overworld) && overworld_state;
	}
	
	public Boolean getNetherState() {
		return nether != null && isGenerate(nether) && nether_state;
	}
	
	public Boolean getEndState() {
		return end != null && isGenerate(end) && end_state;
	}
	
	public List<World> getAll() {
		List<World> wl = new ArrayList<World>();
		if (hasOverWorld()) {
			wl.add(getOverWorld());
		}
		
		if (hasNether()) {
			wl.add(getNether());
		}
		
		if (hasEnd()) {
			wl.add(getEnd());
		}
		
		return wl;
	}
	
	public String getMainWorldName() {
		return main_world;
	}
	
	public String getOverWorldName() {
		return overworld;
	}
	
	public String getNetherName() {
		return nether;
	}
	
	public String getEndName() {
		return end;
	}
	
	public World getMainWorld() {
		if (main_world == null) return null;
		return Bukkit.getWorld(main_world);
	}
	
	public void setMainWorld(World world) {
		this.main_world = world.getName();
	}
	
	public static Boolean isGenerate(String name) {
		return name != null && Bukkit.getWorld(name) != null;
	}
	
	public void setCanRegen(Boolean state) {
		this.regen = state;
	}
	
	public Boolean canRegen() {
		return regen;
	}
}
