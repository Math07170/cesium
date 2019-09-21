/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.topluck;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class TopLuckPlayer {
	private static Map<UUID, TopLuckPlayer> toplucks = new HashMap<>();
	public static TopLuckPlayer getPlayerTopluck(Player player) {
		if(toplucks.get(player.getUniqueId()) == null) {
			toplucks.put(player.getUniqueId(), new TopLuckPlayer(player.getName(), player.getUniqueId()));
		}
		return toplucks.get(player.getUniqueId());
	}
	public static Collection<TopLuckPlayer> getToplucks() {
		return toplucks.values();
	}
	
	public final String name; 
	public final UUID uuid;
	public int stone;
	public int diamond;
	private TopLuckPlayer(String name, UUID uuid) {
		this.name = name;
		this.uuid = uuid;
	}
	public String percent() {
		try {
			return new DecimalFormat("#####.##").format(realPercent()) + "%";
		} catch(Exception ex) {
			return "NaN";
		}
	}
	public double realPercent() {
		try {
			return ((double)diamond) / ((double)stone) * 100.0;
		} catch(Exception ex) {
			return 0;
		}
	}
}