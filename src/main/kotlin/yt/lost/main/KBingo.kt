package yt.lost.main

import org.bukkit.plugin.java.JavaPlugin
import yt.lost.main.commands.CreateTeamCommand
import yt.lost.main.commands.StartStopCommand
import yt.lost.main.entities.BingoTeam
import yt.lost.main.game.Events
import yt.lost.main.game.RunningGame
import java.util.*

open class KBingo : JavaPlugin(){

    var runningGame: RunningGame? = null
    var startStopCommand: StartStopCommand? = null
    private val teams: LinkedList<BingoTeam> = LinkedList()

    override fun onEnable() {
        runningGame = RunningGame(this)
        startStopCommand = StartStopCommand(runningGame!!)

        this.server.pluginManager.registerEvents(Events(), this)

        this.getCommand("start")?.setExecutor(startStopCommand)
        this.getCommand("stopgame")?.setExecutor(startStopCommand)
        this.getCommand("createteam")?.setExecutor(CreateTeamCommand())

    }

    fun addTeam(team: BingoTeam){
        teams.add(team)
    }

    fun removeTeam(team: BingoTeam){
        if(teams.contains(team)){
            teams.remove(team)
        }
    }
}