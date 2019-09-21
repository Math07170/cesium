/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.cesium.topluck.TopLuckPlayer;
import fr.cesium.utils.ItemBuilder;
import fr.redstonneur1256.easyinv.abstractClasses.Gui;

public class GUITopLuck extends Gui {
	
	public static class ShortByPercent implements Comparator<TopLuckPlayer> {
		public int compare(TopLuckPlayer p, TopLuckPlayer p1) {
			return (int) (p1.realPercent() - p.realPercent());
		}
	}
	
	private List<TopLuckPlayer> ordered;
	private int page;
	public GUITopLuck(Player player) {
		super(player, "Topluck", 6);
		ordered = new ArrayList<>();
		
		TopLuckPlayer.getToplucks().stream().sorted(new ShortByPercent()).forEach(p -> ordered.add(p));
		
		update(player);
	}
	
	@Override
	public void update(Player player) {
		if(ordered == null)
			return;
		
		int index = 4 * 9 * (page);
		
		for(int i = 0; i < 9 * 4; i++)
			setSlotData(null, i, "none");
		
		TopLuckPlayer top;
		for(int i = 0; i < 9 * 4; i++) {
			if(ordered.size() > i + index) {
				top = ordered.get(i + index);
				setSlotData(new ItemBuilder(Material.SKULL_ITEM)
						.setDurability((short)SkullType.PLAYER.ordinal())
						.setSkullOwner(top.name)
						.setName(top.name)
						.addLoreLine("§6Pourcentage: §b" + top.percent())
						.addLoreLine("§9Diamants §6minés: §b" + top.diamond)
						.addLoreLine("§7Pierre §6minés: §b" + top.stone)
						.toItemStack(), i, "open_" + (i + index));
			}
		}
		
		for(int i = 36; i < 45; i++)
			setSlotData(new ItemBuilder(Material.STAINED_GLASS_PANE).setDurability((short)15).setName("§6 ").toItemStack(), i, "none");
		
		
		setSlotData(new ItemBuilder(Material.WOOL).setName("§6Page precedente: " + (page - 1)).setDurability((short)14).toItemStack(), 45, "page_" + (page - 1));
		setSlotData(new ItemBuilder(Material.WOOL).setName("§6Page suivante: " + (page + 1)).setDurability((short)5).toItemStack(), 53, "page_" + (page + 1));
		
	}
	
	@Override
	public boolean onClick(Player player, ItemStack stack, String action) {
		int pages = (ordered.size() / (4 * 9)) + 1;
		
		if(action.startsWith("page_")) {
			int newPage = Integer.parseInt(action.split("_")[1]);
			
			if(newPage < 0)
				return true;
			if(newPage >= pages)
				return true;
			
			page = newPage;
			update(player);
		}else if(action.startsWith("open_")) {
			int index = Integer.parseInt(action.split("_")[1]);
			
			TopLuckPlayer p = ordered.get(index);
			
			for(int i = 0; i < size; i++)
				setSlotData(null, i, "none");
			
			setSlotData(new ItemBuilder(Material.SKULL_ITEM)
					.setDurability((short)SkullType.PLAYER.ordinal())
					.setSkullOwner(p.name)
					.setName("§6" + p.name)
					.toItemStack(), 0, "none");
			
			setSlotData(new ItemBuilder(Material.ENDER_PEARL)
					.setName("§6Se téléporter")
					.toItemStack(), 1, "teleport_" + p.uuid.toString());
			
			setSlotData(new ItemBuilder(Material.BLAZE_ROD)
					.setName("Freeze")
					.toItemStack(), 2, "freeze_" + p.name);

			setSlotData(new ItemBuilder(Material.BARRIER)
					.setName("Ban")
					.toItemStack(), 3, "ss_" + p.name);
			
		}else if(action.startsWith("teleport_")) {
			UUID uuid = UUID.fromString(action.split("_")[1]);
			Player target = Bukkit.getPlayer(uuid);
			player.teleport(target);
			player.closeInventory();
			
		}else if(action.startsWith("freeze_")) {
			String pname = action.split("_")[1];
			
			player.performCommand("freeze " + pname);
			
			player.closeInventory();
		}else if(action.startsWith("ss")) {
			String pname = action.split("_")[1];
			player.closeInventory();

			player.performCommand("ss " + pname);
		}
		return true;
	}
	
}