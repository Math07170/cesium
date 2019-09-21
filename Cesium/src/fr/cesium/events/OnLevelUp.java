/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import fr.cesium.utils.Job;

public class OnLevelUp extends Event {
	
	private final Job job;
	private final Player player;
	private final int newLevel;
	private final int xp;
	private final int neededXp;
	public OnLevelUp(Player player, Job job, int lvl, int need, int xp) {
		this.player = player;
		this.job = job;
		this.newLevel = lvl;
		this.neededXp = need;
		this.xp = xp;
		}
	
	private static final HandlerList HANDLERS = new HandlerList();
	  
	public HandlerList getHandlers() {     
		return HANDLERS;  
	}

	  
	public static HandlerList getHandlerList() {    
		return HANDLERS; 
	}


	public Job getJob() {
		return this.job;
	}


	public Player getPlayer() {
		return this.player;
	}


	public int getNewLevel() {
		return this.newLevel;
	}


	public int getXp() {
		return this.xp;
	}


	public int getNeededXp() {
		return this.neededXp;
	}
	    

}
