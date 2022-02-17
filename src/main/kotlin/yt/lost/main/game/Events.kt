package yt.lost.main.game

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import yt.lost.main.configuration.Settings

open class Events(private val game: RunningGame, private val settings: Settings): Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent){
        event.joinMessage = "§9§lBingo §r§7| §8"+event.player.name+" ist Bingo gejoined"
        if(game.getPlayer(event.player) == null)
            game.addPlayer(event.player)
        else{
            if(game.getPlayer(event.player)!!.hasTeam()){
                event.player.sendMessage("Du bist in dem Team ${game.getPlayer(event.player)!!.getTeam()!!.team}")
            }
        }
        event.player.teleport(Location(Bukkit.getWorld("world"),0.0, topCommand(event.player, event.player.location.y)+2, 0.0))
    }

    @EventHandler
    fun onItemCollect(event: PlayerPickupItemEvent){
        if(game.running){
            if(game.getPlayer(event.player)?.getTeam()?.onItemCollect(game.getPlayer(event.player)!!,event.item.itemStack) == true){
                Bukkit.broadcastMessage("§9§lBingo §r§7| §8"+"§6${event.player.name} (${game.getPlayer(event.player)!!.getTeam()!!.name})§a hat §7${event.item.itemStack.type.name}§a aufgesammelt")
            }

            if(game.getPlayer(event.player)!!.getTeam()!!.isWon()){
                game.finishGame(game.getPlayer(event.player)?.getTeam()!!)
            }
        }
    }

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent){
        if(!game.running){
           event.isCancelled = true
        }else{
            event.isCancelled = false
            if(event.cause.name.contains("ENTITY") || event.cause.name.contains("ARROW"))
                return

            if(event.entity is Player) {
                game.getPlayer(event.entity as Player)!!.getTeam()!!.message("§9§lBingo §r§7| §7" + "Der Spieler §9${(event.entity as Player).name} §7hat gerade §4${event.finalDamage/2}♥ §7von §c${event.cause.name}§7 bekommen")
            }
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent){
        if(!game.running){
            event.isCancelled = true
        }else{
            event.isCancelled = false
            if(event.entity is Player) {
                if(!settings.ffOn && game.getPlayer(event.damager as Player)!!.getTeam() == game.getPlayer(event.entity as Player)!!.getTeam()){
                    event.isCancelled
                }
                game.getPlayer(event.entity as Player)!!.getTeam()!!.message("§9§lBingo §r§7| §7" + "Der Spieler §9${(event.entity as Player).name} §7hat gerade §4${event.finalDamage/2}♥ §7von §c${event.cause.name}§7 bekommen")
            }else{
                game.getPlayer(event.entity as Player)!!.getTeam()!!.message("§9§lBingo §r§7| §7" + "Der Spieler §9${(event.entity as Player).name} §7hat gerade §4${event.finalDamage/2}♥ §7von §c${event.cause.name}§7 bekommen")
            }
        }
    }

    @EventHandler
    fun inventoryChange(event: CraftItemEvent){
        if(game.running){
            if(game.getPlayer(event.whoClicked as Player)?.getTeam()?.onItemCollect(game.getPlayer(event.whoClicked as Player)!!,event.currentItem!!) == true){
                Bukkit.broadcastMessage("§9§lBingo §r§7| §8"+"§6${event.whoClicked.name} (${game.getPlayer(event.whoClicked as Player)!!.getTeam()!!.name})§a hat §7${event.currentItem!!.type.name}§a aufgesammelt")
            }

            if(game.getPlayer(event.whoClicked as Player)!!.getTeam()!!.isWon()){
                game.finishGame(game.getPlayer(event.whoClicked as Player)?.getTeam()!!)
            }
        }
    }

    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent){
        for(team in game.teams){
            if(event.inventory == team.inventory){
                event.isCancelled = true
            }
        }
    }

    @EventHandler
    fun onDeath(event: PlayerDeathEvent){
        if(game.running){
            event.deathMessage = "§9§lBingo §r§7| §8" +event.deathMessage
        }
    }

    @EventHandler
    fun onBlockPlaced(event: BlockPlaceEvent){
        if(!game.running){
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent){
        if(!game.running){
            event.isCancelled = true
        }
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