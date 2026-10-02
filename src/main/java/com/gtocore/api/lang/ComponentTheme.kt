package com.gtocore.api.lang

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Style

/** 当前状态的严重程度；操作风险与后果使用 warning 语义。 */
enum class ComponentStatusLevel {
    NORMAL,
    ATTENTION,
    ERROR
}

/**
 * Tooltip 的语义主题，可用 copy 定制。未指定的属性继承外层，显式 false 关闭对应装饰。
 * 注意、故障状态先叠加在 status 上，再叠加到当前作用域。
 */
data class ComponentTheme(
    val base: Style = Style.EMPTY,
    val title: Style = Style.EMPTY.withColor(ChatFormatting.GOLD).withBold(true),
    val description: Style = Style.EMPTY.withColor(ChatFormatting.GRAY),
    val usage: Style = Style.EMPTY.withColor(ChatFormatting.YELLOW),
    val feature: Style = Style.EMPTY.withColor(ChatFormatting.AQUA),
    val enhancement: Style = Style.EMPTY.withColor(ChatFormatting.AQUA),
    val requirement: Style = Style.EMPTY.withColor(ChatFormatting.YELLOW),
    val rule: Style = Style.EMPTY.withColor(ChatFormatting.GRAY),
    val status: Style = Style.EMPTY.withColor(ChatFormatting.WHITE),
    val statusAttention: Style = Style.EMPTY.withColor(ChatFormatting.YELLOW),
    val statusError: Style = Style.EMPTY.withColor(ChatFormatting.RED),
    val hint: Style = Style.EMPTY.withColor(ChatFormatting.YELLOW),
    val detail: Style = Style.EMPTY.withColor(ChatFormatting.DARK_GRAY),
    val warning: Style = Style.EMPTY.withColor(ChatFormatting.RED).withBold(true)
)
