package yt.lost.main

import org.bukkit.plugin.java.JavaPlugin

open class KBingo : JavaPlugin(){

    override fun onEnable() {
        this.server.pluginManager.registerEvents(Events(), this)
    }
}