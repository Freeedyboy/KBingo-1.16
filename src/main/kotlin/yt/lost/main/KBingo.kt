package yt.lost.main

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import yt.lost.main.commands.*
import yt.lost.main.commands.pregame.JumpAndRunCommand
import yt.lost.main.configuration.Settings
import yt.lost.main.entities.BingoTeam
import yt.lost.main.game.Events
import yt.lost.main.game.RunningGame
import java.io.*
import java.util.*

class KBingo : JavaPlugin(){

    var runningGame: RunningGame = RunningGame(this)
    var startStopCommand: StartStopCommand? = null
    private var teamCommands: TeamCommands? = null

    override fun onEnable() {
        startStopCommand = StartStopCommand(runningGame)
        teamCommands = TeamCommands(runningGame)
        val settings = Settings()
        val topCommand = TpaCommand(runningGame)

        this.getCommand("start")?.setExecutor(startStopCommand)
        this.getCommand("stopgame")?.setExecutor(startStopCommand)
        this.getCommand("createteam")?.setExecutor(CreateTeamCommand(this, runningGame))
        this.getCommand("backpack")?.setExecutor(teamCommands)
        this.getCommand("bingo")?.setExecutor(teamCommands)
        this.getCommand("join")?.setExecutor(teamCommands)
        this.getCommand("top")?.setExecutor(TopCommand())
        this.getCommand("tpa")?.setExecutor(topCommand)
        this.getCommand("tomate")?.setExecutor(topCommand)
        this.getCommand("leaveteam")?.setExecutor(teamCommands)
        this.getCommand("settings")?.setExecutor(SettingsCommand(settings, runningGame))
        this.getCommand("jumpandrun")?.setExecutor(JumpAndRunCommand(this, runningGame))
        this.getCommand("team")?.setExecutor(teamCommands)

        this.server.pluginManager.registerEvents(settings, this)
        this.server.pluginManager.registerEvents(Events(runningGame, settings), this)

        Bukkit.getWorld("world")!!.worldBorder.size = 20.0
        Bukkit.getWorld("world")!!.worldBorder.center = Location(Bukkit.getWorld("world"),0.0, 0.0, 0.0)
    }

    override fun onDisable() {

    }

    fun bungeeCommand(p: Player, sn: String) {
        val b = ByteArrayOutputStream()
        val d = DataOutputStream(b)
        try {
            d.writeUTF("Connect")
            d.writeUTF(sn)
            b.close()
        } catch (e: Exception) {
            e.printStackTrace()
            logger.severe(ChatColor.RED.toString() + "Error connecting to the server " + sn)
        }
        p.sendPluginMessage(this, "BungeeCord", b.toByteArray())
    }
}
