/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Help implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String message, String[] args) {
		sender.sendMessage("§c-----------------§eCésium help§c-----------------");
		sender.sendMessage("§c>>> §eLe /shop §7(§6vous permet de vendre ou d'acheter des items/grades/clefs avec de l'argent en jeux§7)");
	    sender.sendMessage("§c>>> §eLe /warp §7(§6Vous permet de vous téléporté aux endroits importants tels que la forge, les tables d'enchant ou autre§7)");
	    sender.sendMessage("§c>>> §eLe /spawn §7(§6vous permet de vous téléporté au spawn§7");
	    sender.sendMessage("§c>>> le /help sera fini lorsque les autres commandes le seront !");	
		return false;
	}

}
