package fr.cesium;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.massivecraft.factions.entity.MPlayer;

import fr.cesium.topluck.TopLuckPlayer;
import fr.cesium.utils.ItemBuilder;
import fr.cesium.utils.PlayerData;
import fr.cesium.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import us.myles.ViaVersion.api.Via;

public class Listeners implements Listener {

	@EventHandler
	public void onLogin(AsyncPlayerPreLoginEvent e) {
		int protocl = Via.getAPI().getPlayerVersion(e.getUniqueId());
		if(protocl < 47) {
			e.setLoginResult(Result.KICK_OTHER);
			e.setKickMessage(""
					+ "§7§m------------------------------\n"
					+ "\n"
					+ "§6Luméria\n"
					+ "\n"
					+ "§cMerci d'utiliser Minecraft 1.8 ou plus.\n"
					+ "Mais pas Pactify!"
					+ "\n"
					+ "§7§m------------------------------");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Cesium lum = Cesium.get();
		Player p = (Player) e.getPlayer();
		e.setJoinMessage("[§2+§f]" + ChatColor.translateAlternateColorCodes('&', GMManager.getGMUser(p).getGroup().getVariables().getVarString("prefix"))
				+" "+p.getDisplayName());
		if(p.hasPermission("administrator")) {
		for(Player other : Bukkit.getOnlinePlayers()) {
			if(lum.getData(other).vanished)
				p.hidePlayer(other);
		}
		}
		/*int protocl = Via.getAPI().getPlayerVersion(p.getUniqueId());
		System.out.println(protocl);
		//Version 1.8->1.11
		if(protocl >= 47 && protocl < 110) {
			p.setResourcePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
			p.setTexturePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
		}
		if(protocl >= 110 && protocl < 210) {
			p.setResourcePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
			p.setTexturePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
		}
		if(protocl >= 210 && protocl < 340) {
			p.setResourcePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
			p.setTexturePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
		}
		if(protocl >= 340) {
			p.setResourcePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
			p.setTexturePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
		}
		p.setResourcePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
		p.setTexturePack("http://download1489.mediafire.com/9ywh6mufs6rg/5htd7tqqpz4eq1o/FaithfulCustom.zip");
		p.sendMessage("Pour avoir les ajouts il est impératif d'utiliser Optifine(Inclu sous Pactify)!");
		p.sendMessage("Télécharger le ici : https://optifine.net/downloads");
		p.sendMessage("Si le pack ne s'active pas vérifier vos options! ");
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(100.0D);
	*/
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		e.setQuitMessage("[§4-§f]" + player.getDisplayName());
		Cesium lum = Cesium.get();
		lum.onDisconnect(player);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		
		if(Cesium.get().getData(e.getPlayer()).moderator) e.setCancelled(true);
		Player player = e.getPlayer();
		Block block = e.getBlock();
		if(block == null) return;
		Material material = block.getType();
		switch (material) {
		case DIAMOND_ORE:
			TopLuckPlayer.getPlayerTopluck(player).diamond++;
			break;
		case STONE:
			TopLuckPlayer.getPlayerTopluck(player).stone++;
			break;
		default:
			break;
		}
	}
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(Cesium.get().getData(e.getPlayer()).moderator) e.setCancelled(true);
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(Cesium.get().getData(e.getPlayer()).freezed)
			e.setCancelled(true);
	}
	
