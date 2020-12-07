package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.team.UHCTeam;
import fr.aiidor.uhc.utils.ItemBuilder;

public class GuiTeamChoose extends Gui {

	private final HashMap<String, ItemStack> dictionary;
	
	private final Integer[] teams_slots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
	
	private final String[][] matrix = {
			{"G", "G", "L", "L", "L", "L", "L", "G", "G"},
			{"G", " ", " ", " ", " ", " ", " ", " ", "G"}, //10, 11, 12, 13, 14, 15, 16
			{"L", " ", " ", " ", " ", " ", " ", " ", "L"}, //19, 20, 21, 22, 23, 24, 25
			{"L", " ", " ", " ", " ", " ", " ", " ", "L"}, //28, 29, 30, 31, 32, 33, 34
			{"G", " ", " ", " ", " ", " ", " ", " ", "G"}, //37, 38, 39, 40, 41, 42, 43
			{"G", "G", "L", "L", "L", "L", "L", "G", "X"},        // size : 28
	};
	
	//CLAY : GREEN = 13 / RED = 14
	
	public GuiTeamChoose() {
		
		dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getExitIcon());
		
		dictionary.put("L", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 4, " ").getItem());
		dictionary.put("G", new ItemBuilder(Material.STAINED_GLASS_PANE, (byte) 1, " ").getItem());
	}
	
	@Override
	public String getTitle() {
		return Lang.INV_TEAM_CHOOSE.get();
	}
	
	@Override
	public Boolean isSameInventory(Inventory inv) {
		return inv.getName().startsWith(getTitle());
	}
	
	@Override
	public void onClick(GuiClickEvent event) {
		
		InventoryClickEvent e = event.getEvent();
		e.setCancelled(true);
		
		if (!event.getGame().isHere(event.getPlayer().getUniqueId())) return;
		if (!event.getGame().hasTeam()) {
			event.getPlayer().closeInventory();
			return;
		}
		
		ItemStack clicked = event.getItemClicked();
		UHCPlayer player = event.getGame().getUHCPlayer(event.getPlayer().getUniqueId());
		
		if (e.getSlot() == 53) {
			event.getPlayer().closeInventory();
			playClickSound(event.getPlayer());
			return;
		}
		
		Inventory inv = event.getInventory();
		
		if (clicked.getType() == Material.BANNER) {
			
			if (e.getSlot() == 49) {
				
				playClickSound(event.getPlayer());
				
				if (player.hasTeam()) {
					player.leaveTeam(true);
					event.getPlayer().closeInventory();
				}
				return;
			}
			
			if (clicked.getItemMeta().hasDisplayName()) {
				for (UHCTeam t : event.getGame().getTeams()) {
					if (clicked.getItemMeta().getDisplayName().equals(t.getName())) {
						
						playClickSound(event.getPlayer());
						if (!t.isInTeam(player)) t.join(player, true);
						event.getPlayer().closeInventory();
						return;
					}
				}
			}

		}

		if (clicked.getType() == Material.SIGN) {
			if (e.getSlot() == 44) {
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(getPage(inv) + 1, player));
			}
			
			if (e.getSlot() == 35) {
				playClickSound(event.getPlayer());
				event.getPlayer().openInventory(getInventory(getPage(inv) - 1,player));
			}
		}

	}
	
	public Integer getPage(Inventory inv) {
		return Integer.valueOf(getPageValues(inv)[0]);
	}
	
	public Integer getMaxPage(Inventory inv) {
		return Integer.valueOf(getPageValues(inv)[1]);
	}
	
	private String[] getPageValues(Inventory inv) {
		return inv.getTitle().substring(Lang.INV_TEAM_CHOOSE.get().length() + 4).replace(")", "").split("/");
	}
	
	@Override
	public Inventory getInventory(UHCPlayer player) {
		return getInventory(1, player);
	}
	
	public Inventory getInventory(Integer page, UHCPlayer player) {
		
		if (page < 1) page = 1;
		
		if (!UHC.getInstance().getGameManager().hasGame()) return Bukkit.createInventory(null, 54, getTitle());
		Game game = UHC.getInstance().getGameManager().getGame();
		
		if (!game.hasTeam()) return Bukkit.createInventory(null, 54, getTitle());
		
		Integer max_pages = (game.getTeams().size() / 28) + 1;
		
		Inventory inv = Bukkit.createInventory(null, 54, getTitle() + "§b (" + page + "/" + max_pages + ")");
		
		for (int c = 0; c != 6; c++) {
			for (int l = 0; l != 9; l++) {
				String s = matrix[c][l];
				
				if (dictionary.containsKey(s)) {
					inv.setItem(c * 9 + l, dictionary.get(s));
				}
			}
		}
		
		
		//TEAMS
		Integer index = (page - 1) * 28;
		
		for (int i = 0; i < teams_slots.length; i++) {
			int slot = teams_slots[i];
			
			if (game.getTeams().size() > index + i) {
				
				UHCTeam t = game.getTeams().get(index + i);
				
				if (player.hasTeam() && player.getTeam().equals(t)) inv.setItem(slot, getTeamIcon(t, true));
				else inv.setItem(slot, getTeamIcon(t, false));
				
				
			} else {
				break;
			}

		}
		
		if (player.hasTeam()) inv.setItem(49, new ItemBuilder(Material.BANNER, (byte) 15, Lang.INV_TEAM_RANDOM.get() + " §c✘").getItem());
		else inv.setItem(49, new ItemBuilder(Material.BANNER, (byte) 15, Lang.INV_TEAM_RANDOM.get() + " §a✔").setGliding().getItem());
		
		//SIGNS
		if (page < max_pages) {
			inv.setItem(44, new ItemBuilder(Material.SIGN, Lang.INV_NEXT_PAGE.get()).getItem());
		}
		
		if (page > 1) {
			inv.setItem(35, new ItemBuilder(Material.SIGN, Lang.INV_LAST_PAGE.get()).getItem());
		}
		
		return inv;
	}
	
	private ItemStack getTeamIcon(UHCTeam t, Boolean isTeam) {
		ItemBuilder builder = new ItemBuilder(Material.BANNER, t.getColor().getBannerColor(), t.getName());
		
		if (isTeam) {
			builder.setDisplayName(t.getName() + " §a✔");
			builder.setGliding();
		}
		
		List<String> lore = new ArrayList<String>();
		
		lore.add(Lang.TEAM_PLAYERS.get().replace(LangTag.TEAM_PLAYER_COUNT.toString(), t.getPlayerCount().toString()
										.replace(LangTag.TEAM_NAME.toString(), t.getName())));
		
		Integer i = 0;
		for (UHCPlayer p : t.getPlayers()) {
			if (i == 10) {
				lore.add("§7...");
				break;
			}
			
			lore.add("§8» §7" + p.getDisplayName());
			
			i++;
		}
		
		builder.setLore(lore);
		
		return builder.getItem();
	}

	@Override
	public Inventory getInventory() {
		return Bukkit.createInventory(null, 54, getTitle());
	}
}
