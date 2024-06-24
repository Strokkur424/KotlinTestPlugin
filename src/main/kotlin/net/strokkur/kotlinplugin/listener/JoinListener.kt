package net.strokkur.kotlinplugin.listener

import net.strokkur.kotlinplugin.commands.FlyCommand
import net.strokkur.kotlinplugin.commands.GamemodeCommand
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class JoinListener : Listener {

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player

        player.gameMode = GamemodeCommand.gamemode(player)
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) {
            return
        }

        FlyCommand.enableFlyIfNeeded(player)
    }




}