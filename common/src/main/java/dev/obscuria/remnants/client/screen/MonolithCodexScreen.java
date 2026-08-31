package dev.obscuria.remnants.client.screen;

import dev.obscuria.remnants.AncientRemnants;
import dev.obscuria.remnants.common.ResearchEntry;
import dev.obscuria.remnants.common.component.StoredResearch;
import dev.obscuria.remnants.registry.AncientRemnantsComponents;
import dev.obscuria.remnants.registry.AncientRemnantsRegistries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonolithCodexScreen extends Screen {

    private static final Identifier BOOK_TEXTURE = AncientRemnants.id("textures/gui/monolith_codex.png");
    private static final Identifier BOOK_MISSING_TEXTURE = AncientRemnants.id("textures/gui/monolith_codex_missing.png");
    private static final FontDescription FONT_DESCRIPTION = new FontDescription.Resource(AncientRemnants.id("header"));

    private final List<Page> pages;
    private @Nullable PageButton forwardButton;
    private @Nullable PageButton backButton;
    private @Nullable Page currentPage;
    private int currentPageIndex = 1;

    public MonolithCodexScreen(ItemStack stack, RegistryAccess registryAccess) {
        this(stack.getOrDefault(AncientRemnantsComponents.STORED_RESEARCH, StoredResearch.EMPTY), registryAccess);
    }

    public MonolithCodexScreen(StoredResearch research, RegistryAccess registryAccess) {
        super(Component.literal("Monolith Codex"));
        this.pages = registryAccess
                .lookupOrThrow(AncientRemnantsRegistries.Keys.RESEARCH)
                .listElements().map(entry -> new Page(entry, research.contains(entry)))
                .toList();
        this.updatePage();
    }

    @Override
    protected void init() {
        var centerX = width / 2;
        var centerY = height / 2;
        this.backButton = this.addRenderableWidget(new PageButton(centerX - 60 - 12, centerY + 100, false, (_) -> this.pageBack(), true));
        this.forwardButton = this.addRenderableWidget(new PageButton(centerX + 60 - 12, centerY + 100, true, (_) -> this.pageForward(), true));
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractBackground(graphics, mouseX, mouseY, a);
        var font = Minecraft.getInstance().font;
        var bookX = width / 2 - 175;
        var bookY = height / 2 - 105;

        if (currentPage != null && currentPage.unlocked) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_TEXTURE, bookX, bookY, 0, 0, 350, 210, 350, 210);
            graphics.blit(RenderPipelines.GUI_TEXTURED, currentPage.texture(), bookX, bookY, 0, 0, 350, 210, 350, 210);

            var offsetX = currentPage.rightPage() ? 130 : 0;

            graphics.pose().pushMatrix();
            graphics.pose().translate(bookX + 56 + offsetX, bookY + 20);
            graphics.pose().scale(1.5f);
            var header = currentPage.header();
            var headerLines = (int) Math.ceil(font.width(header) / 75.0);
            graphics.textWithWordWrap(font, currentPage.header(), 0, 0, 75, 0xff5a2d3d, false);
            graphics.pose().popMatrix();

            graphics.textWithWordWrap(font, currentPage.content(), bookX + 56 + offsetX, bookY + 26 + 14 * headerLines, 110, 0xff5a2d3d, false);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, BOOK_MISSING_TEXTURE, bookX, bookY, 0, 0, 350, 210, 350, 210);
        }

        graphics.centeredText(font,
                Component.translatable("tooltip.ancient_remnants.page_of", currentPageIndex, pages.size()),
                width / 2, height / 2 + 103, 0xffffffff);
    }

    private void pageBack() {
        if (currentPageIndex > 1) --currentPageIndex;
        this.updatePage();
    }

    private void pageForward() {
        if (currentPageIndex < pages.size()) ++currentPageIndex;
        this.updatePage();
    }

    private void updatePage() {
        if (pages.isEmpty()) return;
        this.currentPage = pages.get(currentPageIndex - 1);
        if (forwardButton != null) forwardButton.active = currentPageIndex < pages.size();
        if (backButton != null) backButton.active = currentPageIndex > 1;
    }

    private record Page(Holder<ResearchEntry> entry, boolean unlocked) {

        public Component header() {
            return applyCodexStyle(entry.value().header());
        }

        public Component content() {
            return applyCodexStyle(entry.value().content());
        }

        public Identifier texture() {
            return entry.value().texture();
        }

        public boolean rightPage() {
            return entry.value().rightPage();
        }

        private Component applyCodexStyle(Component component) {
            return component.copy().withStyle(style -> style.withFont(FONT_DESCRIPTION));
        }
    }
}
