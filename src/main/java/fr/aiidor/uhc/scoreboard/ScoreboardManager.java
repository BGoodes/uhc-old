package fr.aiidor.uhc.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.UHCFile;

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

//This file has been edited by B. Goodes.

public class ScoreboardManager {
	
    public final Map<UUID, PersonalScoreboard> scoreboards;
    
    private int ipCharIndex;
    private int cooldown;
    
    //PARAMETRE
    private int cooldown_time;
    
    private final ChatColor ipColor;
    private final ChatColor pointColor;
    private final ChatColor fadeColor;
    
    private final String ip;
    private final Boolean showIp;
    private final Boolean fade;
    
    public ScoreboardManager() {
    	
    	ConfigurationSection config = UHCFile.CONFIG.getYamlConfig().getConfigurationSection("Scoreboard.ip");
    	
    	this.ip = config.getString("ip");
    	this.showIp = config.getBoolean("show-ip");
    	
    	this.scoreboards = new HashMap<UUID, PersonalScoreboard>();
    	this.ipCharIndex = 0;
    	this.cooldown = 0;
        
    	this.ipColor = ChatColor.valueOf(config.getString("ip-color.text-color").toUpperCase());
    	
    	//FADE
    	this.pointColor = ChatColor.valueOf(config.getString("fade.ip-fade-color.point").toUpperCase());
    	this.fadeColor = ChatColor.valueOf(config.getString("fade.ip-fade-color.fade").toUpperCase());
        
    	this.cooldown_time = config.getInt("fade.ip-fade-cooldown");
        
    	this.fade = config.getBoolean("fade.ip-fade");
    	
    	Integer ipFadeUnit =  config.getInt("fade.ip-fade-unit");
        
        UHC.getInstance().getScheduledExecutorService().scheduleAtFixedRate(() ->  {
                	
        	String ip = colorIpAt();
        	
        	for (PersonalScoreboard scoreboard : scoreboards.values()) {
        		UHC.getInstance().getExecutorMonoThread().execute(() -> scoreboard.setLines(ip));
        	}
                    
        }, 80, ipFadeUnit, TimeUnit.MILLISECONDS);

        UHC.getInstance().getScheduledExecutorService().scheduleAtFixedRate(() ->  {
                	
        	for (PersonalScoreboard scoreboard : scoreboards.values()) {
        		UHC.getInstance().getExecutorMonoThread().execute(scoreboard::reloadData);
        	}
        }, 1, 1, TimeUnit.SECONDS);
    }

    public void onDisable() {
        scoreboards.values().forEach(PersonalScoreboard::onLogout);
    }

    public void onLogin(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) return;
        scoreboards.put(player.getUniqueId(), new PersonalScoreboard(player));
    }

    public void onLogout(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).onLogout();
            scoreboards.remove(player.getUniqueId());
        }
    }

    public void update(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            scoreboards.get(player.getUniqueId()).reloadData();
        }
    }
    
    public Boolean showIP() {
    	return showIp;
    }
    
    private String colorIpAt() {
    	
    	if (!fade) return ipColor + ip;
    		
        if (cooldown > 0) {
            cooldown--;
            return ipColor + ip;
        }

        StringBuilder formattedIp = new StringBuilder();

        if (ipCharIndex > 0) {
            formattedIp.append(ip.substring(0, ipCharIndex - 1));
            formattedIp.append(fadeColor).append(ip.substring(ipCharIndex - 1, ipCharIndex));
            
        } else {
            formattedIp.append(ip.substring(0, ipCharIndex));
        }

        formattedIp.append(pointColor).append(ip.charAt(ipCharIndex));

        if (ipCharIndex + 1 < ip.length()) {
            formattedIp.append(fadeColor).append(ip.charAt(ipCharIndex + 1));

            if (ipCharIndex + 2 < ip.length()) {
                formattedIp.append(ipColor).append(ip.substring(ipCharIndex + 2));
            }

            ipCharIndex++;
        } else {
            ipCharIndex = 0;
            cooldown = cooldown_time;
        }

        return ipColor + formattedIp.toString();
    }
}
