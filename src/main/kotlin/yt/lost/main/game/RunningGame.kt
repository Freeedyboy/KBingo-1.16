package yt.lost.main.game

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.*
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import yt.lost.main.entities.BingoPlayer
import yt.lost.main.entities.BingoTeam
import java.text.Collator
import java.util.*


class RunningGame(private val plugin: Plugin) {

    private var time: Int = 0

    var running: Boolean = false
    val players: HashMap<Player, BingoPlayer> = HashMap()
    var teams: LinkedList<BingoTeam> = LinkedList()
    var firstPlace: BingoTeam? = null
    var tmp = 1000

    val runnable : BukkitRunnable = object : BukkitRunnable(){
        override fun run() {
            time += 1
            for(player in Bukkit.getOnlinePlayers()){
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(shortInteger(time)))
            }
            setFirst()
            for(team in teams){
                team.reloadSB(plugin, firstPlace!!, getNext(team), team)
            }
        }
    }

    fun addPlayer(player: Player): BingoPlayer{
        val p = BingoPlayer(player)
        players.put(player, p)
        return p
    }

    fun setFirst(){
        for(team in teams){
            if(team.getItemsLeft() < tmp){
                tmp = team.getItemsLeft()
                firstPlace = team
            }
        }
    }

    fun getNext(team: BingoTeam): BingoTeam{
        sortTeams()
        var c = 0
        var next = team

        for(tmp in teams){
            if(tmp == team && c != 0){
                next = teams[c-1]
            }
            c+=1
        }

        return next
    }

    fun sortTeams(){
        var list: LinkedList<BingoTeam > = LinkedList()

        for(i in 0 until teams.size){
            var tmp = teams.first
            for (team in teams){
                if(team.getItemsLeft() < tmp.getItemsLeft()){
                    tmp = team
                }
            }
            list.add(tmp)
            teams.remove(tmp)
        }
        teams = list
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
            team.setItemsToGet(items.items)
        }

        Bukkit.getWorld("world")!!.worldBorder.reset()

        for(player in Bukkit.getOnlinePlayers()){
            player.sendTitle("Bingo", "wurde gestartet", 1, 30, 1)
        }
    }

    fun finishGame(team: BingoTeam){
        if(running){
            runnable.cancel()
            running = false
            for(player in Bukkit.getOnlinePlayers()){
                player.sendTitle("Bingo", "wurde von ${team.name} beendet", 1, 40, 1)
                player.teleport(team.leader.getPlayer().location)
                player.playSound(team.leader.getPlayer().location, Sound.ENTITY_PLAYER_LEVELUP, 5f, 5f)
            }
        }
    }

    fun getTeam(name: String): BingoTeam?{
        for(team in teams){
            if(name == team.name){
                return team
            }
        }
        return null
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