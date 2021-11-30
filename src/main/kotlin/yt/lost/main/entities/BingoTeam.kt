package yt.lost.main.entities

import CreateItemCommand.createGuiItem
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import java.util.*


class BingoTeam (var name: String, var leader: BingoPlayer) {

    private var itemsToGet: LinkedList<ItemStack> = LinkedList()
    private val member: LinkedList<BingoPlayer> = LinkedList()
    var backpack: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Backpack $name")
    var inventory: Inventory = Bukkit.createInventory(null, 9, "Items $name:")
    private var scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    private var objective = scoreboard.registerNewObjective("Main2", "Main2", "§2§lBingo")

    init{
        member.add(leader)
        leader.getPlayer().scoreboard = scoreboard

        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.getScore("§f").score = 10
        objective.getScore("1. Team: ").score = 9

        objective.getScore("§1").score = 7
        objective.getScore("Nächstes Team: ").score = 6

        objective.getScore("§3").score = 4
        objective.getScore("Dein Team: ").score = 3

        objective.getScore("§a").score = 1
        objective.getScore("§3§olost.yt").score = 0
        /*
        * Bingo
        *10
        *9 1.Team
        *8 Teamname
        *7
        *6 Nächstes Team:
        *5 Teamname
        *4
        *3 Dein Team:
        *2 Teamname
        *1
        *0 lost.yt
        */
        val firstPlace = scoreboard.registerNewTeam("first")
        firstPlace.addEntry(ChatColor.BLUE.toString() + "" + ChatColor.RED)
        objective.getScore(ChatColor.BLUE.toString() + "" + ChatColor.RED).score = 8


        val nextTeam = scoreboard.registerNewTeam("next")
        nextTeam.addEntry(ChatColor.BLACK.toString() + "" + ChatColor.RED)
        objective.getScore(ChatColor.BLACK.toString() + "" + ChatColor.RED).score = 5


        val yourTeam = scoreboard.registerNewTeam("your")
        yourTeam.addEntry(ChatColor.AQUA.toString() + "" + ChatColor.RED)
        objective.getScore(ChatColor.AQUA.toString() + "" + ChatColor.RED).score = 2
    }

    fun reloadSB(plugin: Plugin, first: BingoTeam, next: BingoTeam, your: BingoTeam){
        object : BukkitRunnable(){
            override fun run() {
                scoreboard.getTeam("first")!!.prefix = "§a ${first.name} (${first.getItemsLeft()})"
            }
        }.runTaskLater(plugin, 1)


        object : BukkitRunnable(){
            override fun run() {
                scoreboard.getTeam("next")!!.prefix = "§a ${next.name} (${next.getItemsLeft()})"
            }
        }.runTaskLater(plugin, 1)

        object : BukkitRunnable(){
            override fun run() {
                scoreboard.getTeam("your")!!.prefix = "§a ${your.name} (${your.getItemsLeft()})"
            }
        }.runTaskLater(plugin, 1)
    }

    fun onItemCollect(player: BingoPlayer, item: ItemStack) : Boolean{
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
        val array= itemsToGet.toTypedArray()
        var i = 0
        while(i < array.size){
            if(array[i].type != Material.GREEN_STAINED_GLASS_PANE){
               return false
            }
            i+=1
        }
        return true
    }

    fun getItemsLeft(): Int{
        var c = 0

        for(item in itemsToGet){
            if(item.type != Material.GREEN_STAINED_GLASS_PANE){
                c+=1
            }
        }
        return c
    }

    fun setItemsToGet(list: LinkedList<ItemStack>) {
        this.itemsToGet = list
        for(item in itemsToGet){
            inventory.addItem(item)
        }
    }

    fun removeMember(player: BingoPlayer): Boolean{
        return if(!member.contains(player))
            false
        else{
            member.remove(player)
            true
        }
    }

    fun addMember(player: BingoPlayer): Boolean{
        return if(member.contains(player))
            false
        else{
            member.add(player)
            player.getPlayer().scoreboard = scoreboard
            for(m in member){
                m.sendMessage("Der Spieler ${player.getName()} ist eurem Team beigetreten")
            }
            true
        }
    }
}