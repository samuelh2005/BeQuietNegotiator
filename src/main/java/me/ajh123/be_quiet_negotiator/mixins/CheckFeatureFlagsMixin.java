package me.ajh123.be_quiet_negotiator.mixins;

//? if <1.21.2 {
/*import me.ajh123.be_quiet_negotiator.BeQuietNegotiator;
import me.ajh123.be_quiet_negotiator.ClientConfig;
import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;
import net.neoforged.neoforge.network.configuration.CheckFeatureFlags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
*///?}

//? if <1.21.2 {
/*@Mixin(CheckFeatureFlags.class)
public class CheckFeatureFlagsMixin {
    @Inject(
            method = "handleVanillaServerConnection(Lnet/minecraft/network/protocol/configuration/ClientConfigurationPacketListener;)Z",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void handleVanillaServerConnection(ClientConfigurationPacketListener listener, CallbackInfoReturnable<Boolean> cir) {
        if (BeQuietNegotiator.isConnectedToVanillaServer && ClientConfig.bypassCustomFeatureFlags()) {
            cir.setReturnValue(true);
        }
    }
}
*///?}
