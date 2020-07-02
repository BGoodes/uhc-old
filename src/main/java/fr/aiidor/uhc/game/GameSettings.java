package fr.aiidor.uhc.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.enums.JoinState;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.scenarios.Scenario;
import fr.aiidor.uhc.team.UHCTeam;

public class GameSettings {
	
	private Game game;

	private Integer spec_slots;
	
	//CHAT
	public Boolean chat = true;
	
	//TEAM
	private Integer team_number = 4;
	private Integer team_size;
	public TeamType team_type = TeamType.CHOOSE;
	public Boolean friendly_fire = true;
	public Boolean team_chat = true;
	
	//TIME
	public Integer invincibility_time = 1;
	public Integer pvp_time = 25;
	public Integer ep1_time = 20;
	public Integer ep_time = 1;
	public Integer wb_time = 60;
	
	public Boolean can_win = true;
	 
	//PVP
	public StrenghtNerf strength_nerf = StrenghtNerf.PERCENT;
	public Double strength_nerf_damage = 3.0;
	public Double strength_nerf_percent = 0.20D;
	
	public enum StrenghtNerf {
		OFF, DAMAGE, PERCENT;
	}
	
	//LOOTS
	public Boolean uhc_trees = true;
	
	public Double trees_apple = 0.5;
	public Boolean all_trees_drop = false;
	public Boolean trees_shears = false;
	public Boolean trees_sapling = true;
	public Boolean trees_gapple = false;
	
	//LIFE
	public Boolean display_life = true;
	public Boolean bow_display_life = true;
	public Boolean display_head_life = true;
	
	public Integer start_abso = 0;
	public Integer start_life = 20;
	
	public Boolean uhc_apple = true;
	public Boolean golden_apple = true;
	public Boolean notch_apple = false;
	public Boolean golden_head  = false;
	
	public Integer gapple_food_level = 4;
	public Float gapple_saturation = 9.6f;
	
	public List<PotionEffect> gapple_effects;
	public Integer gapple_abso = 2;
	
	public List<PotionEffect> napple_effects;
	public Integer napple_abso = 2;
	
	public List<PotionEffect> happle_effects;
	public Integer happle_abso = 2;
	
	//DEATH
	public Boolean show_death_message = true;
	public Boolean show_death_reason = true;
	public Boolean show_killer = false;
	public Sound death_sound = Sound.WITHER_SPAWN;
	public float death_sound_volume = 0.5f;
	
	public List<ItemStack> startItems;
	public List<ItemStack> deathItems;
	
	public HashMap<Rank, List<Permission>> permissions;
	
	//WORLDBORDER
	public Integer wb_size_max;
	public Integer wb_size_min;
	public Double wb_speed;
	public Integer wb_damage = 2;
	
	//TIME
	public Boolean uhc_cycle = false;
	public Boolean double_uhc_cycle = false;
	
	private JoinState join_state = JoinState.CLOSE;
	private Set<String> whitelist = new HashSet<String>();
	private List<Scenario> scenarios = new ArrayList<Scenario>();
	
	public GameSettings(Game game) {
		
		this.game = game;
		
		this.spec_slots = 10;
		
		this.setTeamSize(1);
		
		this.wb_size_max = 1250;
		this.wb_size_min = 50;
		this.wb_speed = 0.5;
		
		if (game.hasHost()) {
			this.whitelist.add(game.getHost().getName().toLowerCase());
		}
		
		startItems = new ArrayList<ItemStack>();
		startItems.add(new ItemStack(Material.COOKED_BEEF, 20));
		
		deathItems = new ArrayList<ItemStack>();
		deathItems.add(new ItemStack(Material.GOLDEN_APPLE, 1));
		
		for (int i = 0; i != 39; i++) {
			if (startItems.size() > i) {
				startItems.add(new ItemStack(Material.AIR));
			}
			
			if (i < 27) {
				if (deathItems.size() > i) {
					deathItems.add(new ItemStack(Material.AIR));
				}
			}
		}
		
		
		this.permissions = new HashMap<Rank, List<Permission>>();
		
		this.permissions.put(Rank.HOST, Arrays.asList(Permission.ALL));
		this.permissions.put(Rank.ORGA, Arrays.asList(Permission.ALL));
		this.permissions.put(Rank.STAFF, Arrays.asList(Permission.ALL));
		this.permissions.put(Rank.PLAYER, Arrays.asList(Permission.CHAT));
		this.permissions.put(Rank.SPECTATOR, Arrays.asList(Permission.NONE));
		
		gapple_effects = new ArrayList<PotionEffect>();
		happle_effects = new ArrayList<PotionEffect>();
		napple_effects = new ArrayList<PotionEffect>();
		
		gapple_effects.add(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, true, true));
		
