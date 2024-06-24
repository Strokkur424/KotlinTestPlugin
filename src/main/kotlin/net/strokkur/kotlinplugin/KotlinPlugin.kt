package net.strokkur.kotlinplugin

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIBukkitConfig
import net.strokkur.kotlinplugin.commands.FlyCommand
import net.strokkur.kotlinplugin.commands.GamemodeCommand
import net.strokkur.kotlinplugin.commands.SpeedCommand
import net.strokkur.kotlinplugin.listener.JoinListener
import net.strokkur.kotlinplugin.util.SCommand
import org.bukkit.Bukkit
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class KotlinPlugin : JavaPlugin() {

    companion object {
        var plugin: KotlinPlugin? = null
    }

    override fun onLoad() {
        CommandAPI.onLoad(
            CommandAPIBukkitConfig(this)
                .shouldHookPaperReload(true)
                .silentLogs(true)
                .setNamespace("kotlinpl")
        )
    }

    override fun onEnable() {
        CommandAPI.onEnable()
        plugin = this

        command(FlyCommand())
        command(SpeedCommand())
        command(GamemodeCommand())

        listener(JoinListener())
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }


    private fun command(command: SCommand) {
        command.firstCommand().register()
        command.secondCommand()?.register()
        command.thirdCommand()?.register()
    }

    private val pluginManager = Bukkit.getPluginManager()
    private fun listener(event: Listener) {
        pluginManager.registerEvents(event, this)
    }
}