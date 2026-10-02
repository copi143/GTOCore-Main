package com.gtocore.data.recipe.classified

import com.gtocore.common.data.GTOItems
import com.gtocore.common.data.GTORecipeTypes.ANNIHILATE_GENERATOR_RECIPES
import com.gtocore.data.recipe.dsl.recipe
import com.gtocore.data.recipe.dsl.recipes

internal class AnnihilateGenerator {
    companion object {
        @JvmStatic
        fun init() {
            ANNIHILATE_GENERATOR_RECIPES.recipes {
                recipe("neutronium_antimatter_fuel_rod") {
                    inputItems(GTOItems.NEUTRONIUM_ANTIMATTER_FUEL_ROD)
                    chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asItem(), 9000, 0)
                    EUt(-549755813888L)
                    duration(200)
                }

                recipe("draconium_antimatter_fuel_rod") {
                    inputItems(GTOItems.DRACONIUM_ANTIMATTER_FUEL_ROD)
                    chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asItem(), 8000, 0)
                    EUt(-8796093022208L)
                    duration(200)
                }

                recipe("cosmic_neutronium_antimatter_fuel_rod") {
                    inputItems(GTOItems.COSMIC_NEUTRONIUM_ANTIMATTER_FUEL_ROD)
                    chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asItem(), 7000, 0)
                    EUt(-140737488355328L)
                    duration(200)
                }

                recipe("infinity_antimatter_fuel_rod") {
                    inputItems(GTOItems.INFINITY_ANTIMATTER_FUEL_ROD)
                    chancedOutput(GTOItems.ANNIHILATION_CONSTRAINER.asItem(), 6000, 0)
                    EUt(-2251799813685248L)
                    duration(200)
                }
            }
        }
    }
}
