/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium;

import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
 
public class GMManager {
    public static User getGMUser(Player player){
        Plugin gm_plugin = Bukkit.getServer().getPluginManager()
                .getPlugin("GroupManager");
        if (gm_plugin instanceof GroupManager) {
            GroupManager gm = (GroupManager) gm_plugin;
            OverloadedWorldHolder world = gm.getWorldsHolder()
                    .getWorldDataByPlayerName(player.getName());
            return world.getUser(player.getName());
        }
        return null; // GroupManager not found
    }
    
    public static String getUserPrefix(Player player) {
        User gm_user = getGMUser(player);
        return gm_user.getVariables().getVarString("prefix");
    }
    
    public static void addUserPrefix(Player player, String prefix) {
        User gm_user = getGMUser(player);
        gm_user.getVariables().addVar("prefix", prefix);
    }
    
    public static void delUserPrefix(Player player) {
        User gm_user = getGMUser(player);
        gm_user.getVariables().removeVar("prefix");
    }
}