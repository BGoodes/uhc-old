package fr.aiidor.uhc.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.utils.MCMath;
import fr.aiidor.uhc.utils.Teleportation;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class UHCPlayer {
	
	private final UUID uuid;
	private Game game;
	
	private String name;

	private PlayerState state;
	private Rank rank;
	
	private int kill = 0;
	
	//DEATH
	private Location death_loc;
	private ItemStack[] contents;
	private ItemStack[] armor_contents;
	private Integer level;
	
	public UHCPlayer(Player player, PlayerState state, Rank rank, Game game) {
		
		this.uuid = player.getUniqueId();
		
		this.refreshName(player);
		this.setRank(rank);
		
		this.state = state;
		
		this.game = game;
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public Boolean isPlaying() {
		return isConnected() && isAlive();
	}
	
	public Boolean isAlive() {
		return state == PlayerState.ALIVE || state == PlayerState.DYING;
	}
	
	public Boolean isDead() {
		return state == PlayerState.DEAD;
	}

	public Boolean isSpec() {
		return state == PlayerState.SPECTATOR || rank == Rank.SPECTATOR || state == PlayerState.DEAD;
	}
	
	public Boolean hasPermission(Permission perm) {
		return rank.hasPermission(perm);
	}
	
	public void refreshName(Player player) {
		if (this.name != null && !this.name.equals(player.getName())) {
			if (game.getScoreboard().getEntryTeam(name) != null) {
				
				Team t = game.getScoreboard().getEntryTeam(name);
				t.removeEntry(name);
				t.addEntry(player.getName());
			}
		}
		
		this.name = player.getName();
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		if (hasTeam()) return getTeam().getTeam().getPrefix() + name + getTeam().getTeam().getSuffix();
		return name;
	}
	
	public PlayerState getState() {
		return state;
	}
	
	public void setState(PlayerState state) {
		this.state = state;
	}
	
	public Boolean isConnected() {
		return getPlayer() != null;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(getUUID());
	}
	
	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public int getKills() {
		return kill;
	}

	public void addKill() {
		this.kill++;
	}
	
	public void setKills(int kill) {
		this.kill = kill;
	}
	
	public Boolean hasTeam() {
		return game.playerHasTeam(this);
	}
	
	public UHCTeam getTeam() {
		return game.getPlayerTeam(this);
	}
	
	public void leaveTeam(Boolean announce) {
		if (hasTeam()) {
			getTeam().leave(this, announce);
		}
	}
	
	public void saveDeath() {
		Player player = getPlayer();
		PlayerInventory inv = player.getInventory();
			
		this.death_loc = player.getLocation();
		this.contents = inv.getContents();
		this.armor_contents = inv.getArmorContents();
		this.level = player.getLevel();
	}
	
	public void revive() {
		if (isConnected()) {
			
			setState(PlayerState.ALIVE);
			reset();
			
			Player player = getPlayer();
			player.setNoDamageTicks(20 * 20);
			player.setHealth(player.getMaxHealth());
			
			if (ScenariosManager.BELIEVE_FLY.isActivated()) {
				player.setAllowFlight(true);
			}
			
			PacketPlayInClientCommand paquet = new PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN);
			((CraftPlayer) player).getHandle().playerConnection.a(paquet);
	            	
			if (this.contents != null) player.getInventory().setContents(this.contents);
			if (this.armor_contents != null) player.getInventory().setArmorContents(this.armor_contents);

			if (level != null) player.setLevel(level);
			
	    	if (death_loc != null && MCMath.distance2D(game.getSettings().map_center, death_loc.toVector()) < (death_loc.getWorld().getWorldBorder().getSize() / 2) - 10) {
	    		
	    		for (Entity e : death_loc.getWorld().getNearbyEntities(death_loc, 4, 4, 4)) {
	    			if (e != null && e.getType() == EntityType.DROPPED_ITEM) e.remove();
	    		}
	    		
	    		player.teleport(death_loc);
	    		
	    	} else new Teleportation(this).revive(300).teleport();
		}
	}
	
	public Boolean hasArmor() {
		for (ItemStack i : getPlayer().getInventory().getArmorContents()) {
			if (i != null && i.getType() != Material.AIR) return true;
		}
		
		return false;
	}
	public List<ItemStack> giveItem = new ArrayList<ItemStack>();
	
	public void giveItem(ItemStack item) {
		if (isConnected()) {
			Player player = getPlayer();
			
			if (player.getInventory().firstEmpty() == -1) {
				player.getWorld().dropItem(player.getLocation(), item);
			} else {
				player.getInventory().addItem(item);
			}
		} else {
			giveItem.add(item);
		}
	}
	
	private List<PotionEffect> addPotionEffect = new ArrayList<PotionEffect>();
	private List<PotionEffectType> removePotionEffect = new ArrayList<PotionEffectType>();
	
	public void setAbso(Integer amount, Integer time) {
		if (isConnected()) {
			
			EntityPlayer ep = ((CraftPlayer) getPlayer()).getHandle();
			
			if (amount > ep.getAbsorptionHearts()) {
				getPlayer().removePotionEffect(PotionEffectType.ABSORPTION);
			}
			
			if (amount > 0) {
				if (amount%4 == 0) {
					addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, time, amount/4 - 1, true, true));
				} else {
					
					if (addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, time, (Integer) amount/4, true, true))) {
						ep.setAbsorptionHearts(ep.getAbsorptionHearts() - (4 - amount%4));
					}
				}
			}
		}
	}
	
	public Integer getPotionAmplifier(PotionEffectType pe) {
		if (isConnected()) {
			for (PotionEffect p : getPlayer().getActivePotionEffects()) {
				if (pe.equals(p.getType())) return p.getAmplifier();
			}
		}
			
		return null;
	}
	
	public Boolean addPotionEffect(PotionEffect pe) {
		
		Iterator<PotionEffectType> pl = removePotionEffect.iterator();
		
		while(pl.hasNext()) {
			PotionEffectType pn = pl.next();
			if (pn.equals(pe.getType())) {
				removePotionEffect.remove(pn);
			}
		}
		
		if (isConnected()) {
			if (getPlayer().hasPotionEffect(pe.getType())) {
				for (PotionEffect p : getPlayer().getActivePotionEffects()) {
					
					if (p.getType().equals(pe.getType())) {
						
						if (pe.getAmplifier() == p.getAmplifier()) {
							if (pe.getDuration() >= p.getDuration()) {
								
								getPlayer().removePotionEffect(p.getType());	
								getPlayer().addPotionEffect(pe);
								
								return true;
							}	
							
						} else if (pe.getAmplifier() >= p.getAmplifier()) {
							
							getPlayer().removePotionEffect(p.getType());	
							getPlayer().addPotionEffect(pe);
							
							return true;
						}
						
						return false;
					}
				}
			}
			
			getPlayer().addPotionEffect(pe);
			return true;
		
		} else {
			addPotionEffect.add(pe);
			return false;
		}
	}
	
	public void removePotionEffect(PotionEffectType pe) {
		
		Iterator<PotionEffect> pl = addPotionEffect.iterator();
		
		while(pl.hasNext()) {
			PotionEffect pn = pl.next();
			if (pn.getType().equals(pe)) {
				addPotionEffect.remove(pn);
			}
		}
		
		if (isConnected()) {
			getPlayer().removePotionEffect(pe);
		} else {
			removePotionEffect.add(pe);
		}
	}
	
	public void onLogin() {
		if (isConnected()) {
			Player player = getPlayer();
			
			if (game.isStart()) {
				
				if (getState() == PlayerState.ALIVE) {
					for (ItemStack item : giveItem) {
						if (player.getInventory().firstEmpty() == -1) {
							player.getWorld().dropItem(player.getLocation(), item);
						} else {
							player.getInventory().addItem(item);
						}
					}
					
					for (PotionEffect pe : addPotionEffect) {
						addPotionEffect(pe);
					}
					
					for (PotionEffectType pe : removePotionEffect) {
						removePotionEffect(pe);
					}
				}
				
				if (ScenariosManager.BELIEVE_FLY.isActivated()) {
					player.setAllowFlight(true);
				}
			}

			
			giveItem.clear();
			removePotionEffect.clear();
			
			if (isSpec()) {
				reset();
			}
		}

	}
	
	
	public double distance(UHCPlayer arg0) {
		if (!arg0.isConnected() || !isConnected()) return -1;
		else return distance(arg0.getPlayer().getLocation());
	}
	
	public double distance(Location l0) {
		
		if (isConnected()) {
			Location l1 = getPlayer().getLocation();
			
			if (!l1.getWorld().equals(l0.getWorld())) return -1.0;
			
			Double distance = Math.sqrt(Math.pow((l1.getX() - l0.getBlockX()), 2) + Math.pow((l1.getZ() - l0.getBlockZ()), 2));
			
			return Math.round(distance * 100d) / 100d;
		}
		
		return -1.0;
	}
	
	public void reset() {
		if (isConnected()) {
			
			Player player = getPlayer();
			
			player.setScoreboard(game.getScoreboard());
			
			if (isSpec())  {
				
				player.setGameMode(GameMode.SPECTATOR);
				if (hasTeam()) getTeam().getTeam().removeEntry(name);
				game.getScoreboard().getTeam(Lang.TEAM_SPEC_NAME.get()).addEntry(name);
			    
			} else {
				
				player.setAllowFlight(false);
				player.setFlying(false);
				player.setGameMode(GameMode.SURVIVAL);
				
				if (game.getScoreboard().getTeam(Lang.TEAM_SPEC_NAME.get()).hasEntry(name)) game.getScoreboard().getTeam(Lang.TEAM_SPEC_NAME.get()).removeEntry(name);
				if (hasTeam()) getTeam().getTeam().addEntry(name);
			}
			
			refreshName(player);
			
			player.setCompassTarget(new Location(player.getWorld(), 0, 100, 0));
			player.closeInventory();
			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
			
			player.setFlySpeed(0.1f);
			player.setWalkSpeed(0.2f);
			player.setMaximumNoDamageTicks(20);
			
			player.setFoodLevel(20);
			player.setSaturation(20);
			
			player.setExp(0);
			player.setLevel(0);
			
			player.setMaxHealth(20);
			player.setHealth(20);
			
			player.setFireTicks(0);
			
			player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
			
			for (Achievement a : Achievement.values()) {
				if (player.hasAchievement(a)) player.removeAchievement(a);
			}
			
			EntityPlayer cp = ((CraftPlayer)player).getHandle();
			cp.setAbsorptionHearts(0);
			cp.setInvisible(false);
		}
	}
}
