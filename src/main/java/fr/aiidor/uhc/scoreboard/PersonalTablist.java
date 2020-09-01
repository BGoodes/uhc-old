package fr.aiidor.uhc.scoreboard;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

public class PersonalTablist {
	
	private Player player;
	
	public PersonalTablist(Player player) {
		this.player = player;
	}
	
	public void update() {
		
		StringBuilder top = new StringBuilder();
		StringBuilder bottom = new StringBuilder();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
				
			top.append(game.getName());
			
			Integer tps = 0;
			if (MinecraftServer.getServer().recentTps.length >= 1) {
				tps = (int) MinecraftServer.getServer().recentTps[MinecraftServer.getServer().recentTps.length - 1];
			}
			
			top.append("\n" + Lang.TL_CONNEXION.get()
					.replace(LangTag.VALUE_1.toString(), ""+((CraftPlayer) player).getHandle().ping)
					.replace(LangTag.VALUE_2.toString(), ""+tps));
		}
		
		set(top.toString(), bottom.toString());
	}
	
	private void set(String top, String bottom) {
		
		PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
		Object header = new ChatComponentText(top);
		Object footer = new ChatComponentText(bottom + "\n§eCreator : B. Goodes");
		try {
			Field a = packet.getClass().getDeclaredField("a");
			Field b = packet.getClass().getDeclaredField("b");
			a.setAccessible(true);
			b.setAccessible(true);
			
			a.set(packet, header);
			b.set(packet, footer);
			
			((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		} catch (NoSuchFieldException | IllegalAccessException exception) {
			exception.printStackTrace();
		}
	}
}
