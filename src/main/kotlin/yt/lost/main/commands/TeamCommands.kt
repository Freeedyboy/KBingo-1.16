package yt.lost.main.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import yt.lost.main.game.RunningGame

class TeamCommands(private val runningGame: RunningGame): CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(p1.name == "backpack"){
            if(runningGame.getPlayer(p0 as Player)?.hasTeam() == true) {
                (p0 as Player).openInventory(runningGame.getPlayer(p0 as Player)?.getTeam()?.backpack!!)
            }
        }else if(p1.name == "bingo"){
            if(runningGame.getPlayer(p0 as Player)?.hasTeam() == true) {
                (p0 as Player).openInventory(runningGame.getPlayer(p0 as Player)?.getTeam()?.inventory!!)
                for(item in runningGame.getPlayer(p0 as Player)?.getTeam()?.itemsToGet!!){
                    p0.sendMessage(item.type.name)
                }
            }
        }

        return true
    }

}