	@EventHandler
	public void onTakeDamageByEntity(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player)) return;
		if(event.isCancelled())
			return;
		
		Player player = (Player) event.getDamager();
		PlayerData datap = Cesium.get().getData(player);
		Entity entity = event.getEntity();
		if(entity instanceof Player) {
			PlayerData datae = Cesium.get().getData((Player) entity);
			System.out.println(datap.combatCooldown);
			System.out.println();
			long left = (datap.combatCooldown/ 1000) + 5 - (System.currentTimeMillis() / 1000);
			if(left>0) {
				datap.combatCooldown = 0;
				datap.combatCooldown = System.currentTimeMillis();
				datae.combatCooldown = 0;
				datae.combatCooldown = System.currentTimeMillis();
			}else {
				player.sendMessage("§c[§cCésium§4Combat§c] Vous êtes en combat avec " + entity.getName() + " il vous reste 5§cs pour pouvoir déconnecter");
				datap.combatCooldown = 0;
				datap.combatCooldown = System.currentTimeMillis();
				datae.getPlayer().sendMessage("§c[§cCésium§4Combat§c] Vous êtes en combat avec " + player.getName() + " il vous reste 5§cs pour pouvoir déconnecter");
				datae.combatCooldown = 0;
				datae.combatCooldown = System.currentTimeMillis();
			}
		}
		new Timer().schedule(new TimerTask() {
			public void run() {
				String msg = "§6Combat §a" + entity.getName() + " " + Utils.getHearts((LivingEntity) entity);
				Utils.sendActionBar(player, msg);
			}
		}, 10);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Cesium main = Cesium.get();
		Player p = (Player) e.getPlayer();
		ItemStack item = p.getInventory().getItemInMainHand();
		if(main.getData(p).moderator) {
			if(item.getType() == Material.DIAMOND_ORE) {
				p.performCommand("topluck");
			}
			e.setCancelled(true);
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock() != null
				&& e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
			e.setCancelled(true);
			e.getPlayer().performCommand("cesiumchest");
		}
		
		
	}
	
	//Listeners Staff
	@EventHandler
	public void onInteractEntity(PlayerInteractAtEntityEvent e) {
		if(e.isCancelled()) { return;}
		Cesium main = Cesium.get();
		Player p = (Player) e.getPlayer();
		ItemStack item = p.getInventory().getItemInMainHand();
		if(main.getData(p).moderator && e.getRightClicked() instanceof Player) {
			if(e.getHand() == EquipmentSlot.OFF_HAND) return;
			Player target = (Player)e.getRightClicked();
			if(item.getType() == Material.BLAZE_ROD) {
				p.performCommand("freeze "+target.getName());
				e.setCancelled(true);
			}
			if(item.getType() == Material.BARRIER) {
				p.performCommand("ss "+target.getName());
				e.setCancelled(true);
			}
		}else {
			return;
		}
	}
	@EventHandler
	public void PickupItem(PlayerPickupItemEvent e) {
		PlayerData data = Cesium.get().getData(e.getPlayer());
		if(data.moderator || data.vanished) e.setCancelled(true);
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Cesium m = Cesium.get();
		Player player = event.getPlayer();
		if(!player.hasPermission("chat.admin")) {
			PlayerData data = m.getData(player);
			if(m.chatmute){
				player.sendMessage("§c[§cCésium§4Chat§c] Désolé, le chat est actuellement désactivé.");
				event.setCancelled(true);
				return;
			}else {
					long left = (data.chatCooldown / 1000) + 5 - (System.currentTimeMillis() / 1000);
					if(left > 0) {
						player.sendMessage("§c[§cCésium§4Chat§c] Désolé, vous devez attendre " + left + "§cs pour pouvoir envoyer un message");
						event.setCancelled(true);
						return;
					}else {
						data.chatCooldown = 0;
						data.chatCooldown = System.currentTimeMillis();
					}
			}
		}
		
		MPlayer mp = MPlayer.get(player);
		if(mp.getFaction().getName() != null) {
			String role = "";
			switch (mp.getRole()) {
			case RECRUIT:
				role = "-";
				break;
			case MEMBER:
				role = "+";
				break;
			case OFFICER:
				role = "*";
				break;
			case LEADER:
				role = "**";
				break;
			default:
				break;
			}
			String msg = event.getMessage()
					.replace("<3", "§c❤§f");
			
			TextComponent ss = new TextComponent("§c⚠");
			ss.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ss " + player.getName() + " mute"));
			ss.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[] {new TextComponent("§aClique pour mute " + player.getName())}));
			
			TextComponent message = new TextComponent(role +""+ mp.getFaction().getName() + " " + player.getDisplayName() + "§7: §f" + msg);
			TextComponent mod = new TextComponent(ss, message);
			
			for(Player p : player.getWorld().getPlayers()) {
				if(p.getWorld() != player.getWorld()) continue;
				p.spigot().sendMessage(p.hasPermission("moderator") ? mod : message);
			}
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDie(PlayerDeathEvent e) {
		Cesium m = Cesium.get();
		m.getData(e.getEntity()).chatCooldown = 0;
		e.getEntity().spigot().respawn();
	}
	@SuppressWarnings("serial")
	@EventHandler
	public void onCraftCraftItem(PrepareItemCraftEvent e) {
		System.out.println("OK");
		checkCraft(new ItemStack(Material.DIAMOND), e.getInventory(), new HashMap<Integer, ItemStack>(){{
			put(4, new ItemBuilder(Material.DIAMOND).addLoreLine("Test").toItemStack());
		}});
	}
	public void checkCraft(ItemStack result, CraftingInventory inv, HashMap<Integer, ItemStack> ingredients) {
		ItemStack[] matrix = inv.getMatrix();
		for(int i = 0; i < 9; i++) {
			System.out.println("OK " + matrix[i] + " " +ingredients.get(i));
			if(ingredients.get(i) == null) {
				if(matrix[i] == null || matrix[i].getType() == Material.AIR) {
						continue;
				}
			}
			if(ingredients.containsKey(i)) {
				if(matrix[i] == null || !matrix[i].equals(ingredients.get(i))) {
					return;
				}
			} else {
				if(matrix[i] != null) {
					System.out.println(matrix[i]);
					return;
				}
			}
		}
		inv.setResult(result);
	}
}

