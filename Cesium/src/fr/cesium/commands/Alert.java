/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.earth2me.essentials.utils.StringUtil;

public class Alert implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String message, String[] args) {
		if(args.length == 0) {
			sender.sendMessage("§c[§4LumAlert§c] Vous n'avez pas utiliser le bon format, utiliser celui ci: /alert [message]");
			return false;
		}
		
		Bukkit.broadcastMessage("§c[§4LumAlert§c] " + ChatColor.translateAlternateColorCodes('&',
				StringUtil.joinList(" ", (Object[]) args)));
		return false;
	}

}
