/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cesium.Cesium;
import fr.cesium.utils.PlayerData;

public class PTP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
		if(sender instanceof Player) {
			Cesium ces = Cesium.get();
			Player p = (Player) sender;
			PlayerData data = ces.getData(p);
			if(!data.moderator) {
				p.sendMessage("§c[§cCésium§4Staff§c] Vous devez être en /staff pour effectuer cette commande");
				return true;
			}
			if(strings.length == 1) {
				Player target = Bukkit.getPlayer(strings[0]);
				p.teleport(target);
				p.sendMessage("§c[§cCésium§4Staff§c] Vous vous téléportez à "+ target.getName() + "!");
			}else {
				p.sendMessage("Sytaxe incorecte /ptp <Player>");
			}
		}
		return true;
	}

}
