package yt.lost.main.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import yt.lost.main.game.RunningGame

class TeamCommands(private val runningGame: RunningGame): CommandExecutor, TabCompleter {
    override fun onCommand(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): Boolean {
        if(p1.name == "backpack"){
            if(runningGame.getPlayer(p0 as Player)?.hasTeam() == true)
                (p0 as Player).openInventory(runningGame.getPlayer(p0 as Player)?.getTeam()?.backpack!!)

        }else if(p1.name == "bingo"){
            if(runningGame.getPlayer(p0 as Player)?.hasTeam() == true)
                (p0 as Player).openInventory(runningGame.getPlayer(p0 as Player)?.getTeam()?.inventory!!)

        }else if(p1.name == "join"){
            /*if(runningGame.teams.size >= 2){*/
                if(runningGame.getPlayer(p0 as Player)?.hasTeam() == false) {
                    runningGame.getPlayer(p0 as Player)!!.setTeam(runningGame.getTeam(p3[0])!!)
                    runningGame.getTeam(p3[0])?.addMember(runningGame.getPlayer(p0 as Player)!!)
                    //runningGame.updateSB()
                    p0.sendMessage("Erfolgreich dem Team beigetreten")
                }else
                    (p0 as Player).sendMessage("Du bist bereits in einem Team. Verlasse dieses zuerst")

            /*}else
                p0.sendMessage("Es gibt zu wenig Teams")*/

        }else if(p1.name == "leaveteam"){
            if(runningGame.getPlayer(p0 as Player)?.hasTeam() == false) {
                runningGame.getTeam(p3[0])?.removeMember(runningGame.getPlayer(p0 as Player)!!)
                runningGame.getPlayer(p0 as Player)!!.setTeam(null)
                p0.sendMessage("Erfolgreich das Team verlassen")
            }
        }else if(p1.name == "team"){
            if(runningGame.getPlayer(p0 as Player)?.hasTeam() == true){
                runningGame.getPlayer(p0 as Player)?.getTeam()!!.openTeamList(runningGame.getPlayer(p0 as Player)!!)
            }else{
                (p0 as Player).sendMessage("Du bist in keinem Team drin")
            }
        }

        return true
    }

    override fun onTabComplete(p0: CommandSender, p1: Command, p2: String, p3: Array<out String>): MutableList<String>? {
        val list: ArrayList<String> = ArrayList()

        if(p3.size == 1 && p1.name == "join")
            for(team in runningGame.teams){
                list.add(team.name)
            }

        return list
    }

}