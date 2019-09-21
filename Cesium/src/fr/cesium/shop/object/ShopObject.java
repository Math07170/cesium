/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.shop.object;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class ShopObject {
	public String name;
	private Map<String, CategoryObject> categories = new HashMap<>();
	public ShopObject(String name) {
		this.name = name;
	}
	public CategoryObject addCategory(String name, ItemStack icon, String displayname) {
		if(displayname == null)
			displayname = name;
		CategoryObject cat = new CategoryObject(name, icon, displayname);
		categories.put(name, cat);
		return cat;
	}
	
	public CategoryObject getCategoryByName(String name) {
		return categories.get(name);
	}
	public Collection<CategoryObject> getCats(){
		return categories.values();
	}
	
}