/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.gui;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.cesium.utils.ItemBuilder;
import fr.redstonneur1256.easyinv.abstractClasses.Gui;

public class SSGui extends Gui {
	
	private ItemStack skull;
	private Player target;
	private boolean op;
	public SSGui(Player player, Player target, String toExecute) {
		super(player, "§6SS: §b" + target.getName(), 6);
		this.target = target;
		op = player.isOp();
		player.setOp(true);
		
		skull = new ItemBuilder(Material.SKULL_ITEM)
				.setDurability((short)SkullType.PLAYER.ordinal())
				.setSkullOwner(target.getName())
				.setName("§6" + target.getName())
				.toItemStack();
		
		onClick(player, skull, "opengui" + toExecute);
	}
	
	@Override
	public void onClose(Player player) {
		super.onClose(player);
		
		player.setOp(op);
	}
	
	@Override
	public void update(Player player) {
		
		clear();
		
		setSlotData(new ItemBuilder(Material.SIGN)
				.setName("§6Mute").toItemStack(), 20, "openguimute");
		
		setSlotData(new ItemBuilder(Material.LEATHER_BOOTS)
				.setName("§6Coup de pied au cul (Kick)")
				.setLeatherArmorColor(Color.fromBGR(0, 0, 255))
				.toItemStack(), 24, "openguikick");
		
		setSlotData(new ItemBuilder(Material.BARRIER)
				.setName("Coup de pied definitif")
				.toItemStack(), 31, "openguiban");
	}

	@Override
	public boolean onClick(Player player, ItemStack stack, String action) {
		if(action.equals("openguimute")) {
			clear();
			
			setSlotData(skull, 4, action);
			for(int i = 9; i < 18; i++)
				setSlotData(new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§6").setDurability((short)15).toItemStack(), i, "none");
			
			setSlotData(
					new ItemBuilder(Material.STONE)
					.setName("§6Spam")
					.toItemStack(), 18, "mute_Spam_1h");
			setSlotData(
					new ItemBuilder(Material.STONE)
					.setName("§6Spam MP")
					.toItemStack(), 19, "mute_Spam MP_1h");
			setSlotData(
					new ItemBuilder(Material.STONE)
					.setName("§6Insulte")
					.toItemStack(), 20, "mute_Insulte_2h");
			setSlotData(
					new ItemBuilder(Material.STONE)
					.setName("§6Insulte Staff")
					.toItemStack(), 20, "mute_Insulte Staff_3h");
			
			setSlotData(new ItemBuilder(Material.ARROW).setName("§6Retour").toItemStack(), size-1, "back");
		}else if(action.equals("openguikick")) {
			clear();
			
			setSlotData(skull, 4, action);
			for(int i = 9; i < 18; i++)
				setSlotData(new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§6").setDurability((short)15).toItemStack(), i, "none");
			
			setSlotData(
					new ItemBuilder(Material.STONE)
					.setName("§6...")
					.toItemStack(), 18, "kick_...");
			
			setSlotData(new ItemBuilder(Material.ARROW).setName("§6Retour").toItemStack(), size-1, "back");
		}else if(action.equals("openguiban")) {
			clear();
			
			setSlotData(skull, 4, action);
			for(int i = 9; i < 18; i++)
				setSlotData(new ItemBuilder(Material.STAINED_GLASS_PANE).setName("§6").setDurability((short)15).toItemStack(), i, "none");
			
			int index = 18;
			for(BanReason type : BanReason.values()) {
				setSlotData(
						new ItemBuilder(type.getIcon())
						.setName("§6" + type.getName())
						.toItemStack(), index++, "ban_" + type.getName() + "_" + type.getTime());
			}
			setSlotData(new ItemBuilder(Material.ARROW).setName("§6Retour").toItemStack(), size-1, "back");
		}else if(action.startsWith("mute_")) {
			String[] parts = action.split("_");
			
			String reason = parts[0];
			String time = parts[1];
			
			clear();
			
			setSlotData(new ItemBuilder(Material.SKULL_ITEM).setName("§6" + target.getName())
					.setSkullOwner(target.getName())
					.addLoreLine("Mute: " + reason)
					.setDurability((short)SkullType.PLAYER.ordinal())
					.toItemStack(), 22, "none");
			
			setSlotData(new ItemBuilder(Material.WOOL).setDurability((short)5)
					.setName("§aConfirmer")
					.toItemStack(), 30, "muteconf_" + reason + "_" + time);
			
			setSlotData(new ItemBuilder(Material.WOOL).setDurability((short)14)
					.setName("§4annuler")
					.toItemStack(), 32, "openguimute");
			
		}else if(action.startsWith("kick_")) {
			String[] parts = action.split("_");
			
			String reason = parts[1];
			
			clear();
			
			setSlotData(new ItemBuilder(Material.SKULL_ITEM).setName("§6" + target.getName())
					.setSkullOwner(target.getName())
					.addLoreLine("Kick: " + reason)
					.setDurability((short)SkullType.PLAYER.ordinal())
					.toItemStack(), 22, "none");
			
			setSlotData(new ItemBuilder(Material.WOOL).setDurability((short)5)
					.setName("§aConfirmer")
					.toItemStack(), 30, "kickconf_" + reason);
			
			setSlotData(new ItemBuilder(Material.WOOL).setDurability((short)14)
					.setName("§4annuler")
					.toItemStack(), 32, "openguikick");
			
		}else if(action.startsWith("ban_")) {
			
			String[] parts = action.split("_");
			
			String reason = parts[1];
			String time = parts[2];
			
			clear();
			
			setSlotData(new ItemBuilder(Material.SKULL_ITEM).setName("§6" + target.getName())
					.setSkullOwner(target.getName())
					.addLoreLine("Mute: " + reason)
					.setDurability((short)SkullType.PLAYER.ordinal())
					.toItemStack(), 22, "none");
			
			setSlotData(new ItemBuilder(Material.WOOL).setDurability((short)5)
					.setName("§aConfirmer")
					.toItemStack(), 30, "banconf_" + reason + "_" + time);
			
			setSlotData(new ItemBuilder(Material.WOOL).setDurability((short)14)
					.setName("§4annuler")
					.toItemStack(), 32, "openguiban");
			
		}else if(action.startsWith("banconf_")) {
			String[] parts = action.split("_");
			
			String reason = parts[1];
			String time = parts[2];
			
			player.performCommand("tempban " + target.getName() + " " + time + " " + reason);
			
			player.closeInventory();
		}else if(action.startsWith("muteconf")) {
			String[] parts = action.split("_");
			
			String reason = parts[1];
			String time = parts[2];
			
			player.performCommand("mute " + target.getName() + " " + time + " " + reason);
			
			player.closeInventory();
		}else if(action.startsWith("kickconf_")) {
			String[] parts = action.split("_");
			String reason = parts[1];
			player.performCommand("kick " + target.getName() + " " + reason);
			player.closeInventory();	
		}else if(action.equals("back")) {
			update(player);
		}
		return true;
	}
	
}