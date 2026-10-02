package com.gtocore.api.lang

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack

import java.util.function.BiConsumer
import java.util.function.Supplier

@DslMarker
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
internal annotation class ComponentDsl

/**
 * 按语义构建文本。语义块只控制样式，不自动插入编号、缩进或空行。
 * 优先级：主题基础样式 → 外层块 → 内层块 → 组件自身样式 → 链式 editor。
 * style 和 namespace 在正常或异常退出后都会恢复外层状态。
 */
@ComponentDsl
@Suppress("NOTHING_TO_INLINE")
class Components @PublishedApi internal constructor(
    val theme: ComponentTheme,
    private val destination: MutableList<in MutableComponent>
) {
    private var current: MutableComponent? = null

    @PublishedApi
    internal var currentStyle: Style = theme.base

    @PublishedApi
    internal var currentNamespace: String? = null

    /** 结束正在拼接的行；没有待提交文字时插入一个空行。 */
    val newLine: Unit
        get() {
            destination.add(current ?: Component.empty())
            current = null
        }

    inline fun str(str: String): MutableComponent = Component.literal(str)

    inline fun l10n(l10n: String): MutableComponent = Component.translatable(resolveTranslationKey(l10n))
    inline fun l10n(l10n: String, vararg args: Any?): MutableComponent = translate(l10n, args)

    inline fun line(line: Component): MutableComponentEditor = appendLine(line.copy())
    inline fun line(line: String): MutableComponentEditor = appendLine(str(line))
    /** 沿用 GTO 的延迟样式与动态颜色，每次构建时重新求值。 */
    inline fun line(line: ComponentSupplier): MutableComponentEditor = appendLine(line.get())
    inline fun l10nLine(l10nLine: String): MutableComponentEditor = appendLine(l10n(l10nLine))
    inline fun l10nLine(l10nLine: String, vararg args: Any?): MutableComponentEditor =
        appendLine(translate(l10nLine, args))

    inline fun text(text: Component): MutableComponentEditor = appendText(text.copy())
    inline fun text(text: String): MutableComponentEditor = appendText(str(text))
    inline fun text(text: ComponentSupplier): MutableComponentEditor = appendText(text.get())
    inline fun l10nText(l10nText: String): MutableComponentEditor = appendText(l10n(l10nText))
    inline fun l10nText(l10nText: String, vararg args: Any?): MutableComponentEditor =
        appendText(translate(l10nText, args))

    inline fun lines(components: Iterable<Component>) {
        for (component in components) line(component)
    }

    /** 接入现有双语 tooltip 定义，不改变它们的翻译前缀、行号或样式。 */
    inline fun lines(components: ComponentListSupplier) {
        for (component in components.list) line(component)
    }

    /**
     * 每个 / 插入一次命名空间，非空部分以点连接；不含 / 的键不变。
     * 例如 namespace("a.b") 中的 "item/name" 展开成 "item.a.b.name"。
     * 嵌套作用域替换外层命名空间。传入的组件及翻译参数不会被重写。
     */
    inline fun namespace(namespace: String, builder: Components.() -> Unit) {
        val previous = currentNamespace
        currentNamespace = namespace
        try {
            builder()
        } finally {
            currentNamespace = previous
        }
    }

    @PublishedApi
    internal fun resolveTranslationKey(key: String): String {
        val namespace = currentNamespace ?: return key
        var separator = key.indexOf('/')
        if (separator < 0) return key

        val result = StringBuilder(key.length + namespace.length)
        var start = 0
        while (separator >= 0) {
            if (separator > start) {
                if (result.isNotEmpty()) result.append('.')
                result.append(key, start, separator)
            }
            if (namespace.isNotEmpty()) {
                if (result.isNotEmpty()) result.append('.')
                result.append(namespace)
            }
            start = separator + 1
            separator = key.indexOf('/', start)
        }
        if (start < key.length) {
            if (result.isNotEmpty()) result.append('.')
            result.append(key, start, key.length)
        }
        return result.toString()
    }

    @PublishedApi
    internal fun translate(key: String, args: Array<out Any?>): MutableComponent {
        val resolved = resolveTranslationKey(key)
        // 仅在交给 Minecraft 时复制一次数组，保留调用方参数快照。
        return if (args.isEmpty()) Component.translatable(resolved) else Component.translatable(resolved, *args)
    }

    /** 标题或分节标题。 */
    inline fun title(builder: Components.() -> Unit) = style(theme.title, builder)
    /** 中性的背景简介。 */
    inline fun description(builder: Components.() -> Unit) = style(theme.description, builder)
    /** 使用方法及操作步骤。 */
    inline fun usage(builder: Components.() -> Unit) = style(theme.usage, builder)
    /** 核心功能。 */
    inline fun feature(builder: Components.() -> Unit) = style(theme.feature, builder)
    /** 相比普通版本的增强。 */
    inline fun enhancement(builder: Components.() -> Unit) = style(theme.enhancement, builder)
    /** 使用前提、兼容条件与能力边界。 */
    inline fun requirement(builder: Components.() -> Unit) = style(theme.requirement, builder)
    /** 能耗、配方用量、匹配等机制。 */
    inline fun rule(builder: Components.() -> Unit) = style(theme.rule, builder)

    inline fun status(
        level: ComponentStatusLevel = ComponentStatusLevel.NORMAL,
        builder: Components.() -> Unit
    ) {
        val statusStyle = when (level) {
            ComponentStatusLevel.NORMAL -> theme.status
            ComponentStatusLevel.ATTENTION -> theme.statusAttention.applyTo(theme.status)
            ComponentStatusLevel.ERROR -> theme.statusError.applyTo(theme.status)
        }
        style(statusStyle, builder)
    }

    /** 快捷操作与展开提示。 */
    inline fun hint(builder: Components.() -> Unit) = style(theme.hint, builder)
    /** 用于辨认或排查的次要详情。 */
    inline fun detail(builder: Components.() -> Unit) = style(theme.detail, builder)
    /** 损失、破坏或不可逆操作等风险。 */
    inline fun warning(builder: Components.() -> Unit) = style(theme.warning, builder)

    inline fun style(style: Style, builder: Components.() -> Unit) {
        val previous = currentStyle
        currentStyle = style.applyTo(previous)
        try {
            builder()
        } finally {
            currentStyle = previous
        }
    }

    inline fun color(rgb: Int, builder: Components.() -> Unit) = style(Style.EMPTY.withColor(rgb), builder)
    inline fun bold(enabled: Boolean = true, builder: Components.() -> Unit) =
        style(Style.EMPTY.withBold(enabled), builder)
    inline fun italic(enabled: Boolean = true, builder: Components.() -> Unit) =
        style(Style.EMPTY.withItalic(enabled), builder)
    inline fun underline(enabled: Boolean = true, builder: Components.() -> Unit) =
        style(Style.EMPTY.withUnderlined(enabled), builder)
    inline fun strikethrough(enabled: Boolean = true, builder: Components.() -> Unit) =
        style(Style.EMPTY.withStrikethrough(enabled), builder)
    inline fun obfuscated(enabled: Boolean = true, builder: Components.() -> Unit) =
        style(Style.EMPTY.withObfuscated(enabled), builder)

    @PublishedApi
    internal fun format(formatting: ChatFormatting): Style = Style.EMPTY.applyFormat(formatting)

    inline fun black(builder: Components.() -> Unit) = style(format(ChatFormatting.BLACK), builder)
    inline fun darkBlue(builder: Components.() -> Unit) = style(format(ChatFormatting.DARK_BLUE), builder)
    inline fun darkGreen(builder: Components.() -> Unit) = style(format(ChatFormatting.DARK_GREEN), builder)
    inline fun darkAqua(builder: Components.() -> Unit) = style(format(ChatFormatting.DARK_AQUA), builder)
    inline fun darkRed(builder: Components.() -> Unit) = style(format(ChatFormatting.DARK_RED), builder)
    inline fun darkPurple(builder: Components.() -> Unit) = style(format(ChatFormatting.DARK_PURPLE), builder)
    inline fun gold(builder: Components.() -> Unit) = style(format(ChatFormatting.GOLD), builder)
    inline fun gray(builder: Components.() -> Unit) = style(format(ChatFormatting.GRAY), builder)
    inline fun darkGray(builder: Components.() -> Unit) = style(format(ChatFormatting.DARK_GRAY), builder)
    inline fun blue(builder: Components.() -> Unit) = style(format(ChatFormatting.BLUE), builder)
    inline fun green(builder: Components.() -> Unit) = style(format(ChatFormatting.GREEN), builder)
    inline fun aqua(builder: Components.() -> Unit) = style(format(ChatFormatting.AQUA), builder)
    inline fun red(builder: Components.() -> Unit) = style(format(ChatFormatting.RED), builder)
    inline fun lightPurple(builder: Components.() -> Unit) = style(format(ChatFormatting.LIGHT_PURPLE), builder)
    inline fun yellow(builder: Components.() -> Unit) = style(format(ChatFormatting.YELLOW), builder)
    inline fun white(builder: Components.() -> Unit) = style(format(ChatFormatting.WHITE), builder)

    @PublishedApi
    internal fun appendLine(component: MutableComponent): MutableComponentEditor {
        if (current != null) newLine
        component.style = component.style.applyTo(currentStyle)
        destination.add(component)
        return MutableComponentEditor(component)
    }

    @PublishedApi
    internal fun appendText(component: MutableComponent): MutableComponentEditor {
        component.style = component.style.applyTo(currentStyle)
        val pending = current ?: Component.empty().also { current = it }
        pending.append(component)
        return MutableComponentEditor(component)
    }

    @PublishedApi
    internal fun finish() {
        if (current != null) newLine
    }

    companion object {
        @Volatile
        var defaultTheme = ComponentTheme()

        inline fun build(builder: Components.() -> Unit): List<MutableComponent> = build(defaultTheme, builder)

        inline fun build(theme: ComponentTheme, builder: Components.() -> Unit): List<MutableComponent> {
            val result = ArrayList<MutableComponent>()
            val components = Components(theme, result)
            components.builder()
            components.finish()
            return result
        }

        inline fun build(
            namespace: String,
            theme: ComponentTheme = defaultTheme,
            builder: Components.() -> Unit
        ): List<MutableComponent> = build(theme) { namespace(namespace, builder) }

        /** 直接追加到 tooltip，避免中间列表；若构建抛异常，已追加的行不会回滚。 */
        inline fun appendTo(
            tooltip: MutableList<Component>,
            theme: ComponentTheme = defaultTheme,
            builder: Components.() -> Unit
        ) {
            val components = Components(theme, tooltip)
            components.builder()
            components.finish()
        }

        /** 用于接受列表 supplier 的界面，每次 get() 读取当前默认主题并重新构建。 */
        inline fun supplier(crossinline builder: Components.() -> Unit): Supplier<List<Component>> =
            Supplier { build { builder() } }

        inline fun supplier(
            theme: ComponentTheme,
            crossinline builder: Components.() -> Unit
        ): Supplier<List<Component>> = Supplier { build(theme) { builder() } }

        /** 直接适配 GTM 的 setTooltipBuilder，物品栈与动态内容在回调执行时读取。 */
        inline fun tooltip(
            crossinline builder: Components.(ItemStack) -> Unit
        ): BiConsumer<ItemStack, MutableList<Component>> = BiConsumer { stack, tooltip ->
            appendTo(tooltip) { builder(stack) }
        }

        inline fun tooltip(
            theme: ComponentTheme,
            crossinline builder: Components.(ItemStack) -> Unit
        ): BiConsumer<ItemStack, MutableList<Component>> = BiConsumer { stack, tooltip ->
            appendTo(tooltip, theme) { builder(stack) }
        }
    }
}
