package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiClickEvent;
import fr.aiidor.uhc.inventories.GuiManager;

public class RodLess extends Scenario {
	
	private GuiBuilder gui;
	
	private Boolean rod;
	private Boolean snowball;
	private Boolean egg;
	
	private Boolean pvpOnly;
	
	public RodLess(ScenariosManager manager) {
		super(manager);
		
		rod = false;
		snowball = false;
		egg = false;
		
		pvpOnly = true;
		
		gui = new GuiBuilder() {
			
			@Override
			public Boolean titleIsDynamic() {
				return false;
			}
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				ItemStack clicked = event.getItemClicked();
				
				e.setCancelled(true);
				
				if (e.getSlot() == 0 && clicked.getType() == Material.FISHING_ROD) {
					rod = !rod;
					update();
					return;
				}
				
				if (e.getSlot() == 1 && clicked.getType() == Material.SNOW_BALL) {
					snowball = !snowball;
					update();
					return;
				}
				
				if (e.getSlot() == 2 && clicked.getType() == Material.EGG) {
					egg = !egg;
					update();
					return;
				}
				
				if (e.getSlot() == 7 && clicked.getType() == Material.BOOK) {
					pvpOnly = !pvpOnly;
					update();
					return;
				}
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
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
						{"C", "B", "O", " ", " ", " ", " ", "P", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("C", getConfigItem(Material.FISHING_ROD, Lang.ROD.get(), rod));
				dictionnary.put("B", getConfigItem(Material.SNOW_BALL, Lang.SNOWBALL.get(), snowball));
				dictionnary.put("O", getConfigItem(Material.EGG, Lang.EGG.get(), egg));
				
				dictionnary.put("P", getConfigItem(Material.BOOK, Lang.PVP_ONLY.get(), pvpOnly));
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		
		if (rod) lore.add("§7" + Lang.removeColor(Lang.ROD.get()) + " §8» §a✔");
		else lore.add("§7" + Lang.removeColor(Lang.ROD.get()) + " §8» §c✘");
		
		if (snowball) lore.add("§7" + Lang.removeColor(Lang.SNOWBALL.get()) + " §8» §a✔");
		else lore.add("§7" + Lang.removeColor(Lang.SNOWBALL.get()) + " §8» §c✘");
		
		if (egg) lore.add("§7" + Lang.removeColor(Lang.EGG.get()) + " §8» §a✔");
		else lore.add("§7" + Lang.removeColor(Lang.EGG.get()) + " §8» §c✘");
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "rodless";
	}
	
	@Override
	public Material getIcon() {
		return Material.FISHING_ROD;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP);
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	public Boolean canDamage(Entity damager, Entity damaged) {
		
		if (!(damaged instanceof Player) && pvpOnly) return true;
		
		if (!rod && damager instanceof FishHook) {
			return false;
		}
		
		if (!snowball && damager instanceof Snowball) {
			return false;
		}
		
		if (!egg && damager instanceof Egg) {
			return false;
		}
		
		return true;
	}
}
