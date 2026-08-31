package dev.obscuria.remnants.client.screen.config;

import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.TextScaledButtonWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.network.chat.Component;

public record DualController<K extends Option<?>, V extends Option<?>>(
        OptionPair<K, V> optionPair
) implements Controller<OptionPair<K, V>> {

    @Override
    public Option<OptionPair<K, V>> option() {
        return null;
    }

    @Override
    public Component formatValue() {
        return optionPair.firstOption().controller().formatValue().copy()
                .append(" | ")
                .append(optionPair.secondOption().controller().formatValue());
    }

    @Override
    public AbstractWidget provideWidget(YACLScreen screen, Dimension<Integer> widgetDimension) {
        Dimension<Integer> firstWidgetDimension = widgetDimension.withWidth((int) (widgetDimension.width() * 0.5));
        Dimension<Integer> secondWidgetDimension = widgetDimension.moved(firstWidgetDimension.width(), 0)
                .withWidth(widgetDimension.width() - firstWidgetDimension.width());

        AbstractWidget firstOptionWidget = optionPair.firstOption().controller().provideWidget(screen, firstWidgetDimension);
        AbstractWidget secondOptionWidget = optionPair.secondOption().controller().provideWidget(screen, secondWidgetDimension);
        TextScaledButtonWidget resetButtonWidget;

        if (optionPair.firstOption().controller().option().canResetToDefault() && firstOptionWidget.canReset()
                && optionPair.secondOption().controller().option().canResetToDefault() && secondOptionWidget.canReset()) {

            firstOptionWidget.setDimension(firstOptionWidget.getDimension().expanded(-10, 0));
            secondOptionWidget.setDimension(secondOptionWidget.getDimension().expanded(-10, 0));

            var resetButton = new TextScaledButtonWidget(
                    screen, secondOptionWidget.getDimension().xLimit() - 10, 0, 20, 20, 2f,
                    Component.literal("\u21BB"),
                    button -> {
                        optionPair.firstOption().requestSetDefault();
                        optionPair.secondOption().requestSetDefault();
                    });

            Runnable updateResetButtonState = () -> {
                boolean isModified = !optionPair.firstOption().isPendingValueDefault() || !optionPair.secondOption().isPendingValueDefault();
                boolean isAvailable = optionPair.firstOption().available() && optionPair.secondOption().available();
                resetButton.active = isModified && isAvailable;
            };

            optionPair.firstOption().addListener((opt, val) -> updateResetButtonState.run());
            optionPair.secondOption().addListener((opt, val) -> updateResetButtonState.run());
            updateResetButtonState.run();

            resetButtonWidget = resetButton;
        } else {
            resetButtonWidget = null;
        }

        return new DualControllerElement(widgetDimension, firstOptionWidget, secondOptionWidget, resetButtonWidget);
    }
}