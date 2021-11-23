package yt.lost.main.entities

import org.bukkit.entity.Player

class BingoPlayer (private var player: Player){

    private var kills:Int = 0
    private var itemsCollected:Int = 0
    private var team: BingoTeam? = null

    fun onKill(){
        this.kills += 1
    }

    fun hasTeam(): Boolean{
        return team != null
    }

    fun onItemCollected(){
        this.itemsCollected += 1
    }

    fun setTeam(team: BingoTeam){
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