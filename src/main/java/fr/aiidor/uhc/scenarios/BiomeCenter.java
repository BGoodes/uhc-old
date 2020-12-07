package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.comparators.BiomeComparator;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.utils.ItemBuilder;
import fr.aiidor.uhc.utils.MCMath;

public class BiomeCenter extends Scenario {
	
	private Biome biome;
	private Integer distance;
	
	private Gui gui;
	
	public BiomeCenter(ScenariosManager manager) {
		super(manager);
		
		biome = Biome.PLAINS;
		distance = 250;
		
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
				
				if (e.getSlot() == 0) {
					
					if (e.getClick() == ClickType.LEFT) biome = getNextBiome();
					else if (e.getClick() == ClickType.RIGHT) biome = getLastBiome();
					else return;
					
					update();
					return;
				}
				
				if (e.getSlot() == 2) {
					
					distance -= 50;
					if (distance < 100) distance = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				
				if (e.getSlot() == 3) {
					
					distance-=10;
					if (distance < 100) distance = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					distance+=10;
					if (distance > 3000) distance = 3000;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 6) {
					
					distance += 50;
					if (distance > 3000) distance = 3000;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
			}
			
			public Biome getNextBiome() {
				List<Biome> biomes = Arrays.asList(Biome.values());
				biomes.sort(new BiomeComparator());
				
				Integer index = biomes.indexOf(biome) + 1;
				if (index >= biomes.size()) index = 0;
				
				return biomes.get(index);
			}
			
			public Biome getLastBiome() {
				List<Biome> biomes = Arrays.asList(Biome.values());
				biomes.sort(new BiomeComparator());
				
				Integer index = biomes.indexOf(biome) - 1;
				if (index < 0) index = biomes.size() - 1;
				
				return biomes.get(index);
			}
			
			@Override
			public String getTitle() {
				return getName();
			}
			
			@Override
			public String[][] getMatrix() {

				String[][] items = {
						{"B", "G", "--", "-", "F", "+", "++", "G", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				String distanceLore = Lang.BIOME_DISTANCE.get().replace(LangTag.VALUE.toString(), distance.toString());
				String biomeLore = Lang.BIOME_TYPE.get().replace(LangTag.VALUE.toString(), biome.name());
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("G", getGlass((byte) 13));
				
				dictionnary.put("B", new ItemBuilder(Material.MAP, biomeLore).getItem()); 
				dictionnary.put("F", new ItemBuilder(Material.COMPASS, distanceLore).getItem()); 
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "§c-10").setLore(Arrays.asList(distanceLore)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "§a+10").setLore(Arrays.asList(distanceLore)).getItem());
				dictionnary.put("--",  new ItemBuilder(Material.STONE_BUTTON, "§c-50").setLore(Arrays.asList(distanceLore)).getItem());
				dictionnary.put("++",  new ItemBuilder(Material.STONE_BUTTON, "§a+50").setLore(Arrays.asList(distanceLore)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.BIOME_TYPE.get().replace(LangTag.VALUE.toString(), biome.name()));
		lore.add(Lang.BIOME_DISTANCE.get().replace(LangTag.VALUE.toString(), distance.toString()));
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "biome-center";
	}

	@Override
	public Material getIcon() {
		return Material.LOG_2;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.GENERATION);
	}
	
	@Override
	public Boolean compatibleWith(Scenario scenario) {
		if (scenario.equals(ScenariosManager.HELL)) return false;
		return true;
	}
	
	public void setBiome(Game game, Block block) {
		Vector vector = block.getLocation().toVector();
		
		if (MCMath.distance2D(vector, game.getSettings().map_center) <= distance) {
			block.setBiome(biome);
		}
	}
	
	@Override
	public Boolean needWorldGeneration() {
		return true;
	}
}
