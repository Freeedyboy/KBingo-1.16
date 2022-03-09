package yt.lost.main.game.pregame

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import java.util.*
import kotlin.math.roundToInt

class JumpAndRun(val player: Player, private val baseHeight: Double): Listener{

    private val baseLocation =  Location(Bukkit.getWorld("world"), Math.random()*18-9, baseHeight, Math.random()*18-9)
    private val blocks: LinkedList<Location> = LinkedList()
    private var currentLocation = baseLocation
    private var nextLocation = getNextLocation()
    private var lastLocation = baseLocation

    private var jumps: Int = 0

    init {
        baseLocation.block.type = Material.LIME_CONCRETE
        player.teleport(baseLocation.add(0.0, 1.0, 0.0))
        blocks.add(baseLocation.add(0.0, -1.0, 0.0))
        nextLocation = getNextLocation()
        setNextLocationBlock()
    }

    fun setNextLocationBlock(){
        nextLocation.block.type = Material.GREEN_CONCRETE
        blocks.add(nextLocation)
    }

    fun getNextLocation(): Location{
        val location = Location(this.currentLocation.world, this.currentLocation.x.roundToInt().toDouble(), this.currentLocation.y.roundToInt().toDouble(), this.currentLocation.z.roundToInt().toDouble())
        if(Math.random() > 0.5) {
            location.y = location.y + 0.5
        }

        if(location.x > 0){
            location.x +=0.5
        }else
            location.x -=0.5

        if(location.z > 0){
            location.z +=0.5
        }else
            location.z -=0.5


        var val1 = (Math.random()*3-1.5).roundToInt()

        if(val1 > 0){
            val1 +=2
        }else
            val1 -=2


        var val2 = (Math.random()*3-1.5).roundToInt()

        if(val2 > 0)
            val2+=2
        else
            val2-=2

        location.x = location.x + val1
        location.z = location.z + val2

        if(location.x > 9 || location.x < -9 || location.z > 9 || location.z < -9)
            return getNextLocation()

        /*player.sendMessage("  ")
        player.sendMessage("increment x $val1")
        player.sendMessage("increment z $val2")
        player.sendMessage("x ${location.x}")
        player.sendMessage("y ${location.y}")
        player.sendMessage("z ${location.z}")
        player.sendMessage("Hm ${baseLocation.block.type}")*/

        if(val1 == 4)
            val1 = 3
        else if (val2 == 4)
            val2 = 3

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
            if((event.to!!).distance(nextLocation) <= 1.1 && nextLocation.y < event.to!!.y){
                val tmp = nextLocation
                this.lastLocation.block.type = Material.AIR

                this.currentLocation = tmp
                this.lastLocation = currentLocation
                this.nextLocation = getNextLocation()
                this.baseLocation.block.type = Material.AIR
                this.currentLocation.block.type = Material.LIME_CONCRETE
                setNextLocationBlock()
                this.player.playSound(player.location, Sound.ENTITY_PLAYER_LEVELUP, 5f, 5f)
                this.jumps += 1
            }else if(event.to!!.y < currentLocation.y-10){
                removeAllBlocks()
                //player.sendMessage("cl ${currentLocation.y}")
                player.playSound(player.location, Sound.ENTITY_ITEM_BREAK, 1f, 1f)
                player.sendMessage("Vorbei!\n" +
                                   "Du hast $jumps Jumps geschafft!\n" +
                                   "Um noch einmal zu spielen mach noch einmal /jumpandrun")

                HandlerList.unregisterAll(this)
            }
        }
    }
}