package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.tools.XpOrb;

public class CutClean extends Scenario {
	
	private HashMap<Material, Material> cooked;
	
	public CutClean(ScenariosManager manager) {
		super(manager);
		
		cooked = new HashMap<Material, Material>();
		
		cooked.put(Material.IRON_ORE, Material.IRON_INGOT);
		cooked.put(Material.GOLD_ORE, Material.GOLD_INGOT);
		
		cooked.put(Material.RAW_BEEF, Material.COOKED_BEEF);
		cooked.put(Material.PORK, Material.GRILLED_PORK);
		cooked.put(Material.MUTTON, Material.COOKED_MUTTON);
		cooked.put(Material.RAW_CHICKEN, Material.COOKED_CHICKEN);
		cooked.put(Material.RABBIT, Material.COOKED_RABBIT);
		cooked.put(Material.RAW_FISH, Material.COOKED_FISH);
	}
	
	@Override
	public String getID() {
		return "cutclean";
	}
	
	@Override
	public Material getIcon() {
		return Material.GOLD_INGOT;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.RUN);
	}

	public void heat(List<ItemStack> items) {
		for (ItemStack item : items) {
			heat(item);
		}
	}
	
	public void heat(ItemStack item) {
		if (cooked.containsKey(item.getType())) {
			item.setType(cooked.get(item.getType()));
		}
	}
	
	public void heat(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		if (player.getGameMode() != GameMode.CREATIVE) {
			
			Block b = e.getBlock();
			
			if (cooked.containsKey(b.getType())) {
				
				if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR && !b.getDrops(player.getItemInHand()).isEmpty()) {
					if (player.getItemInHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH)) return;
				}

				b.getWorld().dropItemNaturally(b.getLocation().add(0.5, 0.5, 0.5), new ItemStack(cooked.get(b.getType())));
				
				switch (e.getBlock().getType()) {
				
					case IRON_ORE: new XpOrb(b.getLocation().add(0.5, 0.5, 0.5), 0.75); break;
					case GOLD_ORE: new XpOrb(b.getLocation().add(0.5, 0.5, 0.5), 1); break;

					default: break;
				}
				
				b.setType(Material.AIR);
			}
		}
	}
	
	public void heat(EntityDeathEvent e) {
		
		for (ItemStack drop : e.getDrops()) {
			if (drop != null && drop.getType() != Material.AIR) {
				heat(drop);
			}
		}
	}
}
