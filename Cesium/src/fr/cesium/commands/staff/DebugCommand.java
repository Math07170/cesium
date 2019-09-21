/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands.staff;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.sun.management.OperatingSystemMXBean;


public class DebugCommand implements CommandExecutor {
	private static final OperatingSystemMXBean system = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean());
	private static final Runtime runtime = Runtime.getRuntime();
	private static final double total = system.getTotalPhysicalMemorySize();
	private static final double go = 1024 * 1024 * 1024;
	private static final DecimalFormat format = new DecimalFormat("###.##");

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String label, String[] args) {
		if(!sender.isOp()) {
			sender.sendMessage("§cTu n'a pas la permission!");
			return false;
		}

		processor(sender);
		sender.sendMessage("");
		ram(sender);
		sender.sendMessage("");
		Bukkit.dispatchCommand(sender, "tps");
		sender.sendMessage("");
		Bukkit.dispatchCommand(sender, "plugins");
		
		return false;
	}
	
	private void processor(CommandSender sender) {
		double used = system.getSystemCpuLoad();
		double thisUsed = system.getProcessCpuLoad();
		
		double percent = used * 100;
		double thisPercent = thisUsed * 100;

		sender.sendMessage("§6Usage du processeur:");
		sender.sendMessage("");
		sender.sendMessage("§6Total: §b" + format(percent) + "%");
		sender.sendMessage("§6Ce serveur: §b" + format(thisPercent) + "%");
	}
	
	private void ram(CommandSender sender) {
		double free = system.getFreePhysicalMemorySize();
		double used = total - free;
		
		double percent = (used / total) * 100;
		double totalGo = total / go;
		double usedGo = used / go;
		
		double thisFree = runtime.freeMemory();
		double thisTotal = runtime.totalMemory();
		double thisUsed = thisTotal - thisFree;
		double thisPercent = thisUsed / thisTotal * 100;
		double thisTotalPercent = thisUsed / total * 100;
		
		sender.sendMessage("§6Usage de la ram:");
		sender.sendMessage("");
		sender.sendMessage("§6Total: §b" + format(usedGo) + "§7/§b" + format(totalGo) + "Go §6Pourcentage: §b" + format(percent) + "%");
		sender.sendMessage("§6Ce serveur: §b" + format(thisUsed / go) + "§7/§b" + format(thisTotal / go) + "Go §6Pourcentage: §b" + format(thisPercent) + "% §6Pourcentage total: §b" + format(thisTotalPercent) + "%");
	}
	
	public static String format(double d) {
		String format = DebugCommand.format.format(d);
		
		String[] split = format.split(",");
		
		if(split.length != 2) {
			split = new String[] {String.valueOf((int)d), ""};
		}
		while(split[1].length() < 2) {
			split[1] += "0";
		}
		
		
		return split[0] + "," + split[1];
	}
	
}