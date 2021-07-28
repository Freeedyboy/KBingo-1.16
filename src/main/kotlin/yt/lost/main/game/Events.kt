package yt.lost.main.game

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

open class Events: Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        event.joinMessage = "§9§lBingo §r§7| §8"+event.player.name+" ist Bingo gejoined"
    }
}