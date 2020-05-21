package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;
import fr.aiidor.uhc.inventories.Gui;
import net.md_5.bungee.api.ChatColor;

public abstract class Scenario {
	
	private String name;
	private List<String> description;
	
	public Scenario(ScenariosManager manager) {
		
		this.name = Lang.getFromFile("scenarios/" + getID(), "name");
		
		List<String> description = new ArrayList<String>();
		for (String string : Lang.getFromFile("scenarios/" + getID(), "description").split("\n")) {
			description.add(ChatColor.translateAlternateColorCodes('§', string));
		}
		
		this.description = description;
		
		manager.getScenarios().add(this);
	}

	public Boolean isActivated(Game game) {
		return game.getSettings().IsActivated(this);
	}
	
	public void load() {}
	public void changeStateEvent(ChangeScenarioStateEvent e) {}
	
	public abstract String getID();
	
	public String getName() {
		return name;
	}
	
	public List<String> getDescription() {
		return description;
	}
	
	public abstract Material getIcon();
	
	
	public abstract Boolean isOriginal();
	
	public Gui getSettings()  {
		return null;
	}
	
	public Boolean hasSettings() {
		return getSettings() != null;
	}
	
	
	public abstract List<Category> getCategories();
	
	public Boolean isCategory(Category cat) {
		return getCategories().contains(cat);
	}
	
	public String getConditions() {
		return null;
	}
	
	public Boolean hasCondition() {
		return getConditions() != null && !getConditions().isEmpty(); 
	}
	
	
	public List<String> getLore(Boolean category) {

		List<String> lore = new ArrayList<String>();
		lore.addAll(getDescription());
		
		if (getInformations() != null) {
			lore.addAll(getInformations());
		}
		
		if (category) {
			lore.add(" ");
			lore.add(Lang.INV_CATEGORIES.get());
			
			StringBuilder sb = new StringBuilder();
			
			Integer index = 0;
			for (Category cat : getCategories()) {
				
				sb.append("§d" + cat.getDisplayName());
				
				if (index + 1 < getCategories().size()) {
					sb.append("§d, ");
				}
				
				index++;
			}
			
			lore.add(sb.toString());
		}

		
		if (isOriginal()) {
			lore.add(" ");
			lore.add("§6§l[Original]");
		}
		
		return lore;
	}
	
	public List<String> getInformations() {
		return null;
	}
	
}
