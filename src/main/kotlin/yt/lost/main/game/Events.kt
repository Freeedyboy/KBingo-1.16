package yt.lost.main.game

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent

open class Events(private val game: RunningGame): Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        event.joinMessage = "§9§lBingo §r§7| §8"+event.player.name+" ist Bingo gejoined"
        game.addPlayer(event.player)
    }

    @EventHandler
    fun onItemCollect(event: PlayerPickupItemEvent){
        Bukkit.broadcastMessage(event.item.itemStack.toString())
        if(game.running){
            if(game.getPlayer(event.player)?.getTeam()?.onItemCollect(event.player,event.item.itemStack) == true){
                Bukkit.broadcastMessage("§9${event.player.name} hat §8${event.item.itemStack.type.name} aufgesammelt")
            }
            if(game.getPlayer(event.player)?.getTeam()?.isWon() == true){
                game.finishGame(game.getPlayer(event.player)?.getTeam()!!)
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