		happle_effects.add(new PotionEffect(PotionEffectType.REGENERATION, 200, 1, true, true));
		
		napple_effects.add(new PotionEffect(PotionEffectType.REGENERATION, 600, 4, true, true));
		napple_effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0, true, true));
		napple_effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0, true, true));
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	//PERMISSIONS
	public Boolean rankHasPermission(Rank rank, Permission perm) {
		return rank.hasPermission(perm);
	}
	
	public Boolean playerHasPermission(UHCPlayer player, Permission perm) {
		return rankHasPermission(player.getRank(), perm);
	}
	
	public Boolean playerHasPermission(Player player, Permission perm) {
		return playerHasPermission(player.getUniqueId(), perm);
	}
	
	public Boolean playerHasPermission(UUID uuid, Permission perm) {
		
		if (!game.isHere(uuid)) return false;
		return rankHasPermission(game.getUHCPlayer(uuid).getRank(), perm);
	}
	
	//----------------------
	
	public Integer getSlots() {
		return team_number * team_size;
	}
	
	public Integer getSpectatorSlots() {
		return spec_slots;
	}
	
	public Integer getTeamNumber() {
		return team_number;
	}

	public void setTeamNumber(Integer team_number) {
		this.team_number = team_number;
		
		if (game.hasTeam()) game.destroyTeams();
		
		if (getTeamSize() != 1) {
			for (int i = this.team_number; i != 0; i--) {
				game.createTeam();
			}
		}

	}
	
	public Integer getTeamSize() {
		return team_size;
	}

	public void setTeamSize(Integer teamSize) {
		this.team_size = teamSize;
		
		if (team_size == 1) {
			game.destroyTeams();
			return;
		}
		
		if (!game.hasTeam()) {
			for (int i = team_number; i != 0; i--) {
				game.createTeam();
			}
		}
			
		if (team_type != TeamType.CREATE) {
			for (UHCTeam t : game.getTeams()) {
				t.setSize(team_size);
			}
		}
	}

	public void setSpectatorSlots(Integer spec_slots) {
		this.spec_slots = spec_slots;
	}
	
	public JoinState getJoinState() {
		return join_state;
	}
	
	public void setJoinState(JoinState join_state) {
		this.join_state = join_state;
	}
	
	public Set<String> getWhitelist() {
		return whitelist;
	}
	
	public List<Scenario> getActivatedScenarios() {
		List<Scenario> list = new ArrayList<Scenario>();
		list.addAll(scenarios);
		return list;
	}
	
	public Boolean IsActivated(Scenario scenario) {
		return scenarios.contains(scenario);
	}
	
	public void setActivated(Scenario scenario, Boolean state) {
		if (state) scenarios.add(scenario);
		else scenarios.remove(scenario);
	}
	
	public void saveStartItems(Inventory inv) {
		List<ItemStack> startItems = new ArrayList<ItemStack>();
		
		int slot = 0;
		for (ItemStack item : inv.getContents()) {
			startItems.add(item);
			
			slot++;
			if (slot == 40) break;
		}
		
		if (inv instanceof PlayerInventory) {
			PlayerInventory p_inv = (PlayerInventory) inv;
			
			for (ItemStack item : p_inv.getArmorContents()) {
				startItems.add(item);
			}
		}

		this.startItems = startItems;
	}
	
	public void setStartItems(Player player) {
		
		if (startItems != null) {
			int slot = 0;
			for (ItemStack it : startItems) {
				if (it != null) {
					
					if (slot == 36) player.getInventory().setHelmet(it);
					else if (slot == 37) player.getInventory().setChestplate(it);
					else if (slot == 38) player.getInventory().setLeggings(it);
					else if (slot == 39) player.getInventory().setBoots(it);
					else player.getInventory().setItem(slot, it);
					
				}
				
				slot ++;
			}
			
			player.updateInventory();
		}
	}
	
	public void saveDeathItems(Inventory inv) {
		List<ItemStack> deathItems = new ArrayList<ItemStack>();
		
		int slot = 0;
		
		for (ItemStack item : inv.getContents()) {
			deathItems.add(item);
			slot++;
			if (slot == 27) break;
		}

		this.deathItems = deathItems;
	}
	
	public List<ItemStack> getDeathItems() {
		List<ItemStack> deathItems = new ArrayList<ItemStack>();
		
		for (ItemStack item : this.deathItems) {
			if (item != null && item.getType() != Material.AIR) {
				deathItems.add(item);
			}
		}
		
		return deathItems;
	}
}
