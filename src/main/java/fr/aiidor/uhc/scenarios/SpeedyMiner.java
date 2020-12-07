package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class SpeedyMiner extends Scenario {
	
	private Gui gui;
	
	public Integer y;
	private HashMap<PotionEffectType, Integer> effects;
	
	public SpeedyMiner(ScenariosManager manager) {
		super(manager);
		
		effects = new HashMap<PotionEffectType, Integer>();
		effects.put(PotionEffectType.SPEED, 1);
		
		y = 32;
		
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
				
				if (event.getItemClicked().getType() == Material.POTION && event.getItemClicked().getItemMeta().hasDisplayName()) {
					
					PotionEffectType pe = null;
					
					switch (e.getSlot()) {
						case 0: pe = PotionEffectType.SPEED; break;
						case 1: pe = PotionEffectType.FAST_DIGGING; break;	
						case 2: pe = PotionEffectType.DAMAGE_RESISTANCE; break;
					
						default: break;
					}
					
					if (pe != null) {
						
						Integer level = getPotionLevel(pe);
						
						if (e.getClick() == ClickType.LEFT) {
							level++;
						}
						
						if (e.getClick() == ClickType.RIGHT) {
							level--;
						}
						
						if (level > 5) level = 5;
						
						if (level <= 0) {
							level = 0;
							effects.remove(pe);
						}
						
						else effects.put(pe, level);
					}
					
					playClickSound(event.getPlayer());
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
						{"S", "H", "R", " ", " ", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				dictionnary.put("S", getItem(PotionEffectType.SPEED));
				dictionnary.put("H", getItem(PotionEffectType.FAST_DIGGING));
				dictionnary.put("R", getItem(PotionEffectType.DAMAGE_RESISTANCE));
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		if (effects.isEmpty()) lore.add(Lang.INV_OFF.get());
		else {
			
			lore.add("§b" + Lang.POTIONS.get() + " :");
			
			for (Entry<PotionEffectType, Integer> map : effects.entrySet()) {
				lore.add("§7" + Lang.valueOf(map.getKey().getName()).get() + " §a" + map.getValue());
			}
		}
		
		return lore;
	}
	
	private ItemStack getItem(PotionEffectType pe) {
		
		ItemBuilder builder = new ItemBuilder(Material.POTION, getPotionLevel(pe), "§d" + Lang.valueOf(pe.getName()).get());
		List<String> lore = new ArrayList<String>();
		
		if (getPotionLevel(pe) == 0) lore.add(Lang.INV_OFF.get());
		else lore.add("§7" + Lang.LEVEL.get() + " : §a" + getPotionLevel(pe));
		
		lore.add("");
		lore.add(Lang.INV_LEFT_CLICK.get().replace(LangTag.VALUE.toString(), "1 " + Lang.LEVEL.get() + "."));
		lore.add(Lang.INV_RIGHT_CLICK.get().replace(LangTag.VALUE.toString(), "1 " + Lang.LEVEL.get() + "."));
		
		builder.setLore(lore);
		
		return builder.getItem();
	}
	
	private Integer getPotionLevel(PotionEffectType pe) {
		if (!effects.containsKey(pe)) return 0;
		else return effects.get(pe);
	}
	
	@Override
	public String getID() {
		return "speedy-miner";
	}

	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public Material getIcon() {
		return Material.POTION;
	}

	@Override
	public ItemStack getScenarioIcon(Boolean icon, Boolean category, Boolean config) {
		
		ItemStack item = super.getScenarioIcon(icon, category, config);
		
		if (icon) {
			Potion pot = new Potion(1);
			pot.setType(PotionType.SPEED);
			pot.apply(item);
		}

		return item;
	}
	
	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	
	public void setEffects(UHCPlayer p) {
		if (p.isConnected()) {
			
			if (p.getPlayer().getLocation().getY() <= y) {
				for (Entry<PotionEffectType, Integer> e : effects.entrySet()) {
					if (e.getValue() > 0) p.addPotionEffect(new PotionEffect(e.getKey(), 2*20, e.getValue() - 1, true, true));
				}
			}
		}
	}
}
