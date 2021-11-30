package yt.lost.main.configuration

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

val inventory: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Settings")