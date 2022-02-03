package yt.lost.main.commands

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import yt.lost.main.game.RunningGame
import java.util.*

class TpaCommand(var runningGame: RunningGame) : CommandExecutor{
    private val list: LinkedList<Triple<Player, Player, Boolean>> = LinkedList();

    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(runningGame.isRunning()) {
            if (p1.name == "tpa") {
                if (p3.size == 1) {
                    try {
                        val p = Bukkit.getPlayer(p3[0])
                        return if (runningGame.getPlayer(p!!)!!.getTeam() == runningGame.getPlayer(p0 as Player)!!.getTeam()) {
                            p.sendMessage("Dein Teammate ${p0.name} will sich zu dir teleportieren. Erlauben?")
                            val button = TextComponent("Ja")
                            button.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tomate ${p.name} ")

                            p.spigot().sendMessage(button)
                            list.add(Triple(p0, p, false))

                            true
                        }else{
                            p0.sendMessage("Ihr seid nicht im selben Team")
                            false
                        }
                    } catch (e: NullPointerException) {
                        p0.sendMessage("Der Spieler existiert nicht. Schau nach ob du die Groß- und Kleinschreibung beachtet hast")
                    }
                }
            } else if (p1.name == "tomate") {
                for(element in list){
                    if(element.second == p0){
                        (p0 as Player).teleport(element.first)
                        list.remove(element)
                    }
                }
            }
        }else {
            p0.sendMessage("Das Spiel läuft noch nicht")
            return false
        }
        return true
    }
}