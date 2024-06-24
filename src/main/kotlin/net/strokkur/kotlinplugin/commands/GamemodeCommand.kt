package net.strokkur.kotlinplugin.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.strokkur.kotlinplugin.KotlinPlugin
import net.strokkur.kotlinplugin.util.SCommand
import net.strokkur.kotlinplugin.util.parse
import org.bukkit.GameMode
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType

class GamemodeCommand : SCommand {

    @Suppress("UnstableApiUsage")
    companion object {
        private val gamemodeKey = NamespacedKey(KotlinPlugin.plugin!!, "gamemode")

        fun gamemode(player: Player, gamemode: GameMode) {
            player.gameMode = gamemode
            player.persistentDataContainer.set(gamemodeKey, PersistentDataType.BYTE, gamemode.value.toByte())
            FlyCommand.enableFlyIfNeeded(player)
        }

        fun gamemode(player: Player): GameMode {
            if (player.persistentDataContainer.has(gamemodeKey, PersistentDataType.BYTE)) {
                return GameMode.getByValue(
                    player.persistentDataContainer.get(gamemodeKey, PersistentDataType.BYTE)!!.toInt()
                )!!
            }

            gamemode(player, player.gameMode)
            return player.gameMode
        }
    }

    override fun firstCommand(): CommandAPICommand {

        subcommand(GameMode.CREATIVE, "gmc", "creative").register()
        subcommand(GameMode.SURVIVAL, "gms","survival").register()
        subcommand(GameMode.ADVENTURE, "gma", "adventure").register()
        subcommand(GameMode.SPECTATOR, "gmsp", "spectator").register()

        return CommandAPICommand("gamemode")
            .withAliases("gm")
            .withPermission("kolinpl.gamemode")
            .withSubcommands(
                subcommand(GameMode.CREATIVE, "creative"),
                subcommand(GameMode.SURVIVAL, "survival"),
                subcommand(GameMode.ADVENTURE, "adventure"),
                subcommand(GameMode.SPECTATOR, "spectator")
            )
    }

    private fun subcommand(gamemode: GameMode, name: String, vararg aliases: String): CommandAPICommand {
        val gamemodeName = gamemode.name.lowercase()
        return CommandAPICommand(name)
            .withAliases(*aliases)
            .withPermission("kotlinpl.gamemode.$gamemodeName")
            .withOptionalArguments(
                SCommand.playerArgument("target")
            )
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val target = args.getOrDefault("target", player) as Player

                if (target == player) {
                    if (target.gameMode == gamemode) {
                        player.sendMessage(parse("<dark_red><bold>[!]</dark_red> <red>Your gamemode is already set to <white>$gamemodeName</white>!"))
                        return@PlayerCommandExecutor
                    }

                    target.gameMode = gamemode

                    player.sendMessage(parse("<gold><bold>[!]</gold> <yellow>Successfully set your gamemode to <white>$gamemodeName</white>!"))
                    gamemode(player, gamemode)
                    return@PlayerCommandExecutor
                }

                if (target.gameMode == gamemode) {
                    player.sendMessage(parse("<dark_red><bold>[!]</dark_red> <red><white>${target.name}</white>'s gamemode is already set to <white>$gamemodeName</white>!"))
                    return@PlayerCommandExecutor
                }

                target.gameMode = gamemode

                player.sendMessage(parse("<gold><bold>[!]</gold> <yellow>Successfully set <white>${target.name}</white>'s gamemode to <white>$gamemodeName</white>!"))
                target.sendMessage(parse("<gold><bold>[!]</gold> <yellow>Your gamemode has been set to <white>$gamemodeName</white> by <white>${player.name}</white>!"))
                gamemode(target, gamemode)
            })
            .executes(CommandExecutor { sender, args ->
                val target = args.get("target") as Player?

                if (target == null) {
                    sender.sendMessage(parse("<dark_red>Please specify a player!"))
                    return@CommandExecutor
                }

                if (target.gameMode == gamemode) {
                    sender.sendMessage(parse("<dark_red><bold>[!]</dark_red> <red><white>${target.name}</white>'s gamemode is already set to <white>$gamemodeName</white>!"))
                    return@CommandExecutor
                }

                target.gameMode = gamemode

                sender.sendMessage(parse("<gold><bold>[!]</gold> <yellow>Successfully set <white>${target.name}</white>'s gamemode to <white>$gamemodeName</white>!"))
                target.sendMessage(parse("<gold><bold>[!]</gold> <yellow>Your gamemode has been set to <white>$gamemodeName</white> by <white>${sender.name}</white>!"))
                gamemode(target, gamemode)
            })


    }


}