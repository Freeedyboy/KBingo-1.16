package yt.lost.main.game

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class Items {
    var items: LinkedList<ItemStack> = LinkedList()

    fun mixItems(){
        var i = 4
        while( i > 0){
            val item = ItemStack(Material.values()[Math.round(Math.random() * Material.values().size).toInt() - 1])
            val itemname = item.type.name
            if (isBanned(itemname) || this.items.contains(item)) {
                println("triggerttriggerttriggerttriggert")
            } else {
                items.add(item)
                i-=1
            }
        }
    }

    private fun isBanned(name: String): Boolean{
        try {
            Scanner(Files.newInputStream(Paths.get("plugins/library/PatchedItems.txt"))).use { `in` ->
                var i = 0
                while (`in`.hasNextLine()) {
                    if (name.contains(`in`.nextLine())) return true
                    i++
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }
}