package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;

public class AutralianCrafting extends Scenario {
	
	List<Recipe> base;
	List<Recipe> reverse;
	
	public AutralianCrafting(ScenariosManager manager) {
		super(manager);
		
		base = new ArrayList<Recipe>();
		reverse = new ArrayList<Recipe>();
		
		Iterator<Recipe> it = UHC.getInstance().getServer().recipeIterator();
		while (it.hasNext()) {
			base.add(it.next());
		}
		
		it = UHC.getInstance().getServer().recipeIterator();
		
		while (it.hasNext()) {
			
			Recipe recipe = it.next();
			if (recipe instanceof ShapedRecipe) {
				
				ShapedRecipe sr = (ShapedRecipe) recipe;
				
				if (sr.getShape().length > 1) {
					if (sr.getShape().length == 2) {
						sr.shape(sr.getShape()[1], sr.getShape()[0]);
					}
					if (sr.getShape().length == 3) {
						sr.shape(sr.getShape()[2], sr.getShape()[1], sr.getShape()[0]);
					}
				}
				
				reverse.add(sr);
				
			} else reverse.add(recipe);
		}
	}
	
	@Override
	public String getID() {
		return "australian-crafting";
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
		return Arrays.asList(Category.FUN);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		
		Server s = UHC.getInstance().getServer();
		if (e.getState()) {
			
			s.clearRecipes();
			
			for (Recipe recipe : reverse) {
				s.addRecipe(recipe);
			}
			
		} else {
			
			s.clearRecipes();
			
			for (Recipe recipe : base) {
				s.addRecipe(recipe);
			}
		}
		
		super.changeStateEvent(e);
	}
}
