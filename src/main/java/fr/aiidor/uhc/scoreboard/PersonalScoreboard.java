package fr.aiidor.uhc.scoreboard;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.GameState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.scenarios.ScenariosManager;

/*
 * This file is part of SamaGamesAPI.
 *
 * SamaGamesAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamaGamesAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamaGamesAPI.  If not, see <http://www.gnu.org/licenses/>.
 */
public class PersonalScoreboard {
	
    private Player player;
    private final UUID uuid;
    private final ObjectiveSign objectiveSign;

    PersonalScoreboard(Player player){
        this.player = player;
        uuid = player.getUniqueId();
        objectiveSign = new ObjectiveSign("sidebar", "DevPlugin");

        reloadData();
        objectiveSign.addReceiver(player);
    }

    public void reloadData(){}

    public void setLines(String ip){
    	
    	UHC uhc = UHC.getInstance();
    	if (uhc.getGameManager().hasGame()) {
    		
    		Game game = uhc.getGameManager().getGame();
    		GameState gameState = game.getState();
    		
            objectiveSign.setDisplayName(game.getName());
            
            Integer line = 0;
            
            objectiveSign.setLine(line, "§f");
            line++;
            
            if (game.hasHost()) {
            	objectiveSign.setLine(line, Lang.SB_HOST.get().replace(LangTag.PLAYER_NAME.toString(), game.getHost().getName()));
            	objectiveSign.setLine(line + 1, "§6");
            	line = line + 2;
            }
         
            
            if (gameState == GameState.GAME) {
                objectiveSign.setLine(line, Lang.SB_EPISODE.get());
                line++;
            }
            
            if (!game.hasTeam()) {
                objectiveSign.setLine(line, Lang.SB_PLAYERS.get());
            } else {
            	objectiveSign.setLine(line, Lang.SB_TEAMS.get());
            }
            
            line++;
            
            if (!game.isStart()) {
                
                if (gameState == GameState.LOBBY) objectiveSign.setLine(line, Lang.SB_WAITING.get());
                if (gameState == GameState.STARTING) objectiveSign.setLine(line, Lang.SB_STARTING.get());
                if (gameState == GameState.LOADING) objectiveSign.setLine(line, Lang.SB_LOADING.get());
                
                line++;
            }
            
            if (gameState == GameState.GAME) {
                objectiveSign.setLine(line, "§7");
                objectiveSign.setLine(line + 1, Lang.SB_TIMER.get());
                objectiveSign.setLine(line + 2, Lang.SB_BORDER.get());
                
                line = line + 3;
                
                //ASSASSINS
                if (ScenariosManager.ASSASSINS.isActivated(game)) {
                	if (game.isHere(player.getUniqueId())) {
                		UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
                		
                		if (ScenariosManager.ASSASSINS.hasTarget(p)) {
                			
                			objectiveSign.setLine(line, "§a");
                			
                			UHCPlayer t = ScenariosManager.ASSASSINS.getTarget(p);
                            objectiveSign.setLine(line + 1, Lang.SB_TARGET_NAME.get().replace(LangTag.PLAYER_NAME.toString(), t.getDisplayName()));
                            
                            Integer distance = ScenariosManager.ASSASSINS.getDistance(p);
                            
                            if (distance == -1) objectiveSign.setLine(line + 2, Lang.SB_TARGET_DISTANCE.get().replace(LangTag.VALUE.toString(), "§k0000"));
                            else objectiveSign.setLine(line + 2, Lang.SB_TARGET_DISTANCE.get().replace(LangTag.VALUE.toString(), distance.toString()));
                			line = line + 3;
                		}
                	}
                }
                
               
            }
            
            //IP
            
            if (uhc.getScoreboardManager().showIP()) {
                objectiveSign.setLine(line, "§b");
                objectiveSign.setLine(line + 1, ip);
                line = line + 2;
            }
            
            //UPDATE
            objectiveSign.subString(line);
            objectiveSign.updateLines();
            
    	} else {
    		 objectiveSign.removeReceiver(player);
    	}
    }

    public void onLogout(){
        objectiveSign.removeReceiver(Bukkit.getServer().getOfflinePlayer(uuid));
    }
}