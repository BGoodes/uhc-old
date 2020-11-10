package fr.aiidor.uhc.scenarios;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;

public class Inventors extends Scenario {
	
	private HashMap<Material, UHCPlayer> inventions;
	
	public Inventors(ScenariosManager manager) {
		super(manager);
		
		inventions = new HashMap<Material, UHCPlayer>();
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
		return true;
	}
	
	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.OTHER);
	}
	
	public Boolean craft(UHCPlayer p, ItemStack item, Game game) {
		
		if (hasInvented(p, item, game)) {
			return true;
		}
		
		if (pay(p)) {
			inventions.get(item.getType()).giveItem(new ItemStack(Material.GOLD_INGOT));;
			return true;
		}
		
		p.getPlayer().sendMessage(Lang.CRAFT_ERROR.get());
		return false;
	}
	
	public ItemStack property(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		if (meta.hasLore()) meta.getLore().add(Lang.CRAFT_PROPERTY.get().replace(LangTag.VALUE.toString(), inventions.get(item.getType()).getName()));
		else meta.setLore(Arrays.asList(Lang.CRAFT_PROPERTY.get().replace(LangTag.VALUE.toString(), inventions.get(item.getType()).getName())));
		
		item.setItemMeta(meta);
		
		return item;
	}
	
	public ItemStack unProperty(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		
		if (meta.hasLore()) meta.getLore().remove(Lang.CRAFT_PROPERTY.get().replace(LangTag.VALUE.toString(), inventions.get(item.getType()).getName()));
		item.setItemMeta(meta);
		
		return item;
	}
	
	private Boolean pay(UHCPlayer p) {
		
		if (p.isConnected()) {
			
			for (ItemStack i : p.getPlayer().getInventory()) {
				if (i != null && i.getType() == Material.GOLD_INGOT) {
					
					if (i.getAmount() - 1 <= 0) i.setType(Material.AIR);
					else i.setAmount(i.getAmount() - 1);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Boolean isInvented(Material mat) {
		return inventions.containsKey(mat);
	}
	
	private Boolean hasInvented(UHCPlayer p, ItemStack item, Game game) {
		
		if (!inventions.containsKey(item.getType())) {
			inventions.put(item.getType(), p);
			
			game.broadcast(Lang.INVENTION.get()
					.replace(LangTag.PLAYER_NAME.toString(), p.getDisplayName())
					.replace(LangTag.VALUE.toString(), CraftItemStack.asNMSCopy(item).getName())
			);
			
			property(item);
			
			return true;
		}
		
		if (inventions.get(item.getType()).equals(p)) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public void reload() {
		inventions = new HashMap<Material, UHCPlayer>();
	}
}
