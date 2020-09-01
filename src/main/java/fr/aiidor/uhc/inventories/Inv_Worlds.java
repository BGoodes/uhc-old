package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.ActionChat;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;
import fr.aiidor.uhc.world.UHCWorld;

public class Inv_Worlds extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_WORLDS.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{" ", "G", " ", " ", " ", " ", " ", " ", "C"},
				{"G", "G", "G", "G", "G", "G", "G", "G", "G"},
				{" ", "G", " ", " ", " ", " ", " ", " ", "X"}
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			
			dictionary.put("G", getGlass((byte) 9));
			
			dictionary.put("C", new ItemBuilder(Material.WORKBENCH, Lang.INV_W_CREATION.get()).getItem());
		}
		
		return dictionary;
	}
	
	@Override
	public Inventory getInventory() {
		Inventory inv = super.getInventory();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			World lobby = UHC.getInstance().getSettings().getLobbyWorld();

			setLobbyItem(inv, lobby);
			
			int i = 2;
			for (UHCWorld w : game.getUHCWorlds()) {
				setWorldItem(inv, i, w, game);
				i++;
			}
		}
		
		return inv;
	}
	
	private void setLobbyItem(Inventory inv, World world) {
		ItemBuilder item = new ItemBuilder(Material.COMPASS, "§a" + world.getName());
		item.setLore(Lang.INV_W_LOBBY_WORLD.get());
		inv.setItem(0, item.getItem());
		
		ItemBuilder sign = new ItemBuilder(Material.SIGN);
		inv.setItem(18, sign.getItem());
	}
	
	private void setWorldItem(Inventory inv, Integer i, UHCWorld w, Game game) {
		
		Boolean mainWorld = game.getMainWorld().equals(w);
		
		ItemBuilder item = new ItemBuilder(Material.EMPTY_MAP, "§7" + w.getMainWorldName());
		
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		String overworld = w.getOverworldState() ? Lang.INV_ON.get() : Lang.INV_OFF.get();
		lore.add(Lang.INV_W_OVERWORLD.get() + " : " + overworld);
		
		String nether = w.getNetherState() ? Lang.INV_ON.get() : Lang.INV_OFF.get();
		lore.add(Lang.INV_W_NETHER.get() + " : " + nether);
		
		String end = w.getEndState()? Lang.INV_ON.get() : Lang.INV_OFF.get();
		lore.add(Lang.INV_W_END.get() + " : " + end);
		
		lore.add(" ");
		if (mainWorld) {
			lore.add(Lang.INV_W_MAIN_WORLD.get());
		}
		
		lore.add(Lang.INV_RIGHT_CLICK_OPTION.get());
		
		item.setLore(lore);
		inv.setItem(i, item.getItem());
		
		inv.setItem(i + 9*2, getConfigItem("§7" + w.getMainWorldName(), mainWorld));
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		Game game = event.getGame();
		ItemStack clicked = event.getItemClicked();
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());

			if (!event.getGame().isWaiting()) {
				event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_START.get());
				event.getPlayer().closeInventory();
				playClickSound(event.getPlayer());
				return;
			}
			
			if (game.getUHCWorlds().size() >= 6) {
				game.setWorldPanel(null);
				event.getPlayer().sendMessage(Lang.ST_ERROR_WORLD_CREATION_SIZE.get());
				event.getPlayer().closeInventory();
				playClickSound(event.getPlayer());
				return;
			}
			
			if (game.hasWorldPanel()) {
				event.getPlayer().openInventory(GuiManager.INV_WORLD_CREATION.getInventory());
				
			} else {
				event.getPlayer().closeInventory();
				ActionChat.AddActionChat(event.getPlayer(), ActionChat.WORLD_CREATION);
			}
			
			return;
		}
		
		if (e.getSlot() >= 2 && e.getSlot() < 8 && e.getClick() == ClickType.RIGHT) {
			if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
				for (UHCWorld w : game.getUHCWorlds()) {
					if (clicked.getItemMeta().getDisplayName().equals("§7" + w.getMainWorldName())) {
						
						if (!event.getGame().isWaiting()) {
							event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_START.get());
							event.getPlayer().closeInventory();
							playClickSound(event.getPlayer());
							return;
						}
						
						playClickSound(event.getPlayer());
						event.getPlayer().openInventory(new Inv_World_Settings().getInventory(w));
						return;
					}
				}
			}
		}
		
		if (e.getSlot() >= 20 && e.getSlot() < 26) {
			if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
				for (UHCWorld w : game.getUHCWorlds()) {
					if (clicked.getItemMeta().getDisplayName().equals("§7" + w.getMainWorldName())) {
						
						if (!event.getGame().isWaiting()) {
							event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_START.get());
							event.getPlayer().closeInventory();
							playClickSound(event.getPlayer());
							return;
						}
						
						game.setMainWorld(w);
						
						playClickSound(event.getPlayer());
						update();
						return;
					}
				}
			}
		}
		
		if (e.getSlot() == 26) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
	}
}
