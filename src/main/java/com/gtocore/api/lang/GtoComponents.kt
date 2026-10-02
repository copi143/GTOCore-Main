package com.gtocore.api.lang

import com.gtocore.common.data.translation.ComponentSlang
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

/** GTO 兼容行沿用 ComponentSlang 的符号、缩进和固定配色，保留正文的显式样式。 */
internal fun Components.appendGtoLine(
    prefix: MutableComponent?,
    content: MutableComponent,
    color: ChatFormatting
): MutableComponentEditor {
    val row = prefix?.append(content) ?: content
    row.withStyle(color)
    return appendLine(row)
}

/** 对应旧 highlight：金色星号，不额外加粗。 */
fun Components.gtoHighlight(content: Component): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Star(1).get(), content.copy(), ChatFormatting.GOLD)

fun Components.gtoHighlight(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Star(1).get(), content.get(), ChatFormatting.GOLD)

/** 对应旧 section：金色分节行。 */
fun Components.gtoSection(content: Component): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Bar(1).get(), content.copy(), ChatFormatting.GOLD)

fun Components.gtoSection(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Bar(1).get(), content.get(), ChatFormatting.GOLD)

/** 对应旧 story：灰色正文，无符号。 */
fun Components.gtoDescription(content: Component): MutableComponentEditor =
    appendGtoLine(null, content.copy(), ChatFormatting.GRAY)

fun Components.gtoDescription(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(null, content.get(), ChatFormatting.GRAY)

/** 对应旧 info：灰色 # 说明行，保留原二级缩进。 */
fun Components.gtoInfo(content: Component): MutableComponentEditor =
    appendGtoLine(ComponentSlang.OutTopic(2).get(), content.copy(), ChatFormatting.GRAY)

fun Components.gtoInfo(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(ComponentSlang.OutTopic(2).get(), content.get(), ChatFormatting.GRAY)

/** 对应旧 function：青色圆点功能行。 */
fun Components.gtoFeature(content: Component): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Circle(2).get(), content.copy(), ChatFormatting.AQUA)

fun Components.gtoFeature(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Circle(2).get(), content.get(), ChatFormatting.AQUA)

/** 对应旧 guide：淡紫色星号操作行，与通用 usage 的黄色主题区分。 */
fun Components.gtoGuide(content: Component): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Asterisk(2).get(), content.copy(), ChatFormatting.LIGHT_PURPLE)

fun Components.gtoGuide(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Asterisk(2).get(), content.get(), ChatFormatting.LIGHT_PURPLE)

/** 对应旧 error，缩进规则由 ComponentSlang.Wrong 统一处理。 */
fun Components.gtoError(content: Component, tab: Int = 2): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Wrong(tab).get(), content.copy(), ChatFormatting.RED)

fun Components.gtoError(content: ComponentSupplier, tab: Int = 2): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Wrong(tab).get(), content.get(), ChatFormatting.RED)

/** 对应旧 danger：红色警告符及加粗样式。 */
fun Components.gtoWarning(content: Component): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Warning(1).get(), content.copy(), ChatFormatting.RED)

fun Components.gtoWarning(content: ComponentSupplier): MutableComponentEditor =
    appendGtoLine(ComponentSlang.Warning(1).get(), content.get(), ChatFormatting.RED)

/** 复用原修改标记；Remix 的滚动颜色仍在每次构建时求值。 */
fun Components.gtoEdition(remix: Boolean = false): MutableComponentEditor = line(
    if (remix) ComponentSlang.GTOSignal_Edition_ByGTORemix else ComponentSlang.GTOSignal_Edition_ByGTONormal
)
