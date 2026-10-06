package name.modid.client.mixin;

import name.modid.client.RepeatCommandClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class GuiMixin {

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private Screen prefillChatBox(Screen screen) {
        if (screen instanceof ChatScreen chatScreen) {
            // Only intervene if prefix is actually configured
            if (!RepeatCommandClient.hasPrefix()) {
                return screen;
            }

            ChatScreenAccessor accessor = (ChatScreenAccessor) chatScreen;
            String initialText = accessor.getInitial();

            // If screen was opened with "/" (or already has text / draft restored), do nothing:
            if (initialText != null && !initialText.isEmpty()) {
                return screen;
            }

            // Replace with a new ChatScreen prefilled with the custom prefix
            String prefix = RepeatCommandClient.getFullPrefix();
            return new ChatScreen(prefix, false);
        }
        return screen;
    }
}