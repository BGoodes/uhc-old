package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.UHCType;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.inventories.Gui;
import fr.aiidor.uhc.inventories.GuiBuilder;
import fr.aiidor.uhc.inventories.GuiManager;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;
import fr.aiidor.uhc.listeners.events.GuiClickEvent;
import fr.aiidor.uhc.team.UHCTeam;

public class AssaultAndBattery extends Scenario {
	
	private List<UHCPlayer> sword;
	private List<UHCPlayer> bow;
	
	private GuiBuilder gui;
	public Boolean pvp_only;
	
	public AssaultAndBattery(ScenariosManager manager) {
		super(manager);
		
		sword = new ArrayList<UHCPlayer>();
		bow = new ArrayList<UHCPlayer>();
		
		pvp_only = false;
		
		gui = new GuiBuilder() {
			
			@Override
			public void onClick(GuiClickEvent event) {
				
				InventoryClickEvent e = event.getEvent();
				ItemStack clicked = event.getItemClicked();
				
				e.setCancelled(true);
				
				if (e.getSlot() == 0 && clicked.getType() == Material.BOOK) {
					pvp_only = !pvp_only;
					update();
					return;
				}
				
				if (e.getSlot() == 8) {
					event.getPlayer().openInventory(GuiManager.INV_CONFIG_SCENARIOS.getInventory());
					playClickSound(event.getPlayer());
					return;
				}
			}
			
			@Override
			public String getTitle() {
				return getName();
			}
			
			@Override
			public String[][] getMatrix() {

				String[][] items = {
						{"P", " ", " ", " ", " ", " ", " ", " ", "X"},
				};
				
				return items;
			}
			
			@Override
			public HashMap<String, ItemStack> getDictionary() {
				
				HashMap<String, ItemStack> dictionnary = new HashMap<String, ItemStack>();
				
				dictionnary.put("X", getBackIcon());
				
				dictionnary.put("P", getConfigItem(Material.BOOK, Lang.PVP_ONLY.get(), pvp_only));
				
				return dictionnary;
			}
		};
	}
	
	@Override
	public String getID() {
		return "assault-and-battery";
	}
	
	@Override
	public Material getIcon() {
		return Material.DIAMOND_BLOCK;
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
	public Boolean compatibleWith(UHCType type) {
		if (type == UHCType.DEVIL_WATCHES) return false;
		return true;
	}
	
	@Override
	public Gui getSettings() {
		return gui;
	}
	
	@Override
	public void checkConditions(Boolean state) {
		Game game = UHC.getInstance().getGameManager().getGame();
		if (!game.hasTeam() || game.getSettings().getTeamSize() != 2) {
			
			game.log(Lang.ERROR_CONDITION_TO2.get());
			game.getSettings().setActivated(this, false);
		}
		
		super.checkConditions(state);
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		if (e.getGame().isPvpTime()) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_PVP.get());
			e.getPlayer().closeInventory();
		
		} else if ((!e.getGame().hasTeam() || e.getGame().getSettings().getTeamSize() != 2) && e.getState() == true) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(Lang.ST_ERROR_SCENARIO_CONDITION.get());
			e.getPlayer().closeInventory();
		}
		
		super.changeStateEvent(e);
	}
	
	public Boolean canUseSword(UHCPlayer p) {
		if (sword.contains(p) || (!bow.contains(p) && !sword.contains(p))) {
			return true;
		}
		return false;
	}
	
	public Boolean canUseBow(UHCPlayer p) {
		if (bow.contains(p) || (!bow.contains(p) && !sword.contains(p))) {
			return true;
		}
		return false;
	}
	
	public void removeTeamRestrictions(UHCPlayer player) {
		
		if (player.hasTeam() && player.getTeam().getAlivePlayers().size() == 1) {
			
			UHCPlayer p = getSoloPlayer(player.getTeam());
			
			if (p != null && (sword.contains(p) || bow.contains(p))) {
				
				if (bow.contains(p)) bow.remove(p);
				if (sword.contains(p)) sword.remove(p);

				if (p.isConnected()) p.getPlayer().sendMessage(Lang.BC_ANNOUNCE_USE_ALL.get());
			}
		}
	}
	
	private UHCPlayer getSoloPlayer(UHCTeam t) {
		if (t.getAlivePlayers().size() == 1) {
				for (UHCPlayer pl : t.getAlivePlayers()) {
					return pl;
			}
		}
		return null;
	}
	
	private UHCPlayer getOtherPlayer(UHCPlayer p) {
		if (p.hasTeam()) {
			UHCTeam t = p.getTeam();
			if (t.getSize() == 2 && t.getAlivePlayers().size() == 2) {
				for (UHCPlayer pl : t.getAlivePlayers()) {
					if (!p.equals(pl)) return pl;
				}
			}
		}
		return null;
	}
	
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e, Game game) {
		
		if (!(e.getEntity() instanceof Player) && pvp_only) return;
		
		if (e.getDamager() instanceof Player) {
			
			Player player = (Player) e.getDamager();
			
			if (game.isHere(player.getUniqueId())) {
				UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
				
				
				if (ScenariosManager.ASSAULT_AND_BATTERY.isActivated()) {
					if (!ScenariosManager.ASSAULT_AND_BATTERY.canUseSword(p)) {
						e.setDamage(0);
					}
				}
			}
		}
		
		
		
		if (e.getDamager() instanceof Arrow) {
			
			if (((Arrow) e.getDamager()).getShooter() instanceof Player) {
				Player player = (Player) ((Arrow) e.getDamager()).getShooter();
				
				if (game.isHere(player.getUniqueId())) {
					UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
					
					
					if (ScenariosManager.ASSAULT_AND_BATTERY.isActivated()) {
						if (!ScenariosManager.ASSAULT_AND_BATTERY.canUseBow(p)) {
							e.setDamage(0);
						}
					}
				}
			}
		}	
	}
	
	public void load(Game game) {
		for (UHCTeam t : game.getAliveTeams()) {
			if (t.getSize() == 2) {
				
				if (t.getAlivePlayers().size() == 2) {
					int n = new Random().nextInt(2);
					
					UHCPlayer p = (UHCPlayer) t.getAlivePlayers().toArray()[n];
					sword.add(p);
					if (p.isConnected()) p.getPlayer().sendMessage(Lang.BC_ANNOUNCE_SWORD.get());
					
					UHCPlayer p2 = getOtherPlayer(p);
					bow.add(p2);
					if (p2.isConnected()) p2.getPlayer().sendMessage(Lang.BC_ANNOUNCE_BOW.get());
					
				} else if (t.getAlivePlayers().size() == 1) {
					
					for (UHCPlayer p : t.getAlivePlayers())  {
						if (p.isConnected()) p.getPlayer().sendMessage(Lang.BC_ANNOUNCE_SOLO.get());
					}
					
				}
			} else {
				for (UHCPlayer p : t.getAlivePlayers()) {
					
					if (new Random().nextBoolean()) {
						sword.add(p);
						if (p.isConnected()) p.getPlayer().sendMessage(Lang.BC_ANNOUNCE_SWORD.get());
					
					} else {
						bow.add(p);
						if (p.isConnected()) p.getPlayer().sendMessage(Lang.BC_ANNOUNCE_BOW.get());
					}
				}
			}
		}
	}
	
	@Override
	public void reload() {
		sword = new ArrayList<UHCPlayer>();
		bow = new ArrayList<UHCPlayer>();
	}
}
