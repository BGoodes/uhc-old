package fr.aiidor.uhc.gamemodes;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.tools.Titles;

public abstract class UHCMode {
	
	protected Game game;
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public abstract UHCType getUHCType();
	public abstract void checkConditions();
	public abstract void loading();
	
	public abstract void begin();
	public abstract void episode(Integer ep);
	public abstract void run();
	
	public void end() {
		
		if (!game.hasTeam()) {
			
			if (game.getAlivePlayers().size() == 1) {
				for (UHCPlayer player : game.getAlivePlayers()) {
					
					Bukkit.broadcastMessage(Lang.BC_SOLO_VICTORY.get().replace(LangTag.PLAYER_NAME.toString(), player.getDisplayName()));
					if (player.isConnected()) {
						
						new Titles(player.getPlayer()).sendTitle(Lang.BC_VICTORY_TITLE_LINE_1.get(), Lang.BC_VICTORY_TITLE_LINE_2.get(), 60);
						FireWork(player, Color.PURPLE);
					}
					
					for (UHCPlayer p : game.getIngamePlayers()) {
						if (p.isConnected() && p.isDead()) {
							new Titles(p.getPlayer()).sendTitle(Lang.BC_LOSE_TITLE_LINE_1.get(), Lang.BC_LOSE_TITLE_LINE_2.get(), 60);
						}
					}
					
					UHC.getInstance().getGameManager().stopGame();
					break;
				}
			}
		} else {
			
			if (game.getAliveTeams().size() == 1) {
				
				if (ScenariosManager.ONLY_ONE_WINNER.isActivated()) {
					game.destroyTeams();
					game.playSound(Sound.GHAST_MOAN, 0.6f);
					game.broadcast(Lang.BC_ONE_WINNER.get());
					return;
				}
				
				for (UHCTeam team : game.getAliveTeams()) {
					
					Bukkit.broadcastMessage(Lang.BC_TEAM_VICTORY.get().replace(LangTag.TEAM_NAME.toString(), team.getName()));
					
					for (UHCPlayer p : game.getIngamePlayers()) {
						if (p.isConnected()) {
							if (p.isDead()) new Titles(p.getPlayer()).sendTitle(Lang.BC_LOSE_TITLE_LINE_1.get(), Lang.BC_LOSE_TITLE_LINE_2.get(), 60);
							else {
								new Titles(p.getPlayer()).sendTitle(Lang.BC_VICTORY_TITLE_LINE_1.get(), Lang.BC_VICTORY_TITLE_LINE_2.get(), 60);
								FireWork(p, team.getColor().getColor());
							}
						}
					}
					
					UHC.getInstance().getGameManager().stopGame();
					break;
				}
				
				
			}
		}
	}
	
	private void FireWork(UHCPlayer player, Color color) {
		
		Player p = player.getPlayer();
		
		Firework f = (Firework) p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREWORK);
		f.detonate();
		
		FireworkMeta fM = f.getFireworkMeta();
		
		FireworkEffect effect = FireworkEffect.builder()
				.flicker(true)
				.withColor(color)
				.withFade(Color.ORANGE)
				.with(Type.values()[new Random().nextInt(Type.values().length)])
				.trail(true)
				.build();
		
		fM.addEffect(effect);
		fM.setPower(1);
		f.setFireworkMeta(fM);
		
	}
	
	public abstract void stop();
}
