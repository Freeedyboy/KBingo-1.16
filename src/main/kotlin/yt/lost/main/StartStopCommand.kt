package yt.lost.main

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

open class StartStopCommand: CommandExecutor {

    var runningGame: RunningGame? = null

    constructor(runningGame: RunningGame){
        this.runningGame = runningGame
    }

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        var player: Player = p0 as Player

        if(p1.name == "start"){
            if(runningGame!!.isRunning())
                player.sendMessage("§9§lBingo §r§7| §c"+"Das Spiel läuft bereits")
            else
                runningGame!!.startGame()
        }else if(p1.name == "stopgame"){
            if(runningGame!!.isRunning()){
               runningGame!!.stopGame()
            }else{
                player.sendMessage("§9§lBingo §r§7| §c"+"Es läuft gerade kein Spiel")
            }
        }
        return true
    }
}