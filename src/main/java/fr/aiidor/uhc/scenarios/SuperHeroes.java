package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.enums.UHCFile;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.team.UHCTeam;

public class SuperHeroes extends Scenario {
	
	private HashMap<UHCPlayer, Power> powers;
	private BukkitRunnable task;
	
	private Gui gui;
	
	public SuperHeroes(ScenariosManager manager) {
		super(manager);
		
		Power.STRENGTH.effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0, false, false));
		
		Power.RESISTANCE.effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
		
		Power.SPEED.effects.add(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
		Power.SPEED.effects.add(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0, false, false));
		
		Power.JUMP_BOOST.effects.add(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 3, false, false));
		Power.JUMP_BOOST.effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
		
		Power.DOUBLE_HEALTH.effects.add(new PotionEffect(PotionEffectType.HEALTH_BOOST, Integer.MAX_VALUE, 4, false, false));
		
		Power.INVICIBILITY.effects.add(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false));
		Power.INVICIBILITY.effects.add(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, 0, false, false));
		
		powers = new HashMap<UHCPlayer, SuperHeroes.Power>();
		
		Power.INVICIBILITY.setActivated(false);
		
		gui = new Gui() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				InventoryClickEvent e = event.getEvent();
				ItemStack clicked = event.getItemClicked();
				
				e.setCancelled(true);
				
				if (e.getSlot() >= 0 && e.getSlot() < 8 && clicked.getType() == Material.INK_SACK) {
					if (clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName()) {
						
						if (Power.getByName(clicked.getItemMeta().getDisplayName()) != null) {
							Power power = Power.getByName(clicked.getItemMeta().getDisplayName());
							power.setActivated(!power.isActivated());
							playClickSound(event.getPlayer());
							update();
							return;
						}	
					}
				}
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
			}
			
			@Override
			public Inventory getInventory() {
				Inventory inv = Bukkit.createInventory(null, 9, getName());
				
				int i = 0;
				for (Power pow : Power.values()) {
					inv.setItem(i, getConfigItem(pow.getName(), pow.isActivated()));
					i++;
				}
				
				inv.setItem(8, getExitIcon());
				return inv;
			}
		};
	}
	
	@Override
	public String getID() {
		return "superheroes";
	}

	@Override
	public Material getIcon() {
		return Material.NETHER_STAR;
	}

	@Override
	public Boolean isOriginal() {
		return false;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.PVP, Category.FUN);
	}
	
	@Override
	public List<String> getInformations() {
		List<String> lore = new ArrayList<String>();
		
		if (!getActivatedPowers().isEmpty()) {
			lore.add(" ");
			lore.add(Lang.POWERS.get());
			lore.add(" ");
			
			for (Power pow : getActivatedPowers()) {
				lore.add("§8» §7" + pow.getName());
			}
		}
		
		return lore;
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		
		super.changeStateEvent(e);
		
		if (!e.getGame().isWaiting()) {
			
			if (e.getPlayer() != null) {
				e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_START.get());
				e.getPlayer().closeInventory();
			}
			
			e.setCancelled(true);
		}
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	public List<Power> getActivatedPowers() {
		List<Power> list = new ArrayList<SuperHeroes.Power>();
		
		for (Power p : Power.values()) {
			if (p.isActivated()) list.add(p);
		}
		
		return list;
	}
	
	public void start(Game game) {
		
		if (getActivatedPowers().isEmpty()) return;
		
		if (!game.hasTeam()) {
			for (UHCPlayer p : game.getAlivePlayers()) {
				setPower(p, getActivatedPowers().get(new Random().nextInt(getActivatedPowers().size())));
			}
			
		} else {
			
			for (UHCTeam t : game.getAliveTeams()) {
				List<Power> powers = getActivatedPowers();
				
				for (UHCPlayer p : t.getAlivePlayers()) {
					if (powers.isEmpty()) powers = getActivatedPowers();
					
					Power pow = powers.get(new Random().nextInt(powers.size()));
					powers.remove(pow);
					setPower(p, pow);
				}
			}
		}
		
		task = new BukkitRunnable() {
			
			@Override
			public void run() {
				for (UHCPlayer p : powers.keySet()) {
					if (p.isConnected() && hasPower(p)) {
						if (p.isAlive()) {
							for (PotionEffect pe : getPower(p).effects) {
								if (!p.getPlayer().hasPotionEffect(pe.getType()) || p.getPotionAmplifier(pe.getType()) < pe.getAmplifier())
									p.addPotionEffect(pe);
							}
						}
					}
				}
			}
		};
		
		task.runTaskTimer(UHC.getInstance(), 0, 100);
	}
	
	
	public void setPower(UHCPlayer p, Power pow) {
		powers.put(p, pow);
		
		if (p.hasTeam() && pow == Power.INVICIBILITY) {
			p.getTeam().getTeam().removeEntry(p.getName());
		}
			
		for (PotionEffect pe : getPower(p).effects) {
			p.addPotionEffect(pe);
		}
		
		if (p.isConnected()) {
			p.getPlayer().setHealth(p.getPlayer().getMaxHealth());
			p.getPlayer().sendMessage(Lang.POWER_ANNOUNCE.get().replace(LangTag.VALUE.toString(), Lang.removeColor(pow.getName())));
		}
	}
	
	public Boolean hasPower(UHCPlayer p) {
		return powers.containsKey(p);
	}
	
	public Power getPower(UHCPlayer p) {
		return powers.get(p);
	}
	
	public Power removePower(UHCPlayer p) {
		return powers.remove(p);
	}
	
	public enum Power {
		STRENGTH, RESISTANCE, SPEED, JUMP_BOOST, DOUBLE_HEALTH, INVICIBILITY;
		
		private String name;
		private Boolean state;
		
		public List<PotionEffect> effects;
		
		private Power() {
			name = (String) UHCFile.LANG.getJSONObject("scenarios/superheroes/power").get(name());
			effects = new ArrayList<PotionEffect>();
			state = true;
		}
		
		public String getName() {
			return name;
		}
		
		public Boolean isActivated() {
			return state;
		}
		
		public void setActivated(Boolean state) {
			this.state = state;
		}
		
		public static Power getByName(String name) {
			for (Power p : values()) {
				if (p.getName().equals(name)) return p;
			}
			return null;
		}
	}
	
	@Override
	public void reload() {
		if (task != null) task.cancel();
		task = null;
		powers = new HashMap<UHCPlayer, SuperHeroes.Power>();
		
		if (UHC.getInstance().getGameManager().hasGame()) {
			Game game = UHC.getInstance().getGameManager().getGame();
			for (UHCPlayer p : game.getAllPlayers()) {
				if (p.hasTeam()) {
					if (!p.getTeam().getTeam().hasEntry(p.getName())) p.getTeam().getTeam().addEntry(p.getName());
				}
			}
		}
	}
}
