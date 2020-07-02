package fr.aiidor.dwuhc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.gamemodes.DevilWatches;

public class Prophet extends DWRole {
	
	private int power;
	
	public Prophet(DWplayer player, DWCamp camp) {
		super(player, camp);
		power = 3;
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.PROPHET;
	}
	
	public void see(String targetName) {
		
		DevilWatches dw = (DevilWatches) UHC.getInstance().getGameManager().getGame().getUHCMode();
		
		Player player = p.getUHCPlayer().getPlayer();
		
		if (!hasPower()) {
			player.sendMessage(Lang.DW_ERROR_NO_POWER.get());
			return;
		}
		
		if (power <= 0) {
			player.sendMessage(Lang.DW_ERROR_NO_POWER_USE.get().replace(LangTag.VALUE.toString(), ""+3));
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
		
		player.getWorld().strikeLightningEffect(player.getLocation());
		
		if (player.getMaxHealth() <= 2) player.setHealth(0);
		else player.setMaxHealth(player.getMaxHealth() - 2);
		
		power--;
		
		player.sendMessage(Lang.DW_PROPHET_PROPHECY.get()
				.replace(LangTag.PLAYER_NAME.toString(), t.getUHCPlayer().getDisplayName())
				.replace(LangTag.ROLE_NAME.toString(), t.getRole().getRoleType().getName())
				.replace(LangTag.CAMP_NAME.toString(), Lang.removeColor(t.getCamp().getName()))
			);
	}
}
