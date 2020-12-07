package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class BestPve extends Scenario {
	
	private List<UHCPlayer> bestPve;
	
	public Integer time = 20;
	
	private Gui gui;
	
	public BestPve(ScenariosManager manager) {
		super(manager);
		
		bestPve = new ArrayList<UHCPlayer>();
		
		gui = new GuiBuilder() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 2) {
					
					time -= 5;
					if (time < 1) time = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				
				if (e.getSlot() == 3) {
					
					time--;
					if (time < 1) time = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					time++;
					if (time > 60) time = 60;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 6) {
					
					time += 5;
					if (time > 60) time = 60;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
			}
			
			@Override
			public String getTitle() {
				return getName();
			}
			
			@Override
			public String[][] getMatrix() {

				String[][] items = {
						{" ", " ", "--", "-", "F", "+", "++", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				String name = Lang.SCENARIO_PERIOD.get().replace(LangTag.VALUE.toString(), time.toString());
				
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.WATCH, name).getItem()); 
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-1").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+1").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("--",  new ItemBuilder(Material.STONE_BUTTON, "§c-5").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("++",  new ItemBuilder(Material.STONE_BUTTON, "§a+5").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.SCENARIO_PERIOD.get().replace(LangTag.VALUE.toString(), time.toString()));
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "best-pve";
	}

	@Override
	public Material getIcon() {
		return Material.LAVA_BUCKET;
	}

	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.SURVIVAL);
	}
	
	public void increase() {
		for (UHCPlayer p : bestPve) {
			if (p.isConnected()) {
				p.getPlayer().setMaxHealth(p.getPlayer().getMaxHealth() + 2);
				p.getPlayer().sendMessage(Lang.BEST_PVE_INCREASE.get());
			}
		}
	}
	
	public void add(UHCPlayer player) {
		if (!bestPve.contains(player)) bestPve.add(player);
	}
	
	public void remove(UHCPlayer player) {
		if (bestPve.contains(player)) {
			bestPve.remove(player);
			if (player.isConnected()) player.getPlayer().sendMessage(Lang.BEST_PVE_DAMAGE.get());
		}
	}
	
	@Override
	public void reload() {
		bestPve = new ArrayList<UHCPlayer>();
	}
}
