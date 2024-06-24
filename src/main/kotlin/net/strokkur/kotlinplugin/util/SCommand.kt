package net.strokkur.kotlinplugin.util

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.SafeSuggestions
import org.bukkit.Bukkit
import org.bukkit.entity.Player

interface SCommand {

    companion object {
        fun playerArgument(name: String): Argument<Player> {
            return PlayerArgument(name).replaceSafeSuggestions(SafeSuggestions.suggest {
                Bukkit.getOnlinePlayers().toTypedArray()
            })
        }
    }

    fun firstCommand(): CommandAPICommand

    fun secondCommand(): CommandAPICommand? {
        return null
    }

    fun thirdCommand(): CommandAPICommand? {
        return null
    }

}