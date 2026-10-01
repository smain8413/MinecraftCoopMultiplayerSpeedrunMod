package com.github.smain8413.untitled.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.options.AccessibilityOptionsScreen;
import net.minecraft.client.gui.screen.options.LanguageOptionsScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TitleScreen.class)
public abstract class MainMenuMixin extends Screen {
    @Shadow
    @Final
    private static Identifier ACCESSIBILITY_ICON_TEXTURE;
    @Shadow
    private String splashText;
    @Shadow
    private int copyrightTextWidth;
    @Shadow
    private int copyrightTextX;
    @Shadow
    private boolean realmsNotificationsInitialized;
//    @Shadow
//    private RealmsScreen realmsNotificationGui;

    public MainMenuMixin(Text title) {
        super(title);
    }

    @Override
    public void init() {
        if (this.splashText == null) {
            assert this.client != null;
            this.splashText = this.client.getSplashTextLoader().get();
        }

        this.copyrightTextWidth = this.textRenderer.getWidth("Copyright Mojang AB. Do not distribute!");
        this.copyrightTextX = this.width - this.copyrightTextWidth - 2;
        int i = 24;
        int j = this.height / 4 + 48;
        assert this.client != null;
        if (this.client.isDemo()) {
            this.initWidgetsDemo(j, 24);
        } else {
            this.initWidgetsNormal(j, 24);
        }

        this.addButton(new ButtonWidget(this.width / 2 - 100, j - 50, 100, 20, new LiteralText("BUNTTON"), (button) -> this.client.openScreen(new SelectWorldScreen(this))));
        this.addButton(new TexturedButtonWidget(this.width / 2 - 124, j + 72 + 12, 20, 20, 0, 106, 20, ButtonWidget.WIDGETS_LOCATION, 256, 256, (buttonWidget) -> this.client.openScreen(new LanguageOptionsScreen(this, this.client.options, this.client.getLanguageManager())), new TranslatableText("narrator.button.language")));
        this.addButton(new ButtonWidget(this.width / 2 - 100, j + 72 + 12, 98, 20, new TranslatableText("menu.options"), (buttonWidget) -> this.client.openScreen(new OptionsScreen(this, this.client.options))));
//        this.addButton(new ButtonWidget(this.width / 2 + 2, j + 72 + 12, 98, 20, new TranslatableText("menu.quit"), (buttonWidget) -> this.client.scheduleStop()));
        this.addButton(new TexturedButtonWidget(this.width / 2 + 104, j + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_ICON_TEXTURE, 32, 64, (buttonWidget) -> this.client.openScreen(new AccessibilityOptionsScreen(this, this.client.options)), new TranslatableText("narrator.button.accessibility")));
        this.client.setConnectedToRealms(false);
        if (this.client.options.realmsNotifications && !this.realmsNotificationsInitialized) {
            RealmsBridge realmsBridge = new RealmsBridge();
//            this.realmsNotificationGui = realmsBridge.getNotificationScreen(this);
            this.realmsNotificationsInitialized = true;
        }

//        if (this.areRealmsNotificationsEnabled()) {
//            this.realmsNotificationGui.init(this.client, this.width, this.height);
//        }

    }
    @Shadow
    private boolean areRealmsNotificationsEnabled() {
        return false;
    }

    @Shadow
    private void initWidgetsNormal(int j, int i) {
    }

    @Shadow
    private void initWidgetsDemo(int j, int i) {
    }
}
