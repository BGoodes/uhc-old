package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class GoneFishing extends Scenario {
	
	private List<ItemStack> items;
	
	public GoneFishing(ScenariosManager manager) {
		super(manager);
		
		items = new ArrayList<ItemStack>();
		
		ItemBuilder rod = new ItemBuilder(Material.FISHING_ROD);
		rod.addEnchant(Enchantment.LUCK, 250);
		rod.addEnchant(Enchantment.DURABILITY, 250);
		
		items.add(rod.getItem());
		items.add(new ItemStack(Material.ANVIL, 20));
	}
	
	@Override
	public String getID() {
		return "gone-fishing";
	}

	@Override
	public Material getIcon() {
		return Material.FISHING_ROD;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.ENCHANTMENT, Category.FUN);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (e.getGame().isStart()) {
			
			e.getPlayer().closeInventory();
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
			e.setCancelled(true);
		}
	}
	
	public void GiveItems(UHCPlayer player) {
		for (ItemStack item : items) {
			player.giveItem(item);
		}
	}
}