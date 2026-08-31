package dev.obscuria.remnants.client.screen.config;

import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.AbstractWidget;
import dev.isxander.yacl3.gui.TextScaledButtonWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.Nullable;

public final class DualControllerElement extends AbstractWidget {

    private final AbstractWidget firstElement;
    private final AbstractWidget secondElement;
    private final @Nullable TextScaledButtonWidget resetButton;

    public DualControllerElement(
            Dimension<Integer> dimension,
            AbstractWidget firstElement,
            AbstractWidget secondElement,
            @Nullable TextScaledButtonWidget resetButton
    ) {
        super(dimension);
        this.firstElement = firstElement;
        this.secondElement = secondElement;
        this.resetButton = resetButton;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        firstElement.mouseMoved(mouseX, mouseY);
        secondElement.mouseMoved(mouseX, mouseY);

        if (resetButton != null) {
            resetButton.mouseMoved(mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent mouseButtonEvent, boolean doubleClick) {
        firstElement.setFocused(false);
        secondElement.setFocused(false);

        if (firstElement.mouseClicked(mouseButtonEvent, doubleClick)) {
            firstElement.setFocused(true);
            return true;
        }

        if (secondElement.mouseClicked(mouseButtonEvent, doubleClick)) {
            secondElement.setFocused(true);
            return true;
        }

        return resetButton != null && resetButton.mouseClicked(mouseButtonEvent, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
        return firstElement.mouseReleased(mouseButtonEvent)
                || secondElement.mouseReleased(mouseButtonEvent)
                || (resetButton != null && resetButton.mouseReleased(mouseButtonEvent));
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent mouseButtonEvent, double dx, double dy) {
        return firstElement.mouseDragged(mouseButtonEvent, dx, dy)
                || secondElement.mouseDragged(mouseButtonEvent, dx, dy)
                || (resetButton != null && resetButton.mouseDragged(mouseButtonEvent, dx, dy));
    }

    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        return firstElement.keyPressed(keyEvent) || secondElement.keyPressed(keyEvent);
    }

    @Override
    public boolean keyReleased(KeyEvent keyEvent) {
        return firstElement.keyReleased(keyEvent) || secondElement.keyReleased(keyEvent);
    }

    @Override
    public boolean charTyped(CharacterEvent characterEvent) {
        return firstElement.charTyped(characterEvent) || secondElement.charTyped(characterEvent);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return firstElement.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
                || secondElement.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setDimension(Dimension<Integer> dim) {
        Dimension<Integer> firstElementDimension = dim.moved(0, 0)
                .withWidth(firstElement.getDimension().width())
                .withHeight(firstElement.getDimension().height());
        Dimension<Integer> secondElementDimension = dim.moved(firstElement.getDimension().width(), 0)
                .withWidth(secondElement.getDimension().width())
                .withHeight(secondElement.getDimension().height());

        firstElement.setDimension(firstElementDimension);
        secondElement.setDimension(secondElementDimension);

        if (resetButton != null) {
            resetButton.setY(dim.y());
        }

        super.setDimension(dim);
    }

    @Override
    public void unfocus() {
        firstElement.unfocus();
        secondElement.unfocus();
        super.unfocus();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        firstElement.extractRenderState(graphics, mouseX, mouseY, partialTick);
        secondElement.extractRenderState(graphics, mouseX, mouseY, partialTick);

        if (resetButton != null) {
            resetButton.setY(getDimension().y());
            resetButton.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean matchesSearch(String query) {
        return firstElement.matchesSearch(query) || secondElement.matchesSearch(query);
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        firstElement.updateNarration(builder);
        secondElement.updateNarration(builder);
    }
}