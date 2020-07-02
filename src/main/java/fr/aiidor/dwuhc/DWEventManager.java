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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.gamemodes.DevilWatches;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class DWEventManager {
	
	private DevilWatches DW;
	public List<CorpseTask> corpse_tasks;
	
	public DWEventManager(DevilWatches DW) {
		this.DW = DW;
		corpse_tasks = new ArrayList<DWEventManager.CorpseTask>();
	}
	
	public Boolean deathEvent(PlayerDeathEvent e, Game game, UHCPlayer p) {
		
		Player player = e.getEntity();
		DevilWatches dw = (DevilWatches) game.getUHCMode();
		
		if (DW.isDWPlayer(p.getUUID())) {
			DWplayer dwp = DW.getDWPlayer(p.getUUID());
			
			if (dwp.isProtected() && dwp.guardian.hasPower()) {
				dwp.guardian.death(dwp);
				p.revive(false, false);
				
				e.setDeathMessage(null);
				e.setKeepInventory(true);
				e.setKeepLevel(true);
				return false;
			}
			
			if (dw.playersHasRole()) {
				
				game.playSound(Sound.AMBIENCE_THUNDER, game.getSettings().death_sound_volume);	
				game.broadcast(Lang.DW_DEATH_REASON.get()
							.replace(LangTag.PLAYER_NAME.toString(), dwp.getUHCPlayer().getDisplayName())
							.replace(LangTag.DW_SECTARIAN.toString(), dwp.getCamp() == DWCamp.SECTARIANS ? Lang.DW_SECTARIAN.get() : "")
							.replace(LangTag.ROLE_NAME.toString(), dwp.getRole().getName())
						);
			}
			
			Player killer = player.getKiller();
			
			if (killer != null) {
				
				if (DW.hasRole(killer.getUniqueId())) {
					DWplayer k = DW.getDWPlayer(killer.getUniqueId());
					
					//KILLER
					if (k.getRole().getRoleType() == DWRoleType.PROWLER && k.getRole().hasPower() && k.isAlive() && dwp.isAlive()) {
						((Prowler) k.getRole()).kill(dwp.getCamp());
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
				if (killer != null && DW.hasRole(killer.getUniqueId())) k = DW.getDWPlayer(killer.getUniqueId());
				
				CorpseTask t = new CorpseTask(player.getLocation(), dwp, k, dw);
				corpse_tasks.add(t);
				t.runTaskTimer(UHC.getInstance(), 0, 20);
			}
		}
		
		return true;
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
