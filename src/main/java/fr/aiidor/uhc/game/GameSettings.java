package fr.aiidor.uhc.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import fr.aiidor.uhc.enums.JoinState;
import fr.aiidor.uhc.enums.TeamType;
import fr.aiidor.uhc.scenarios.Scenario;

public class GameSettings {
	
	private Game game;
	
	private Integer spec_slots;
	
	//TEAM
	private Integer team_number;
	private Integer team_size;
	public TeamType team_type;
	public Boolean friendly_fire;
	
	//TIME
	public Integer invincibility_time = 60;
	public Integer pvp_time = 20;
	public Integer ep_time = 20;
	
	public Boolean canWin = true;
	
	//DEATH
	public Boolean showDeathMessage = true;
	public Boolean showDeathReason = true;
	public Sound DeathSound = Sound.WITHER_SPAWN;
	public float DeathSound_Volume = 0.5f;
	
	public List<ItemStack> startItems;
	public List<ItemStack> deathItems;
	
	
	//WORLDBORDER
	public Integer wbSize;
	
	private JoinState join_state = JoinState.CLOSE;
	private Set<String> whitelist = new HashSet<String>();
	private List<Scenario> scenarios = new ArrayList<Scenario>();
	
	public GameSettings(Game game) {
		
		this.game = game;
		
		this.team_number = Bukkit.getServer().getMaxPlayers();
		this.spec_slots = 10;
		
		team_type = TeamType.CHOOSE;
		this.setTeamSize(1);
		
		this.wbSize = 1250;
		
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
		
		friendly_fire = true;
	}
	
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
			
		if (team_type != TeamType.CREATE) {
			
			for (int i = team_number; i != 0; i--) {
				game.createTeam();
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
