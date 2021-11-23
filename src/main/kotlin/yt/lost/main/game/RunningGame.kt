package yt.lost.main.game

import net.md_5.bungee.api.ChatMessageType
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import net.md_5.bungee.api.chat.*
import org.bukkit.Sound
import org.bukkit.entity.Player
import yt.lost.main.Items
import yt.lost.main.entities.BingoPlayer
import yt.lost.main.entities.BingoTeam
import java.util.*
import kotlin.collections.HashMap

class RunningGame(private val plugin: Plugin) {

    private var time: Int = 0

    var running: Boolean = false
    val players: HashMap<Player, BingoPlayer> = HashMap()
    val teams: LinkedList<BingoTeam> = LinkedList()

    val runnable : BukkitRunnable = object : BukkitRunnable(){
        override fun run() {
            time += 1
            for(player in Bukkit.getOnlinePlayers()){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(shortInteger(time)))
            }
        }
    }

    fun addPlayer(player: Player): BingoPlayer{
        val p = BingoPlayer(player)
        players.put(player, p)
        return p
    }

    fun getPlayer(player: Player): BingoPlayer?{
        return players.get(player)
    }

    fun startGame(){
        runnable.runTaskTimer(this.plugin, 1, 20)
        running = true

        val items: Items = Items()
        items.mixItems()

        for(team in teams){
            team.setItemsToGeta(items.items)
            Bukkit.broadcastMessage(team.name)
        }

        for(item in items.items){
            Bukkit.broadcastMessage(item.toString())
        }

        for(player in Bukkit.getOnlinePlayers()){
            player.teleport(Bukkit.getWorld("world")!!.spawnLocation)
            player.sendTitle("Bingo", "wurde gestartet", 1, 30, 1)
        }
    }

    fun finishGame(team: BingoTeam){
        if(running){
            runnable.cancel()
            running = false
            for(player in Bukkit.getOnlinePlayers()){
                player.sendTitle("Bingo", "wurde von ${team.name} beendet", 1, 40, 1)
                player.teleport(team.leader.location)
                player.playSound(team.leader.location, Sound.ENTITY_PLAYER_LEVELUP, 5f, 5f)
            }
        }
    }

    fun addTeam(team: BingoTeam): Boolean{
        return if(!teams.contains(team)){
            teams.add(team)
            true
        }else{
            false
        }
    }

    fun removeTeam(team: BingoTeam){
        if(teams.contains(team)){
            teams.remove(team)
        }
    }

    fun stopGame(){
        if(running){
            runnable.cancel()
            running = false
            for(player in Bukkit.getOnlinePlayers()){
                player.sendTitle("Bingo", "wurde gestoppt", 1, 30, 1)
            }
        }
    }

    fun isRunning(): Boolean{
        return running
    }

    private fun shortInteger(duration: Int): String {
        var duration = duration
        var string = ""
        var hours = 0
        var minutes = 0
        var seconds = 0
        if (duration / 60 / 60 / 24 >= 1) {
            duration -= duration / 60 / 60 / 24 * 60 * 60 * 24
        }
        if (duration / 60 / 60 >= 1) {
            hours = duration / 60 / 60
            duration -= duration / 60 / 60 * 60 * 60
        }
        if (duration / 60 >= 1) {
            minutes = duration / 60
            duration -= duration / 60 * 60
        }
        if (duration >= 1) seconds = duration
        string = if (hours <= 9) {
            string + "0" + hours + ":"
        } else {
            "$string$hours:"
        }
        string = if (minutes <= 9) {
            string + "0" + minutes + ":"
        } else {
            "$string$minutes:"
        }
        string = if (seconds <= 9) {
            string + "0" + seconds
        } else {
            string + seconds
        }
        return string
    }
}