package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class FlowerPower extends Scenario {
	
	List<Material> blackList;
	
	private Boolean randomSpawner = true;
	private Boolean randomEgg = true;
	
	private int probability = 100;
	
	private Gui gui;
	
	private HashMap<String, List<String>> books;
	
	public FlowerPower(ScenariosManager manager) {
		super(manager);
		
		blackList = Arrays.asList(Material.COMMAND, Material.COMMAND_MINECART, Material.SOIL, Material.BURNING_FURNACE, Material.BARRIER, Material.BEDROCK);
		
		for (Material mat : Material.values()) {
			if (isItem(mat) && !blackList.contains(mat)) {
				
				if (getMaxData(mat) != 0) {
					for (byte i = 0; i <= getMaxData(mat); i++) {
						drops.add(new Item(mat, i));
					}
				
				} else {
					drops.add(new Item(mat));
				}

			}
			
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
		
		books = new HashMap<String, List<String>>();
		books.put("§fOuvre Moi", Arrays.asList("Bon toutou !"));
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
	
	private List<Item> drops = new ArrayList<Item>();
	private List<Material> flowers = Arrays.asList(Material.RED_ROSE, Material.YELLOW_FLOWER, Material.DOUBLE_PLANT);
	private List<Byte> double_flowers = Arrays.asList((byte) 0, (byte) 1, (byte) 4, (byte) 5);
	
	@SuppressWarnings("deprecation")
	public Boolean dropItem(Block b) {
		if (!flowers.contains(b.getType())) return false;
		if (b.getType() == Material.DOUBLE_PLANT && !double_flowers.contains(b.getData())) return false;
		
		if (probability == 100 || new Random().nextInt(100) <= probability) {
			Location loc = b.getLocation().clone().add(0.5, 0.5, 0.5);
			
			Random r = new Random();
			
			Item item = drops.get(r.nextInt(drops.size()));
			Integer stack = r.nextInt(item.getType().getMaxStackSize());
			
			if (stack == 0) stack = 1;
			b.getWorld().dropItem(loc, getRandomItemstack(item, stack));

			return true;
		} else {	
			b.setType(Material.AIR);
		}
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private ItemStack getRandomItemstack(Item it, Integer stack) {
		
		ItemStack item;
		if (!it.hasData()) item = new ItemStack(it.getType(), stack);
		else item = new ItemStack(it.getType(), stack, it.getData());
		
		Random r = new Random();
		
		if (it.getType() == Material.ENCHANTED_BOOK) {
			EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
			
			for (Integer i = 0; i <= r.nextInt(4); i++) {
				Enchantment enchant = Enchantment.values()[r.nextInt(Enchantment.values().length)];
				
				Integer level = r.nextInt(enchant.getMaxLevel()) + 1;
				meta.addStoredEnchant(enchant, level, true);
			}
			
			item.setItemMeta(meta);
		}
		
		if (it.getType() == Material.MOB_SPAWNER && randomSpawner) {
			BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
			CreatureSpawner cs = (CreatureSpawner) meta.getBlockState();
			
			EntityType type = getRandomEntity();
			
			if (type != null) {
				cs.setSpawnedType(type);
				
				meta.setBlockState(cs);
				item.setItemMeta(meta);
			}
		}
		
		if (it.getType() == Material.MONSTER_EGG && randomEgg) {
			EntityType type = getRandomEntity();
			
			if (type != null) {
				item = new ItemStack(Material.MONSTER_EGG, 1, type.getTypeId());
			}
		}
		
		if (it.getType() == Material.FIREWORK) {
			FireworkMeta meta = (FireworkMeta) item.getItemMeta();
			
	        FireworkEffect.Builder builder = FireworkEffect.builder();
	        
	        if (r.nextBoolean()) builder.withTrail();
	        if (r.nextBoolean()) builder.withFlicker();
	        
	        builder.withColor(Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
	        
	        if (r.nextBoolean()) {
	        	List<Color> colors = new ArrayList<Color>();
	        	for (int i = 0; i <= r.nextInt(5) + 1; i++) {
	        		Color color = Color.fromRGB(r.nextInt(255), r.nextInt(255), r.nextInt(255));
	        		builder.withFade(color);
	        	}
	        	
	        	builder.withFade(colors);
	        }
	        
	        FireworkEffect.Type type = FireworkEffect.Type.values()[r.nextInt(FireworkEffect.Type.values().length)];
	        builder.with(type);
	        
	        meta.addEffect(builder.build());
	        meta.setPower(r.nextInt(3) + 1);
	        item.setItemMeta(meta);
		}
		
		if (it.getType() == Material.WRITTEN_BOOK) {
			BookMeta meta = (BookMeta) item.getItemMeta();
			meta.setAuthor("§eB_Goodes");
			
			String title = (String) books.keySet().toArray()[new Random().nextInt(books.keySet().size())];
			List<String> pages = books.get(title);
			
			meta.setTitle(title);
			meta.setPages(pages);
			
			item.setItemMeta(meta);
		}
		
		return item;
	}
	
	private EntityType getRandomEntity() {
		Random r = new Random();
		
		EntityType type = EntityType.values()[r.nextInt(EntityType.values().length)];
		
		while (!type.isAlive() || !type.isSpawnable()) {
			type = EntityType.values()[r.nextInt(EntityType.values().length)];
		}
		
		return type;
	}
	
	private Boolean isItem(Material mat) {
		return CraftMagicNumbers.getItem(mat) != null;
	}
	
	private class Item {
		
		private Material mat;
		private Byte data;
		
		public Item(Material mat) {
			this(mat, null);
		}
		
		public Item(Material mat, Byte data) {
			this.mat = mat;
			this.data = data;
		}
		
		public Material getType() {
			return mat;
		}
		
		public Boolean hasData() {
			return data != null;
		}
		
		public Byte getData() {
			return data;
		}
	}
	
	
	private byte getMaxData(Material mat) {
		switch (mat) {
		case GOLDEN_APPLE: return 1; 
		case SKULL_ITEM: return 4; 
		
		case LOG: return 3; 
		case LOG_2: return 1; 
		
		case WOOD:
		case WOOD_STEP: return 5; 
		
		case WOOL:
		case BANNER: 
		case INK_SACK:
		case STAINED_CLAY: 
		case STAINED_GLASS:
		case STAINED_GLASS_PANE: return 15; 

		default: return 0;
		}
	}
}
