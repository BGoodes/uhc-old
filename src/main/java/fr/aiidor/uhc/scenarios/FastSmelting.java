package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;

public class FastSmelting extends Scenario {
	
	private GuiBuilder gui;
	
	public FastSmelting(ScenariosManager manager) {
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
				
				if (e.getSlot() == 3) {
					
					speed--;
					if (speed < 1) speed = 1;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					speed++;
					if (speed > 15) speed = 15;
					
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
						{" ", " ", " ", "-", "F", "+", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				String name = Lang.FURNACE_SPEED.get().replace(LangTag.VALUE.toString(), speed.toString());
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.FURNACE, name).getItem());
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-1").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+1").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.FURNACE_SPEED.get().replace(LangTag.VALUE.toString(), speed.toString()));
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "fastsmelting";
	}
	
	@Override
	public Material getIcon() {
		return Material.FURNACE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	public void increaseFurnace(Furnace furnace) {
		new FurnaceUpdate(furnace);
	}
	
	public Short speed = 4;
	
	private class FurnaceUpdate extends BukkitRunnable {

		private Furnace furnace;
		
		public FurnaceUpdate(Furnace furnace) {
			this.furnace = furnace;
			
			this.runTaskTimer(UHC.getInstance(), 0, 3);
		}
		
		@Override
		public void run() {
			
			if (furnace.getCookTime() > 0 || furnace.getBurnTime() > 0) {
				
				furnace.setCookTime((short) (furnace.getCookTime() + speed));
				furnace.update();
				
			} else {
				cancel();
			}
		}
		
	}
}
