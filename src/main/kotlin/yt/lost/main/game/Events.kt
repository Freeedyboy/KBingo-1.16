package yt.lost.main.game

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent

open class Events(private val game: RunningGame): Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        event.joinMessage = "§9§lBingo §r§7| §8"+event.player.name+" ist Bingo gejoined"
        game.addPlayer(event.player)
        event.player.teleport(event.player.world.spawnLocation)
    }

    @EventHandler
    fun onItemCollect(event: PlayerPickupItemEvent){
        if(game.running){
            if(game.getPlayer(event.player)?.getTeam()?.onItemCollect(game.getPlayer(event.player)!!,event.item.itemStack) == true){
                Bukkit.broadcastMessage("§9§lBingo §r§7| §8"+"§6${event.player.name} (${game.getPlayer(event.player)!!.getTeam()!!.name})§a hat §7${event.item.itemStack.type.name}§a aufgesammelt")
            }

            if(game.getPlayer(event.player)!!.getTeam()!!.isWon()){
                game.finishGame(game.getPlayer(event.player)?.getTeam()!!)
            }
        }
    }

    @EventHandler
    fun inventoryChange(event: CraftItemEvent){
        if(game.running){
            if(game.getPlayer(event.whoClicked as Player)?.getTeam()?.onItemCollect(game.getPlayer(event.whoClicked as Player)!!,event.currentItem!!) == true){
                Bukkit.broadcastMessage("§9§lBingo §r§7| §8"+"§6${event.whoClicked.name} (${game.getPlayer(event.whoClicked as Player)!!.getTeam()!!.name})§a hat §7${event.currentItem!!.type.name}§a aufgesammelt")
            }

            if(game.getPlayer(event.whoClicked as Player)!!.getTeam()!!.isWon()){
                game.finishGame(game.getPlayer(event.whoClicked as Player)?.getTeam()!!)
            }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        for(team in game.teams){
            if(event.inventory == team.inventory){
                event.isCancelled = true
            }
        }
    }
}