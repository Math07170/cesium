/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.topluck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cesium.gui.GUITopLuck;
import fr.redstonneur1256.easyinv.EasyInventorys;

public class Topluck implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command USELESS, String label, String[] args) {
		if(!(sender instanceof Player)) {
			List<TopLuckPlayer> players = new ArrayList<TopLuckPlayer>(TopLuckPlayer.getToplucks());
			Collections.sort(players, new GUITopLuck.ShortByPercent());
			for(TopLuckPlayer player : players) {
				sender.sendMessage("§a" + player.name + " diamants: " + player.diamond + " stone: " + player.stone + " %: " + player.percent());
			}
			return false;
		}
		
		Player player = (Player)sender;
		EasyInventorys.get().openGui(player, new GUITopLuck(player));
		return false;
	}

}
