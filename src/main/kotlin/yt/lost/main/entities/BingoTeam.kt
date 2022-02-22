package yt.lost.main.entities

import CreateItemCommand.createGuiItem
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Team
import java.util.*


class BingoTeam (var name: String, var leader: BingoPlayer) {

    private val member: LinkedList<BingoPlayer> = LinkedList()

    private var itemsToGet: LinkedList<ItemStack> = LinkedList()
    var backpack: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Backpack $name")
    var inventory: Inventory = Bukkit.createInventory(null, 9, "Items $name:")
    val teamList: Inventory = Bukkit.createInventory(null, InventoryType.BARREL, "Team $name")

    private var rightScoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    private var rightObjective = rightScoreboard.registerNewObjective("Main2", "Main2", "§2§lBingo")
    var team: Team = rightScoreboard.registerNewTeam(name)


    init{
        member.add(leader)
        leader.getPlayer().scoreboard = rightScoreboard

        rightObjective.displaySlot = DisplaySlot.SIDEBAR
        rightObjective.getScore("§f").score = 10
        rightObjective.getScore("§91. Team: ").score = 9

        rightObjective.getScore("§1").score = 7
        rightObjective.getScore("§9Nächstes Team: ").score = 6

        rightObjective.getScore("§3").score = 4
        rightObjective.getScore("§9Dein Team: ").score = 3

        rightObjective.getScore("§a").score = 1
        rightObjective.getScore("§3§olost.yt").score = 0

        team.prefix = "§7[§f$name§7]§f"

        val firstPlace = rightScoreboard.registerNewTeam("first")
        firstPlace.addEntry(ChatColor.BLUE.toString() + "" + ChatColor.RED)
        rightObjective.getScore(ChatColor.BLUE.toString() + "" + ChatColor.RED).score = 8

        val nextTeam = rightScoreboard.registerNewTeam("next")
        nextTeam.addEntry(ChatColor.BLACK.toString() + "" + ChatColor.RED)
        rightObjective.getScore(ChatColor.BLACK.toString() + "" + ChatColor.RED).score = 5

        val yourTeam = rightScoreboard.registerNewTeam("your")
        yourTeam.addEntry(ChatColor.AQUA.toString() + "" + ChatColor.RED)
        rightObjective.getScore(ChatColor.AQUA.toString() + "" + ChatColor.RED).score = 2
    }

    fun reloadSB(plugin: Plugin, first: BingoTeam, next: BingoTeam, your: BingoTeam){
        object : BukkitRunnable(){
            override fun run() {
                rightScoreboard.getTeam("first")!!.prefix = "§a §7${first.name} §8(${first.getItemsLeft()})"
            }
        }.runTaskLater(plugin, 1)


        object : BukkitRunnable(){
            override fun run() {
                rightScoreboard.getTeam("next")!!.prefix = "§a §7${next.name} §8(${next.getItemsLeft()})"
            }
        }.runTaskLater(plugin, 1)

        object : BukkitRunnable(){
            override fun run() {
                rightScoreboard.getTeam("your")!!.prefix = "§a §7${your.name} §8(${your.getItemsLeft()})"
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

    fun openTeamList(player: BingoPlayer){
        var c = 0
        teamList.setItem(0, getPlayerSkull(leader.getPlayer(), "§7Status: §6Leader",
                                                                     "§7Items gesammelt: §9${leader.getItemsCollected()}",
                                                                     "§7Schaden genommen: §c${leader.getDamageTaken()}",
                                                                     "§7Schaden ausgeteilt: §a${leader.getDamageCaused()}",
                                                                     "§7Kills: §4${leader.getKills()}"))

        for(p in member){
            if(p != leader){
                teamList.setItem(9+c, getPlayerSkull(p.getPlayer(), "§7Status: §fMitglied",
                                                                          "§7Items gesammelt: §9${leader.getItemsCollected()}",
                                                                          "§7Schaden genommen: §4${leader.getDamageTaken()}",
                                                                          "§7Schaden ausgeteilt: §a${leader.getDamageCaused()}",
                                                                          "§7Kills: §4${leader.getKills()}"))
            }
            c+=1
        }
        player.getPlayer().openInventory(teamList)
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

    fun getMemberAmount(): Int{
        return member.size
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
            team.addPlayer(player.getPlayer())
            player.getPlayer().scoreboard = rightScoreboard
            for(m in member){
                m.sendMessage("Der Spieler ${player.getName()} ist eurem Team beigetreten")
            }
            true
        }
    }

    fun message(message: String){
        for(player in member){
            player.sendMessage(message)
        }
    }

    private fun getPlayerSkull(player: Player, vararg lore: String): ItemStack? {
        val skull = ItemStack(
            Material.LEGACY_SKULL_ITEM, 1,
            SkullType.PLAYER.ordinal.toShort()
        )
        val meta = skull.itemMeta as SkullMeta?
        meta!!.owner = player.name
        meta.setDisplayName(ChatColor.GOLD.toString() + "§l" + player.name)
        meta.lore = listOf(*lore)
        skull.itemMeta = meta
        return skull
    }
}