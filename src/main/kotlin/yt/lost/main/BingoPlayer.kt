package yt.lost.main

import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

class BingoPlayer {

    private var kills:Int = 0
    private var itemsCollected:Int = 0
    private var team: Team? = null
    private var player: Player? = null

    constructor(player: Player){
        this.player = player
    }

    fun onKill(){
        this.kills += 1
    }

    fun onItemCollected(){
        this.itemsCollected += 1
    }

    fun setTeam(team: Team){
        this.team = team
    }

    fun getKills():Int{
        return this.kills
    }

    fun getItemsCollected():Int{
        return this.itemsCollected
    }


}