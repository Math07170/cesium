/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.chest;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.cesium.Cesium;
import fr.redstonneur1256.easyinv.EasyInventorys;

public class CesiumChestOpen implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if(!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		
    	File folder = new File(Cesium.get().getDataFolder(), "inventorys");
    	if(!folder.exists())
    		folder.mkdirs();
    	File file = new File(folder, player.getUniqueId() + ".yml");
    	
		YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
		ConfigurationSection section;
		if(c.isConfigurationSection("inventory")) {
			section = c.getConfigurationSection("inventory");
		}else {
			section = c.createSection("inventory");
		}
		
		if(!section.isSet("level"))
			section.set("level", 1);
		if(!section.isSet("content"))
			section.set("content", new ItemStack[0]);
		try {
			c.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		EasyInventorys.get().openGui(player, new CesiumChest(player, section.getInt("level"), file));
		return false;
	}
	
}