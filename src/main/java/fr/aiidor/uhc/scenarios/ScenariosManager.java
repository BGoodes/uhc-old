package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.GameManager;

public class ScenariosManager {
	
	private List<Scenario> scenarios;
	
	public ScenariosManager() {
		load();
	}
	
	public void load() {
		scenarios = new ArrayList<Scenario>();
		
		CUTCLEAN = new CutClean(this);
		FASTSMELING = new FastSmelting(this);
		HASTEY_BOYS = new HasteyBoys(this);
		HASTEY_BABIES = new HasteyBabies(this);
		TIMBER = new Timber(this);
		RODLESS = new RodLess(this);
		FIRELESS = new FireLess(this);
		BLEEDING_SWEETS = new BleedingSweets(this);
		GONE_FISHING = new GoneFishing(this);
		ENCHANTED_DEATH = new EnchantedDeath(this);
		NOFALL = new NoFall(this);
		BELIEVE_FLY = new BelieveFly(this);
		NO_CLEAN_UP = new NoCleanUp(this);
		FLOWER_POWER = new FlowerPower(this);
		CAT_EYES = new CatEyes(this);
		EGGS = new Eggs(this);
		STINGY_WORLD = new StingyWorld(this);
		ASSASSINS = new Assassins(this);
		BEDBOMB = new BedBomb(this);
		TNTFLY = new TntFly(this);
		ETERNAL_ITEMS = new EternalItems(this);
		GOOD_GAME = new GoodGame(this);
		FINAL_HEAL = new FinalHeal(this);
		BETA_ZOMBIE = new BetaZombie(this);
		ASSAULT_AND_BATTERY = new AssaultAndBattery(this);
		BLOOD_DIAMOND = new BloodDiamond(this);
		SUPER_HEROES = new SuperHeroes(this);
		BOOKCEPTION = new Bookception(this);
		ONLY_ONE_WINNER = new OnlyOneWinner(this);
		WEBCAGE = new WebCage(this);
		PUPPY_POWER = new PuppyPower(this);
		CUPID = new Cupid(this);
	}
	
	public static CutClean CUTCLEAN;
	public static FastSmelting FASTSMELING;
	public static HasteyBoys HASTEY_BOYS;
	public static HasteyBabies HASTEY_BABIES;
	public static Timber TIMBER;
	public static RodLess RODLESS;
	public static FireLess FIRELESS;
	public static BleedingSweets BLEEDING_SWEETS;
	public static GoneFishing GONE_FISHING;
	public static EnchantedDeath ENCHANTED_DEATH;
	public static NoFall NOFALL;
	public static BelieveFly BELIEVE_FLY;
	public static NoCleanUp NO_CLEAN_UP;
	public static FlowerPower FLOWER_POWER;
	public static CatEyes CAT_EYES;
	public static Eggs EGGS;
	public static StingyWorld STINGY_WORLD;
	public static Assassins ASSASSINS;
	public static BedBomb BEDBOMB;
	public static TntFly TNTFLY;
	public static EternalItems ETERNAL_ITEMS;
	public static GoodGame GOOD_GAME;
	public static FinalHeal FINAL_HEAL;
	public static BetaZombie BETA_ZOMBIE;
	public static AssaultAndBattery ASSAULT_AND_BATTERY;
	public static BloodDiamond BLOOD_DIAMOND;
	public static SuperHeroes SUPER_HEROES;
	public static Bookception BOOKCEPTION;
	public static OnlyOneWinner ONLY_ONE_WINNER;
	public static WebCage WEBCAGE;
	public static PuppyPower PUPPY_POWER;
	public static Cupid CUPID;
	
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	
	public List<Scenario> getScenarios(Comparator<Scenario> comparator) {
		List<Scenario> list = new ArrayList<Scenario>();
		
		list.addAll(getScenarios());
		list.sort(comparator);
		
		return list;
	}
	
	public List<Scenario> getScenarios(Category cat) {
		
		if (cat == Category.ALL) return getScenarios();
		List<Scenario> list = new ArrayList<Scenario>();
				
		for (Scenario scenario : getScenarios()) {
			if (scenario.getCategories().contains(cat)) {
				list.add(scenario);
			}
		}
		return list;
	}
	
	public List<Scenario> getScenarios(List<Category> categories) {
		
		if (categories.contains(Category.ALL)) return getScenarios();
		List<Scenario> list = new ArrayList<Scenario>();
		
		for (Scenario scenario : getScenarios()) {
			for (Category cat : categories) {
				if (scenario.getCategories().contains(cat)) {
					list.add(scenario);
				}
			}
		}
		return list;
	}
	
	public List<Scenario> getScenarios(Comparator<Scenario> comparator, Category cat) {
		
		List<Scenario> list = getScenarios(cat);
		list.sort(comparator);
		
		return list;
	}
	
	public List<Scenario> getScenarios(Comparator<Scenario> comparator, List<Category> categories) {
		
		List<Scenario> list = getScenarios(categories);
		list.sort(comparator);
		
		return list;
	}
	
	public Boolean isActivated(Scenario scenario) {
		GameManager gameManager = UHC.getInstance().getGameManager();
		
		if (!gameManager.hasGame()) return false;
		return gameManager.getGame().getSettings().IsActivated(scenario);
	}
}
