package fr.aiidor.dwuhc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Ocelot.Type;
import org.bukkit.entity.Player;

import fr.aiidor.uhc.enums.Lang;
import fr.aiidor.uhc.enums.LangTag;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.gamemodes.DevilWatches;

public class CatLady extends DWRole {
	
	private List<Ocelot> cats = new ArrayList<Ocelot>();
	
	public CatLady(DWplayer player, DWCamp camp) {
		super(player, camp);
	}

	@Override
	public final DWRoleType getRoleType() {
		return DWRoleType.CAT_LADY;
	}
	
	public List<Ocelot> getCats() {
		return cats;
	}
	
	public void summonCat() {
		
		Player player = p.getUHCPlayer().getPlayer();
		
		if (cats.size() >= 3) {
			player.sendMessage(Lang.DW_CAT_NUMBER.get());
			return;
		}
		
		Ocelot cat = (Ocelot) player.getWorld().spawnEntity(player.getLocation(), EntityType.OCELOT);
		
		cat.setCatType(new Type[] {Type.BLACK_CAT, Type.RED_CAT, Type.SIAMESE_CAT}[new Random().nextInt(3)]);
		cat.setTamed(true);
		cat.setSitting(true);
		cat.setOwner(player);
		
		cats.add(cat);
	}
	
	public Boolean isCatOwner(Ocelot oc) {
		return cats.contains(oc);
	}
	
	public void death(Ocelot oc, Player killer) {
		
		DevilWatches dw = DevilWatches.getDevilWatches();
		dw.getGame().broadcast(Lang.DW_CAT_MEOW.get());
		
		if (p.getUHCPlayer().isConnected()) {
			Player player = p.getUHCPlayer().getPlayer();
			
			if (killer != null && dw.hasRole(killer.getUniqueId())) {
				
				UHCPlayer k = dw.getDWPlayer(killer.getUniqueId()).getUHCPlayer();
				player.sendMessage(Lang.DW_CAT_WHISPER.get().replace(LangTag.VALUE.toString(), 
						Lang.DW_CAT_WHISPER_DEATH_KILLER.get().replace(LangTag.PLAYER_NAME.toString(), k.getDisplayName())));
			
			} else {
				
				StringBuilder names = new StringBuilder();
				
				for (Entity e : oc.getNearbyEntities(50, 50, 50)) {
					if (e instanceof Player) {
						Player p = (Player) e;
						if (dw.hasRole(p.getUniqueId())) {
							
							String name = dw.getDWPlayer(p.getUniqueId()).getUHCPlayer().getDisplayName();
							names.append(", " + name);
						}
					}
				}
				
				if (names.toString().length() <= 2) player.sendMessage(Lang.DW_CAT_WHISPER.get().replace(LangTag.VALUE.toString(), Lang.DW_CAT_WHISPER_DEATH.get()));
				else player.sendMessage(Lang.DW_CAT_WHISPER.get().replace(LangTag.VALUE.toString(), 
						Lang.DW_CAT_WHISPER_DEATH_AROUND.get().replace(LangTag.VALUE.toString(), names.toString().substring(2))));
			}
		}

		cats.remove(oc);
	}
	
	public static Boolean isMysticCat(Ocelot oc) {
		return oc.getCatType() != Type.WILD_OCELOT && oc.isTamed();
	}
}
