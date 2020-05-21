package fr.aiidor.uhc.tools;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class IdentityChanger {
	
	public static void changeName(Player player, String name) {
		
		player.setPlayerListName(name);
		
		try {
			Method getHandle = player.getClass().getMethod("getHandle",(Class<?>[]) null);
			try {
				Class.forName("com.mojang.authlib.GameProfile");
			         
			} catch (ClassNotFoundException e) {
				
				return;
			}
			       	
			Object profile = getHandle.invoke(player).getClass()
					.getMethod("getProfile")
					.invoke(getHandle.invoke(player));
			         
			Field ff = profile.getClass().getDeclaredField("name");
			ff.setAccessible(true);
			         
			ff.set(profile, name);
			ff.setAccessible(false);
			
			for (Player all : Bukkit.getOnlinePlayers()) {
				all.hidePlayer(player);
				all.showPlayer(player);
			}
			        
		} catch (NoSuchMethodException | SecurityException| IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchFieldException e) {
			e.printStackTrace();
			return;
		}
	}
}
