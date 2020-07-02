package fr.aiidor.dwuhc;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.task.GameTask;

public abstract class DWRoleEpisode extends DWRole {
	
	private Boolean state = false;
	private BukkitRunnable task = null;
	
	public DWRoleEpisode(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	public Boolean canUsePower() {
		return state;
	}
	
	public void setUse(Boolean state) {
		this.state = state;
	}
	
	protected abstract Integer getTimeUse();
	protected abstract String getUseMessage();
	
	protected Boolean doEpisode(Integer ep) {
		return true;
	}
	
	protected void doTimeLimit() {}
	
	public void givePower(Integer ep) {
		
		if (!doEpisode(ep)) return;
		if (!hasPower() || !p.isAlive()) return;
		
		if (task != null) task.cancel();
		setUse(true);
		
		if (p.getUHCPlayer().isConnected()) {
			Player player = p.getUHCPlayer().getPlayer();
			
			player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.5f, 1f);
			player.sendMessage(getUseMessage());
		}
		
		if (getTimeUse() > 1) {
			task = new Later();
			task.runTaskLater(UHC.getInstance(), getTimeUse() - (ep == 2 ? 20 * 3 : 0));
		}
	}
	
	private class Later extends BukkitRunnable {
		
		@Override
		public void run() {
			if (canUsePower()) {
				
				if (UHC.getInstance().getGameManager().hasGame()) {
					Game game = UHC.getInstance().getGameManager().getGame();
					
					if (!game.isRunning() || game.getUHCMode().getUHCType() != UHCType.DEVIL_WATCHES || !(game.getRunner() instanceof GameTask)) return;
					
					setUse(false);
					doTimeLimit();

					if (p.getUHCPlayer().isConnected()) {
						
						Player player = p.getUHCPlayer().getPlayer();
						
						player.sendMessage(Lang.DW_ERROR_POWER_TIME_LIMIT_EPISODE.get().replace(LangTag.VALUE.toString(), "" + getTimeUse()/20/60));
					}
				}

			}
		}
	}
	
	@Override
	public void stop() {
		if (task != null) task.cancel();
	}
}
