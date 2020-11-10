package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class Tracker extends Scenario {
	
	public Tracker(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "tracker";
	}

	@Override
	public Material getIcon() {
		return Material.COMPASS;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.ASSASSINS)) return false;
		return true;
	}
	
	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	public void track(UHCPlayer p) {
		
		if (p.isConnected()) {
			Player player = p.getPlayer();
			UHCPlayer t = getNearestPlayer(p);
			
			if (t == null) {
				player.sendMessage(Lang.TRACKER_NO_TARGET.get());
				
			} else {
				player.sendMessage(Lang.TRACKER_TARGET.get().replace(LangTag.PLAYER_NAME.toString(), t.getDisplayName()).replace(LangTag.VALUE.toString(), "" + (int) p.distance(t)));
				if (t.isConnected()) player.setCompassTarget(t.getPlayer().getLocation());
			}
		}
	}
	
	private UHCPlayer getNearestPlayer(UHCPlayer p) {
		
		Game game = UHC.getInstance().getGameManager().getGame();
		
		UHCPlayer player = null;
		
		for (UHCPlayer t : game.getPlayingPlayers()) {
			if (p.distance(t) >= 5 && !p.equals(t)) {
				
				if (p.hasTeam() && p.getTeam().isInTeam(t)) continue;
				
				if (player == null) player = t;
				else {
					if (p.distance(t) >= 0) player = p.distance(t) < p.distance(player) ? t : player;
				}
			}

		}
		
		return player;
	}
}
