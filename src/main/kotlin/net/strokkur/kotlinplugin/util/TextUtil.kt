package net.strokkur.kotlinplugin.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver

object TextUtil {

    fun parse(message: String): Component {
        return parse(message, TagResolver.empty())
    }

    fun parse(message: String, vararg resolvers: TagResolver): Component {
        return MiniMessage.miniMessage().deserialize(message, *resolvers)
    }

    fun insertingTag(name: String, component: ComponentLike): TagResolver {
        return TagResolver.resolver(name, Tag.inserting(component))
    }

    fun selfClosingTag(name: String, component: ComponentLike): TagResolver {
        return TagResolver.resolver(name, Tag.selfClosingInserting(component))
    }

}