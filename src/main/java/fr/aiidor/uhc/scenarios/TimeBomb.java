package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
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
import fr.aiidor.uhc.tools.ItemBuilder;

public class TimeBomb extends Scenario {
	
	public Integer boom_time;
	private GuiBuilder gui;
	
	public TimeBomb(ScenariosManager manager) {
		super(manager);
		
		boom_time = 30;
		
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
				
				if (e.getSlot() == 2) {
					
					boom_time -= 5;
					if (boom_time < 0) boom_time = 0;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 3) {
					
					boom_time--;
					if (boom_time < 0) boom_time = 0;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 5) {
					
					boom_time++;
					if (boom_time > 100) boom_time = 100;
					
					event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.WOOD_CLICK, 0.5f, 1f);
					update();
					return;
				}
				
				if (e.getSlot() == 6) {
					
					boom_time += 5;
					if (boom_time > 100) boom_time = 100;
					
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
				
				String name = Lang.INV_TIME_OPTION.get().replace(LangTag.VALUE.toString(), boom_time.toString() + "s");
				
				dictionnary.put("X", getBackIcon());
				dictionnary.put("F", new ItemBuilder(Material.WATCH, name).getItem());
				
				dictionnary.put("-",  new ItemBuilder(Material.WOOD_BUTTON, "븎-1").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("+",  new ItemBuilder(Material.WOOD_BUTTON, "브+1").setLore(Arrays.asList(name)).getItem());
				
				dictionnary.put("--",  new ItemBuilder(Material.STONE_BUTTON, "븎-5").setLore(Arrays.asList(name)).getItem());
				dictionnary.put("++",  new ItemBuilder(Material.STONE_BUTTON, "브+5").setLore(Arrays.asList(name)).getItem());
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		lore.add(" ");
		lore.add(Lang.INV_TIME_OPTION.get().replace(LangTag.VALUE.toString(), boom_time.toString() + "s"));
		
		return lore;
	}
	
	@Override
	public String getID() {
		return "timebomb";
	}

	@Override
	public Material getIcon() {
		return Material.CHEST;
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
	
	private BlockFace[] faces = {BlockFace.SOUTH, BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST};
	
	public Boolean createChest(List<ItemStack> list, Location location) {
		
		Block b = location.getBlock();
		if (b.getType() == Material.AIR) {
			
			if (list.size() <= 27) {
				b.setType(Material.CHEST);
				Chest chest = (Chest) b.getState();
				
				chest.getBlockInventory().setContents(list.toArray(new ItemStack[list.size()]));
				if (boom_time > 0) new Explode(boom_time, Arrays.asList(b)).runTaskTimer(UHC.getInstance(), 0, 20);
				else new Explode(boom_time, Arrays.asList(b)).boom();
					
				return true;
				
			} else if (list.size() <= 27*2) {
				
				for (BlockFace face : faces) {
					
					if (b.getRelative(face) != null && b.getRelative(face).getType() == Material.AIR) {
						
						b.setType(Material.CHEST);
						Block b2 = b.getRelative(face);
						b2.setType(Material.CHEST);
						
						DoubleChest dchest = (DoubleChest) ((Chest) b2.getState()).getInventory().getHolder();
						
						dchest.getInventory().setContents(list.toArray(new ItemStack[list.size()]));
						if (boom_time > 0) new Explode(boom_time, Arrays.asList(b, b2)).runTaskTimer(UHC.getInstance(), 0, 20);
						else new Explode(boom_time, Arrays.asList(b, b2)).boom();
							
						return true;
					}
				}
				
			} 
		}
		return false;
	}
	
	private class Explode extends BukkitRunnable {

		int time;
		List<Block> blocks;
		
		ArmorStand as;
		
		public Explode(int time ,List<Block> blocks) {
			this.time = time;
			this.blocks = blocks;
			
			if (time > 0) {
				Location l = ((Block) blocks.toArray()[0]).getLocation();
				as = (ArmorStand) l.getWorld().spawnEntity(l.add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
				
				as.setMarker(true);
				as.setGravity(false);
				as.setVisible(false);
				as.setSmall(true);
				as.setCustomNameVisible(true);
				as.setCustomName("브" + time + "s");
				as.setNoDamageTicks(time * 20);
			}
		}
		
		@Override
		public void run() {
			
			if (hasChest()) {
				
				if (time == 0) {
					end();
					return;
				}
				
				if (as != null) as.setCustomName("브" + time + "s");
				
			} else {
				
				stop();
				return;
			}

			
			time --;
		}
		
		private void stop() {
			cancel();
			if (as != null) as.remove();
		}
		
		public void boom() {
			Boolean boom = false;
			
			for (Block b : blocks) {
				if (b.getType() == Material.CHEST) {
					
					boom = true;
					b.setType(Material.AIR);
				}
			}
			
			Block b = (Block) blocks.toArray()[0];
			if (boom) b.getWorld().createExplosion(b.getLocation(), 4);
		}
		
		private void end() {
			boom();
			stop();
		}
		
		private Boolean hasChest() {
			for (Block b : blocks) {
				if (b != null && b.getType() == Material.CHEST) return true;
			}
			
			return false;
		}
	}
}
