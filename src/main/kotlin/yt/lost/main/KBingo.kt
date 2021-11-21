package yt.lost.main

import org.bukkit.plugin.java.JavaPlugin
import yt.lost.main.commands.CreateTeamCommand
import yt.lost.main.commands.StartStopCommand
import yt.lost.main.entities.BingoPlayer
import yt.lost.main.entities.BingoTeam
import yt.lost.main.game.Events
import yt.lost.main.game.RunningGame
import java.util.*

class KBingo : JavaPlugin(){

    var runningGame: RunningGame = RunningGame(this)
    var startStopCommand: StartStopCommand? = null
    private val teams: LinkedList<BingoTeam> = LinkedList()
    private var players: LinkedList<BingoPlayer> = LinkedList()
    private val instance = this



    override fun onEnable() {
        startStopCommand = StartStopCommand(runningGame)

        this.server.pluginManager.registerEvents(Events(runningGame), this)

        this.getCommand("start")?.setExecutor(startStopCommand)
        this.getCommand("stopgame")?.setExecutor(startStopCommand)
        this.getCommand("createteam")?.setExecutor(CreateTeamCommand(this))

    }

    fun addTeam(team: BingoTeam): Boolean{
        return runningGame.addTeam(team)
    }

    fun removeTeam(team: BingoTeam){
        if(teams.contains(team)){
            teams.remove(team)
        }
    }
}
