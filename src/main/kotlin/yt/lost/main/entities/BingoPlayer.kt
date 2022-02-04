package yt.lost.main.entities

import org.bukkit.entity.Player

class BingoPlayer (private var player: Player){

    //stats
    private var kills:Int = 0
    private var itemsCollected:Int = 0
    private var damageTaken: Double = 0.0
    private var damageCaused: Double = 0.0

    private var team: BingoTeam? = null


    fun onRoundEnd(){
        
    }

    fun getName(): String{
        return player.name
    }

    fun getPlayer(): Player{
        return player
    }

    fun sendMessage(text: String){
        player.sendMessage(text)
    }

    fun hasTeam(): Boolean{
        return team != null
    }

    fun onDamageCaused(damage: Double){
        this.damageCaused += damage
    }

    fun onDamageTaken(damage: Double){
        this.damageTaken += damage
    }

    fun onKill(){
        this.kills += 1
    }

    fun onItemCollected(){
        this.itemsCollected += 1
    }

    fun setTeam(team: BingoTeam?){
        this.team = team
    }

    fun getTeam(): BingoTeam?{
        return team
    }

    fun getKills():Int{
        return this.kills
    }

    fun getItemsCollected():Int{
        return this.itemsCollected
    }
}