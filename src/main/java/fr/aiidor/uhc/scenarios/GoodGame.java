package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;
import fr.aiidor.uhc.tools.ItemBuilder;

public class GoodGame extends Scenario {
	
	public Boolean death;
	private ItemStack[] items;
	
	public GoodGame(ScenariosManager manager) {
		super(manager);
		death = false;
		
		items = new ItemStack[] {
			new ItemBuilder(Material.DIAMOND, 1).getItem(),
			new ItemBuilder(Material.GOLD_INGOT, 3).getItem(),
			new ItemBuilder(Material.IRON_INGOT, 5).getItem(),
			new ItemBuilder(Material.CAKE, 1).getItem(),
		};
	}
	
	@Override
	public String getID() {
		return "good-game";
	}

	@Override
	public Material getIcon() {
		return Material.CAKE;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		death = false;
	}
	
	public void giveItem(UHCPlayer p) {
		p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.CAT_MEOW, 0.4f, 1f);
		Bukkit.getScheduler().runTask(UHC.getInstance(), () -> p.giveItem(items[new Random().nextInt(items.length)]));
	}
}
