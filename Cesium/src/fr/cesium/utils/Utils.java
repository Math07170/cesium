package fr.cesium.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.EntityPlayer;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;

/*
 * Author: Silas (Redstonneur1256)
 * pour certaine chose surement copier coler d'internet par Redstonneur1256
 */
public class Utils {
	
	public static int getPing(Player p) {
		int ping = -1;
		CraftPlayer cp = (CraftPlayer) p;
		EntityPlayer ep = cp.getHandle();
		ping = ep.ping;
		return ping;
	}

	@SuppressWarnings("deprecation")
	public static String getHearts(LivingEntity p) {
		final String hearth = "§4❤";
		final String demihearth = "§c❤";
		final String deathheart = "§7❤";
		StringBuilder SHeal = new StringBuilder();
		double heal = p.getHealth()/2;
		while(heal > 0) {
			if(heal == -0.5 || heal == 0.5) {
				SHeal.append(demihearth);
			}else {
				SHeal.append(hearth);
			}
			heal -= 1;
		}
		for(int i = 0; i < 50; i++) {
			SHeal.append(deathheart);
		}
		SHeal.setLength((int)(p.getMaxHealth()/2)*3);
		return SHeal.toString();	
	}
	
	public static void sendActionBar(Player p, String text) {
		  PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
		  ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
	}

	public static Class<?> getNmsClass(String nmsClassName)	throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
	}
	
	public static String getServerVersion()	{
		return Bukkit.getServer().getClass().getPackage().getName().substring(23);
	}
	
	public static void sendTab(Player p, String head, String foot) {
		try {
			if (getServerVersion().equalsIgnoreCase("v1_12_R1")) {
				Object header = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { head });
				Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { foot });
				
				Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[0]).newInstance(new Object[0]);
				
				Field fa = ppoplhf.getClass().getDeclaredField("a");
				fa.setAccessible(true);
				fa.set(ppoplhf, header);
				Field fb = ppoplhf.getClass().getDeclaredField("b");
				fb.setAccessible(true);
				fb.set(ppoplhf, footer);
				
				Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
				Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
				
				pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
			}else if ((getServerVersion().equalsIgnoreCase("v1_9_R1")) || 
					(getServerVersion().equalsIgnoreCase("v1_9_R2")) || 
					(getServerVersion().equalsIgnoreCase("v1_10_R1")) || 
					(getServerVersion().equalsIgnoreCase("v1_11_R1")) || 
					(getServerVersion().equalsIgnoreCase("v1_12_R1"))) {
			        Object header = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { head});
			        Object footer = getNmsClass("ChatComponentText").getConstructor(new Class[] { String.class }).newInstance(new Object[] { foot });
			        
			        Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
			        
			        Field f = ppoplhf.getClass().getDeclaredField("b");
			        f.setAccessible(true);
			        f.set(ppoplhf, footer);
			        
			        Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
			        Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
			        
			        pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
			}else if ((getServerVersion().equalsIgnoreCase("v1_8_R2")) || 
					(getServerVersion().equalsIgnoreCase("v1_8_R3"))) {
				Object header = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + head + "'}" });
				Object footer = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + foot + "'}" });
				
				Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
				
				Field f = ppoplhf.getClass().getDeclaredField("b");
				f.setAccessible(true);
				f.set(ppoplhf, footer);
				
				Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
				Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
				
				pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
			}else {
				Object header = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + head + "'}" });
				Object footer = getNmsClass("ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + foot + "'}" });
				
				Object ppoplhf = getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[] { getNmsClass("IChatBaseComponent") }).newInstance(new Object[] { header });
				
				Field f = ppoplhf.getClass().getDeclaredField("b");
				f.setAccessible(true);
				f.set(ppoplhf, footer);
				
				Object nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
				Object pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
				
				pcon.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(pcon, new Object[] { ppoplhf });
			}
		}
		catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException|NoSuchMethodException|SecurityException|ClassNotFoundException|InstantiationException|NoSuchFieldException e) {
			e.printStackTrace();
		}
	}
	
	public double distance(Location loc1, Location loc2) {
		double dX = loc1.getX() - loc2.getX();
		double dZ = loc1.getZ() - loc2.getZ();
		return Math.sqrt(Math.pow(dX, 2) + Math.pow(dZ, 2));
	}
	public static int getChunkChest(Chunk base, int rayon) {
		int i = 0;
		World w = base.getWorld();
		List<Chunk> chunks = new ArrayList<>();
		for(int x = (base.getX()-rayon); x <= (base.getX()+rayon); x++) {
			for(int z = (base.getZ()-rayon); z <= (base.getZ()+rayon); z++) {
				chunks.add(w.getChunkAt(x, z));
			}
		}
		for(Chunk chunk : chunks) {
			if(!chunk.isLoaded()) chunk.load();
			for(BlockState e : chunk.getTileEntities()) {
				if(e.getBlock().getType() == Material.CHEST 
						|| e.getBlock().getType() == Material.TRAPPED_CHEST 
						|| e.getBlock().getType() == Material.ENDER_CHEST
						|| e.getBlock().getType() == Material.HOPPER)
				i++;
			}
		}
		return i;
	}
	@SuppressWarnings("unchecked")
	public static List<Chest> getAllChest(World world) {
		List<Chest> chests = new ArrayList<>();
		for(Chunk c : world.getLoadedChunks()) {
			chests.addAll((Collection<? extends Chest>) Arrays.asList(c.getTileEntities())
					.stream()
					.filter(b -> b instanceof Chest)
					.collect(Collectors.toList()));
        }
		return chests;
	}
}
