package yt.lost.main.entities

import CreateItemCommand.createGuiItem
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*


class BingoTeam (var name: String, var leader: Player) {

    var itemsToGet: LinkedList<ItemStack> = LinkedList()
    private val member: LinkedList<Player> = LinkedList()
    var backpack: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Backpack $name")
    var inventory: Inventory = Bukkit.createInventory(null, 9, "Items $name:")

    init{
        member.add(leader)
    }

    fun onItemCollect(player: Player, item: ItemStack) : Boolean{
        if(!member.contains(player)) {
            return false
        }
        var c = 0
        for(currentItem in this.itemsToGet){
            if(currentItem.type == item.type){
                val array= itemsToGet.toTypedArray()
                array[c] = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "§9${currentItem.type.name} §ageschafft!", "§7Dieses Item musst du nun nicht mehr sammeln")
                itemsToGet = LinkedList(array.asList())
                reloadItems()
                return true
            }
            c += 1
        }
        return false
    }

    private fun reloadItems(){
        var i = 0
        for(items in itemsToGet){
            inventory.setItem(i, items)
            i+=1
        }
    }

    fun isWon(): Boolean{
        Bukkit.broadcastMessage("Häää")
        val array= itemsToGet.toTypedArray()
        Bukkit.broadcastMessage("asa"+array.size)
        var i = 0
        while(i < array.size){
            if(array[i].type != Material.GREEN_STAINED_GLASS_PANE){
               Bukkit.broadcastMessage("Item: ${array[i].type.toString()}")
               return false
            }
            i+=1
        }
        return true
    }

    fun setItemsToGeta(list: LinkedList<ItemStack>) {
        this.itemsToGet = list
        for(item in itemsToGet){
            inventory.addItem(item)
        }
    }

    fun removeMember(player: Player): Boolean{
        return if(!member.contains(player))
            false
        else{
            member.remove(player)
            true
        }
    }

    fun addMember(player: Player): Boolean{
        return if(member.contains(player))
            false
        else{
            member.add(player)
            true
        }
    }
}