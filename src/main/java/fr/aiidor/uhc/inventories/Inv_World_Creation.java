package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;
import fr.aiidor.uhc.world.UHCWorld;
import fr.aiidor.uhc.world.WorldPanel;

public class Inv_World_Creation extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_W_CREATION.get();
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{"C", "C", "B", "B",    "T", "B", "B", "C", "D"},
				{"C", "O", "N", "E",    "B", " ", " ", " ", "C"},
				{"B", "SO", "SN", "SE", "B", " ", " ", " ", "B"},
				{"B", "TO", "TN", "TE", "B", " ", " ", " ", "B"},
				{"C", "GO", "GN", "GE", "B", " ", " ", " ", "C"},
				{"C", "C", "B", "B",    "W", "B", "B", "C", "X"}
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			Game game = UHC.getInstance().getGameManager().getGame();
			
			dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
			dictionary.put("X", getBackIcon());
			
			dictionary.put("C", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 3, " ").getItem());
			dictionary.put("B", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 9, " ").getItem());
			
			dictionary.put("W", new ItemBuilder(Material.SLIME_BALL, Lang.INV_W_CREATION.get() + " ?").getItem());
			
			dictionary.put("D", new ItemBuilder(Material.DARK_OAK_DOOR_ITEM, Lang.INV_W_DELETE.get()).getItem());
			
			if (game.hasWorldPanel()) {
				WorldPanel panel = game.getWorldPanel();
				
				dictionary.put("T", new ItemBuilder(Material.NAME_TAG, "§7-==§6 " + panel.overworld_name + " §7==-").getItem()); 
				
				dictionary.put("O", new ItemBuilder(Material.GRASS, Lang.INV_W_OVERWORLD.get()).getItem()); 
				dictionary.put("N", new ItemBuilder(Material.NETHERRACK, Lang.INV_W_NETHER.get()).getItem()); 
				dictionary.put("E", new ItemBuilder(Material.ENDER_STONE, Lang.INV_W_END.get()).getItem()); 
				
				dictionary.put("SO", new ItemBuilder(Material.PAPER, Lang.INV_W_SEED.get()).setLore(panel.overworld_seed == 0 ? Lang.INV_RANDOM.get() : "§a" + panel.overworld_seed).getItem()); 
				dictionary.put("SN", new ItemBuilder(Material.PAPER, Lang.INV_W_SEED.get()).setLore(panel.nether_seed == 0 ? Lang.INV_RANDOM.get() : "§a" + panel.nether_seed).getItem()); 
				dictionary.put("SE", new ItemBuilder(Material.PAPER, Lang.INV_W_SEED.get()).setLore(panel.end_seed == 0 ? Lang.INV_RANDOM.get() : "§a" + panel.end_seed).getItem()); 
				
				dictionary.put("TO", new ItemBuilder(Material.PAPER, Lang.INV_W_TYPE.get()).setLore("§6" + panel.overworld_type.getName()).getItem()); 
				dictionary.put("TN", new ItemBuilder(Material.PAPER, Lang.INV_W_TYPE.get()).setLore("§6" + panel.nether_type.getName()).getItem()); 
				dictionary.put("TE", new ItemBuilder(Material.PAPER, Lang.INV_W_TYPE.get()).setLore("§6" + panel.end_type.getName()).getItem()); 
				
				dictionary.put("GO", getConfigItem(Lang.INV_W_GENERATE_STRUCTURE.get(), panel.overworld_structure)); 
				dictionary.put("GN", getConfigItem(Lang.INV_W_GENERATE_STRUCTURE.get(), panel.nether_structure)); 
				dictionary.put("GE", getConfigItem(Lang.INV_W_GENERATE_STRUCTURE.get(), panel.end_structure)); 
			}
		}
		
		return dictionary;
	}
	
	private WorldType getNextWorldType(WorldType type) {
		List<WorldType> worldTypes = new ArrayList<WorldType>();
		
		for (WorldType wt : WorldType.values()) { worldTypes.add(wt); }
		
		Integer i = worldTypes.indexOf(type) + 1 >= worldTypes.size() ? 0 : worldTypes.indexOf(type) + 1;
		return worldTypes.get(i);
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		WorldPanel panel = event.getGame().getWorldPanel();
		
		if (!event.getGame().hasWorldPanel()) {
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
		
		if (!event.getGame().isWaiting()) {
			
			event.getGame().setWorldPanel(null);
			event.getPlayer().sendMessage(Lang.ST_ERROR_OPTION_START.get());
			event.getPlayer().closeInventory();
			return;
		}
		
		for (Environment en : Environment.values()) {
			if (Bukkit.getWorld(panel.getWorldName(en)) != null) {
				event.getPlayer().sendMessage(Lang.ST_ERROR_WORLD_EXIST.get());
				event.getPlayer().closeInventory();
				return;
			}
		}
		
		if (e.getSlot() == 8) {
			event.getGame().setWorldPanel(null);
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
		
		if (e.getSlot() == 28) {
			
			panel.overworld_type = getNextWorldType(panel.overworld_type);
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 29) {
			
			panel.nether_type = getNextWorldType(panel.nether_type);
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 30) {
			
			panel.end_type = getNextWorldType(panel.end_type);
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 37) {
			panel.overworld_structure = !panel.overworld_structure;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 38) {
			panel.nether_structure = !panel.nether_structure;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 39) {
			panel.end_structure = !panel.end_structure;
			playClickSound(event.getPlayer());
			update();
			return;
		}
		
		if (e.getSlot() == 49) {

			UHCWorld w = new UHCWorld(panel.overworld_name, panel.getWorldName(panel.main_dimension));
			
			playClickSound(event.getPlayer());
			event.getPlayer().closeInventory();

			if (!w.isGenerate()) w.generate(event.getGame().getWorldPanel());
			
			event.getGame().setWorldPanel(null);
			if (w.isGenerate()) event.getGame().addUHCWorld(w);
			return;
		}
		
		if (e.getSlot() == 53) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG_WORLDS.getInventory());
			return;
		}
	}
}
