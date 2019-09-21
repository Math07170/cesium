/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.cesium.Cesium;

public class Cooldown implements CommandExecutor {
	
	public boolean onCommand(CommandSender Sender, Command cmd, String s, String[] strings) {
		Cesium m = Cesium.get();
        if(m.chatmute == false){
            m.chatmute = true;
            Sender.sendMessage("�c[�cC�sium�4Chat�c] Vous venez de d�sactiver le chat.");
            Bukkit.broadcastMessage("�c[�cC�sium�4Chat�c] Le chat est d�sactiv�!");            
        }
        else if(m.chatmute == true) {
            m.chatmute = false;
            Sender.sendMessage("�c[�cC�sium�4Chat�c] Vous venez de r�activer le chat!");
            Bukkit.broadcastMessage("�c[�cC�sium�4Chat�c] Le chat est de nouveau actif!");
        }
        return false;
    }
	
}