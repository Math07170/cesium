/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.chest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.redstonneur1256.easyinv.abstractClasses.Gui;

public class CesiumChest extends Gui {
	private File file;
	@SuppressWarnings("unchecked")
	protected CesiumChest(Player player, int level, File file) {
		super(player, "§6CesiumChest: §b" + player.getName(), level);
		this.file = file;
		int i = 0;
		for(ItemStack item : (List<ItemStack>) YamlConfiguration.loadConfiguration(file)
				.getConfigurationSection("inventory").get("content")) {
			setSlotData(item, i++, "");
		}
	}
	
	@Override
	public void onClose(Player p) {
        YamlConfiguration c = new YamlConfiguration();
        ConfigurationSection section = c.createSection("inventory");
        section.set("level", inventory.getTopInventory().getSize() / 9);
        section.set("content", inventory.getTopInventory().getContents());
        try {
			c.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Player player) {
	}
	
	@Override
	public boolean onClick(Player player, ItemStack stack, String action) {
		return false;
	}
	
}