/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cesium.Cesium;
import fr.cesium.utils.PlayerData;

public class VanishComand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String message, String[] argument) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cErreur: La console ne peut pas se mettre en vanish.");
			return false;
		}
		Player player = (Player) sender;
		Cesium main = Cesium.get();
		PlayerData data = main.getData(player);
		
		if(data.vanished) {
			player.sendMessage("§aWoosh!!! §bVous êtes redevenu §4visible!");
			Bukkit.broadcastMessage("[§2+§f]"+player.getName());
			data.vanished = false;
			player.setInvulnerable(false);
			for(Player p : Bukkit.getOnlinePlayers())
				p.showPlayer(player);
		}else {
			player.sendMessage("§aWoosh!!! §bVous êtes evenu §4invisible!");
			data.vanished = true;
			Bukkit.broadcastMessage("[§4-§f]"+player.getName());
			player.setInvulnerable(true);
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p.hasPermission("administrator")) continue;
				p.hidePlayer(player);
			}
		}
		
		return true;
	}
	
}