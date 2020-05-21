package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class Assassins extends Scenario {
	
	private Game game;
	private HashMap<UHCPlayer, UHCPlayer> targets;
	private HashMap<UHCPlayer, Integer> distance;
	
	public Assassins(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public void load() {
		game = null;
		targets = new HashMap<UHCPlayer, UHCPlayer>();
		distance = new HashMap<UHCPlayer, Integer>();
	}
	
	@Override
	public String getID() {
		return "assassins";
	}

	@Override
	public Material getIcon() {
		return Material.IRON_SWORD;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	public Boolean isReady() {
		return game != null;
	}
	
	public Boolean hasTarget(UHCPlayer p) {
		return targets.containsKey(p);
	}
	
	public UHCPlayer getTarget(UHCPlayer p) {
		return targets.get(p);
	}	
	
	public Integer getDistance(UHCPlayer p) {
		return distance.get(p);
	}
	
	public void setDistance() {
		if (isReady()) {
			for (Entry<UHCPlayer, UHCPlayer> map : targets.entrySet()) {
				distance.put(map.getKey(), (int) map.getKey().distance(map.getValue()));
			}
		}
	}
	
	public void setTarget(UHCPlayer p, UHCPlayer t) {
		
		if (t != null) {
			
			targets.put(p, t);
			if (p.isConnected()) p.getPlayer().sendMessage(Lang.TARGET_MSG.get().replace(LangTag.PLAYER_NAME.toString(), t.getDisplayName()));
			
		} else {
			if (p.isConnected()) p.getPlayer().sendMessage(Lang.NO_TARGET_MSG.get());
		}
	}
	
	public void setTargets(Game game) {
		this.game = game;
		
		for (UHCPlayer player : game.getAlivePlayers()) {
			if (game.getAlivePlayers().size() < 2) setTarget(player, null);
			else {
				UHCPlayer p = game.getRandomPlayer();
				
				while (p.equals(player) && game.getAlivePlayers().size() >= 2) {
					p = game.getRandomPlayer();
				}
				
				setTarget(player, p);
			}
		}
	}
	
	public Boolean canDrop(Player player) {
		
		if (player.getKiller() != null && game.isHere(player.getKiller().getUniqueId())) {
			UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
			UHCPlayer k = game.getUHCPlayer(player.getKiller().getUniqueId());
			
			if (hasTarget(k) && hasTarget(p)) {
				if (getTarget(k).equals(p) || getTarget(p).equals(k)) {
					return true;
				}
				return false;
			}
			return true;
		}
		
		return false;
	}
}
