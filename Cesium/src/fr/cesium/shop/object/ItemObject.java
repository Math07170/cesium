/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.shop.object;

import org.bukkit.inventory.ItemStack;

public class ItemObject {
	public final int sell, buy;
	public final ItemStack itemstack;
	public final String name, command;
	public ItemObject(ItemStack itemstack, int sell, int buy, String name, String command) {
		this.sell = sell;
		this.buy = buy;
		this.itemstack = itemstack;
		this.name = name;
		this.command=command;
	}
}
