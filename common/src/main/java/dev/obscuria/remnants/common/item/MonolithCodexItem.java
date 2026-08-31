package dev.obscuria.remnants.common.item;

import dev.obscuria.remnants.client.AncientRemnantsClient;
import dev.obscuria.remnants.common.component.StoredResearch;
import dev.obscuria.remnants.registry.AncientRemnantsComponents;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class MonolithCodexItem extends Item {

    public MonolithCodexItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (level.isClientSide()) AncientRemnantsClient.openMonolithCodexScreen(stack, level.registryAccess());
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.SUCCESS;
    }

    @Override
    @SuppressWarnings("all")
    public void appendHoverText(
            ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag tooltipFlag
    ) {
        var storedResearch = stack.getOrDefault(AncientRemnantsComponents.STORED_RESEARCH, StoredResearch.EMPTY);
        if (storedResearch.isEmpty()) {
            builder.accept(Component
                    .translatable("tooltip.ancient_remnants.codex_empty")
                    .withStyle(ChatFormatting.GRAY));
        } else {
            var total = context.registries().lookupOrThrow(AncientRemnantsRegistries.Keys.RESEARCH).listElements().count();
            if (storedResearch.entries().size() >= total) {
                builder.accept(Component
                        .translatable("tooltip.ancient_remnants.codex_complete")
                        .withStyle(ChatFormatting.GOLD));
                builder.accept(CommonComponents.EMPTY);
            }
            for (var entry : storedResearch.entries()) {
                builder.accept(Component
                        .translatable("tooltip.ancient_remnants.codex_entry", entry.value().header())
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
