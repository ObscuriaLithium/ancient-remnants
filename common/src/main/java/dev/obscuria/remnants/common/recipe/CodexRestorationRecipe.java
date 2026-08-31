package dev.obscuria.remnants.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.obscuria.remnants.common.component.StoredResearch;
import dev.obscuria.remnants.registry.AncientRemnantsComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.List;

public final class CodexRestorationRecipe extends NormalCraftingRecipe {

    public static final MapCodec<CodexRestorationRecipe> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, CodexRestorationRecipe> STREAM_CODEC;
    public static final RecipeSerializer<CodexRestorationRecipe> SERIALIZER;

    private final Ingredient codex;
    private final Ingredient scrap;
    private final ItemStackTemplate result;

    public CodexRestorationRecipe(
            CommonInfo commonInfo, CraftingBookInfo bookInfo,
            Ingredient codex, Ingredient scrap,
            ItemStackTemplate result
    ) {
        super(commonInfo, bookInfo);
        this.codex = codex;
        this.scrap = scrap;
        this.result = result;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() < 2) return false;
        var hasCodex = false;
        var hasScrap = false;

        for (var stack : input.items()) {
            if (stack.isEmpty()) continue;
            if (codex.test(stack)) {
                if (hasCodex) return false;
                hasCodex = true;
            } else {
                if (!scrap.test(stack) || !stack.has(AncientRemnantsComponents.STORED_RESEARCH)) return false;
                hasScrap = true;
            }
        }

        return hasScrap && hasCodex;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        var codexStack = ItemStack.EMPTY;
        var storedResearch = StoredResearch.EMPTY;

        for (var stack : input.items()) {
            if (codex.test(stack)) {
                codexStack = stack;
                storedResearch = merge(storedResearch, stack);
            } else if (scrap.test(stack)) {
                storedResearch = merge(storedResearch, stack);
            }
        }

        if (!codexStack.isEmpty() && !storedResearch.isEmpty()) {
            var result = TransmuteRecipe.createWithOriginalComponents(this.result, codexStack);
            result.set(AncientRemnantsComponents.STORED_RESEARCH, storedResearch);
            return result;
        } else return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<CodexRestorationRecipe> getSerializer() {
        return SERIALIZER;
    }

    @Override
    protected PlacementInfo createPlacementInfo() {
        return PlacementInfo.create(List.of(codex, scrap));
    }

    private StoredResearch merge(StoredResearch research, ItemStack stack) {
        var stackResearch = stack.getOrDefault(AncientRemnantsComponents.STORED_RESEARCH, StoredResearch.EMPTY);
        if (stackResearch.isEmpty()) return research;
        return research.merge(stackResearch);
    }

    static {
        CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
                CommonInfo.MAP_CODEC.forGetter(it -> it.commonInfo),
                CraftingBookInfo.MAP_CODEC.forGetter(it -> it.bookInfo),
                Ingredient.CODEC.fieldOf("codex").forGetter(it -> it.codex),
                Ingredient.CODEC.fieldOf("scrap").forGetter(it -> it.scrap),
                ItemStackTemplate.CODEC.fieldOf("result").forGetter(it -> it.result)
        ).apply(builder, CodexRestorationRecipe::new));
        STREAM_CODEC = StreamCodec.composite(
                CommonInfo.STREAM_CODEC, it -> it.commonInfo,
                CraftingBookInfo.STREAM_CODEC, it -> it.bookInfo,
                Ingredient.CONTENTS_STREAM_CODEC, it -> it.codex,
                Ingredient.CONTENTS_STREAM_CODEC, it -> it.scrap,
                ItemStackTemplate.STREAM_CODEC, it -> it.result,
                CodexRestorationRecipe::new);
        SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);
    }
}
