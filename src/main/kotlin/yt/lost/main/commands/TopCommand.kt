package yt.lost.main.commands

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TopCommand: CommandExecutor {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        (p0 as Player).teleport(
            Location(
                p0.world,
                p0.location.x,
                topCommand(p0, p0.location.y),
                p0.location.z
            )
        )
        p0.sendMessage("§9§lBingo §r| " + "§aDu wurdest nach oben teleportiert")

        return true
    }

    fun topCommand(playerToTeleport: Player, yCoord: Double): Double {
        return if (Location(
                playerToTeleport.location.world,
                playerToTeleport.location.x,
                yCoord.toDouble(),
                playerToTeleport.location.z
            )
                .block
                .blockData
                .material
            == Material.AIR
        ) yCoord else topCommand(playerToTeleport, yCoord + 1)
    }
}