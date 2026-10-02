package com.gtocore.api.lang

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style

/** 编辑当前追加的组件。值类在常规链式调用中不分配额外包装对象。 */
@ComponentDsl
@JvmInline
@Suppress("NOTHING_TO_INLINE")
value class MutableComponentEditor @PublishedApi internal constructor(
    @PublishedApi internal val component: MutableComponent
) {
    inline fun style(style: Style): MutableComponentEditor = apply {
        component.style = style.applyTo(component.style)
    }

    inline fun color(rgb: Int): MutableComponentEditor = apply {
        component.style = component.style.withColor(rgb)
    }

    inline fun bold(enabled: Boolean): MutableComponentEditor = apply {
        component.style = component.style.withBold(enabled)
    }

    inline fun italic(enabled: Boolean): MutableComponentEditor = apply {
        component.style = component.style.withItalic(enabled)
    }

    inline fun underline(enabled: Boolean): MutableComponentEditor = apply {
        component.style = component.style.withUnderlined(enabled)
    }

    inline fun strikethrough(enabled: Boolean): MutableComponentEditor = apply {
        component.style = component.style.withStrikethrough(enabled)
    }

    inline fun obfuscated(enabled: Boolean): MutableComponentEditor = apply {
        component.style = component.style.withObfuscated(enabled)
    }

    @PublishedApi
    internal inline fun format(formatting: ChatFormatting): MutableComponentEditor = apply {
        component.withStyle(formatting)
    }

    inline val black get() = format(ChatFormatting.BLACK)
    inline val darkBlue get() = format(ChatFormatting.DARK_BLUE)
    inline val darkGreen get() = format(ChatFormatting.DARK_GREEN)
    inline val darkAqua get() = format(ChatFormatting.DARK_AQUA)
    inline val darkRed get() = format(ChatFormatting.DARK_RED)
    inline val darkPurple get() = format(ChatFormatting.DARK_PURPLE)
    inline val gold get() = format(ChatFormatting.GOLD)
    inline val gray get() = format(ChatFormatting.GRAY)
    inline val darkGray get() = format(ChatFormatting.DARK_GRAY)
    inline val blue get() = format(ChatFormatting.BLUE)
    inline val green get() = format(ChatFormatting.GREEN)
    inline val aqua get() = format(ChatFormatting.AQUA)
    inline val red get() = format(ChatFormatting.RED)
    inline val lightPurple get() = format(ChatFormatting.LIGHT_PURPLE)
    inline val yellow get() = format(ChatFormatting.YELLOW)
    inline val white get() = format(ChatFormatting.WHITE)
    inline val obfuscated get() = format(ChatFormatting.OBFUSCATED)
    inline val bold get() = format(ChatFormatting.BOLD)
    inline val strikethrough get() = format(ChatFormatting.STRIKETHROUGH)
    inline val underline get() = format(ChatFormatting.UNDERLINE)
    inline val italic get() = format(ChatFormatting.ITALIC)
}
