package fr.aiidor.uhc.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.tools.Titles;

public class UHCWorld {
	
	private String main_world;
	
	private String overworld;
	private String nether;
	private String end;
	
	private Boolean overworld_state = true;
	private Boolean nether_state = true;
	private Boolean end_state = false;
	
	public UHCWorld(String main_world, String overworld) {
		
		this.main_world = main_world;
		this.overworld = overworld;
		this.nether = overworld + "_nether";
		this.end = overworld + "_the_end";
	}
	
	public Boolean isGenerate() {
		
		if (getOverworldState() && !isGenerate(overworld)) return false;
		if (getNetherState() && !isGenerate(nether)) return false;
		if (getEndState() && !isGenerate(end)) return false;
					
		return true;
	}
	
	public void generate(WorldPanel panel) {
		Game game = UHC.getInstance().getGameManager().getGame();
		
		if (panel == null) panel = new WorldPanel(main_world);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.closeInventory();
				
			if (game.isHere(player.getUniqueId()) && game.getUHCPlayer(player.getUniqueId()).hasPermission(Permission.CONFIG)) {
				new Titles(player).sendTitle(Lang.BC_WORLD_GENERATION.get(), "");
				player.setGameMode(GameMode.SPECTATOR);
			}
			else player.kickPlayer(Lang.CAUSE_WORLD_GENERATING.get());
		}
			
		WorldManager wOverworld = new WorldManager(overworld);
		WorldManager wNether = new WorldManager(nether);
		WorldManager wEnd = new WorldManager(end);
			
		if (!isGenerate(overworld)) wOverworld.create(panel.overworld_seed, Environment.NORMAL, panel.overworld_type, panel.overworld_structure);
		if (!isGenerate(nether)) wNether.create(panel.nether_seed, Environment.NETHER, panel.nether_type, panel.nether_structure);
		if (!isGenerate(end)) wEnd.create(panel.end_seed, Environment.THE_END, panel.end_type, panel.end_structure);
			
		wOverworld.load();
		wNether.load();
		wEnd.load();
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.closeInventory();
			player.teleport(UHC.getInstance().getSettings().lobby);
			player.setGameMode(GameMode.SURVIVAL);
		}
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

	public Boolean getOverworldState() {
		return overworld != null && isGenerate(overworld) && overworld_state;
	}
	
	public Boolean getNetherState() {
		return nether != null && isGenerate(nether) && nether_state;
	}
	
	public Boolean getEndState() {
		return end != null && isGenerate(end) && end_state;
	}
	
	public void setOverworldState(Boolean state) {
		this.overworld_state = state;
	}
	
	public void getNetherState(Boolean state) {
		this.nether_state = state;
	}
	
	public void getEndState(Boolean state) {
		this.end_state = state;
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
}
