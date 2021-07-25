package yt.lost.main

import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class BingoTeam {

    private var name: String? = null
    private var amount: Int? = null
    private var itemsToGet: LinkedList<ItemStack>? = LinkedList()
    private var backpack: Inventory? = null

    constructor(name: String, amount: Int){
        this.name = name
        this.amount = amount

        backpack = Bukkit.createInventory(null, InventoryType.BARREL, "Backpack $name")
    }

    fun onItemCollect(item: ItemStack){
        if(itemsToGet!!.contains(item)){

        }
    }

    fun setItemsToGet(list: LinkedList<ItemStack>){
        this.itemsToGet = list
    }

    fun getItems(): Inventory{
        val inventory: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Items: $name")

        for(item in itemsToGet!!){
            inventory.addItem(item)
        }
    }
}