package yt.lost.main.game

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

class Scoreboard {
    private var scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    private var objective = scoreboard.registerNewObjective("Main2", "Main2", "§2§lBingo")

    private fun initializeScoreboard(player: Player) {
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.getScore("§3§olost.yt").score = 0
        objective.getScore("§a").score = 1
        objective.getScore("").score = 3
        objective.getScore("§aDein Team").score = 6
        objective.getScore("§f").score = 7


    }

    fun getBoard(player: Player):Scoreboard{
        return scoreboard
    }
}