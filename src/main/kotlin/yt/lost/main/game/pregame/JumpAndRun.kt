package yt.lost.main.game.pregame

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
import kotlin.math.roundToInt

class JumpAndRun(val player: Player, private val baseHeight: Double): Listener{

    private val baseLocation =  Location(Bukkit.getWorld("world"), Math.random()*18-9, baseHeight, Math.random()*18-9)
    private val blocks: LinkedList<Location> = LinkedList()
    private var currentLocation = baseLocation
    private var nextLocation = getNextLocation()

    init {
        baseLocation.block.type = Material.GREEN_CONCRETE
        player.teleport(baseLocation.add(0.0, 1.0, 0.0))
        blocks.add(baseLocation)
        nextLocation = getNextLocation()
        setNextLocationBlock()
    }

    fun setNextLocationBlock(){
        nextLocation.block.type = Material.GREEN_CONCRETE
        blocks.add(nextLocation)
    }

    fun getNextLocation(): Location{
        val location = currentLocation
        if(Math.random() > 0.5) {
            location.y = currentLocation.y
        }else {
            location.y = currentLocation.y - 1
        }
        var val1 = (Math.random()*4-2).roundToInt()
        var val2 = (Math.random()*4-2).roundToInt()

        if(val1 > 0){
            val1 +=1
        }else
            val1 -=1

        if(val2 > 0)
            val2+=1
        else
            val2-=1

        location.x = location.x + val1
        location.z = location.z + val2
        //location.add((Math.random()*6-3), 0.0, (Math.random()*6-3))
        player.sendMessage("x $val1")
        player.sendMessage("z $val2")

        return location
    }

    fun removeAllBlocks(){
        for(block in blocks){
            block.block.type = Material.AIR
        }
    }

    //biggest sin i will ever do in my entire life
    @EventHandler
    fun onPlayerMovement(event: PlayerMoveEvent){
        if(event.player == player){
            if(event.to!!.distance(nextLocation) < 1.0){
                currentLocation.block.type = Material.AIR
                currentLocation = nextLocation
                nextLocation = getNextLocation()
                player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 5f, 5f)
                setNextLocationBlock()
            }
        }
    }
}