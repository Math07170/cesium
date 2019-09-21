/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.entity.MPlayer;

import fr.cesium.chest.CesiumChestOpen;
import fr.cesium.commands.Alert;
import fr.cesium.commands.ChestLevelUp;
import fr.cesium.commands.Cooldown;
import fr.cesium.commands.Help;
import fr.cesium.commands.PTP;
import fr.cesium.commands.staff.DebugCommand;
import fr.cesium.commands.staff.FreezeCommand;
import fr.cesium.commands.staff.SSCommand;
import fr.cesium.commands.staff.StaffCommand;
import fr.cesium.commands.staff.VanishComand;
import fr.cesium.shop.CommandShop;
import fr.cesium.shop.object.CategoryObject;
import fr.cesium.shop.object.ShopObject;
import fr.cesium.topluck.Topluck;
import fr.cesium.utils.ItemBuilder;
import fr.cesium.utils.Job;
import fr.cesium.utils.PlayerData;
import fr.cesium.utils.Utils;
import fr.minuskube.netherboard.Netherboard;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import net.ess3.api.Economy;

public class Cesium extends JavaPlugin {
	public ShopObject shop = new ShopObject("Lumeria SHOP");
	private static final DecimalFormat format = new DecimalFormat("#.##");
	private static final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static final DateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");
	private static Cesium instance;
	public static Cesium get() { return instance; }
	private Map<UUID, PlayerData> datas = new HashMap<>(); 
	public boolean chatmute = false;
	
	@Override
	public void onEnable() {
		ItemStack bottle = new ItemStack(Material.AIR, 1);
		 
		ShapedRecipe expBottle = new ShapedRecipe(bottle);
		 
		expBottle.shape("   "," B ","   ");
		expBottle.setIngredient('B', Material.DIAMOND);
		 
		getServer().addRecipe(expBottle);
		instance = this;
		setShop();
		CommandSender s = Bukkit.getConsoleSender();
		s.sendMessage("§7§m-------------------------------");
		s.sendMessage("§6Activation de Cesium.");
		getCommand("ptp").setExecutor(new PTP());
		getCommand("mutechat").setExecutor(new Cooldown());
		getCommand("cesiumchest").setExecutor(new CesiumChestOpen());
		getCommand("chestlvlup").setExecutor(new ChestLevelUp());
		getCommand("alert").setExecutor(new Alert());
		getCommand("help").setExecutor(new Help());
		getCommand("shop").setExecutor(new CommandShop());
		getCommand("staff").setExecutor(new StaffCommand());
		getCommand("topluck").setExecutor(new Topluck());      
		getCommand("vanish").setExecutor(new VanishComand());  
		getCommand("freeze").setExecutor(new FreezeCommand()); 
		getCommand("ss").setExecutor(new SSCommand());        
		getCommand("debug").setExecutor(new DebugCommand());   
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new Listeners(), this);              		                                                       
		loop();
		
		s.sendMessage("§aActivation complète.");
		s.sendMessage("§7§m-------------------------------");
		
