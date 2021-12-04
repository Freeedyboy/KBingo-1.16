package yt.lost.main.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import yt.lost.main.configuration.Settings
import yt.lost.main.game.RunningGame

class SettingsCommand(val settings: Settings, val game: RunningGame): CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(!game.running){
            (p0 as Player).openInventory(settings.inventory)
        }

        return true
    }
}