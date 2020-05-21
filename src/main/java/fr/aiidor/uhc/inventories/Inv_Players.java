package fr.aiidor.uhc.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.enums.JoinState;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.tools.ItemBuilder;

public class Inv_Players extends GuiBuilder {

	@Override
	public String getTitle() {
		return Lang.INV_PLAYERS.get();
	}

	@Override
	public Boolean titleIsDynamic() {
		return false;
	}
	
	@Override
	public InventoryHolder getHolder() {
		return null;
	}
	
	@Override
	public String[][] getMatrix() {
		
		String[][] item = {
				{" ", " ", " ", " ", " ", " ", " ", "G", "X"},
		};
		
		return item;
	}

	@Override
	public HashMap<String, ItemStack> getDictionary() {
		
		HashMap<String, ItemStack> dictionary = new HashMap<String, ItemStack>();
		
		dictionary.put(" ", new ItemBuilder(Material.AIR).getItem());
		dictionary.put("X", getBackIcon());
		
		dictionary.put("G", new ItemBuilder(Material.BOOK_AND_QUILL, Lang.INV_JOIN_STATE.get()).getItem());
		
		return dictionary;
	}

	@Override
	public void onClick(GuiClickEvent event) {
		InventoryClickEvent e = event.getEvent();
		
		e.setCancelled(true);
		
		if (e.getSlot() == 8) {
			playClickSound(event.getPlayer());
			event.getPlayer().openInventory(GuiManager.INV_CONFIG.getInventory());
			return;
		}
		
		if (e.getSlot() == 7 && event.getItemClicked().getType() == Material.BOOK_AND_QUILL) {
			
			event.getGame().getSettings().setJoinState(getNextJoinState(event.getGame().getSettings().getJoinState()));
			playClickSound(event.getPlayer());
			update();
			return;
		}
	}
	
	private JoinState getNextJoinState(JoinState state) {
		List<JoinState> join_state_values = new ArrayList<JoinState>();
		
		for (JoinState value : JoinState.values()) {
			join_state_values.add(value);
		}
		
		Integer index = join_state_values.indexOf(state) + 1;
		
		if (index >= join_state_values.size()) {
			index = 0;
		}
		
		return join_state_values.get(index);
	}
}