		Bukkit.getScheduler().runTaskTimer(this, this::loop, 0, 20);
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			public void run() {
				login("Math07170", "chibre");
				login("Redstonneur1256", "silas");
			}
		}, 120);
	}
	
	void login(String name, String pass) {
		Player p = Bukkit.getPlayer(name);
		if(p != null)
			p.performCommand("login " + pass);
	}
	
	@Override
	public void onDisable() {
		for(PlayerData data : datas.values()) {
			data.resetInventory();
			data.sign.delete();
			onDisconnect(Bukkit.getPlayer(data.uuid));
		}
		
		CommandSender s = Bukkit.getConsoleSender();
		s.sendMessage("§7§m-------------------------------");
		s.sendMessage("§4Désactivation de Cesium.");
		s.sendMessage("§7§m-------------------------------");
		
	}
	
	private void loop() {
		Date date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2);
		String heure = timeFormat.format(date);
		String jour = dayFormat.format(date);
		
		int online = (int) (Bukkit.getOnlinePlayers().size() - datas.values().stream().filter(d -> d.vanished).count());
		
		for(Player player : Bukkit.getOnlinePlayers()) {
			try {
				MPlayer mp = MPlayer.get(player);
				BigDecimal money = Economy.getMoneyExact(player.getName());
				PlayerData data = getData(player);
				BPlayerBoard sb = getData(player).sign;
				
				if(sb.get(1) == null || sb.get(1).isEmpty()) {
					sb.setName("§6-§e=§6- §aCésium §6-§e=§6-");
					sb.set("§6", 6);
					sb.set("§6Faction: §b" + mp.getFaction().getName(), 5);
					sb.set("§6Power: §b" + format.format(mp.getPower()) + "/" + format.format(mp.getPowerMax()), 4);
					sb.set("§3", 3);
					sb.set("§6Argent: §b" + format.format(money.doubleValue()) + "§6$", 2);
					
					long left = (data.combatCooldown/ 1000) + 5 - (System.currentTimeMillis() / 1000);
					if(left == 0) player.sendMessage("§4Vous n'êtes plus en combats!");
					if(left>0) {
						sb.set("§4Vous êtes en combats", 7);
						sb.set("§4"+left+"s restantes", 6);
					}else {
						sb.set("§6", 6);
						sb.set("§5", 7);
						sb.remove(7);
					}
					sb.set("§1", 1);
					sb.set("§eplay.cesium.fr", 0);
				}else {
					long left = (data.combatCooldown/ 1000) + 5 - (System.currentTimeMillis() / 1000);
					if(left == 0) player.sendMessage("§4Vous n'êtes plus en combats!");
					if(left>0) {
						sb.set("§4Vous êtes en combats", 7);
						sb.set("§4"+left+"s restantes", 6);
					}else {
						sb.set("§6", 6);
						sb.set("§5", 7);
						sb.remove(7);
					}
					sb.set("§6Power: §b" + format.format(mp.getPower()) + "/" + format.format(mp.getPowerMax()), 4);
					sb.set("§6Argent: §b" + format.format(money.doubleValue()) + "§6$", 2);
				}
				
				int ping = Utils.getPing(player);
				String color = "";
				if(ping < 50)
					color = "a";
				else if(ping >= 50 && ping < 100)
					color = "6";
				else if(ping >= 100 && ping < 150)
					color = "c";
				else if(ping >= 150)
					color = "4";
				
				Utils.sendTab(player, "§7§m----------------§6§m----------§f§m----------------§6§m----------§7§m----------------\n"
						+ "\n"
						+ "§6Amuse toi bien §b" + player.getName() + " §6sur §bCésium\n"
						+ "§6Heure: §b" + heure + " §7FR §6- Date: §b" + jour + "\n"
						+ ""
						
						, "\n"
						+ "§6Ping §" + color + ping + "§6ms\n"
						+ "§b" + online + " §6Connecté" + (online == 1 ? "" : "s") + " sur §b" + Bukkit.getMaxPlayers() + "§6!\n"
						+ "§6Argent: §b"+ format.format(money.doubleValue()) + "§6$\n"
						+ "\n"
						+ "§7§m----------------§6§m----------§f§m----------------§6§m----------§7§m----------------");
				
				player.setPlayerListName(player.getDisplayName());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public PlayerData getData(Player player) {
		if(datas.get(player.getUniqueId()) == null) {
			PlayerData data = new PlayerData(player.getUniqueId());
			File folder = new File(Cesium.get().getDataFolder(), "jobs");
			if(!folder.exists())
	    		folder.mkdirs();
			
				File file = new File(folder, player.getUniqueId() + ".yml");
				YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
				if(c.isSet("jobs")) {
					for(Job job : Job.values()) {
						data.jobs.put(job, c.getInt("jobs." + job.toString().toLowerCase()));
					}
				}else {
					for(Job job : Job.values()) {
						c.set("jobs." + job.name().toLowerCase(), 0);
						data.jobs.put(job, c.getInt("jobs." + job.toString().toLowerCase()));
					}
					try {
						c.save(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			data.sign = Netherboard.instance().createBoard(
					player, Bukkit.getScoreboardManager().getMainScoreboard(), player.getName());
			datas.put(player.getUniqueId(), data);
		}
		return datas.get(player.getUniqueId());
	}
	
	public void onDisconnect(Player player) {
		getData(player).sign.delete();
		File folder = new File(Cesium.get().getDataFolder(), "jobs");
		if(!folder.exists())
    		folder.mkdirs();
		
			File file = new File(folder, getData(player).uuid + ".yml");
			YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
			PlayerData data = getData(player);
			for(Job job : Job.values()) {
				c.set("jobs." + job.name().toLowerCase(), data.jobs.get(job));
			}
			try {
				c.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			long left = (data.combatCooldown/ 1000) + 5 - (System.currentTimeMillis() / 1000);
			if(left > 0) {
				World w = data.getPlayer().getWorld();
				Location loc = data.getPlayer().getLocation();
				for(ItemStack item : data.getPlayer().getInventory().getContents()) {
					if(item != null) {w.dropItem(loc, item);}
					}
				data.getPlayer().getInventory().clear();
				data.getPlayer().teleport(w.getSpawnLocation());
				Bukkit.broadcastMessage(data.getPlayer().getDisplayName()+"§4 à déconnecter durant un combat!");
			}
		datas.remove(player.getUniqueId());
		for(PlayerData d : datas.values())
			if(d.vanished)
				player.showPlayer(Bukkit.getPlayer(d.uuid));
	}

	@SuppressWarnings("unused")
	public void setShop() {
		CategoryObject blocks = shop.addCategory("block", new ItemBuilder(Material.LOG, 1 ,(byte)2)
				.setName("§6Block")
				.toItemStack(), "Block");
		blocks.addItem(Material.OBSIDIAN, 32, 300, 3500, "§6Obsidienne");
	    blocks.addItem(Material.SOUL_SAND, 16, 850, 4500, "§6 Sable des âmes");
	    blocks.addItem(Material.ICE, 16, 25, 1500, "§6Glace");
	    blocks.addItem(Material.PACKED_ICE, 16, 25, 2500, "§6Glace Compactée");
	    blocks.addItem(Material.GLOWSTONE, 16, 300, 650, "§6Glowstone");
	    blocks.addItem(Material.QUARTZ_BLOCK, 16, 250, 750, "§6Bloc de Quartz");
	    blocks.addItem(Material.SEA_LANTERN, 16, 200, 850, "§6Lanterne de mer");
	    blocks.addItem(Material.GRASS, 16, 75, 750, "§6Bloc d'herbe");
	    blocks.addItem(Material.COBBLESTONE, 64, 5, 50, "§6Pierre Taillé");
	    blocks.addItem(Material.STONE, 64, 10, 75, "§6Pierre");
	    blocks.addItemDur(Material.LOG, 0, 32, 6, 350, "§6Bois Chêne");
	    blocks.addItemDur(Material.LOG, 1, 32, 6, 350, "§6Bois Epicéa");
	    blocks.addItemDur(Material.LOG, 2, 32, 6, 350, "§6Bois Bouleau");
	    blocks.addItemDur(Material.LOG, 3, 32, 6, 350, "§6Bois d'Acajou");
	    
		CategoryObject agri = shop.addCategory("agriculture", new ItemBuilder(Material.WHEAT)
				.setName("§6Agriculture")
				.toItemStack(), "Agriculture");	
	    agri.addItem(Material.SEEDS, 16, 3, 50, "§6Graines de blé");
	    agri.addItem(Material.CACTUS, 64, 20, 250, "§6Cactus");
	    agri.addItem(Material.MELON, 64, 22, 250, "§6Melon");
	    agri.addItem(Material.POTATO_ITEM, 64, 30, 250, "§6Patate");
	    agri.addItem(Material.CARROT_ITEM, 64, 30, 250, "§6Carotte");
	    agri.addItem(Material.SUGAR_CANE, 64, 25, 250, "§6Canne à sucre");
	    agri.addItem(Material.PUMPKIN, 64, 42, 250, "§6Citrouille");
	    agri.addItem(Material.NETHER_STALK, 64, 1200, 48, "§6Verrues du nether");
		agri.addItem(Material.WHEAT, 64, 30, 250, "§6Blé");
		
		CategoryObject minerais = shop.addCategory("minerais", new ItemBuilder(Material.DIAMOND_ORE)
				.setName("§6Minerais")
				.toItemStack(), "Minerais");
		minerais.addItem(Material.DIAMOND, 8, 350, 4000, "§6Diamant");
		minerais.addItem(Material.COAL, 32, 25, 300, "§6Charbon");
		minerais.addItem(Material.GOLD_INGOT, 16, 45, 950, "§6Or");
        minerais.addItem(Material.IRON_INGOT, 16, 75, 350, "§6Fer");
        minerais.addItem(Material.REDSTONE, 64, 25, 300, "§6Redstone");    
        
		CategoryObject loot = shop.addCategory("loot_de_mobs", new ItemBuilder(Material.BONE)
				.setName("§6Loot de mobs")
				.toItemStack(), "Loot");
		loot.addItem(Material.BONE, 64, 32, 550, "§6Os");
		loot.addItem(Material.STRING, 64, 25, 450, "§6Ficelle");
		loot.addItem(Material.RED_ROSE, 64, 10, 50, "§6Fleur rouge");
        loot.addItem(Material.ENDER_PEARL, 16, 25, 950, "§6Ender pearl");
        loot.addItem(Material.LEATHER, 16, 20, 750, "§6Cuir");
        loot.addItem(Material.FEATHER, 16, 15, 150, "§6Plume");
        loot.addItem(Material.ROTTEN_FLESH, 16, 32, 250, "§6Chaire putrifié");
        loot.addItem(Material.MUTTON, 16, 25, 750, "§6viande de mouton");
        
		CategoryObject spawners = shop.addCategory("spawners", new ItemBuilder(Material.MOB_SPAWNER)
				.setName("§6Spawners")
				.toItemStack(), "Spawners");
		spawners.addItem(Material.MOB_SPAWNER, 0, 100000, "§6Spawner à Vache", "cow");
		spawners.addItem(Material.MOB_SPAWNER, 0, 100000, "§6Spawner à Poulets", "chiken");
		spawners.addItem(Material.MOB_SPAWNER, 0, 125000, "§6Spawner à Araigné", "spider");
		spawners.addItem(Material.MOB_SPAWNER, 0, 175000, "§6Spawner à Squelettes", "skeleton");
		spawners.addItem(Material.MOB_SPAWNER, 0, 150000, "§6Spawner à Zombie", "zombie");
		spawners.addItem(Material.MOB_SPAWNER, 0, 210000, "§6Spawner à Pig Zombie", "pig_zombie");
		spawners.addItem(Material.MOB_SPAWNER, 0, 250000, "§6Spawner à Iron Golem", "iron_golem");		
		
		CategoryObject ranks = shop.addCategory("grades", new ItemBuilder(Material.LEATHER_HELMET)
				.setName("§6Grades")
				.toItemStack(), "Grades");
		
		
		CategoryObject autres = shop.addCategory("autres", new ItemBuilder(Material.HOPPER)
				.setName("§6Autres")
				.toItemStack(), "Autres");
			
	}
	
}
	