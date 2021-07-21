package yt.lost.main

import org.bukkit.plugin.java.JavaPlugin
import yt.lost.main.configuration.Config

open class KBingo : JavaPlugin(){

    var runningGame: RunningGame? = null
    var startStopCommand: StartStopCommand? = null

    override fun onEnable() {
        runningGame = yt.lost.main.RunningGame(this)
        startStopCommand = StartStopCommand(runningGame!!)

        this.server.pluginManager.registerEvents(Events(), this)
        Config("texts.yml", this.dataFolder)
        this.getCommand("start")?.setExecutor(startStopCommand)
        this.getCommand("stopgame")?.setExecutor(startStopCommand)

    }
}