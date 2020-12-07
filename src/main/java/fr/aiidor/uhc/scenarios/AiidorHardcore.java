package fr.aiidor.uhc.scenarios;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Rabbit.Type;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import fr.aiidor.uhc.UHC;
import fr.aiidor.uhc.enums.Category;
import fr.aiidor.uhc.game.Game;
import fr.aiidor.uhc.game.UHCPlayer;
import fr.aiidor.uhc.listeners.events.ChangeScenarioStateEvent;
import fr.aiidor.uhc.utils.ItemBuilder;
import fr.aiidor.uhc.utils.MCMath;

public class AiidorHardcore extends Scenario {
	
	private ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
	private List<BukkitRunnable> tasks = new ArrayList<BukkitRunnable>();
	
	public AiidorHardcore(ScenariosManager manager) {
		super(manager);
		
		effects.add(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
		effects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
		effects.add(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1));
		effects.add(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
		effects.add(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
	}
	
	@Override
	public String getID() {
		return "aiidor-hardcore";
	}

	@Override
	public Material getIcon() {
		return Material.POISONOUS_POTATO;
	}

	@Override
	public Boolean isOriginal() {
		return true;
	}

	@Override
	public List<Category> getCategories() {
		return Arrays.asList(Category.SURVIVAL);
	}
	
	public void blockBreakEvent(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Block block = e.getBlock();
		Material type = block.getType();
		Random r = new Random();
		
		if (player.getGameMode() == GameMode.SURVIVAL) {
			if (type == Material.WEB) {
				e.setCancelled(true);
				block.setType(Material.AIR);
				return;
			}
			
			if (type == Material.STONE) {
				if (r.nextInt(200) == 0) block.getWorld().spawnEntity(block.getLocation(), EntityType.SILVERFISH);
				return;
			}
			
			if (type == Material.ENDER_STONE) {
				if (r.nextInt(100) == 0) block.getWorld().spawnEntity(block.getLocation(), EntityType.ENDERMITE);
				return;
			}
		}
	}
	
	public void entityDamageEvent(EntityDamageEvent e) {
		
		Game game = UHC.getInstance().getGameManager().getGame();
		
		if (e.getEntity() instanceof Skeleton) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE) e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE_TICK) e.setCancelled(true);
		}
		
