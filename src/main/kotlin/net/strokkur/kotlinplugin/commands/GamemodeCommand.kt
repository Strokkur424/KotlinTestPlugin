package net.strokkur.kotlinplugin.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.ConsoleCommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.strokkur.kotlinplugin.KotlinPlugin
import net.strokkur.kotlinplugin.util.SCommand
import net.strokkur.kotlinplugin.util.TextUtil
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class GamemodeCommand: SCommand {

    companion object {
        private val gamemodeKey = NamespacedKey(KotlinPlugin.plugin!!, "gamemode")

        fun gamemode(player: Player, gamemode: GameMode) {
            player.gameMode = gamemode
            player.persistentDataContainer.set(gamemodeKey, PersistentDataType.BYTE, gamemode.ordinal.toByte())
        }

        @Suppress("UnstableApiUsage")
        fun gamemode(player: Player): GameMode {
            if (player.persistentDataContainer.has(gamemodeKey, PersistentDataType.BYTE)) {
                return GameMode.getByValue(player.persistentDataContainer.get(gamemodeKey, PersistentDataType.BYTE)!!.toInt())!!
            }

            gamemode(player, player.gameMode)
            return player.gameMode
        }
    }

    override fun firstCommand(): CommandAPICommand {
        val subcommands = emptyArray<CommandAPICommand>()

        subcommands[0] = subcommand(GameMode.CREATIVE, "c")
        subcommands[1] = subcommand(GameMode.SURVIVAL, "s")
        subcommands[2] = subcommand(GameMode.ADVENTURE, "a")
        subcommands[3] = subcommand(GameMode.SPECTATOR, "sp")

        for (v in subcommands) {
            v.register()
        }

        return CommandAPICommand("gamemode")
            .withAliases("gm")
            .withPermission("kolinpl.gamemode")
            .withSubcommands(*subcommands)
    }

    private fun subcommand(gamemode: GameMode, short: String): CommandAPICommand {
        val gamemodeName = gamemode.name.lowercase()
        return CommandAPICommand(gamemodeName)
            .withAliases("gm$short")
            .withPermission("kotlinpl.gamemode.$gamemodeName")
            .withRequirement { sender -> if (sender is Player) sender.gameMode != gamemode else true }
            .withOptionalArguments(
                SCommand.playerArgument("target").executesConsole(ConsoleCommandExecutor { sender, args ->
                    val target = args.get("target") as Player
                    target.gameMode = gamemode

                    sender.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> <yellow>Successfully set <white>${target.name}</white>'s gamemode to <white>$gamemodeName</white>!"))
                    target.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> <yellow>Your gamemode has been set to <white>$gamemodeName</white> by <white>${sender.name}</white>!"))
                    gamemode(target, gamemode)
                })
            )
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val target = args.getOrDefault("target", player) as Player
                target.gameMode = gamemode

                if (target == player) {
                    player.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> <yellow>Successfully set your gamemode to <white>$gamemodeName</white>!"))
                    gamemode(player, gamemode)
                    return@PlayerCommandExecutor
                }

                player.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> <yellow>Successfully set <white>${target.name}</white>'s gamemode to <white>$gamemodeName</white>!"))
                target.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> <yellow>Your gamemode has been set to <white>$gamemodeName</white> by <white>${player.name}</white>!"))
                gamemode(target, gamemode)
            })


    }


}