package com.gtocore.data.recipe.classified

import com.gregtechceu.gtceu.common.data.GTMaterials
import com.gtocore.common.data.GTOItems
import com.gtocore.common.data.GTOMaterials
import com.gtocore.common.data.GTORecipeTypes.BREWING_RECIPES
import com.gtocore.data.recipe.dsl.recipe
import com.gtolib.api.machine.GTOCleanroomType

internal class Brewing {
    companion object {
        @JvmStatic
        fun init() {
            BREWING_RECIPES.recipe("dragon_blood") {
                inputItems(GTOItems.DRAGON_CELLS)
                inputFluids(GTMaterials.SterileGrowthMedium, 1000)
                outputFluids(GTOMaterials.DragonBlood, 1000)
                EUt(480)
                duration(6000)
                cleanroom(GTOCleanroomType.LAW_CLEANROOM)
            }
        }
    }
}
