/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.shop;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cesium.Cesium;
import fr.redstonneur1256.easyinv.EasyInventorys;

public class CommandShop implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String message, String[] argument) {
		Cesium main = Cesium.get();
		if(sender instanceof Player) {
			Player p = (Player) sender;
			EasyInventorys.get().openGui(p, new MainShopGui(p, main.shop));
			return true;
		}else {
			return false;
		}
	}

}
