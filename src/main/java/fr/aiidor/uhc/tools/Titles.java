package fr.aiidor.uhc.tools;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;

public class Titles {
	
	public Player player;
	
	public Titles(Player player) {
		this.player = player;
	}
	
	public void sendTitle(String title, String subtitle) {
		sendTitle(title, subtitle, 60);
	}
	
    public void sendTitle(String title, String subtitle, int ticks) {
    	sendTitle(title, subtitle, 20, ticks, 20);
    }
    
    public void sendTitle(String title, String subtitle, int appear, int ticks, int disappear) {
    	
        IChatBaseComponent basetitle = ChatSerializer.a("{\"text\": \"" + title + "\"}");
        IChatBaseComponent basesubtitle = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
        
        PacketPlayOutTitle titlepacket = new PacketPlayOutTitle(EnumTitleAction.TITLE, basetitle);
        PacketPlayOutTitle subtitlepacket = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, basesubtitle);
        
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitlepacket);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlepacket);
        
        sendTime(appear, ticks, disappear);
    }
   
    private void sendTime(int appear, int ticks, int disappear){
        PacketPlayOutTitle titlepacket = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, appear, ticks, disappear);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(titlepacket);
    }
    
    public void sendActionText(String message){
    	
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(message), (byte)2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
