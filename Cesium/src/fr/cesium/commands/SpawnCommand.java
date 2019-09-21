/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import fr.cesium.Cesium;

public class SpawnCommand implements CommandExecutor {

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String message, String[] argument) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("§cLa console ne peut être téléportée.");
			return false;
		}
		final Player player = (Player) sender;
		Cesium main = Cesium.get();
		
		Bukkit.getScheduler().runTaskAsynchronously(main, new BukkitRunnable() {
			public void run() {
				Location current = player.getLocation().getBlock().getLocation();
				
				player.sendMessage("Téléportation dans 5 secondes, ne bougez pas.");
				
				for(int i = 0; i < 5; i++) {
					try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
					if(!current.equals(player.getLocation().getBlock().getLocation())) {
						player.sendMessage("§cVous avez bougé, téléportation annulée.");
						cancel();
						return;
					}
				}
				
				player.sendMessage("§aTéléportation!");
				player.teleport(player.getWorld().getSpawnLocation().getBlock().getLocation().clone().add(0.5, 0, 0.5));
			}
		});
		return false;
		
	}

}