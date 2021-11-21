package yt.lost.main.game

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
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
        if(game.running){
            Bukkit.broadcastMessage("HIHIHI")
            if(game.getPlayer(event.player)?.getTeam()?.onItemCollect(event.item.itemStack)!!){
                Bukkit.broadcastMessage(event.player.name+" hat ein Item aufgesammelt")
            }
        }else
            Bukkit.broadcastMessage("HURENSOHN")
    }
}