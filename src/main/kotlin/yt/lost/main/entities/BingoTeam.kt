package yt.lost.main.entities

import com.sun.org.apache.xpath.internal.operations.Bool
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*

class BingoTeam (private var name: String, private var amount: Int, private var leader: Player) {

    private var itemsToGet: LinkedList<ItemStack> = LinkedList()
    private val member: LinkedList<Player> = LinkedList()
    private var backpack: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Backpack $name")

    fun onItemCollect(item: ItemStack) {
        if(itemsToGet.contains(item)){

        }
    }

    fun setItemsToGet(list: LinkedList<ItemStack>) {
        this.itemsToGet = list
    }

    fun setLeader(player: Player){
        this.leader = player
    }

    fun changeName(player: Player, name: String): Boolean{
        return if(player == leader){
            this.name = name
            true
        }else{
            false
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

    fun getItems() : Inventory{
        val inventory: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Items: $name")

        for(item in itemsToGet){
            inventory.addItem(item)
        }

        return inventory
    }
}