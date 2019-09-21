/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.cesium.shop.object.CategoryObject;
import fr.cesium.shop.object.ShopObject;
import fr.redstonneur1256.easyinv.EasyInventorys;
import fr.redstonneur1256.easyinv.abstractClasses.Gui;

public class MainShopGui extends Gui {
	
	private ShopObject shop;
	public MainShopGui(Player player, ShopObject shop) {
		super(player, "Lumeria SHOP", 2);
		this.shop = shop;
		update(player);
	}
	
	@Override
	public void update(Player player) {
		if(shop == null)
			return;
		
		int index = 0;
		for(CategoryObject cat : shop.getCats()) {
			setSlotData(cat.icon, index++, cat.name);
		}
	}
	
	/*
	 * Method called when an item get clicked
	*/
	@Override
	public boolean onClick(Player player, ItemStack stack, String action) {
		if(stack == null || stack.getType() == Material.AIR)
			return true;
		EasyInventorys.get().openGui(player, new CategoryShopGui(player, shop.getCategoryByName(action)));
		return true;
	}
	
}