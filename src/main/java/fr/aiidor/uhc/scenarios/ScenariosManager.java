package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.GameManager;
import fr.aiidor.uhc.scenarios.ItemScenario.GiveTime;

public class ScenariosManager {
	
	protected List<Scenario> scenarios;
	protected List<ItemScenario> item_scenarios;
	
	public ScenariosManager() {
		load();
	}
	
	public void load() {
		scenarios = new ArrayList<Scenario>();
		item_scenarios = new ArrayList<ItemScenario>();
		
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
		INVENTORS = new Inventors(this);
		TIME_BOMB = new TimeBomb(this);
		BOW_SWAP = new BowSwap(this);
		HIGHWAY_TO_HELL = new HighwayToHell(this);
		SOUP = new Soup(this);
		DOUBLE_JUMP = new DoubleJump(this);
		TRACKER = new Tracker(this);
		MASTER_LEVEL = new MasterLevel(this);
		GOLDEN_RETRIEVER = new GoldenRetriever(this);
		SPEEDY_MINER = new SpeedyMiner(this);
		FENCE_HEAD = new FenceHead(this);
		AUSTRALIAN_CRAFTING = new AutralianCrafting(this);
		NO_WOODEN_TOOL = new NoWoodenTool(this);
		OPEN_HOUSE = new OpenHouse(this);
		DROPPING_COINS = new DroppingCoins(this);
		NO_BUCKET = new NoBucket(this);
		SWEAT_WORLD = new SweatWorld(this);
		CHUNK_APOCALYPSE = new ChunkApocalypse(this);
		ENCHANTING_CENTER = new EnchantingCenter(this);
		PROGRESSIVE_SPEED = new ProgressiveSpeed(this);
		WORLD_IS_SMALL = new WorldIsSmall(this);
		BOMBERS = new Bombers(this);
		SKYHIGH = new SkyHigh(this);
		GAPZAP = new GapZap(this);
		INFINITE_ENCHANTER = new InfiniteEnchanter(this);
		DIAMOND_ISNT = new DiamondIsnt(this);
		REINCARNATION = new Reincarnation(this);
		OVIPAROUS = new Oviparous(this);
		ENDER_REPLACEMENT = new EnderReplacement(this);
		NETHERIBUS = new Netheribus(this);
		BEST_PVE = new BestPve(this);
		URBAN = new Urban(this);
		VILLAGER_MADNESS = new VillagerMadness(this);
		KILL_THE_WITCH = new KillTheWitch(this);
		BIOME_APOCALYPSE = new BiomeApocalypse(this);
		HELL = new Hell(this);
		COORDONOIA = new Coordonoia(this);
		BIOME_CENTER = new BiomeCenter(this);
		AIIDOR_HARDCORE = new AiidorHardcore(this);
		
		for (Scenario s : scenarios) {
			if (s instanceof ItemScenario) {
				item_scenarios.add((ItemScenario) s);
			}
		}
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
	public static Inventors INVENTORS;
	public static TimeBomb TIME_BOMB;
	public static BowSwap BOW_SWAP;
	public static HighwayToHell HIGHWAY_TO_HELL;
	public static Soup SOUP;
	public static DoubleJump DOUBLE_JUMP;
	public static Tracker TRACKER;
	public static MasterLevel MASTER_LEVEL;
	public static GoldenRetriever GOLDEN_RETRIEVER;
	public static SpeedyMiner SPEEDY_MINER;
	public static FenceHead FENCE_HEAD;
	public static AutralianCrafting AUSTRALIAN_CRAFTING;
	public static NoWoodenTool NO_WOODEN_TOOL;
	public static OpenHouse OPEN_HOUSE;
	public static DroppingCoins DROPPING_COINS;
	public static NoBucket NO_BUCKET;
	public static SweatWorld SWEAT_WORLD;
	public static ChunkApocalypse CHUNK_APOCALYPSE;
	public static EnchantingCenter ENCHANTING_CENTER;
	public static ProgressiveSpeed PROGRESSIVE_SPEED;
	public static WorldIsSmall WORLD_IS_SMALL;
	public static Bombers BOMBERS;
	public static SkyHigh SKYHIGH;
	public static GapZap GAPZAP;
	public static InfiniteEnchanter INFINITE_ENCHANTER;
	public static DiamondIsnt DIAMOND_ISNT;
	public static Reincarnation REINCARNATION;
	public static Oviparous OVIPAROUS;
	public static EnderReplacement ENDER_REPLACEMENT;
	public static Netheribus NETHERIBUS;
	public static BestPve BEST_PVE;
	public static Urban URBAN;
	public static VillagerMadness VILLAGER_MADNESS;
	public static KillTheWitch KILL_THE_WITCH;
	public static BiomeApocalypse BIOME_APOCALYPSE;
	public static Hell HELL;
	public static Coordonoia COORDONOIA;
	public static BiomeCenter BIOME_CENTER;
	public static AiidorHardcore AIIDOR_HARDCORE;
	
	public List<Scenario> getScenarios() {
		List<Scenario> list = new ArrayList<Scenario>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			Game game = UHC.getInstance().getGameManager().getGame();
			
			for (Scenario s : scenarios) {
				if (s.compatibleWith(game.getType())) list.add(s);
			}
			
		} else {
			list.addAll(scenarios);
		}
		
		return list;
	}
	
	public List<ItemScenario> getItemScenarios(GiveTime time) {
		List<ItemScenario> list = new ArrayList<ItemScenario>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			
			Game game = UHC.getInstance().getGameManager().getGame();
			
			for (ItemScenario s : item_scenarios) {
				if (s.compatibleWith(game.getType()) && s.giveTime() == time) list.add(s);
			}
		}
		
		return list;
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
		
		if (cat == Category.ORIGINALS) {
			for (Scenario scenario : getScenarios()) {
				if (scenario.isOriginal()) {
					list.add(scenario);
				}
			}
			
			return list;
		}
		
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
