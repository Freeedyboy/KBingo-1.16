package yt.lost.main

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Scoreboard

class Scoreboard {
    private var scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard
    private var objective = scoreboard.registerNewObjective("Main2", "Main2", "§2§lBingo")

    private fun initializeScoreboard() {
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.getScore("§3§olost.yt").score = 0
        objective.getScore("§a").score = 1
        objective.getScore("§9Team 2: ").score = 3
        objective.getScore("§cTeam 1:").score = 6
        objective.getScore("§f").score = 7


    }

    fun getBoard():Scoreboard{
        return scoreboard
    }
}