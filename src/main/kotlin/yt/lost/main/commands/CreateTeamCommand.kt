package yt.lost.main.commands

import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import yt.lost.main.KBingo
import yt.lost.main.entities.BingoTeam

open class CreateTeamCommand(private val plugin: KBingo) : CommandExecutor{
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(!plugin.runningGame.isRunning()){
            if(plugin.addTeam(BingoTeam(p3[0], p0 as Player ))){

                p0.sendMessage("§9§lBingo §r§7| §a"+"Das Team §9"+p3[0]+"§a wurde erfolgreich erstellt")
                p0.playSound(p0.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f)

            }else{
                p0.sendMessage("§9§lBingo §r§7| §4Das Team existiert bereits")
                p0.playSound(p0.location, Sound.ENTITY_PLAYER_LEVELUP, 1f, 10f)
            }
        }else{
            p0.sendMessage("§9§lBingo §r§7| §8"+"§4Du kannst während das Spiel läuft kein Team erstellen")
        }

        return true
    }
}