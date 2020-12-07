package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class FlowerPower extends DropScenario {
	
	private int probability = 100;
	
	private Gui gui;
	
	public FlowerPower(ScenariosManager manager) {
		super(manager);
		
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
					
					probability = probability - 5;
					if (probability < 1) probability = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				
				if (e.getSlot() == 3) {
					
					probability--;
					if (probability < 1) probability = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					probability++;
					if (probability > 100) probability = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 6) {
					
					probability = probability + 5;
					if (probability > 100) probability = 100;
					
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
				
				String name = Lang.FLOWER_DROP.get().replace(LangTag.VALUE.toString(), probability + "%");
				
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.RED_ROSE, name, (byte) 1).getItem()); 
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-1%").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+1%").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("--",  new ItemBuilder(Material.STONE_BUTTON, "§c-5%").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("++",  new ItemBuilder(Material.STONE_BUTTON, "§a+5%").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.FLOWER_DROP.get().replace(LangTag.VALUE.toString(), probability + "%"));
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "flower-power";
	}

	@Override
	public Material getIcon() {
		return Material.RED_ROSE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.FUN);
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	private List<Material> flowers = Arrays.asList(Material.RED_ROSE, Material.YELLOW_FLOWER, Material.DOUBLE_PLANT);
	private List<Byte> double_flowers = Arrays.asList((byte) 0, (byte) 1, (byte) 4, (byte) 5);
	
	@SuppressWarnings("deprecation")
	public Boolean dropItem(Block b) {
		
		if (!flowers.contains(b.getType())) return false;
		if (b.getType() == Material.DOUBLE_PLANT && !double_flowers.contains(b.getData())) return false;
		
		if (probability == 100 || new Random().nextInt(100) <= probability) {
			Location loc = b.getLocation().clone().add(0.5, 0.5, 0.5);
			
			b.getWorld().dropItem(loc, getRandomItemstack());
			return true;
			
		} else {	
			b.setType(Material.AIR);
		}
		
		return false;
	}

}
