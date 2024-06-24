package net.strokkur.kotlinplugin.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.strokkur.kotlinplugin.KotlinPlugin
import net.strokkur.kotlinplugin.util.SCommand
import net.strokkur.kotlinplugin.util.TextUtil
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class FlyCommand : SCommand {

    override fun firstCommand(): CommandAPICommand {
        return CommandAPICommand("fly")
            .withPermission("kotlinpl.fly")
            .executesPlayer(PlayerCommandExecutor { player, _ ->
                val enabled = togglePlayerFlight(player)

                if (enabled) {
                    player.sendMessage(TextUtil.parse("<dark_green><bold>[!]</dark_green> <green>You are now flying!"))
                } else {
                    player.sendMessage(TextUtil.parse("<dark_red><bold>[!]</dark_red> <red>You are no longer flying!"))
                }
            })
    }

    companion object {
        private val flyKey: NamespacedKey = NamespacedKey(KotlinPlugin.plugin!!, "flyEnabled")

        fun togglePlayerFlight(player: Player): Boolean {
            if (player.persistentDataContainer.has(flyKey, PersistentDataType.BOOLEAN)) {
                val enable = !player.persistentDataContainer.get(flyKey, PersistentDataType.BOOLEAN)!!
                player.persistentDataContainer.set(flyKey, PersistentDataType.BOOLEAN, enable)
                enableFlyIfNeeded(player, enable)
                return enable
            }

            player.persistentDataContainer.set(flyKey, PersistentDataType.BOOLEAN, true)
            enableFlyIfNeeded(player, true)
            return true
        }

        private fun enableFlyIfNeeded(player: Player, flying: Boolean) {
            if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) {
                player.allowFlight = true
                player.isFlying = true
                return
            }

            player.allowFlight = flying
            player.isFlying = flying
        }

        fun enableFlyIfNeeded(player: Player): Boolean {
            if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR) {
                player.allowFlight = true
                player.isFlying = true
                return true
            }

            if (player.persistentDataContainer.has(flyKey, PersistentDataType.BOOLEAN)) {
                val enable = player.persistentDataContainer.get(flyKey, PersistentDataType.BOOLEAN)!!
                player.allowFlight = enable
                player.isFlying = enable
                return enable
            }

            player.allowFlight = true
            player.isFlying = true
            player.persistentDataContainer.set(flyKey, PersistentDataType.BOOLEAN, true)
            return true
        }
    }

}