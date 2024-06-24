package net.strokkur.kotlinplugin.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.strokkur.kotlinplugin.util.SCommand
import net.strokkur.kotlinplugin.util.TextUtil

class SpeedCommand : SCommand {

    override fun firstCommand(): CommandAPICommand {
        return CommandAPICommand("speed")
            .withArguments(IntegerArgument("speed", 0, 10))
            .withOptionalArguments(MultiLiteralArgument("type", "walk", "fly"))
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val type: String = args.getOrDefault("type", if (player.isFlying) "fly" else "walk") as String
                val speed: Int = args.get("speed") as Int

                if (type == "fly") {
                    player.flySpeed = speed / 10f
                } else {
                    player.walkSpeed = speed / 10f
                }

                player.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> Your ${type}ing speed has been set to <white>$speed</white>!"))
            })
    }

    override fun secondCommand(): CommandAPICommand {
        return CommandAPICommand("flyspeed")
            .withArguments(IntegerArgument("speed", 0, 10))
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val speed: Int = args.get("speed") as Int
                player.flySpeed = speed / 10f

                player.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> Your flying speed has been set to <white>$speed</white>!"))
            })
    }

    override fun thirdCommand(): CommandAPICommand {
        return CommandAPICommand("walkspeed")
            .withArguments(IntegerArgument("speed", 0, 10))
            .executesPlayer(PlayerCommandExecutor { player, args ->
                val speed: Int = args.get("speed") as Int
                player.walkSpeed = speed / 10f

                player.sendMessage(TextUtil.parse("<gold><bold>[!]</gold> Your walking speed has been set to <white>$speed</white>!"))
            })
    }
}