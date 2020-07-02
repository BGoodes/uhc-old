package fr.aiidor.dwuhc;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.gamemodes.DevilWatches;

public class Guardian extends DWRoleEpisode {
	
	private DWplayer target;
	
	public Guardian(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.GUARDIAN;
	}
	
	public void protect(String targetName) {
		
		DevilWatches dw = DevilWatches.getDevilWatches();
		Player player = p.getUHCPlayer().getPlayer();
		
		if (!hasPower()) {
			player.sendMessage(Lang.DW_ERROR_NO_POWER.get());
			return;
		}
		
		if (!canUsePower()) {
			player.sendMessage(Lang.DW_ERROR_NO_POWER_EPISODE_TIME.get());
			return;
		}
		
		if (!p.isAlive()) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_ALIVE.get());
			return;
		}
		
		if (Bukkit.getPlayerExact(targetName) == null) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFLINE.get().replace(LangTag.PLAYER_NAME.toString(), targetName));
			return;
		}
		
		Player target = Bukkit.getPlayerExact(targetName);
		
		if (!dw.isDWPlayer(target.getUniqueId())) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_OFFGAME.get());
			return;
		}
		
		DWplayer t = dw.getDWPlayer(target.getUniqueId());
		
		if (!t.isAlive()) {
			player.sendMessage(Lang.CMD_ERROR_PLAYER_DEAD.get());
			return;
		}
		
		if (t.equals(this.target)) {
			player.sendMessage(Lang.DW_GUARDIAN_ERROR_TARGET.get());
			return;
		}
		
		setUse(false);
		player.sendMessage(Lang.DW_GUARDIAN_PROTECTION_MSG.get().replace(LangTag.PLAYER_NAME.toString(), t.getUHCPlayer().getDisplayName()));
		
		if (!t.isProtected()) {
			this.target = t;
			t.guardian = this;
			
			if (!t.equals(p)) {
				target.sendMessage(Lang.DW_GUARDIAN_PROTECTED_MSG.get());
			}
			
			target.playSound(target.getLocation(), Sound.DRINK, 0.5f, 1f);
		}
	}
	
	
	public void death(DWplayer t) {
		
		t.guardian = null;
		setPower(false);
		
		if (p.getUHCPlayer().isConnected()) {
			p.getUHCPlayer().getPlayer().sendMessage(Lang.DW_GUARDIAN_PROTECTED_DEATH_MSG.get().replace(LangTag.PLAYER_NAME.toString(), t.getUHCPlayer().getDisplayName()));
		}
		
		if (!t.equals(p) && t.getUHCPlayer().isConnected()) {
			t.getUHCPlayer().getPlayer().sendMessage(Lang.DW_GUARDIAN_PROTECTED_DEATH.get());
		}
	}
	
	@Override
	protected Boolean doEpisode(Integer ep) {
		if (this.target != null) {
			this.target.guardian = null;
			this.target.getUHCPlayer().removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		}
		
		return true;
	}
	
	@Override
	protected void doTimeLimit() {
		this.target = null;
	}
	
	@Override
	protected Integer getTimeUse() {
		return 20 * 60 * 5;
	}

	@Override
	protected String getUseMessage() {
		return Lang.DW_GUARDIAN_CAN_USE.get();
	}
}
