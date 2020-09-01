package fr.aiidor.uhc.enums;

import java.util.HashMap;

import org.bukkit.entity.Player;

public enum ActionChat {
	
	SCENARIO_SEARCH, WORLD_CREATION, WORLD_SEED, DELETE_WORLD;
	
	private final String action_message;
	
	private ActionChat() {
		this.action_message = Lang.format((String) UHCFile.LANG.getJSONObject("message/action-chat/" + name()).get("action-message"));
	}
	
	public Boolean hasActionMessage() {
		return action_message != null;
	}
	
	public String getActionMessage() {
		return action_message;
	}
	
	private static HashMap<Player, ActionChat> players = new HashMap<Player, ActionChat>();
	
	public static Boolean hasActionChat(Player player) {
		return players.containsKey(player);
	}
	
	public static ActionChat getActionChat(Player player) {
		return players.get(player);
	}
	
	public static void setActionChat(Player player, ActionChat a) {
		players.put(player, a);
	}
	
	public static void AddActionChat(Player player, ActionChat a) {
		if (hasActionChat(player)) removeActionChat(player);
		
		players.put(player, a);
		if (a.hasActionMessage()) player.sendMessage(a.getActionMessage());
	}
	
	public static void removeActionChat(Player player) {
		players.remove(player);
		player.sendMessage(Lang.AC_CANCEL_MESSAGE.get());
	}
	
	public static void onLogout(Player player) {
		if (hasActionChat(player)) removeActionChat(player);
	}
}
