/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands;

import java.io.File;
import java.math.BigDecimal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.cesium.Cesium;
import net.ess3.api.Economy;

public class ChestLevelUp implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cNTM!");
			return false;
		}
		Player player = (Player) sender;
		
		try {
			BigDecimal money = Economy.getMoneyExact(player.getName());
			if(money.doubleValue() < 200) {
				player.sendMessage("§cVous n'avez pas assez d'argent.");
				return false;
			}
			
	    	File folder = new File(Cesium.get().getDataFolder(), "inventorys");
	    	if(!folder.exists())
	    		folder.mkdirs();
	    	File file = new File(folder, player.getUniqueId() + ".yml");
	    	if(!file.exists()) {
	    		player.sendMessage("§cVous n'avez pas encore d'enderchest!");;
	    		return false;
	    	}
	    	
			YamlConfiguration c = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection sec = c.getConfigurationSection("inventory");
			
			int current = sec.getInt("level");
			int newLevel = current + 1;
			
			if(newLevel > 6) {
				player.sendMessage("§cVous ne pouvez pas avoir plus de 6 lignes.");
				return false;
			}
			
			sec.set("level", newLevel);
			c.save(file);
			Economy.add(player.getName(), new BigDecimal(-200));
			player.sendMessage("§aVous avez augmenté votre enderchest de une ligne, cela vous a couté 200$");
			
		}catch (Exception e) {
			e.printStackTrace();
			player.sendMessage("§cUne erreur est survenue.");
		}
		
		return false;
	}
	
}