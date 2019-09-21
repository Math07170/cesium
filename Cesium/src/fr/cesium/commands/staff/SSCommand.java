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

import fr.cesium.gui.SSGui;
import fr.redstonneur1256.easyinv.EasyInventorys;

public class SSCommand implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cTu as déjà vu beaucoup d'inventaire dans la console toi!");
			return false;
		}
		Player player = (Player) sender;
		if(args.length < 1) {
			player.sendMessage("§cUsage: /" + label + " <joueur>");
			return false;
		}
		Player target = Bukkit.getPlayer(args[0]);
		if(target == null) {
			player.sendMessage("§cLe joueur " + args[0] + " n'existe pas!");
			return false;
		}
		EasyInventorys.get().openGui(player, new SSGui(player, target, args.length == 2 ? args[1] : null));
		return false;
	}

}
