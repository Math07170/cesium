/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import fr.cesium.events.OnLevelUp;
import fr.minuskube.netherboard.bukkit.BPlayerBoard;

public class PlayerData {
	
	public final UUID uuid;
	public boolean freezed, vanished, moderator;
	public BPlayerBoard sign;
	public User groupManagerUser;
	private ItemStack[] items;
	private boolean saved = false;
	public long chatCooldown;
	public long combatCooldown;
	public Map<Job, Integer> jobs = new HashMap<>(); 
	
	public PlayerData(UUID uuid) {
		Player player = Bukkit.getPlayer(uuid);
		Plugin gm_plugin = Bukkit.getServer().getPluginManager()
                .getPlugin("GroupManager");
        if (gm_plugin instanceof GroupManager) {
            GroupManager gm = (GroupManager) gm_plugin;
            OverloadedWorldHolder world = gm.getWorldsHolder()
                    .getWorldDataByPlayerName(player.getName());
            this.groupManagerUser = world.getUser(player.getName());
        }
		this.uuid = uuid;
	}
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public void resetInventory() {
		if(!saved)
			return;
		getPlayer().getInventory().setContents(items);
		saved = false;
	}
	
	public void saveInventory() {
		if(saved)
			return;
		items = getPlayer().getInventory().getContents();
		saved = true;
	}
	
	public void addXp(Job job, int xp) {
		int lvl = getLevel(jobs.get(job));
		jobs.put(job, jobs.get(job) + xp);
		if(lvl != getLevel(jobs.get(job))) {
			OnLevelUp levelupevent = new OnLevelUp(getPlayer(), job, getLevel(jobs.get(job)), getNeeded(jobs.get(job)), jobs.get(job));
			Bukkit.getPluginManager().callEvent(levelupevent);
		}
	}
	public int getLevel(int xp) {
		int level = 0;
		// (LVL*2500)+(LVL*2500)*0,25
		// 2500
		
		int price = (int) ((level * 2500) + (level * 2500) * 0.25); // Xp pour passer au niveau suivant
		while(xp >= price) {                                         // Tant que l'xp > au prix
			level++;                                                // On monte le level
			xp -= price;                                            // On retire a l'xp le prix requis
			price = (int) ((level * 2500) + (level * 2500) * 0.25); // On recalcule le prix pour le niveau suivant
		}
		return level;
	}
	public int getNeeded(int xp) {
		int level = getLevel(xp);
		int price = (int) ((level * 2500) + (level * 2500) * 0.25); // Xp pour passer au niveau suivant
		return price;
	}
	public int getProgression(int xp) {
		int level = getLevel(xp);
		int tempxp = xp;
		int i = 0;
		int price = (int) ((i * 2500) + (i * 2500) * 0.25);
		while(i < level) {
			i++;
			tempxp -= price;
			price = (int) ((i * 2500) + (i * 2500) * 0.25);
		}
		return tempxp;
	}
	
}
