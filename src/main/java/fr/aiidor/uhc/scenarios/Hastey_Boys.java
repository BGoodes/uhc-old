package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiClickEvent;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Hastey_Boys extends Scenario {

	private Gui gui;
	
	public Hastey_Boys(ScenariosManager manager) {
		super(manager);
		
		enchants = new HashMap<Enchantment, Integer>();
		
		enchants.put(Enchantment.DIG_SPEED, 3);
		enchants.put(Enchantment.DURABILITY, 1);
		
		//SILK_TOUCH, DURABILITY, DIG_SPEED, LOOT_BONUS_BLOCKS
		
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
				
				if (event.getItemClicked().getType() == Material.ENCHANTED_BOOK && event.getItemClicked().getItemMeta().hasDisplayName()) {
					
					Enchantment enchant = null;
					
					switch (e.getSlot()) {
						case 0: enchant = Enchantment.DIG_SPEED; break;
						case 1: enchant = Enchantment.DURABILITY; break;	
						case 2: enchant = Enchantment.LOOT_BONUS_BLOCKS; break;
						case 3: enchant = Enchantment.SILK_TOUCH; break;
					
						default: break;
					}
					
					if (enchant != null) {
						if (e.getClick() == ClickType.LEFT) {
							addEnchantLevel(enchant);
						}
						
						if (e.getClick() == ClickType.RIGHT) {
							removeEnchantLevel(enchant);
						}
					}
					
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
						{"E", "U", "F", "S", " ", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				dictionnary.put("E", getItem(Enchantment.DIG_SPEED));
				dictionnary.put("U", getItem(Enchantment.DURABILITY));
				dictionnary.put("F", getItem(Enchantment.LOOT_BONUS_BLOCKS));
				dictionnary.put("S", getItem(Enchantment.SILK_TOUCH));
				
				return dictionnary;
			}
		};
	}
	
	private String[] levels = {"0", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
	
	private ItemStack getItem(Enchantment enchant) {
		ItemBuilder builder = new ItemBuilder(Material.ENCHANTED_BOOK, "§d" + Lang.valueOf(enchant.getName()).get());
		List<String> lore = new ArrayList<String>();
		
		if (getEnchantLevel(enchant) == 0) lore.add(Lang.INV_OFF.get());
		else lore.add(Lang.ENCHANT_LEVEL.get().replace(LangTag.VALUE.toString(), levels[getEnchantLevel(enchant)]));
		
		lore.add("");
		lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), " 1 " + Lang.LEVEL.get() + "."));
		lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), " 1 " + Lang.LEVEL.get() + "."));
		
		builder.setLore(lore);
		
		return builder.getItem();
	}
	
	private Integer getEnchantLevel(Enchantment enchant) {
		if (!enchants.containsKey(enchant)) return 0;
		else return enchants.get(enchant);
	}
	
	private void addEnchantLevel(Enchantment enchant) {
		
		if (getEnchantLevel(enchant) + 1 >= enchant.getMaxLevel()) {
			enchants.put(enchant, enchant.getMaxLevel());
			return;
		}
		
		if (enchants.containsKey(enchant)) {
			enchants.put(enchant, enchants.get(enchant) + 1);
		}
		else {
			enchants.put(enchant, 1);
		}
	}
	
	private void removeEnchantLevel(Enchantment enchant) {
		
		if (getEnchantLevel(enchant) - 1 <= 0) {
			enchants.remove(enchant);
			return;
		}
		
		if (enchants.containsKey(enchant)) {
			enchants.put(enchant, enchants.get(enchant) - 1);
		}
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		if (enchants.isEmpty()) lore.add(Lang.INV_OFF.get());
		else {
			
			lore.add("§b" + Lang.ENCHANTMENTS.get() + " :");
			
			for (Entry<Enchantment, Integer> map : enchants.entrySet()) {
				lore.add("§7" + Lang.valueOf(map.getKey().getName()).get() + " §a" +  levels[map.getValue()]);
			}
		}
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "hastey-boys";
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public Material getIcon() {
		return Material.DIAMOND_PICKAXE;
	}
	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	
	private HashMap<Enchantment, Integer> enchants;
	
	public ItemStack setEnchants(ItemStack tool) {
		ItemMeta toolM = tool.getItemMeta();
		
		for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
			toolM.addEnchant(enchant.getKey(), enchant.getValue(), true);
		}
		
		tool.setItemMeta(toolM);
		return tool;
	}
}
