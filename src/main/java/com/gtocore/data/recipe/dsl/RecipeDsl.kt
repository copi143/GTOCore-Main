package com.gtocore.data.recipe.dsl

import com.gtolib.api.recipe.RecipeBuilder
import com.gtolib.api.recipe.RecipeType

@DslMarker
@Target(AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
annotation class RecipeDsl

/** 在同一种配方类型下声明多条配方；声明块直接内联到调用点。 */
inline fun RecipeType.recipes(block: @RecipeDsl RecipeType.() -> Unit) {
    block()
}

/**
 * 使用 recipeBuilder(name) 创建配方，配置正常结束后保存一次。
 * 输入、输出、条件和注册校验仍由现有 RecipeBuilder 处理。
 * 原来使用 builder(name, ...) 的配方须先核对该入口的语义，不能直接替换。
 */
inline fun RecipeType.recipe(name: String, crossinline configure: @RecipeDsl RecipeBuilder.() -> Unit) {
    val builder = recipeBuilder(name)
    builder.configure()
    builder.save()
}
