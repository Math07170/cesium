/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.gui;

import org.bukkit.Material;

public enum BanReason {
	
	CHEATN("CHEAT(Non avoué)", Material.BARRIER, "21d"),
	CHEATA("CHEAT Avoué", Material.BARRIER, "21d"),
	MDDOS("Menace de DDOS", Material.BEDROCK, "21d"),
	DDOS("DDOS", Material.BEDROCK, "21d"),
	IRRESPECT("Non respect d'un modérateur", Material.FISHING_ROD, "7d"),
	IRRESPECTC("Non respect d'un modérateur + CHEAT", Material.FEATHER, "30d"),;
	
	private String name;
	private Material icon;
	private String time;
	private BanReason(String name, Material icon, String time) {
		this.name = name;
		this.icon = icon;
		this.time=time;
		
	}
	
	public String getName() {
		return name;
	}
	public Material getIcon() {
		return icon;
	}
	public String getTime() {
		return time;
	}
	
}