package fr.aiidor.dwuhc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import fr.aiidor.uhc.listeners.events.UHCPlayerDeathEvent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class DWEventManager {
	
	private DevilWatches DW;
	public List<CorpseTask> corpse_tasks;
	
	public DWEventManager(DevilWatches DW) {
		this.DW = DW;
		corpse_tasks = new ArrayList<DWEventManager.CorpseTask>();
	}
	
	public void deathEvent(UHCPlayerDeathEvent e) {
		
		Player player = e.getPlayer();
		UHCPlayer p = e.getUHCPlayer();
		Game game = e.getGame();
		DevilWatches dw = (DevilWatches) game.getUHCMode();
		
		if (DW.isDWPlayer(p.getUUID())) {
			DWplayer dwp = DW.getDWPlayer(p.getUUID());
			
			if (dwp.isProtected() && dwp.guardian.hasPower()) {
				
				dwp.guardian.death(dwp);
				
				e.setCancelTeleport(true);
				e.setCancelled(true);
				return;
			}
			
			if (dwp.hasRole()) {
				
				e.setDeathSound(Sound.AMBIENCE_THUNDER);
				e.setDeathMessage(
						Lang.DW_DEATH_REASON.get()
							.replace(LangTag.PLAYER_NAME.toString(), dwp.getUHCPlayer().getDisplayName())
							.replace(LangTag.DW_SECTARIAN.toString(), dwp.getCamp() == DWCamp.SECTARIANS ? Lang.DW_SECTARIAN.get() : "")
							.replace(LangTag.ROLE_NAME.toString(), dwp.getRole().getName())
							.replace(LangTag.ROLE_CAMP_PREFIX.toString(), dwp.getRole().getRoleType().getBaseCamp().getPrefix() + "§o")
						);
				
				if (dwp.getCamp().equals(DWCamp.DEMONS)) e.getLocation().getWorld().strikeLightningEffect(e.getLocation());
			}
			
			if (e.hasKiller()) {
				
				if (DW.hasRole(e.getKiller().getUUID())) {
					DWplayer k = DW.getDWPlayer(e.getKiller().getUUID());
					DWRole r = k.getRole();
					
					//KILLER
					if (r.getRoleType() == DWRoleType.PROWLER && r.hasPower() && k.isAlive() && dwp.isAlive()) {
						((Prowler) k.getRole()).kill(dwp.getCamp());
					}
					
					if (r.getRoleType() == DWRoleType.GREEDY_DEMON && r.hasPower() && k.isAlive() && dwp.isAlive()) {
						if (k.getUHCPlayer().isConnected()) {
							Player killer = k.getUHCPlayer().getPlayer();
							
							killer.setMaxHealth(killer.getMaxHealth() + 1);
							
							if (r.getRoleType() == DWRoleType.GREEDY_DEMON) {
								k.getUHCPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 60, 0));
							}
						}
					}
					
					if (r.getRoleType() == DWRoleType.MISCHIEVOUS_DEMON) {
						k.getUHCPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 1));
					}
					
					if (r.getRoleType() == DWRoleType.DEMON) {
						k.getUHCPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20 * 60, 0));
					}
					
					if (dw.getGameRoles().containsKey(DWRoleType.CAT_LADY)) {
						for (Entity ent : player.getNearbyEntities(50, 50, 50)) {
							if (ent instanceof Ocelot && CatLady.isMysticCat((Ocelot) ent)) {
								
								for (DWplayer c : dw.getPlayingPlayers(DWRoleType.CAT_LADY)) {
									
									CatLady cl = (CatLady) c.getRole();
									
									if (cl.isCatOwner((Ocelot) ent)) {
										
										game.broadcast(Lang.DW_CAT_MEOW.get());
										c.getUHCPlayer().getPlayer().sendMessage(Lang.DW_CAT_WHISPER.get().replace(LangTag.VALUE.toString(), Lang.DW_CAT_WHISPER_KILL.get()
												.replace(LangTag.PLAYER_NAME.toString(), p.getName())
												.replace(LangTag.VALUE.toString(), k.getUHCPlayer().getName())));
									}
								}
							}
						}
					}
				}
			}
			
			if (dw.getGameRoles().containsKey(DWRoleType.HUNTER)) {
				
				DWplayer k = null;	
				if (e.hasKiller() && DW.hasRole(e.getKiller().getUUID())) k = DW.getDWPlayer(e.getKiller().getUUID());
				
				CorpseTask t = new CorpseTask(player.getLocation(), dwp, k, dw);
				corpse_tasks.add(t);
				t.runTaskTimer(UHC.getInstance(), 0, 20);
			}
		}
	}
	
	
	public CorpseTask getCorpseTask(ArmorStand a) {
		
		if (corpse_tasks != null && !corpse_tasks.isEmpty()) {
			for (CorpseTask t : corpse_tasks) {
				if (a.getUniqueId().equals(t.asID)) return t;
			}
		}
		return null;
	}
	
	public Boolean isCorpsetask(ArmorStand a) {
		return getCorpseTask(a) != null;
	}
	
	public class CorpseTask extends BukkitRunnable {

		private Integer timer;
		
		
		public UUID asID;
		private Location loc;
		
		public UHCPlayer p;
		public UHCPlayer k;
		
		private DevilWatches dw;
		
		private PacketPlayOutWorldParticles particles;
		
		public CorpseTask(Location loc, DWplayer p, DWplayer k, DevilWatches dw) {
			
			timer = 5 * 60;
			
			this.p = p.getUHCPlayer();
			this.loc = loc;
			
			this.k = null;
			if (k != null) this.k = k.getUHCPlayer();
			
			this.dw = dw;
			
			ArmorStand as = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			
			as.setSmall(true);
			as.setGravity(false);
			as.setRemoveWhenFarAway(false);
			as.setVisible(false);
			
			as.setNoDamageTicks(20 * timer);
			
			this.asID = as.getUniqueId();
			
			
			float x = (float) loc.getX();
			float y = (float) loc.getY() + 0.3f;
			float z = (float) loc.getZ();
			
			this.particles = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, true, x, y - 0.1f, z, 0.1f, 0.2f, 0.1f, 0.01f, 30);
		}
		
		@Override
		public void run() {
			
			if (timer <= 0 || findAS() == null) {
				stop();
				return;
			}
			
			for (Entity e : loc.getWorld().getNearbyEntities(loc, 50, 50, 50)) {
				
				if (e instanceof Player) {
					Player player = (Player) e;
					if (dw.hasRole(player.getUniqueId())) {
						DWRole role = dw.getDWPlayer(player.getUniqueId()).getRole();
						
						if (role.getRoleType() == DWRoleType.HUNTER && role.hasPower()) {
	                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
						}
					}
				}
			}
			
			
			timer --;
		}
		
		public Boolean hasKiller() {
			return k != null;
		}
		
		public ArmorStand findAS() {
			if (!loc.getChunk().isLoaded()) loc.getChunk().load();
			for (Entity e : loc.getWorld().getNearbyEntities(loc, 1, 1, 1)) {
				if (e.getUniqueId().equals(asID) && e instanceof ArmorStand) {
					return (ArmorStand) e;
				}
			}
			
			return null;
		}
		
		public void stop(Player p) {
			

			if (hasKiller()) {

				String location = "§k(0, 0, 0)";
				if (k.isConnected()) {
					Location l = k.getPlayer().getLocation();
					
					location = "\nx = " + l.getBlockX() + ", y = " + l.getBlockY() + ", z = " + l.getBlockZ();
				}
					
				p.sendMessage(Lang.DW_HUNTER_INSPECT_LOCATE.get().replace(LangTag.VALUE.toString(), location).replace(LangTag.PLAYER_NAME.toString(), this.p.getName()));
				p.playSound(p.getLocation(), Sound.WOLF_BARK, 0.5f, 1f);
				
			} else {
				p.playSound(p.getLocation(), Sound.WOLF_WHINE, 0.5f, 1f);
				p.sendMessage(Lang.DW_HUNTER_INSPECT_NULL.get().replace(LangTag.PLAYER_NAME.toString(), this.p.getName()));
			}
			
			stop();
		}
		
		public void stop() {
			ArmorStand as = findAS();
			if (as != null) as.remove();
			cancel();
			
			corpse_tasks.remove(this);
		}
	}
}
