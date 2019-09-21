/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands.staff;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cesium.Cesium;
import fr.cesium.utils.PlayerData;

public class FreezeCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command USELESS, String label, String[] args) {
		if(!(sender instanceof Player)) {
			String[] messages = {"FDP", "CONNARD", "PUTE"};
			Random random = new Random();
			
			sender.sendMessage(messages[random.nextInt(messages.length)]);
			return false;
		}
		
		if(args.length == 0) {
			sender.sendMessage("§cMerci de presicer un joueur");
			return false;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		if(target == null || !target.isOnline()) {
			sender.sendMessage("§cLe joueur " + args[0] + " n'est pas connecté!");
			return false;
		}
		PlayerData data = Cesium.get().getData(target);
		
		if(data.freezed)
			sender.sendMessage("§aTu unfreeze " + target.getName());
		else
			sender.sendMessage("§atu freeze " + target.getName());
		
		data.freezed = !data.freezed;
		
		return false;
	}

}
