package yt.lost.main.game

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.*
import net.minecraft.server.v1_16_R3.*
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import yt.lost.main.entities.BingoPlayer
import yt.lost.main.entities.BingoTeam
import org.bukkit.scoreboard.Scoreboard
import java.util.*


class RunningGame(private val plugin: Plugin) {

    private var time: Int = 0

    //var globalScoreboard = Scoreboard()

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
        var c = 0
        var next = team

        for(tmp in teams){
            if(tmp.getItemsLeft() < team.getItemsLeft()){
                next = tmp
            }
        }
        if(next == team){
            for(tmp in teams){
                if(tmp.getItemsLeft() == team.getItemsLeft()){
                    next = tmp
                }
            }
        }

        return next
    }

    fun getPlayer(player: Player): BingoPlayer?{
        return players.get(player)
    }

    fun getSmallestTeam(): BingoTeam{
        var small = teams.first

        for(team in teams){
            if(team.getMemberAmount() < small.getMemberAmount()){
                small = team
            }
        }
        return small
    }

    fun setAllTablist(){
        for(team in teams){
            for(tmp in teams){
                team.rightScoreboard.teams.add(tmp.team)
            }
        }
    }

    fun autofill(){
        for(player in players.keys){
            if(!players.get(player)!!.hasTeam()){
                val team = getSmallestTeam()
                team.addMember(getPlayer(player)!!)
                players.get(player)!!.setTeam(team)
                player.sendMessage("Du bist durch autofill dem Team ${team.name} beigetreten")
            }
        }
    }

    fun startGame(){
        for(i in 0 until 5){
            for(player in Bukkit.getOnlinePlayers()) {
                player.sendTitle("§9§l${5-i}","§aSekunden bis Bingo startet" , 1, 18, 1)
            }
            Thread.sleep(1000)
        }

        autofill()

        runnable.runTaskTimer(this.plugin, 1, 20)
        running = true

        val items: Items = Items()
        items.mixItems()

        for(team in teams){
            team.setItemsToGet(items.items)
        }

        Bukkit.getWorld("world")!!.worldBorder.reset()

        for(player in Bukkit.getOnlinePlayers()){
            player.sendTitle("§a§lBingo", "§awurde gestartet", 1, 40, 1)
        }
    }

    fun finishGame(team: BingoTeam){
        if(running){
            runnable.cancel()
            running = false
            for(player in Bukkit.getOnlinePlayers()){
                player.sendTitle("§a§lBingo", "§awurde von §9${team.name} §agewonnen", 1, 60, 1)
                player.teleport(team.leader.getPlayer().location)
                player.playSound(team.leader.getPlayer().location, Sound.ENTITY_PLAYER_LEVELUP, 5f, 5f)
                getPlayer(player)!!.onRoundEnd()
            }
            for(teamm in teams){
                teamm.reloadSB(plugin, firstPlace!!, getNext(teamm), teamm)
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
            setAllTablist()

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

    /*fun updateSB(){
        for (player in Bukkit.getOnlinePlayers()) {
            try {
                if (!globalScoreboard.getTeam(getPlayer(player)?.getTeam()?.name)?.playerNameSet?.contains(player.name)!!)
                    globalScoreboard.getTeam(getPlayer(player)?.getTeam()?.name).playerNameSet.add(player.name)

                println("\n")
                println("Spieler: "+getPlayer(player))
                println("Team: "+getPlayer(player)?.getTeam())
                println("Gleicher Name: "+getPlayer(player)?.getTeam()?.name)
                println("PREFIX: "+globalScoreboard.getTeam(getPlayer(player)?.getTeam()?.name).prefix.text)

                if(getPlayer(player)?.hasTeam()!!){
                    println("inif")
                    sendPacket(PacketPlayOutScoreboardTeam(globalScoreboard.getTeam(getPlayer(player)?.getTeam()?.name), 1))
                    println("nach erstem")
                    sendPacket(PacketPlayOutScoreboardTeam(globalScoreboard.getTeam(getPlayer(player)?.getTeam()?.name), 0))
                    println("hmmmmm")
                }
            }catch (e: NullPointerException){
                e.printStackTrace()
            }
        }
    }*/

    private fun sendPacket(packet: Packet<*>) {
        for (onlinePlayer in Bukkit.getOnlinePlayers()) {
            val p = onlinePlayer as CraftPlayer
            p.handle.playerConnection.sendPacket(packet)
        }
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