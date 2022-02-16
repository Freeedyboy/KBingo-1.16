package yt.lost.main

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import yt.lost.main.commands.*
import yt.lost.main.configuration.Settings
import yt.lost.main.entities.BingoTeam
import yt.lost.main.game.Events
import yt.lost.main.game.RunningGame
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

        this.server.pluginManager.registerEvents(settings, this)
        this.server.pluginManager.registerEvents(Events(runningGame), this)

        Bukkit.getWorld("world")!!.worldBorder.size = 20.0
    }

    fun addTeam(team: BingoTeam): Boolean{
        return runningGame.addTeam(team)
    }
}
