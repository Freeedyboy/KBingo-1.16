package yt.lost.main.game

import net.md_5.bungee.api.ChatMessageType
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import net.md_5.bungee.api.chat.*

class RunningGame {

    var time: Int = 0
    var plugin: Plugin? = null
    var running: Boolean = false

    val runnable : BukkitRunnable = object : BukkitRunnable(){
        override fun run() {
            time += 1
            for(player in Bukkit.getOnlinePlayers()){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(shortInteger(time)))
            }
        }
    }

    constructor(plugin: Plugin){
        this.plugin = plugin
    }

    fun startGame(){
        runnable.runTaskTimer(this.plugin!!, 1, 20)
        running = true

        for(player in Bukkit.getOnlinePlayers()){
            player.teleport(Bukkit.getWorld("world")!!.spawnLocation)
            player.sendTitle("Bingo", "wurde gestartet", 1, 30, 1)
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

    private fun shortInteger(duration: Int): String? {
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