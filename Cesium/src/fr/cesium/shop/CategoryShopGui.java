/*******************************************************************************
 * Copyright (c) 2019 Mathias ROBERT and Cesium to Present
 * All Rights Reserved.
 *******************************************************************************/
package fr.cesium.shop;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import fr.cesium.Cesium;
import fr.cesium.shop.object.CategoryObject;
import fr.cesium.shop.object.ItemObject;
import fr.cesium.utils.ItemBuilder;
import fr.redstonneur1256.easyinv.abstractClasses.Gui;
import net.ess3.api.Economy;

public class CategoryShopGui extends Gui {
	private CategoryObject cat;
	public CategoryShopGui(Player player, CategoryObject cat) {
		super(player, "SHOP Luméria: §6"+cat.displayName, 3);
		this.cat = cat;
		update(player);
	}
	
	@Override
	public void update(Player player) {
		if(cat == null)
			return;
		
		int i = 0;
		for(ItemObject item : cat.items) {
			setSlotData(item.itemstack, i++, item.itemstack.getItemMeta().getDisplayName());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onClick(Player player, ItemStack stack, String action, ClickType clickType) {
		if(stack == null) return true;
		if(stack.getType() == null || stack.getType() == Material.AIR) return true;
		
		
		//Catégorie grades
		if(cat.displayName.equalsIgnoreCase("grades")) {
			try {
				if(Economy.getMoneyExact(player.getName()).doubleValue() < cat.getItemByItemStack(stack).buy) {
					player.closeInventory();
					player.sendMessage("§cVous n'avez pas assez d'argent.");
					return true;
				}
				Economy.add(player.getName(), -cat.getItemByItemStack(stack).buy);;
				String args = cat.getItemByItemStack(stack).command;
				System.out.println(args);
				Cesium lum = Cesium.get();
				switch (lum.getData(player).groupManagerUser.getGroupName()) {
				case "Guerrier": break;
				default:
					break;
				}					
				
				ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
				Bukkit.dispatchCommand(console, "manuadd "+player.getName()+" "+args);
				player.sendMessage("§aVotre nouveau grade vien d'être appliquer bon jeux sur Luméria!");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}
		
		
		
		//Spawners
		if(cat.name.equalsIgnoreCase("spawners")) {
			try {
				if(Economy.getMoneyExact(player.getName()).doubleValue() < cat.getItemByItemStack(stack).buy) {
					player.closeInventory();
					player.sendMessage("§cVous n'avez pas assez d'argent.");
					return true;
				}
				Economy.add(player.getName(), -cat.getItemByItemStack(stack).buy);;
				String args = cat.getItemByItemStack(stack).command;
				System.out.println(args);
				ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
				Bukkit.dispatchCommand(console, "spawners add "+args+" "+player.getName());
				player.sendMessage("§aVotre spawner de "+args+" a bien été ajouter à votre /spawners!");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return true;
			}
		}
		
		//miscelanous
		if(cat.name.equalsIgnoreCase("autres")) {
			if(cat.getItemByItemStack(stack).command != null) {
				try {
					if(Economy.getMoneyExact(player.getName()).doubleValue() < cat.getItemByItemStack(stack).buy) {
						player.closeInventory();
						player.sendMessage("§cVous n'avez pas assez d'argent.");
						return true;
					}
					Economy.add(player.getName(), -cat.getItemByItemStack(stack).buy);;
					String args = cat.getItemByItemStack(stack).command;
					ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
					Bukkit.dispatchCommand(console, args.replaceAll("%player%", player.getName()));
					player.sendMessage("§aVotre spawner de "+args+" a bien été ajouter à votre /spawners!");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return true;
				}
			}
		}
		
		
		//autres catégories
		if(clickType.isLeftClick()) {
			try {
				ItemObject item = cat.getItemByItemStack(stack);
				int buy = item.buy;
				if(buy <= 0) {
					player.closeInventory();
					player.sendMessage("§cCet objet ne peut pas être acheté");
					return true;
				}
				if(Economy.getMoneyExact(player.getName()).doubleValue() < buy) {
					player.closeInventory();
					player.sendMessage("§cVous n'avez pas assez d'argent.");
					return true;
				}
				ItemStack stacks = new ItemBuilder(item.itemstack.getType(), item.itemstack.getAmount(), (byte)item.itemstack.getDurability())
						.toItemStack();
				Economy.add(player.getName(), -item.buy);
				player.getInventory().addItem(stacks);
			} catch (Exception e) {
				e.printStackTrace();
				player.sendMessage("§cUne erreur est survenue, merci de contacter un administrateur.");
			}
			
		}else if(clickType.isRightClick()) {
			ItemStack[] items = player.getInventory().getContents();
			for(int i = 0; i < items.length; i++) {
				ItemStack it = items[i];
				if(it == null)
					continue;
				if(it.getType() == Material.AIR) return true;
				if(it.getType() == null) return true;
				if(it.getType() != stack.getType())
					continue;
				ItemObject item = cat.getItemByItemStack(stack);
				if(item == null)
					continue;
				if(it.getAmount() < stack.getAmount())
					continue;
				
				try {
					Economy.add(player.getName(), item.sell);
					if(it.getAmount() == stack.getAmount()) 
						player.getInventory().setItem(i, null);
					else
						it.setAmount(it.getAmount()-stack.getAmount());
					player.updateInventory();
					player.sendMessage("§aVous avez bien vendu " + it.getType() + "§bx" + stack.getAmount() + "§a!");
				} catch (Exception e) {
					e.printStackTrace();
					player.sendMessage("§cUne erreur est survenue.");
				}
				return true;
			}
			player.sendMessage("§cVous ne possédez pas cet objet! Si vous avez cet objet vérifier que vous l'avez stacker");
		}else if(clickType == ClickType.MIDDLE) {
			int amountitem = 0;
			Double money = 0.0; 
			for(ItemStack item : player.getInventory().getContents()) {
				if(item == null) continue;
				if(item.getType() == Material.AIR || item.getType() == null || item == null) continue;
				if(item.getType() == stack.getType()) {
					try {
						ItemObject object = cat.getItemByItemStack(stack);
						Double value1 = (double) object.sell/object.itemstack.getAmount();
						money = money + (item.getAmount()*value1);
						amountitem = amountitem+item.getAmount();
						Economy.add(player.getName(), (item.getAmount()*value1));
						
					} catch (Exception e) {
						e.printStackTrace();
						return true;
					} 
				}
			}
			player.getInventory().remove(stack.getType());
			player.sendMessage("§6Vous avez vendu §2"+amountitem+" "+stack.getType().name()+"§6 pour §2"+money+"$!");
		}
		return true;
	}
}