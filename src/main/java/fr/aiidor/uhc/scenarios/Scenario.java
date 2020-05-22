package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.inventories.ChangeScenarioStateEvent;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.tools.ItemBuilder;
import net.md_5.bungee.api.ChatColor;

public abstract class Scenario {
	
	private String name;
	private List<String> description;
	private List<String> condition;
	
	public Scenario(ScenariosManager manager) {
		
		this.name = Lang.getFromFile("scenarios/" + getID(), "name");
		
		List<String> description = new ArrayList<String>();
		
		for (String string : Lang.getFromFile("scenarios/" + getID(), "description").split("\n")) {
			description.add(ChatColor.translateAlternateColorCodes('§', string));
		}
		
		if (UHCFile.LANG.getJSONObject("scenarios/" + getID()).get("condition") != null) {
			
			condition = new ArrayList<String>();
			
			for (String string : Lang.getFromFile("scenarios/" + getID(), "condition").split("\n")) {
				condition.add(ChatColor.translateAlternateColorCodes('§', string));
			}
		}

		this.description = description;
		
		manager.getScenarios().add(this);
	}

	public Boolean isActivated() {
		return UHC.getInstance().getGameManager().getGame().getSettings().IsActivated(this);
	}
	
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
	
	public Boolean isMainScenario() {
		return false;
	}
	
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
	
	public List<String> getConditions() {
		return condition;
	}
	
	public Boolean hasCondition() {
		return getConditions() != null && !getConditions().isEmpty(); 
	}
	
	//COMPATIBLE
	public Boolean compatibleWith(Scenario scenario) {
		return true;
	}
	
	public void checkConditions() {
		Game game = UHC.getInstance().getGameManager().getGame();
		
		for (Scenario s : game.getSettings().getActivatedScenarios()) {
			if (!compatibleWith(s) || !s.compatibleWith(this)) {
				
				game.log(Lang.ST_ERROR_SCENARIO_COMPATIBILITY.get()
						.replace(LangTag.VALUE_1.toString(), this.name)
						.replace(LangTag.VALUE_2.toString(), s.getName())
					);
				
				game.getSettings().setActivated(this, false);
			}
		}
	}
	
	public List<String> getLore(Boolean condition, Boolean config) {

		List<String> lore = new ArrayList<String>();
		lore.addAll(getDescription());
		
		if (getInformations() != null) {
			lore.addAll(getInformations());
		}
		
		if (hasCondition() && condition) {
			lore.add(" ");
			lore.add("§6" + Lang.CONDITION.get() + " :");
			lore.addAll(getConditions());
		}
		
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

		if (hasSettings() && config) {
			lore.add(" ");
			lore.add(Lang.INV_RIGHT_CLICK_OPTION.get());
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
	
	public ItemStack getScenarioIcon(Boolean icon, Boolean condition, Boolean config) {
		ItemBuilder builder;
		
		String displayName = getName();
		List<String> lore = getLore(condition, config);
		
		if (icon) {
			builder = new ItemBuilder(getIcon(), getName());
			
		} else {
			
			if (isActivated()) builder = new ItemBuilder(Material.STAINED_CLAY, (byte) 13, displayName + " §a✔");
			else builder = new ItemBuilder(Material.STAINED_CLAY, (byte) 14, displayName + " §c✘");
		}
		
		builder.setLore(lore);
		
		return builder.getItem();
	}
	
	public void stop() {}
}
