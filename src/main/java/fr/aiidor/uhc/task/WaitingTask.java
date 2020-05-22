package fr.aiidor.uhc.task;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class WaitingTask extends UHCTask {

	private Game game;
	public long time;
	
	public WaitingTask(Game game) {
		this.game = game;
		time = 12000;
	}
	
	@Override
	public void launch() {
		game.setRunner(this);
		game.setState(GameState.WAITING);
		this.runTaskTimer(UHC.getInstance(), 0, 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		
		game.getMainWorld().getMainWorld().setTime(time);
		
		if (game.hasTeam() && UHC.getInstance().getSettings().hasCage()) {
			for (UHCPlayer p : game.getPlayingPlayers()) {
				
				Player player = p.getPlayer();
				if (p.hasTeam() && (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE)) {
					Block b = player.getLocation().subtract(0, 1, 0).getBlock();
					if (b.getType() == Material.BARRIER || b.getType() == Material.STAINED_GLASS) {
						b.setTypeIdAndData(Material.STAINED_GLASS.getId(), p.getTeam().getColor().getGlassColor(), true);
					}
				}
			}
		}
	}
	
	@Override
	public void stop() {
		cancel();
		game.setRunner(null);
	}

	@Override
	public Integer getTime() {
		return 0;
	}
}
