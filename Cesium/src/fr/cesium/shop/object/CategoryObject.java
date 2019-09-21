/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.shop.object;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.cesium.utils.ItemBuilder;

public class CategoryObject {
	public List<ItemObject> items = new ArrayList<>();
	public final ItemStack icon;
	public final String name;
	public final String displayName;
	public CategoryObject(String name, ItemStack icon, String displayName) {
		this.icon = icon;
		this.name = name;
		this.displayName = displayName;
	}
	
	public ItemObject getItemByItemStack(ItemStack stack) {
		return items.stream().filter(o -> o.itemstack.getType() == stack.getType()).findFirst().get();
	}
	public void addItem(Material material, int sell, int buy, String name) {
		addItem(material, 1, 0, sell, buy, name, null);
	}
	public void addItem(Material material, int sell, int buy, String name, String command) {
		addItem(material, 1, 0, sell, buy, name, command);
	}
	public void addItem(Material material, int amount, int sell, int buy, String name) {
		addItem(material, amount, 0, sell, buy, name, null);
	}
	public void addItem(Material material, int amount, int sell, int buy, String name, String command) {
		addItem(material, amount, 0, sell, buy, name, command);
	}
	public void addItemDur(Material material, int dur, int sell, int buy, String name) {
		addItem(material, 1, dur, sell, buy, name, null);
	}
	public void addItemDur(Material material, int dur, int amount, int sell, int buy, String name) {
		addItem(material, amount, dur, sell, buy, name, null);
	}
	public void addItemDur(Material material, int dur, int sell, int buy, String name, String command) {
		addItem(material, 1, dur, sell, buy, name, command);
	}
	public void addItem(Material material, int amount, int dur, int sell, int buy, String name, String command) {
		items.add(new ItemObject(new ItemBuilder(material, amount, (byte)dur).setName(name)
				.addLoreLine("§bAchat: §4"+buy+"$")
				.addLoreLine("§bVente: §2"+sell+"$")
				.addLoreLine("§eClick gauche pour acheter")
				.addLoreLine("§eClick droit pour vendre")
				.addLoreLine("§eClick de la mollette pour tout vendre")
				.toItemStack(), sell, buy, name, command));
		        
	}
	
}