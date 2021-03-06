package yt.lost.main.configuration

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import CreateItemCommand.createGuiItem
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class Settings: Listener{
    val inventory: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Settings")
    var ffOn: Boolean = false

    init{
        inventory.setItem(0, createGuiItem(Material.IRON_SWORD, "PVP", "PVP ist", if(Bukkit.getServer().getWorld("world")!!.pvp){"§aAn"} else{"§4Aus"}))
        inventory.setItem(1, createGuiItem(Material.APPLE, "Friendly Fire", "Friendly Fire ist: ", if(ffOn){"§aAn"}else{"§4Aus"}))
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        if(event.inventory != inventory)
            return

        event.isCancelled = true
        val item = event.currentItem

        if(item!!.type == Material.IRON_SWORD){
            Bukkit.getServer().getWorld("world")!!.pvp = !Bukkit.getServer().getWorld("world")!!.pvp
            inventory.setItem(0, createGuiItem(Material.IRON_SWORD, "PVP", "PVP ist", if(Bukkit.getServer().getWorld("world")!!.pvp){"An"} else{"Aus"}))
        }else if(item.type == Material.APPLE){
            ffOn = !ffOn
            inventory.setItem(1, createGuiItem(Material.APPLE, "Friendly Fire", "Friendly Fire ist: ", if(ffOn){"§aAn"}else{"§4Aus"}))
        }
    }
}