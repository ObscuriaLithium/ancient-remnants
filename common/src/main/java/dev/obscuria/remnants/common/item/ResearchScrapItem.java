package dev.obscuria.remnants.common.item;

import dev.obscuria.remnants.common.component.StoredResearch;
import dev.obscuria.remnants.registry.AncientRemnantsComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class ResearchScrapItem extends Item {

    public ResearchScrapItem(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(
            ItemStack stack, TooltipContext context, TooltipDisplay display,
            Consumer<Component> builder, TooltipFlag tooltipFlag
    ) {
        var storedResearch = stack.getOrDefault(AncientRemnantsComponents.STORED_RESEARCH, StoredResearch.EMPTY);
        for (var entry : storedResearch.entries()) {
            builder.accept(Component
                    .translatable("tooltip.ancient_remnants.codex_entry", entry.value().header())
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
