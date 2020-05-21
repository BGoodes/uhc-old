package fr.aiidor.uhc.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.enums.Permission;
import fr.aiidor.uhc.enums.PlayerState;
import fr.aiidor.uhc.enums.Rank;
import fr.aiidor.uhc.team.UHCTeam;
import net.minecraft.server.v1_8_R3.EntityLiving;

public class UHCPlayer {
	
	private final UUID uuid;
	private Game game;
	
	private String name;
	private String displayName;
	
	private PlayerState state;
	private Rank rank;
	
	private int kill = 0;
	
	public UHCPlayer(Player player, Rank rank, Game game) {
		
		this.uuid = player.getUniqueId();
		
		this.setName(player.getName());
		this.setRank(rank);
		this.setDisplayName(getName());
		
		if (rank != Rank.SPECTATOR) state = PlayerState.ALIVE;
		else state = PlayerState.SPECTATOR;
		
		this.game = game;
	}

	public UUID getUUID() {
		return uuid;
	}
	
	public Boolean isPlaying() {
		return isConnected() && state == PlayerState.ALIVE;
	}
	
	public Boolean isAlive() {
		return state == PlayerState.ALIVE;
	}
	
	public Boolean hasPermission(Permission perm) {
		return rank.hasPermission(perm);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
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
	
	public void leaveTeam() {
		if (hasTeam()) {
			getTeam().leave(this);
		}
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
	
	public void addPotionEffect(PotionEffect pe) {
		
		Iterator<PotionEffectType> p = removePotionEffect.iterator();
		
		while(p.hasNext()) {
			PotionEffectType pn = p.next();
			if (pn.equals(pe.getType())) {
				removePotionEffect.remove(pn);
			}
		}
		
		if (isConnected()) {
			getPlayer().addPotionEffect(pe);
		} else {
			addPotionEffect.add(pe);
		}
	}
	
	public void removePotionEffect(PotionEffectType pe) {
		
		Iterator<PotionEffect> p = addPotionEffect.iterator();
		
		while(p.hasNext()) {
			PotionEffect pn = p.next();
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
			if (game.isStart() && getState() == PlayerState.ALIVE) {
				Player player = getPlayer();
				
				for (ItemStack item : giveItem) {
					if (player.getInventory().firstEmpty() == -1) {
						player.getWorld().dropItem(player.getLocation(), item);
					} else {
						player.getInventory().addItem(item);
					}
				}
				
				for (PotionEffectType pe : removePotionEffect) {
					player.removePotionEffect(pe);
				}
			}
			
			giveItem.clear();
			removePotionEffect.clear();
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
			
			if (getState() == PlayerState.SPECTATOR || getState() == PlayerState.DEAD) player.setGameMode(GameMode.SPECTATOR);
			else player.setGameMode(GameMode.SURVIVAL);
			
			player.closeInventory();
			player.getInventory().clear();
			player.getInventory().setArmorContents(new ItemStack[] {null, null, null, null});
			
			player.setAllowFlight(false);
			player.setFlying(false);
			player.setFlySpeed(0.1f);
			player.setWalkSpeed(0.2f);
			
			player.setFoodLevel(20);
			player.setSaturation(20);
			
			player.setExp(0);
			player.setLevel(0);
			
			player.setMaxHealth(20);
			player.setHealth(20);
			
			player.setFireTicks(0);
			
			player.getActivePotionEffects().forEach(p -> player.removePotionEffect(p.getType()));
			
			EntityLiving cp = ((CraftPlayer)player).getHandle();
			cp.setAbsorptionHearts(0);
			cp.setInvisible(false);
		}
	}
}