		if (e.getEntity() instanceof Zombie) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE) e.setCancelled(true);
			if (e.getCause() == DamageCause.FIRE_TICK) e.setCancelled(true);
		}
		
		if (e.getEntity() instanceof Spider) {
			if (e.getCause() == DamageCause.POISON) e.setCancelled(true);
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
		}
		
		if (e.getEntity() instanceof Slime) {
			if (e.getCause() == DamageCause.FALL) e.setCancelled(true);
		}
		
		if (e.getEntity() instanceof Player) {
			
			Player player = (Player) e.getEntity();
			
			if (game.isHere(player.getUniqueId())) {
				UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
				
				if (e.getCause() == DamageCause.ENTITY_EXPLOSION || e.getCause() == DamageCause.BLOCK_EXPLOSION) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int) (e.getFinalDamage() * 20 * 0.7), 0, false, false));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 1.8 * 20), 1, false, false));
					return;
				}
				
				if (e.getCause() == DamageCause.FALL) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (e.getFinalDamage() * 2 * 20), 2, false, false));
					return;
				}
				
				if (e.getCause() == DamageCause.STARVATION) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 1, false, false));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 100, 1, false, false));
					return;
				}
			}
		}
	}
	
	public void entityCombustEvent(EntityCombustEvent e) {
		if (e.getEntity() instanceof Skeleton || e.getEntity() instanceof Zombie) {
			e.setCancelled(true);
		}
	}
	
	public void entityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		
		Game game = UHC.getInstance().getGameManager().getGame();
		Entity damaged = e.getEntity();
		Entity damager = e.getDamager();
		
		Random r = new Random();
		
		if (damager instanceof Spider) { //SPIDER
			Block web = damaged.getWorld().getBlockAt(damaged.getLocation());
			if (r.nextInt(3) == 0) web = damaged.getWorld().getBlockAt(damaged.getLocation().clone().add(0, 1, 0));
			
			if (web != null && web.getType() == Material.AIR) {
				web.setType(Material.WEB);
			}
		}
		
		if (damager.getType() == EntityType.SLIME) { //SLIME
			Slime slime = (Slime) e.getDamager();
			damaged.setVelocity(slime.getLocation().getDirection().multiply(slime.getSize() - 2).setY(1.1));
		}
				
		if (damager.getType() == EntityType.MAGMA_CUBE) { //MAGMA CUBE
			damaged.setFireTicks((r.nextInt(27) + 3) * 20);
		}
		
		if (damaged instanceof Slime) { //SLIME & MAGMA CUBE
			if (damaged instanceof Projectile) e.setCancelled(true);
		}
		
		if (e.getDamager() instanceof Zombie) { //ZOMBIE
			
			Zombie zombie = (Zombie) e.getDamager();
			
			if (zombie.getEquipment().getHelmet() != null && zombie.getEquipment().getHelmet().getType() == Material.TNT) {
				e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 3);
				e.setDamage(0);
				return;
			}
			
			if (e.getFinalDamage() == 0) return;
			
			if (e.getEntity() instanceof Player) {
				Player player = (Player) e.getEntity();
				
				if (game.isHere(player.getUniqueId())) {
					UHCPlayer p = game.getUHCPlayer(player.getUniqueId());
					p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 2));
					if (r.nextInt(5) == 0) p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 2));
				}
				
				
			}
		}
		
		//PROJECTILES -----------------------------------
		if (damager instanceof Arrow) {
			if (damaged instanceof Ghast) {
				 e.setCancelled(true);
			}
		}
	}
	
	public void entityDeathEvent(EntityDeathEvent e) {
		Entity entity = e.getEntity();
		EntityType type = e.getEntityType();
		
		if (type == EntityType.SPIDER) {
			
			Random r = new Random();
			World world = e.getEntity().getWorld();
			
			Spider spider = (Spider) entity;
			
			for (int x = spider.getLocation().getBlockX()-4; x <= spider.getLocation().getBlockX() + 4; x++) {
				for (int z = spider.getLocation().getBlockZ()-4; z <= spider.getLocation().getBlockZ() + 4; z++) {
					
					if (r.nextInt(3) == 0) {
						
						int choose = r.nextInt(2);
						int s = r.nextBoolean() ? 1 : -1;
						
						Block web = world.getBlockAt(x, spider.getLocation().getBlockY() + s *  choose , z);
						if (web != null && web.getType() == Material.AIR) web.setType(Material.WEB);
					}
				}
			}
		}
	}
	
	private void fire(Location location, Integer min, Integer max)  {
		int number = min + new Random().nextInt(max - min);
		
		for (int i = 0; i != number; i ++) {
			
			float x = -2.0F + (float) (Math.random() * 2.0D + 1.0D);
			float y = -3.0F + (float) (Math.random() * 3.0D + 1.0D);
			float z = -2.0F + (float) (Math.random() * 2.0D + 1.0D);
			
			@SuppressWarnings("deprecation")
			FallingBlock b = location.getWorld().spawnFallingBlock(location, Material.FIRE, (byte) 0);
			 
			b.setDropItem(false);
			b.setVelocity(new Vector(x, y, z));
			
		}
	}
	
	public void projectileHitEvent(ProjectileHitEvent e) {
		
		Projectile entity = e.getEntity();
		ProjectileSource shooter = entity.getShooter();
		
		EntityType type = e.getEntityType();
		
		if (type == EntityType.ARROW) {
			if (shooter != null && shooter  instanceof Skeleton) {
				Skeleton skeleton = (Skeleton) e.getEntity().getShooter();
				
				if (skeleton.getCustomName() != null && skeleton.getCustomName().equals("§cFire Skeleton")) {
					fire(skeleton.getLocation(), 3, 8);
				}
			}
		}
		
		if (type == EntityType.SMALL_FIREBALL) {
			if (shooter != null && (shooter instanceof Blaze || shooter instanceof Witch)) {
				fire(entity.getLocation(), 5, 20);
			}
		}
	}
	
	public void potionSplashEvent(PotionSplashEvent e) {
		ThrownPotion entity = e.getEntity();
		Random r = new Random();
		
		if (entity.getShooter() instanceof Witch) {
			
			if (r.nextInt(5) == 0) {
				e.getAffectedEntities().forEach(en->en.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 0)));
			}
			
			else if (r.nextInt(5) == 0) {
				e.getAffectedEntities().forEach(en->en.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 10, 0)));
			}
		}
	}
	
	public void projectileLaunchEvent(ProjectileLaunchEvent e) {
		Projectile entity = e.getEntity();
		ProjectileSource shooter = e.getEntity().getShooter();
		
		EntityType type = e.getEntityType();
		
		if (type == EntityType.FIREBALL) {
			Fireball ball = (Fireball) entity;
			
			if (ball.getShooter() != null && ball.getShooter() instanceof Ghast) {
				Ghast ghast = (Ghast) ball.getShooter();
				
				if (!ghast.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
					ball.setYield(4);
					ball.setVelocity(ball.getDirection().multiply(5));
				}
			}
			
			return;
		}
		
		if (shooter instanceof Witch) {
			if (e.getEntity() instanceof ThrownPotion) {
				
				ThrownPotion potion = (ThrownPotion) e.getEntity();
				Witch witch = (Witch) e.getEntity().getShooter();
				
				Random r = new Random();
				
				if (potion.getLocation().distance(witch.getLocation()) > 1.5 && r.nextInt(4) == 0) {
					e.setCancelled(true);
					shooter.launchProjectile(Fireball.class, witch.getLocation().getDirection());
					
				} else if (r.nextInt(4) == 0) {
					e.setCancelled(true);
					shooter.launchProjectile(SmallFireball.class, witch.getLocation().getDirection());
					
				} else if (r.nextInt(4) == 0) {
					e.setCancelled(true);
					shooter.launchProjectile(Arrow.class, witch.getLocation().getDirection());

				} else if (r.nextInt(8) == 0) {
					witch.getWorld().spawnEntity(witch.getLocation(), EntityType.ZOMBIE);
					e.setCancelled(true);
				}
			}
		}
	}
	
	public void entityExplodeEvent(EntityExplodeEvent e) {
		Entity entity = e.getEntity();
		
		if (entity instanceof Creeper) {
			Creeper creeper = (Creeper) entity;		
			e.setCancelled(true);
			
			if (creeper.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
				creeper.getWorld().createExplosion(e.getLocation(), 3, true);
				return;
			}
			
			if (creeper.getCustomName() != null && creeper.getCustomName().equals("§cCreeper Master")) {
				creeper.getWorld().createExplosion(e.getLocation(), 40, true);
				return;
			}
			
			if (new Random().nextInt(5) == 0) creeper.getWorld().createExplosion(e.getLocation(), 6, true);
			else creeper.getWorld().createExplosion(e.getLocation(), 5);
			return;
		}
	}
	
	public void entitySpawnEvent(EntitySpawnEvent e) {
		
		Game game = UHC.getInstance().getGameManager().getGame();
		Entity entity = e.getEntity();
		Location location = e.getLocation();
		Random r = new Random();
		
		//OVERWORLD -------------------------------------
		if (e.getEntity() instanceof Rabbit) { //RABBIT
			
			Rabbit rabbit = (Rabbit) e.getEntity();
			rabbit.setRabbitType(Type.THE_KILLER_BUNNY);
			rabbit.setCustomName("§cKiller Bunny");
			rabbit.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3));
			rabbit.setMaxHealth(1);
			return;
		}
		
		if (e.getEntity() instanceof Squid) { //GUARDIAN
			if (r.nextInt(5) == 0) {
				
				e.setCancelled(true);
				location.getWorld().spawnEntity(location, EntityType.GUARDIAN);
			}
			
			return;
		}
		
		if (entity instanceof Spider) { //SPIDER
			
			Spider spider = (Spider) entity;
			spider.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false));
			
			for (int i = r.nextInt(4); i >= 0; i--) {
				if (r.nextInt(4 + i) == 0) {
					spider.addPotionEffect(effects.get(r.nextInt(effects.size())));
				}
			}

		}
		
		if (entity instanceof Silverfish) { //SILVERFISH
			
			Silverfish silverFish = (Silverfish) entity;
			
			for (int i = r.nextInt(4); i >= 0; i--) {
				if (r.nextInt(4 + i) == 0) {
					silverFish.addPotionEffect(effects.get(r.nextInt(effects.size())));
				}
			}

		}
		
		if (entity instanceof Creeper) { //CREEPER
			 
			Creeper creeper = (Creeper) entity;			
			
			if (r.nextInt(60) == 0) {
				creeper.setCustomName("§cCreeper Master");
				creeper.setMaxHealth(100);
				
			} else if(r.nextInt(25) == 0) {
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, true, true));
				
			} else {
				creeper.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, false));
			}
		}
		
		if (entity instanceof Enderman) { //ENDERMAN
			Enderman enderman = (Enderman) entity;
			enderman.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2, false, false));
			return;
		}
		
		if (entity instanceof Skeleton) { //SKELETON
			
			Skeleton skeleton = (Skeleton) entity;
			EntityEquipment ee = skeleton.getEquipment();
			
			skeleton.setCanPickupItems(true);
			
			//WITHER SKELETON
			if (skeleton.getSkeletonType() == SkeletonType.WITHER) {
				
				if (r.nextInt(5) == 0) {
					ee.setItemInHand(new ItemStack(Material.BOW));
					return;
				}
			}
			
			//AXE
			if (r.nextInt(20) == 0) {
				ee.setItemInHand(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.FIRE_ASPECT, 2).getItem());
				skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
				
			//PUNCH
			} else if (r.nextInt(20) == 0) {
				
				ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
				LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
				meta.setColor(Color.GREEN);
				helmet.setItemMeta(meta);
				
				ee.setHelmet(helmet);
				ee.setItemInHand(new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_KNOCKBACK, 10).getItem());
				
				ee.setItemInHandDropChance(0);
				
			//FIRE
			} else if (r.nextInt(20) == 0) {
				
				skeleton.setCustomName("§cFire Skeleton");
				
				ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
				LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
				meta.setColor(Color.RED);
				helmet.setItemMeta(meta);
				
				ee.setHelmet(helmet);
			}
		}
		
		if (entity instanceof Zombie) { //ZOMBIE
			Zombie zombie = (Zombie) entity;
			
			zombie.setCanPickupItems(true);
			zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
			zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 1, false, false));
			
			if (r.nextInt(10) == 0) {
				Silverfish sv = (Silverfish) location.getWorld().spawnEntity(e.getLocation(), EntityType.SILVERFISH);
				zombie.setPassenger(sv);
			}
			
			if (r.nextInt(20) == 0) {
				
				zombie.setCanPickupItems(false);
				EntityEquipment ee = zombie.getEquipment();
				ee.setHelmet(new ItemStack(Material.TNT));
				return;
			}
			
			if (r.nextInt(20) == 0) {
				zombie.setCanPickupItems(false);
					
				EntityEquipment ee = zombie.getEquipment();
					
				ee.setItemInHand(new ItemStack(Material.IRON_SPADE));
				ee.setHelmet(new ItemStack(Material.GOLD_HELMET));
				return;
			}
			
			if (r.nextInt(20) == 0) {
				
				List<Player> players = getPlayersNext(game, zombie);
				if (!players.isEmpty() && zombie.getType() != EntityType.PIG_ZOMBIE) {
					
					int choose = r.nextInt(players.size());
					zombie.setTarget(players.get(choose));
					
					zombie.setCanPickupItems(false);
					
					EntityEquipment ee = zombie.getEquipment();
					
					ee.setItemInHand(new ItemStack(Material.IRON_PICKAXE));
					ee.setHelmet(new ItemStack(Material.GOLD_HELMET));
					
					ZombieMiner task = new ZombieMiner(game, zombie);
					tasks.add(task);
					task.runTaskTimer(UHC.getInstance(), 0, 60);
					return;
				}
			}
			
			int c = r.nextInt(8);
			
			if (c == 0) {
				zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2));
			}
			
			if (c == 1) {
				zombie.setMaxHealth(40);
			}
			
			if (c == 2) {
				zombie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
			}
			
			zombie.setHealth(zombie.getMaxHealth());
		}
		
		//NETHER ----------------------------------------
		if (entity instanceof Ghast) { //GHAST
			Ghast ghast = (Ghast) entity;
			
			if (r.nextInt(20) == 0) {
				ghast.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			}
			
			return;
		}
		
		if (entity instanceof Blaze) { //GHAST
			Blaze blaze = (Blaze) entity;
			
			if (r.nextInt(30) == 0) {
				blaze.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			}
			
			return;
		}
	}
	
	public class ZombieMiner extends BukkitRunnable {

		private Game game;
		private Zombie z;

		public ZombieMiner(Game game, Zombie z) {
			this.game = game;
			this.z = z;
		}

		@Override
		public void run() {

			if (z != null && !z.isDead() && z.isValid()) {
				if (!getPlayersNext().isEmpty()) {

					if (z.getTarget() != null) {
						
						if (canMineBotom()) {
							Break(z.getWorld().getBlockAt(z.getLocation().clone().add(0, -1, 0)));
							return;
						}

						Float yaw = (float) Math.round(z.getLocation().getYaw() / 90);

						BlockFace bf = null;
						if (yaw == -4 || yaw == 0 || yaw == 4) bf = BlockFace.SOUTH;
						if (yaw == -1 || yaw == 3) bf = BlockFace.EAST;
						if (yaw == -2 || yaw == 2) bf = BlockFace.NORTH;
						if (yaw == -3 || yaw == 1) bf = BlockFace.WEST;

						if (yaw != null && bf != null) {

							Block frontBlock = z.getLocation().getBlock().getRelative(bf, 1);
							Block frontBlockTop = z.getWorld().getBlockAt(frontBlock.getLocation().clone().add(0, 1, 0));

							Break(frontBlock);
							Break(frontBlockTop);

						}
					} else {
						List<Player> players = getPlayersNext();
						z.setTarget(players.get(new Random().nextInt(players.size())));
						return;
					}

				}
			} else {
				cancel();
				tasks.remove(this);
			}
		}

		private Boolean canMineBotom() {

			if (z != null && z.getTarget() != null) {
				if (z.getWorld().equals(z.getTarget().getWorld())) {
					if (z.getTarget().getLocation().getY() + 3 <= z.getLocation().getY() && z.getTarget().getLocation().getBlockY() <= 60) {
						
						if (MCMath.distance2D(z.getLocation(), z.getTarget().getLocation()) <= 10) {
							return true;
						}
					}
				}

			}

			return false;
		}

		private void Break(Block b) {
			
			if (b == null) return;

			if (b.getType() != Material.AIR && 
				b.getType() != Material.BEDROCK && 
				b.getType() != Material.OBSIDIAN && 
				b.getType().name().contains("WATER") && 
				b.getType().name().contains("LAVA")) {
				
				
				b.getLocation().getWorld().playEffect(b.getLocation().add(0.5, 0, 0.5), Effect.STEP_SOUND, b.getType());
				b.setType(Material.AIR);
			}
		}

		private List<Player> getPlayersNext() {

			List<Player> players = new ArrayList<Player>();

			for (UHCPlayer pl : game.getPlayingPlayers()) {
				Player p = pl.getPlayer();

				if (p.getWorld().equals(z.getWorld()) && p.getGameMode() == GameMode.SURVIVAL) {

					if (p.getLocation().distance(z.getLocation()) <= 30) {
						players.add(p);
					}
				}

			}

			return players;
		}
	}
	
	private List<Player> getPlayersNext(Game game, Zombie z) {
		
		List<Player> players = new ArrayList<Player>();
		
		for (UHCPlayer pl : game.getPlayingPlayers()) {
			Player p = pl.getPlayer();
			
			if (p.getWorld().equals(z.getWorld()) && p.getGameMode() == GameMode.SURVIVAL) {
				
				if (p.getLocation().distance(z.getLocation()) <= 30) {
					players.add(p);
				}
			}

		}
		
		return players;
	}
	
	@Override
	public void changeStateEvent(ChangeScenarioStateEvent e) {
		super.changeStateEvent(e);
		
		if (!e.getState()) {
			tasks.clear();
		}
	}
	
	@Override
	public void reload() {
		tasks.clear();
	}
}
