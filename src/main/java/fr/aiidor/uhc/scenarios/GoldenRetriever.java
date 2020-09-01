package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.tools.UHCItem;

public class GoldenRetriever extends ItemScenario {
	
	public GoldenRetriever(ScenariosManager manager) {
		super(manager);
	}
	
	@Override
	public String getID() {
		return "golden-retriever";
	}

	@Override
	public Material getIcon() {
		return Material.GOLDEN_APPLE;
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
	public GiveTime giveTime() {
		return GiveTime.DEATH;
	}

	@Override
	public List<ItemStack> getItems() {
		return Arrays.asList(UHCItem.golden_head);
	}
}
