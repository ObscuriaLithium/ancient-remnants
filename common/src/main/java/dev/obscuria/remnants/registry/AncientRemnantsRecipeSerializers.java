package dev.obscuria.remnants.registry;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.recipe.CodexRestorationRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public final class AncientRemnantsRecipeSerializers {

    public static final RecipeSerializer<CodexRestorationRecipe> CODEX_RESTORATION = register("codex_restoration", CodexRestorationRecipe.SERIALIZER);

    private static <T extends Recipe<?>> RecipeSerializer<T> register(String name, RecipeSerializer<T> value) {
        AncientRemnantsRegistries.REGISTRAR.register(Registries.RECIPE_SERIALIZER, AncientRemnants.id(name), () -> value);
        return value;
    }

    static void init() {}
}
