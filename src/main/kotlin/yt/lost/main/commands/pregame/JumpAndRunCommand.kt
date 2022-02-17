package yt.lost.main.commands.pregame

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import yt.lost.main.KBingo
import yt.lost.main.game.RunningGame
import yt.lost.main.game.pregame.JumpAndRun
import java.util.*

class JumpAndRunCommand(private val plugin: KBingo, private val runningGame: RunningGame): CommandExecutor {

    private val jr: LinkedList<JumpAndRun> = LinkedList()

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(!runningGame.running){
            for(tmp in jr){
                if(tmp.player == p0 as Player){
                    tmp.removeAllBlocks()
                    HandlerList.unregisterAll(tmp)
                    jr.remove(tmp)
                }
            }

            val tmp = JumpAndRun((p0 as Player), Math.random() * 100 + 100)
            jr.add(tmp)
            plugin.server.pluginManager.registerEvents(tmp, plugin)
        }
        return true
    }

}