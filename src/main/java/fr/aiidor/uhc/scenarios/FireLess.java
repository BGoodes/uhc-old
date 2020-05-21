package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiClickEvent;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.tools.ItemBuilder;

public class FireLess extends Scenario {

	private GuiBuilder gui;
	
	private Boolean noFireDamage;
	private Boolean noFireTick;
	public Boolean lava_bucket;
	public Boolean flint_and_steel;
	
	public Boolean player_only;
	
	public FireLess(ScenariosManager manager) {
		super(manager);
		
		noFireDamage = true;
		noFireTick = false;

		
		lava_bucket = true;
		flint_and_steel = true;
		
		player_only = true;
		
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
				
				if (e.getSlot() == 0 && clicked.getType() == Material.BLAZE_POWDER) {
					noFireDamage = !noFireDamage;
					update();
					return;
				}
				
				if (e.getSlot() == 1 && clicked.getType() == Material.FIREBALL) {
					noFireTick = !noFireTick;
					update();
					return;
				}
				
				if (e.getSlot() == 2 && clicked.getType() == Material.LAVA_BUCKET) {
					lava_bucket = !lava_bucket;
					update();
					return;
				}
				
				if (e.getSlot() == 3 && clicked.getType() == Material.FLINT_AND_STEEL) {
					flint_and_steel = !flint_and_steel;
					update();
					return;
				}
				
				if (e.getSlot() == 7 && clicked.getType() == Material.BOOK) {
					player_only = !player_only;
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
						{"F", "T", "B", "S", " ", " ", " ", "P", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("F", getItem(Material.BLAZE_POWDER, Lang.NO_FIRE_DAMAGE.get(), noFireDamage));
				dictionnary.put("T", getItem(Material.FIREBALL, Lang.NO_FIRE_TICK.get(), noFireTick));
				dictionnary.put("B", getItem(Material.LAVA_BUCKET, Lang.LAVA_BUCKET.get(), lava_bucket));
				dictionnary.put("S", getItem(Material.FLINT_AND_STEEL, Lang.FLINT_AND_STEEL.get(), flint_and_steel));
				
				dictionnary.put("P", getItem(Material.BOOK, Lang.PLAYER_ONLY.get(), player_only));
				return dictionnary;
			}
		};
	}
	
	
	private ItemStack getItem(Material mat, String displayName, Boolean on) {
		ItemBuilder builder = new ItemBuilder(mat, displayName);
		List<String> lore = new ArrayList<String>();
		
		if (on) lore.add(Lang.INV_ON.get());
		else lore.add(Lang.INV_OFF.get());
		
		builder.setLore(lore);
		
		return builder.getItem();
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		
		if (noFireDamage) lore.add("§6" + Lang.removeColor(Lang.NO_FIRE_DAMAGE.get()) + " §8» §a✔");
		else lore.add("§6" + Lang.removeColor(Lang.NO_FIRE_DAMAGE.get()) + " §8» §c✘");
		
		if (noFireTick) lore.add("§6" + Lang.removeColor(Lang.NO_FIRE_TICK.get()) + " §8» §a✔");
		else lore.add("§6" + Lang.removeColor(Lang.NO_FIRE_TICK.get()) + " §8» §c✘");
		
		lore.add(" ");
		if (lava_bucket) lore.add("§7" + Lang.removeColor(Lang.LAVA_BUCKET.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.LAVA_BUCKET.get()) + " §8» " + Lang.INV_OFF.get());
		
		if (flint_and_steel) lore.add("§7" + Lang.removeColor(Lang.FLINT_AND_STEEL.get()) + " §8» " + Lang.INV_ON.get());
		else lore.add("§7" + Lang.removeColor(Lang.FLINT_AND_STEEL.get()) + " §8» " + Lang.INV_OFF.get());
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "fireless";
	}
	
	@Override
	public Material getIcon() {
		return Material.BLAZE_POWDER;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.SURVIVAL);
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	public Boolean canDamage(DamageCause cause, Entity damaged) {
		
		if (!(damaged instanceof Player) && player_only) return true;
		
		if (cause == DamageCause.FIRE || cause == DamageCause.LAVA || cause == DamageCause.FIRE_TICK) {
			
			if (noFireDamage) {
				return false;
			}
			
			if (noFireTick && cause == DamageCause.FIRE_TICK) {
				return false;
			}
		}
		
		return true;
	}
}
