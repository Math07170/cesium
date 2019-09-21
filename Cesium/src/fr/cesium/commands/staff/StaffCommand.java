/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands.staff;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.cesium.Cesium;
import fr.cesium.utils.ItemBuilder;
import fr.cesium.utils.PlayerData;

public class StaffCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command USELESS, String label, String[] args) {
		Cesium main = Cesium.get();
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cErreur: La console ne peut pas se mettre en staff.");
			return false;
		}
		Player player = (Player) sender;
		PlayerData d = main.getData(player);
		if(d.moderator == true) {
			d.resetInventory();
			player.sendMessage("§cVous sortez du mode modérateur.");
			d.moderator = false;
			d.getPlayer().setInvulnerable(false);
		}else {
			d.saveInventory();
			d.moderator = true;
			player.getInventory().clear();
			player.getInventory().addItem(new ItemBuilder(Material.BLAZE_ROD).setName("Freeze").toItemStack());
			player.getInventory().addItem(new ItemBuilder(Material.BARRIER).setName("SS").toItemStack());
			player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_ORE).setName("TopLuck").toItemStack());
			player.sendMessage("§aVous passez en mode modération");
			d.getPlayer().setInvulnerable(true);
		}
		
		return false;
	}

}