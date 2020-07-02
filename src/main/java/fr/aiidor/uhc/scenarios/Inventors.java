package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class Inventors extends Scenario {
	
	List<Material> inventions;
	
	public Inventors(ScenariosManager manager) {
		super(manager);
		
		inventions = new ArrayList<Material>();
	}
	
	@Override
	public String getID() {
		return "inventors";
	}

	@Override
	public Material getIcon() {
		return Material.WORKBENCH;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
	
	public void hasInvented(UHCPlayer p, ItemStack item, Game game) {
		
		if (!inventions.contains(item.getType())) {
			inventions.add(item.getType());
			
			game.broadcast(Lang.INVENTION.get()
					.replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName())
					.replace(LangTag.VALUE.toString(), CraftItemStack.asNMSCopy(item).getName())
			);
		}
	}
	
	@Override
	public void reload() {
		inventions = new ArrayList<Material>();
	}
}
