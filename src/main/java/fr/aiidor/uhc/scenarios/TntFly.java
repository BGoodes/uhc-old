package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiClickEvent;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.ItemBuilder;

public class TntFly extends Scenario {
	
	private GuiBuilder gui;
	
	public Integer boost = 3;
	public Boolean chain = true;
	public Integer damage_reduction = 25;
	
	public TntFly(ScenariosManager manager) {
		super(manager);
		
		gui = new GuiBuilder() {
			
			@Override
			public Boolean titleIsDynamic() {
				return false;
			}
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				e.setCancelled(true);
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
				
				if (e.getSlot() == 0) {
					if (e.getClick() == ClickType.LEFT) {
							boost++;
					}
					
					if (e.getClick() == ClickType.RIGHT) {
							boost--;
					}
					
					if (boost < 1) boost = 1;
					if (boost > 10) boost = 10;
					
					update();
					return;
				}
				
				if (e.getSlot() == 1) {
					if (e.getClick() == ClickType.LEFT) {
						damage_reduction = damage_reduction + 5;
					}
					
					if (e.getClick() == ClickType.RIGHT) {
							damage_reduction = damage_reduction - 5;
					}
					
					if (damage_reduction < 0) damage_reduction = 0;
					if (damage_reduction > 100) damage_reduction = 100;
					
					update();
					return;
				}
				
				if (e.getSlot() == 2) {
					
					chain = !chain;
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
						{"B", "D", "C", " ", " ", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				List<String> boost_lore = new ArrayList<String>();
				boost_lore.add("§8» §b§lx" + boost);
				boost_lore.add(" ");
				boost_lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1"));
				boost_lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1"));
				
				dictionnary.put("B", new ItemBuilder(Material.QUARTZ, Lang.TNT_BOOST.get()).setLore(boost_lore).getItem());
				
				List<String> damage_lore = new ArrayList<String>();
				
				if (damage_reduction != 0) damage_lore.add("§8» §b§l/" + damage_reduction);
				else damage_lore.add(Lang.INV_OFF.get());
				
				damage_lore.add(" ");
				damage_lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "5"));
				damage_lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "5"));
				
				
				dictionnary.put("D", new ItemBuilder(Material.IRON_CHESTPLATE, Lang.TNT_REDUCTION.get()).setLore(damage_lore).getItem());
				
				List<String> chain_lore = new ArrayList<String>();
				if (chain) chain_lore.add(Lang.INV_ON.get());
				else chain_lore.add(Lang.INV_OFF.get());
				
				dictionnary.put("C", new ItemBuilder(Material.TNT, Lang.TNT_CHAIN.get()).setLore(chain_lore).getItem());
				
				return dictionnary;
			}
		};
	}
	
	
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		
		lore.add("§7" + Lang.removeColor(Lang.TNT_BOOST.get()) + " §8» §ex" + boost);
		
		if (damage_reduction != 0) lore.add("§7" + Lang.removeColor(Lang.TNT_REDUCTION.get()) + " §8» §e/" + damage_reduction);
		else lore.add("§7" + Lang.removeColor(Lang.TNT_REDUCTION.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (chain) lore.add("§7" + Lang.removeColor(Lang.TNT_CHAIN.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.TNT_CHAIN.get()) + " §8» " + Lang.INV_OFF.get());
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "tnt-fly";
	}
	
	@Override
	public Material getIcon() {
		return Material.TNT;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
}
