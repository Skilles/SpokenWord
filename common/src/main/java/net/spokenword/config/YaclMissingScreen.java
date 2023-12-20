package net.spokenword.config;

import dev.architectury.platform.Platform;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public class YaclMissingScreen extends Screen {

    private static final Component TITLE = Component.translatable("spokenword.config.yacl_missing").withStyle(ChatFormatting.BOLD, ChatFormatting.DARK_RED);
    private static final Component CONTENT = Component.translatable("spokenword.config.yacl_missing.desc", Platform.getConfigFolder().toString());
    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
    private static final Component OPEN_FOLDER = Component.translatable("spokenword.config.yacl_missing.open_config");
    private static final Component DOWNLOAD_YACL = Component.translatable("spokenword.config.yacl_missing.download_yacl");
    private static final String YACL_URL = "https://modrinth.com/mod/yacl";

    private static final int LINE_HEIGHT = 18;

    private final Screen parent;
    private MultiLineLabel content;
    private int heightOffset = 0;

    private YaclMissingScreen(Screen parent) {
        super(TITLE);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.content = MultiLineLabel.create(this.font, CONTENT, (int) (this.width * 0.8));
        int offset = (this.content.getLineCount() + 1) * LINE_HEIGHT;

        int height = offset + 20 - LINE_HEIGHT;
        this.heightOffset = (int) ((this.height - height) * 0.4);

        this.addRenderableWidget(
                Button.builder(OPEN_FOLDER, button -> Util.getPlatform().openUri(Platform.getConfigFolder().toUri()))
                      .pos(this.width / 2 - 75 - 75 - 10, heightOffset + offset + 20)
                      .build()
        );
        this.addRenderableWidget(
                Button.builder(DOWNLOAD_YACL, button -> {
                          Util.getPlatform().openUri(YACL_URL);
                          this.minecraft.setScreen(this.parent);
                      })
                      .pos(this.width / 2 - 50, heightOffset + offset + 45)
                      .width(100)
                      .build()
        );
        this.addRenderableWidget(
                Button.builder(CommonComponents.GUI_BACK, button -> this.minecraft.setScreen(this.parent))
                      .pos(this.width / 2 - 75 + 75 + 10, heightOffset + offset + 20)
                      .build()
        );
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        this.renderBackground(guiGraphics);
        int titleX = this.width / 2 - this.font.width(this.title) / 2;
        guiGraphics.drawString(this.font, this.title, titleX, heightOffset, 16777215);
        this.content.renderCentered(guiGraphics, (int) (this.width * 0.5), (int) (heightOffset + LINE_HEIGHT * 1.5), LINE_HEIGHT, 16777215);
        super.render(guiGraphics, i, j, f);
    }

    @Override
    public @NotNull Component getNarrationMessage() {
        return NARRATION;
    }

    public static Screen create(Screen parent) {
        return new YaclMissingScreen(parent);
    }
}
